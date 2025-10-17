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
