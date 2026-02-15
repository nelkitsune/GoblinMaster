package ar.utn.tup.goblinmaster.campaigns;

import ar.utn.tup.goblinmaster.characters.Character;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campaign_characters")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CampaignCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(name = "deleted_at")
    private java.time.Instant deletedAt;
}
