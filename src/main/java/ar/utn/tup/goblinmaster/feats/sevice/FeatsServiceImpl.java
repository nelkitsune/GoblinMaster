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
}