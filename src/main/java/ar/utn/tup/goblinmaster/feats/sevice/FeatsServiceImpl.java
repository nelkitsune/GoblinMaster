package ar.utn.tup.goblinmaster.feats.sevice;

import ar.utn.tup.goblinmaster.feats.dto.FeatsRequest;
import ar.utn.tup.goblinmaster.feats.dto.FeatsResponse;
import ar.utn.tup.goblinmaster.feats.dto.PrereqGroupRequest;
import ar.utn.tup.goblinmaster.feats.dto.PrereqConditionRequest;
import ar.utn.tup.goblinmaster.feats.entity.Feats;
import ar.utn.tup.goblinmaster.feats.entity.PrereqGroup;
import ar.utn.tup.goblinmaster.feats.entity.PrereqCondition;
import ar.utn.tup.goblinmaster.feats.mapper.FeatsMapper;
import ar.utn.tup.goblinmaster.feats.repository.FeatsRepository;
import ar.utn.tup.goblinmaster.campaigns.CampaignMemberRepository;
import ar.utn.tup.goblinmaster.campaigns.CampaignMember.CampaignRole;
import ar.utn.tup.goblinmaster.campaigns.CampaignRepository;
import ar.utn.tup.goblinmaster.feats.entity.CampaignFeat;
import ar.utn.tup.goblinmaster.feats.repository.CampaignFeatRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class FeatsServiceImpl implements FeatsService {

    private final FeatsRepository featsRepository;
    private final CampaignFeatRepository campaignFeatRepository;
    private final CampaignRepository campaignRepository;
    private final CampaignMemberRepository campaignMemberRepository;

    @Override
    public FeatsResponse createFeat(FeatsRequest request) {
        Feats feat = FeatsMapper.toEntity(request);

        // Crear y asociar grupos y condiciones (fusionando mismos groupIndex)
        if (request.getPrereqGroups() != null) {
            Map<Integer, PrereqGroup> gruposPorIndice = new HashMap<>();
            for (PrereqGroupRequest groupReq : request.getPrereqGroups()) {
                int idx = groupReq.getGroupIndex();
                PrereqGroup group = gruposPorIndice.computeIfAbsent(idx, k -> {
                    PrereqGroup nuevo = new PrereqGroup();
                    nuevo.setFeat(feat);
                    nuevo.setGroupIndex(k);
                    nuevo.setConditions(new HashSet<>());
                    return nuevo;
                });
                if (groupReq.getConditions() != null) {
                    for (PrereqConditionRequest condReq : groupReq.getConditions()) {
                        PrereqCondition cond = new PrereqCondition();
                        cond.setGroup(group);
                        cond.setKind(condReq.getKind());
                        cond.setFeatId(condReq.getFeatId());
                        cond.setTarget(condReq.getTarget());
                        cond.setIntValue(condReq.getIntValue());
                        group.getConditions().add(cond);
                    }
                }
            }
            feat.setPrereqGroups(new HashSet<>(gruposPorIndice.values()));
        }

        Feats saved = featsRepository.save(feat);
        return FeatsMapper.toResponse(saved);
    }

    @Override
    public List<FeatsResponse> getAllFeats() {
        return featsRepository.findAllConPrereqs().stream()
                .map(FeatsMapper::toResponse)
                .toList();
    }
    @Override
    public FeatsResponse getFeatById(Long id) {
        Feats feat = featsRepository.findByIdConPrereqs(id)
                .orElseThrow(() -> new RuntimeException("Feat not found with id: " + id));
        return FeatsMapper.toResponse(feat);
    }

    @Override
    public List<FeatsResponse> mine(Authentication auth) {
        var email = auth.getName();
        return featsRepository.findByOwnerEmail(email).stream()
                .map(FeatsMapper::toResponse)
                .toList();
    }

    @Override
    public void enableInCampaign(Long featId, Long campaignId, Authentication auth) {
        var campaign = campaignRepository.findById(campaignId).orElseThrow();
        boolean isOwner = campaignMemberRepository.existsByCampaignIdAndUserEmailAndRole(campaignId, auth.getName(), CampaignRole.OWNER);
        if (!isOwner) throw new SecurityException("No autorizado para modificar la campaña");
        Feats f = featsRepository.findById(featId).orElseThrow();
        if (f.getOwner() == null) throw new IllegalArgumentException("Solo se puede habilitar homebrew");
        if (!f.getOwner().getEmail().equals(auth.getName())) throw new SecurityException("Homebrew ajeno");
        if (!campaignFeatRepository.existsByCampaignIdAndFeatId(campaignId, featId)) {
            CampaignFeat cf = CampaignFeat.builder().campaign(campaign).feat(f).build();
            campaignFeatRepository.save(cf);
        }
    }

    @Override
    public List<FeatsResponse> listCampaignHomebrew(Long campaignId, Authentication auth) {
        boolean isMember = campaignMemberRepository.findByCampaignIdAndUserEmail(campaignId, auth.getName()).isPresent();
        if (!isMember) throw new SecurityException("No perteneces a la campaña");
        return campaignFeatRepository.findAllByCampaignId(campaignId).stream()
                .map(cf -> FeatsMapper.toResponse(cf.getFeat()))
                .toList();
    }

    @Override
    public void disableInCampaign(Long campaignId, Long featId, Authentication auth) {
        boolean isOwner = campaignMemberRepository.existsByCampaignIdAndUserEmailAndRole(campaignId, auth.getName(), CampaignRole.OWNER);
        if (!isOwner) throw new SecurityException("No autorizado para modificar la campaña");
        campaignFeatRepository.deleteByCampaignIdAndFeatId(campaignId, featId);
    }
}