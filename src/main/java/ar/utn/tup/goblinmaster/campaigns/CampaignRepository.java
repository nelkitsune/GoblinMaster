// campaigns/CampaignRepository.java
package ar.utn.tup.goblinmaster.campaigns;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    @Query("""
      select c from Campaign c
      where c.owner.email = :email
         or exists (select 1 from CampaignMember m
                    where m.campaign = c and m.user.email = :email)
      order by c.createdAt desc
    """)
    List<Campaign> findAllForUser(@Param("email") String email);

    @Query("""
        SELECT DISTINCT c
        FROM Campaign c
        LEFT JOIN c.members m
        WHERE c.owner.id = :userId or m.user.id = :userId
""")
    List<Campaign> findAllByUserParticipation(@Param("userId") Long userId);

    boolean existsByJoinCode(String joinCode);
}
