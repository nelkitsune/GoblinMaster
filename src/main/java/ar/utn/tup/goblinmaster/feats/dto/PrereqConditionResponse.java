package ar.utn.tup.goblinmaster.feats.dto;

import ar.utn.tup.goblinmaster.feats.entity.PrereqCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrereqConditionResponse {
    private PrereqCondition.PrereqKind kind;
    private String target;   // p.ej. STR, DEX, SKILL_NAME, CLASS, ALIGNMENT
    private Integer intValue; // p.ej. 13 (ABILITY_SCORE), 6 (BAB)
    private Long featId;     // cuando kind = FEAT
}