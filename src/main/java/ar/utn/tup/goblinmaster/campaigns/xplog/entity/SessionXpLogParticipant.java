package ar.utn.tup.goblinmaster.campaigns.xplog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "session_xp_log_participant")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionXpLogParticipant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false)
    private SessionXpLog log;

    @Enumerated(EnumType.STRING)
    @Column(name = "participant_type", nullable = false)
    private ParticipantType participantType;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "display_name", length = 120)
    private String displayName;
}

