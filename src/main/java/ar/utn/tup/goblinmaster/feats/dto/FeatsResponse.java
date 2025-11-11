package ar.utn.tup.goblinmaster.feats.dto;

import ar.utn.tup.goblinmaster.feats.entity.Feats;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeatsResponse {
    private Long id;
    private String name;
    private String originalName;
    private String code;
    private String descripcion;
    private String source;
    private String benefit;
    private String special;
    private List<Feats.Tipo> tipo;
    private List<PrereqGroupResponse> prereqGroups;

}