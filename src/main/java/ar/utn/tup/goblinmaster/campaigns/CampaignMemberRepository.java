// campaigns/CampaignMemberRepository.java
package ar.utn.tup.goblinmaster.campaigns;

import ar.utn.tup.goblinmaster.campaigns.CampaignMember.CampaignRole;
import ar.utn.tup.goblinmaster.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampaignMemberRepository extends JpaRepository<CampaignMember, Long> {
    Optional<CampaignMember> findByCampaignIdAndUserEmail(Long campaignId, String email);
    boolean existsByCampaignIdAndUserId(Long campaignId, Long userId);
    boolean existsByCampaignIdAndUserEmailAndRole(Long campaignId, String email, CampaignRole role);
}
