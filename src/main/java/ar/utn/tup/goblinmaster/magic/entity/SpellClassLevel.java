package ar.utn.tup.goblinmaster.magic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="spell_class_levels")
@Getter
@Setter
public class SpellClassLevel {
    @EmbeddedId
    private SpellClassLevelId id;

    @ManyToOne
    @MapsId("spellId")
    @JoinColumn(name="spell_id")
    private Spell spell;

    @ManyToOne @MapsId("classId")
    @JoinColumn(name="class_id")
    private SpellClass spellClass;

    @Column(nullable=false)
    private Integer level; // 0..9
}
