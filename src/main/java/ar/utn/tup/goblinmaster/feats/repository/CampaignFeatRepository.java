package ar.utn.tup.goblinmaster.feats.repository;

import ar.utn.tup.goblinmaster.feats.entity.CampaignFeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampaignFeatRepository extends JpaRepository<CampaignFeat, Long> {
    boolean existsByCampaignIdAndFeatId(Long campaignId, Long featId);
    List<CampaignFeat> findAllByCampaignId(Long campaignId);
    void deleteByCampaignIdAndFeatId(Long campaignId, Long featId);
}

