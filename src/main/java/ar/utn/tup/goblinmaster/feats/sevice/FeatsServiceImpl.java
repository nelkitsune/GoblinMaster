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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatsServiceImpl implements FeatsService {

    private final FeatsRepository featsRepository;

    @Override
    public FeatsResponse createFeat(FeatsRequest request) {
        Feats feat = FeatsMapper.toEntity(request);

        // Crear y asociar grupos y condiciones
        if (request.getPrereqGroups() != null) {
            List<PrereqGroup> groups = new ArrayList<>();
            for (PrereqGroupRequest groupReq : request.getPrereqGroups()) {
                PrereqGroup group = new PrereqGroup();
                group.setFeat(feat);
                group.setGroupIndex(groupReq.getGroupIndex());

                List<PrereqCondition> conditions = new ArrayList<>();
                if (groupReq.getConditions() != null) {
                    for (PrereqConditionRequest condReq : groupReq.getConditions()) {
                        PrereqCondition cond = new PrereqCondition();
                        cond.setGroup(group);
                        cond.setKind(condReq.getKind());
                        conditions.add(cond);
                    }
                }
                group.setConditions(conditions);
                groups.add(group);
            }
            feat.setPrereqGroups(groups);
        }

        Feats saved = featsRepository.save(feat);
        return FeatsMapper.toResponse(saved);
    }

    @Override
    public List<FeatsResponse> getAllFeats() {
        return featsRepository.findAll().stream()
                .map(FeatsMapper::toResponse)
                .toList();
    }
}