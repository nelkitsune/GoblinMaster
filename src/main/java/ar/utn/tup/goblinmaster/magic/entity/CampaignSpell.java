package ar.utn.tup.goblinmaster.magic.entity;

import ar.utn.tup.goblinmaster.campaigns.Campaign;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campaign_spells")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CampaignSpell {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spell_id", nullable = false)
    private Spell spell;
}

