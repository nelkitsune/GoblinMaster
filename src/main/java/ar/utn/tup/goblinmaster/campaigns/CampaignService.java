// campaigns/CampaignService.java
package ar.utn.tup.goblinmaster.campaigns;

import ar.utn.tup.goblinmaster.campaigns.CampaignMember.CampaignRole;
import ar.utn.tup.goblinmaster.campaigns.dto.*;
import ar.utn.tup.goblinmaster.users.User;
import ar.utn.tup.goblinmaster.users.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@Service
public class CampaignService {

    private final CampaignRepository campaigns;
    private final CampaignMemberRepository members;
    private final UserRepository users;

    public CampaignService(CampaignRepository campaigns, CampaignMemberRepository members, UserRepository users) {
        this.campaigns = campaigns; this.members = members; this.users = users;
    }

    private User me(Authentication auth) {
        // TODO: reemplazar por JWT cuando esté disponible
        return users.findById(1L).orElseGet(() -> users.findByEmail(auth != null ? auth.getName() : null).orElseThrow());
    }

    private String generateJoinCode() {
        final String ALPH = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random rnd = new SecureRandom();
        for (int attempt = 0; attempt < 10; attempt++) {
            int len = 8; // 6-10, elegimos 8
            StringBuilder sb = new StringBuilder("GM-");
            for (int i = 0; i < len; i++) sb.append(ALPH.charAt(rnd.nextInt(ALPH.length())));
            String code = sb.toString();
            if (!campaigns.existsByJoinCode(code)) return code;
        }
        throw new IllegalStateException("No se pudo generar join_code único");
    }

