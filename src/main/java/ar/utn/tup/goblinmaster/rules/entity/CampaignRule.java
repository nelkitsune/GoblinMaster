package ar.utn.tup.goblinmaster.rules.entity;

import ar.utn.tup.goblinmaster.campaigns.Campaign;
import ar.utn.tup.goblinmaster.rules.Rule;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campaign_rules")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CampaignRule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rule_id", nullable = false)
    private Rule rule;
}

