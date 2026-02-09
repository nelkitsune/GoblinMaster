package ar.utn.tup.goblinmaster.feats.sevice;


import ar.utn.tup.goblinmaster.feats.dto.FeatsRequest;
import ar.utn.tup.goblinmaster.feats.dto.FeatsResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface FeatsService {
    FeatsResponse createFeat(FeatsRequest request);
    List<FeatsResponse> getAllFeats();
    FeatsResponse getFeatById(Long id);

    // Homebrew support
    List<FeatsResponse> mine(Authentication auth);
    void enableInCampaign(Long featId, Long campaignId, Authentication auth);
    List<FeatsResponse> listCampaignHomebrew(Long campaignId, Authentication auth);
    void disableInCampaign(Long campaignId, Long featId, Authentication auth);
}