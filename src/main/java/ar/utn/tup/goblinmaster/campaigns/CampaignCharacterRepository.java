package ar.utn.tup.goblinmaster.campaigns;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CampaignCharacterRepository extends JpaRepository<CampaignCharacter, Long> {
    boolean existsByCampaignIdAndCharacterId(Long campaignId, Long characterId);
    List<CampaignCharacter> findAllByCampaignId(Long campaignId);
    void deleteByCampaignIdAndCharacterId(Long campaignId, Long characterId);
    Optional<CampaignCharacter> findByCampaignIdAndCharacterId(Long campaignId, Long characterId);
}
