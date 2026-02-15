package ar.utn.tup.goblinmaster.feats.entity;

import ar.utn.tup.goblinmaster.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "feats")
@Getter @Setter
public class Feats {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "original_name", unique = true, nullable = false)
    private String originalName;

    @Column(name="descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "Benefit", nullable = false, columnDefinition = "TEXT")
    private String benefit;

    @Column(name = "Special")
    private String special;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private User owner; // null => oficial, no null => homebrew

    @OneToMany(mappedBy = "feat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PrereqGroup> prereqGroups = new HashSet<>();

    public enum Tipo {
        ARTISTICAS,
        COOPERATIVAS,
        AGALLAS,
        COMBATE,
        ESTILO,
        CREACION_DE_OBJETOS,
        CRITICO,
        METAMAGICAS,
        RACIAL
    }

    public void setPrereqGroups(Set<PrereqGroup> prereqGroups) {
        this.prereqGroups = prereqGroups;
    }
}