package ar.utn.tup.goblinmaster.feats.sevice;


import ar.utn.tup.goblinmaster.feats.dto.FeatsRequest;
import ar.utn.tup.goblinmaster.feats.dto.FeatsResponse;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import ar.utn.tup.goblinmaster.feats.entity.Feats.Tipo;

public interface FeatsService {
    FeatsResponse createFeat(FeatsRequest request, Authentication auth);
    List<FeatsResponse> getAllFeats();
    FeatsResponse getFeatById(Long id);
    FeatsResponse updateFeat(Long id, FeatsRequest request, Authentication auth);

    // Homebrew support
    List<FeatsResponse> mine(Authentication auth);
    void enableInCampaign(Long featId, Long campaignId, Authentication auth);
    List<FeatsResponse> listCampaignHomebrew(Long campaignId, Authentication auth);
    void disableInCampaign(Long campaignId, Long featId, Authentication auth);
    Page<FeatsResponse> getFeats(Pageable pageable, Optional<List<Tipo>> tipos);
    void deleteFeat(Long id, Authentication auth);
}