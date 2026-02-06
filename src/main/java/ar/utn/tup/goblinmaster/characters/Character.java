package ar.utn.tup.goblinmaster.characters;

import ar.utn.tup.goblinmaster.users.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "characters")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // nullable: NPC si es null

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "max_hp", nullable = false)
    private Integer maxHp;

    @Column(name = "base_initiative", nullable = false)
    private Integer baseInitiative;

    @Column(name = "is_npc", nullable = false)
    private Boolean isNpc;

    @Column(name = "deleted_at")
    private java.time.Instant deletedAt;
}
