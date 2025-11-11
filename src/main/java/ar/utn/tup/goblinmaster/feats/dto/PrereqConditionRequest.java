package ar.utn.tup.goblinmaster.feats.dto;


import ar.utn.tup.goblinmaster.feats.entity.PrereqCondition.PrereqKind;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrereqConditionRequest {
    private PrereqKind kind;
    private Long featId; // cuando kind = FEAT
    private String target; // p.ej. STR, DEX, SKILL_NAME, CLASS, ALIGNMENT
    private Integer intValue; // p.ej. 13 (ABILITY_SCORE), 6 (BAB), nivel, rangos
}