    @Transactional
    public CampaignResponse create(CreateCampaignRequest req, Authentication auth) {
        User owner = me(auth);
        if (req.name() == null || req.name().length() < 2 || req.name().length() > 60)
            throw new IllegalArgumentException("Nombre inválido");
        if (req.description() != null && req.description().length() > 500)
            throw new IllegalArgumentException("Descripción demasiado larga");

        Campaign c = Campaign.builder()
                .name(req.name())
                .description(req.description())
                .owner(owner)
                .active(true)
                .joinCode(generateJoinCode())
                .build();
        Campaign saved = campaigns.save(c);
        members.save(CampaignMember.builder()
                .campaign(saved).user(owner).role(CampaignRole.OWNER).build());

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CampaignResponse> myCampaigns(Authentication auth) {
        var user = me(auth);
        return campaigns.findAllByUserParticipation(user.getId())
                .stream().map(this::toDto).toList();
    }




    @Transactional(readOnly = true)
    public CampaignResponse getOne(Long id, Authentication auth) {
        ensureMember(id, auth);
        Campaign c = campaigns.findById(id).orElseThrow();
        return toDto(c);
    }


    @Transactional(readOnly = true)
    public List<MemberResponse> listMembers(Long campaignId, Authentication auth) {
        ensureMember(campaignId, auth);
        return members.findAllByCampaignId(campaignId).stream()
                .map(m -> new MemberResponse(
                        m.getId(),
                        m.getUser().getId(),
                        m.getUser().getEmail(),
                        m.getRole().name()))
                .toList();
    }

    @Transactional
    public boolean addMember(Long campaignId, AddMemberRequest req, Authentication auth) {
        ensureOwner(campaignId, auth);
        Campaign c = campaigns.findById(campaignId).orElseThrow();
        User u = users.findById(req.userId()).orElseThrow();

        if (members.existsByCampaignIdAndUserId(campaignId, u.getId())) return false;

        CampaignMember m = CampaignMember.builder()
                .campaign(c).user(u)
                .role(CampaignRole.valueOf(req.role()))
                .build();
        members.save(m);
        return true;
    }

    @Transactional
    public void removeMember(Long campaignId, Long userId, Authentication auth) {
        ensureOwner(campaignId, auth);
        // No permitir dejar sin OWNER
        var owners = members.countByCampaignIdAndRole(campaignId, CampaignRole.OWNER);
        var isOwnerTarget = users.findById(userId)
                .map(u -> members.existsByCampaignIdAndUserId(campaignId, u.getId()) &&
                        members.existsByCampaignIdAndUserEmailAndRole(campaignId, u.getEmail(), CampaignRole.OWNER))
                .orElse(false);

        if (isOwnerTarget && owners <= 1) {
            throw new IllegalStateException("No se puede quitar al último OWNER de la campaña");
        }

        members.deleteByCampaignIdAndUserId(campaignId, userId);
    }

    @Transactional
    public void transferOwnership(Long campaignId, Long toUserId, Authentication auth) {
        ensureOwner(campaignId, auth);
        User target = users.findById(toUserId).orElseThrow();

        // Asegurar que el destino sea miembro (si no, lo agregamos)
        if (!members.existsByCampaignIdAndUserId(campaignId, target.getId())) {
            Campaign c = campaigns.findById(campaignId).orElseThrow();
            members.save(CampaignMember.builder()
                    .campaign(c).user(target).role(CampaignRole.PLAYER).build());
        }

        // Degradar al owner actual (quien hace la acción) a PLAYER y promover al destino a OWNER
        User current = me(auth);
        var c = campaigns.findById(campaignId).orElseThrow();

        members.findAllByCampaignId(campaignId).forEach(m -> {
            if (m.getUser().getId().equals(current.getId())) m.setRole(CampaignRole.PLAYER);
            if (m.getUser().getId().equals(target.getId())) m.setRole(CampaignRole.OWNER);
        });
        campaigns.save(c); // flush por cascada si aplica
    }

    // ------- helpers -------
    private void ensureMember(Long campaignId, Authentication auth) {
        boolean isMember = members.findByCampaignIdAndUserEmail(campaignId, auth.getName()).isPresent();
        if (!isMember) throw new SecurityException("No perteneces a esta campaña");
    }

    private void ensureOwner(Long campaignId, Authentication auth) {
        boolean isOwner = members.existsByCampaignIdAndUserEmailAndRole(campaignId, auth.getName(), CampaignRole.OWNER);
        if (!isOwner) throw new SecurityException("Solo OWNER puede realizar esta acción");
    }

    private CampaignResponse toDto(Campaign c) {
        return new CampaignResponse(
                c.getId(),
                c.getName(),
                c.getDescription(),
                Boolean.TRUE.equals(c.getActive()),
                c.getOwner().getId(),
                c.getOwner().getEmail(),
                c.getSystem(),
                c.getSetting(),
                c.getImageUrl(),
                c.getJoinCode(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getDeletedAt()
        );
    }
    @Transactional(readOnly = true)
    public List<CampaignResponse> listUserCampaigns(Authentication auth) {
        var user = users.findByEmail(auth.getName()).orElseThrow();
        return campaigns.findAllByUserParticipation(user.getId())
                .stream().map(this::toDto).toList();
    }

    @Transactional
    public CampaignResponse update(Long id, UpdateCampaignRequest req, Authentication auth) {
        ensureOwner(id, auth);
        Campaign c = campaigns.findById(id).orElseThrow();

        if (req.name() != null && !req.name().isBlank()) c.setName(req.name());
        if (req.description() != null) c.setDescription(req.description());
        if (req.game_system() != null) c.setSystem(req.game_system());
        if (req.setting() != null) c.setSetting(req.setting());
        if (req.imageUrl() != null) c.setImageUrl(req.imageUrl());
        if (req.active() != null) {
            c.setActive(req.active());
            if (!req.active() && c.getDeletedAt() == null) c.setDeletedAt(Instant.now());
            if (req.active() && c.getDeletedAt() != null) c.setDeletedAt(null);
        }

        return toDto(campaigns.save(c));
    }

    @Transactional
    public void softDelete(Long id, Authentication auth) {
        ensureOwner(id, auth);
        Campaign c = campaigns.findById(id).orElseThrow();
        if (Boolean.FALSE.equals(c.getActive()) && c.getDeletedAt() != null) return;
        c.setActive(false);
        c.setDeletedAt(Instant.now());
        campaigns.save(c);
    }

}
