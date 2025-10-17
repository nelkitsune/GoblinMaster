package ar.utn.tup.goblinmaster.feats.dto;


import ar.utn.tup.goblinmaster.feats.entity.PrereqCondition.PrereqKind;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrereqConditionRequest {
    private PrereqKind kind;
}