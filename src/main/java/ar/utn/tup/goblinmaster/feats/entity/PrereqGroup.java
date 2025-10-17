package ar.utn.tup.goblinmaster.feats.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prereq_groups")
@Getter @Setter
public class PrereqGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "feat_id")
    private Feats feat;

    private int groupIndex;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrereqCondition> conditions = new ArrayList<>();

}
