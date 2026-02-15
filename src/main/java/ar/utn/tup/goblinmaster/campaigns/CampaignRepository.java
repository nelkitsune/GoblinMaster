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

    Campaign findByJoinCode(String joinCode);

    @Query("""
        SELECT c as campaign, COUNT(DISTINCT m) as membersCount
        FROM Campaign c
        LEFT JOIN c.members m
        LEFT JOIN c.members cmUser
        WHERE (c.owner.id = :userId OR cmUser.user.id = :userId)
          AND (:active IS NULL OR c.active = :active)
          AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        GROUP BY c
        ORDER BY c.createdAt DESC
    """)
    List<CampaignWithCount> findUserCampaignsWithFilters(@Param("userId") Long userId,
                                                         @Param("active") Boolean active,
                                                         @Param("name") String name);

    interface CampaignWithCount {
        Campaign getCampaign();
        long getMembersCount();
    }
}
