package ar.utn.tup.goblinmaster.campaigns.xplog.repository;

import ar.utn.tup.goblinmaster.campaigns.xplog.entity.SessionXpLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessionXpLogRepository extends JpaRepository<SessionXpLog, Long> {
    @EntityGraph(attributePaths = {"participants"})
    Page<SessionXpLog> findByCampaignIdOrderByCreatedAtDesc(Long campaignId, Pageable pageable);

    @EntityGraph(attributePaths = {"participants"})
    @Query("select distinct l from SessionXpLog l join l.participants p where l.campaignId = :campaignId and p.userId = :userId order by l.createdAt desc")
    Page<SessionXpLog> findByCampaignIdAndParticipantUserId(Long campaignId, Long userId, Pageable pageable);
}

