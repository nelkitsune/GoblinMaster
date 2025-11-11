package ar.utn.tup.goblinmaster.feats.dto;

import ar.utn.tup.goblinmaster.feats.entity.Feats;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeatsRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String originalName;
    @NotBlank
    private String code;
    @NotBlank
    private String source;
    @NotBlank
    private String benefit;
    private String special;
    private String descripcion;
    private List<Feats.Tipo> tipo;
    private List<PrereqGroupRequest> prereqGroups;
}