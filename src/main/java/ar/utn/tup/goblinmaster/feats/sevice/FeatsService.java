package ar.utn.tup.goblinmaster.feats.sevice;


import ar.utn.tup.goblinmaster.feats.dto.FeatsRequest;
import ar.utn.tup.goblinmaster.feats.dto.FeatsResponse;

import java.util.List;

public interface FeatsService {
    FeatsResponse createFeat(FeatsRequest request);
    List<FeatsResponse> getAllFeats();
    FeatsResponse getFeatById(Long id);
}