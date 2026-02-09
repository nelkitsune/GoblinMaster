package ar.utn.tup.goblinmaster.magic.repository;

import ar.utn.tup.goblinmaster.magic.entity.CampaignSpell;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampaignSpellRepository extends JpaRepository<CampaignSpell, Long> {
    boolean existsByCampaignIdAndSpellId(Long campaignId, Long spellId);
    List<CampaignSpell> findAllByCampaignId(Long campaignId);
    void deleteByCampaignIdAndSpellId(Long campaignId, Long spellId);
}

