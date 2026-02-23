// new file
package ar.utn.tup.goblinmaster.magic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "spell_subschools")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SpellSubschool {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private SpellSchool school;

    @Column(nullable = false)
    private String name;
}

