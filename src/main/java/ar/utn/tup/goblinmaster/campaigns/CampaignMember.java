package ar.utn.tup.goblinmaster.campaigns;

import ar.utn.tup.goblinmaster.users.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "campaign_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"campaign_id","user_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CampaignMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="campaign_id")
    private Campaign campaign;

    @ManyToOne(optional=false) @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=16)
    private CampaignRole role;

    public enum CampaignRole { OWNER, PLAYER }
}
