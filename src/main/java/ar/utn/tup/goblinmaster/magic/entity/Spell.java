package ar.utn.tup.goblinmaster.magic.entity;

import ar.utn.tup.goblinmaster.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "spells")
@Getter @Setter
public class Spell {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "original_name")
    private String originalName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "school_id")
    private SpellSchool school;

    @Column(name = "casting_time")
    private String castingTime;

    @Column(name = "range_text")
    private String rangeText;

    @Column(name = "area_text")
    private String areaText;

    @Column(name = "duration_text")
    private String durationText;

    @Column(name = "saving_throw")
    private String savingThrow;

    @Column(name = "spell_resistance", nullable = false)
    private boolean spellResistance;

    @Column(name = "components_v", nullable = false)
    private boolean componentsV;

    @Column(name = "components_s", nullable = false)
    private boolean componentsS;

    @Column(name = "components_m", nullable = false)
    private boolean componentsM;

    @Column(name = "material_desc")
    private String materialDesc;

    private String source;

    @Column(name = "summary")
    private String summary;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private User owner; // null => oficial, no null => homebrew
}
