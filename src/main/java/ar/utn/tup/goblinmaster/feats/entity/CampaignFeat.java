package ar.utn.tup.goblinmaster.feats.entity;

import ar.utn.tup.goblinmaster.campaigns.Campaign;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campaign_feats")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CampaignFeat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feat_id", nullable = false)
    private Feats feat;
}

