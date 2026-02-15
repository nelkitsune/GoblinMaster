package ar.utn.tup.goblinmaster.rules.repository;

import ar.utn.tup.goblinmaster.rules.entity.CampaignRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRuleRepository extends JpaRepository<CampaignRule, Long> {
    boolean existsByCampaignIdAndRuleId(Long campaignId, Long ruleId);
    List<CampaignRule> findAllByCampaignId(Long campaignId);
    void deleteByCampaignIdAndRuleId(Long campaignId, Long ruleId);
}

