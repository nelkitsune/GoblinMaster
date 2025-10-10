// campaigns/CampaignService.java
package ar.utn.tup.goblinmaster.campaigns;

import ar.utn.tup.goblinmaster.campaigns.CampaignMember.CampaignRole;
import ar.utn.tup.goblinmaster.campaigns.dto.*;
import ar.utn.tup.goblinmaster.users.User;
import ar.utn.tup.goblinmaster.users.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CampaignService {

    private final CampaignRepository campaigns;
    private final CampaignMemberRepository members;
    private final UserRepository users;

    public CampaignService(CampaignRepository campaigns, CampaignMemberRepository members, UserRepository users) {
        this.campaigns = campaigns; this.members = members; this.users = users;
    }

    private User me(Authentication auth) {
        return users.findByEmail(auth.getName()).orElseThrow();
    }

    @Transactional
    public CampaignResponse create(CreateCampaignRequest req, Authentication auth) {
        User owner = me(auth);
        Campaign c = Campaign.builder()
                .name(req.name())
                .description(req.description())
                .owner(owner)
                .active(true)
                .build();
        // El dueño también queda como OWNER en members
        Campaign saved = campaigns.save(c);
        members.save(CampaignMember.builder()
                .campaign(saved).user(owner).role(CampaignRole.OWNER).build());

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CampaignResponse> myCampaigns(Authentication auth) {
        return campaigns.findAllForUser(auth.getName())
                .stream().map(this::toDto).toList();
    }

    @Transactional
    public void addMember(Long campaignId, AddMemberRequest req, Authentication auth) {
        // Solo OWNER puede agregar
        boolean isOwner = members.existsByCampaignIdAndUserEmailAndRole(
                campaignId, auth.getName(), CampaignRole.OWNER);
        if (!isOwner) throw new SecurityException("Solo OWNER puede agregar miembros");

        Campaign c = campaigns.findById(campaignId).orElseThrow();
        User u = users.findById(req.userId()).orElseThrow();

        if (members.existsByCampaignIdAndUserId(campaignId, u.getId())) return;

        CampaignMember m = CampaignMember.builder()
                .campaign(c).user(u)
                .role(CampaignRole.valueOf(req.role()))
                .build();
        members.save(m);
    }

    private CampaignResponse toDto(Campaign c) {
        return new CampaignResponse(
                c.getId(), c.getName(), c.getDescription(),
                Boolean.TRUE.equals(c.getActive()),
                c.getOwner().getId(), c.getOwner().getEmail()
        );
    }
}
