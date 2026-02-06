package ar.utn.tup.goblinmaster.feats.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrereqGroupRequest {
    private int groupIndex;
    private List<PrereqConditionRequest> conditions;
}