package ar.utn.tup.goblinmaster.feats.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prereq_conditions")
@Getter @Setter
public class PrereqCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "prereq_group_id")
    private PrereqGroup group;

    @Enumerated(EnumType.STRING)
    private PrereqKind kind;

    @Column (name = "feat_id")
    private Long featId;

    @Column(name = "target")
    private String target; // p.ej.: STR, DEX, SKILL_NAME, SIZE, ALIGNMENT, CLASS, etc.

    @Column(name = "int_value")
    private Integer intValue; // p.ej.: 13 para ABILITY_SCORE, 6 para BAB, etc.



    public enum PrereqKind {
        FEAT,
        RACE,
        CLASS,
        ALIGNMENT,
        CHAR_LEVEL,
        CLASS_LEVEL,
        CASTER_LEVEL,
        CAN_CAST,
        KNOWN_SPELL,
        ABILITY_SCORE,
        BAB,
        SKILL_RANKS,
        SIZE,
        DEITY,
        TAG
    }
}
