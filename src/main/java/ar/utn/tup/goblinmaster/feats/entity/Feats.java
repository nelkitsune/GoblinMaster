package ar.utn.tup.goblinmaster.feats.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "feat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrereqGroup> prereqGroups;

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

    public void setPrereqGroups(List<PrereqGroup> prereqGroups) {
        this.prereqGroups = prereqGroups;
    }
}