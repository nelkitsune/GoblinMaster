package ar.utn.tup.goblinmaster.campaigns.xplog.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "session_xp_log")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionXpLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Column(name = "xp_gained", nullable = false)
    private Integer xpGained;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_by_user_id", nullable = false)
    private Long createdByUserId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() { if (createdAt == null) createdAt = Instant.now(); }

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SessionXpLogParticipant> participants = new ArrayList<>();
}

