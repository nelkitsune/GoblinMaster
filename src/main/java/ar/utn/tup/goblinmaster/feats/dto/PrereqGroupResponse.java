package ar.utn.tup.goblinmaster.feats.dto;

import java.util.List;

public class PrereqGroupResponse {
    private int groupIndex;
    private List<PrereqConditionResponse> conditions;
    public PrereqGroupResponse(int groupIndex, List<PrereqConditionResponse> conditions) {
        this.groupIndex = groupIndex;
        this.conditions = conditions;
    }
    public int getGroupIndex() {
        return groupIndex;
    }
    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }
    public List<PrereqConditionResponse> getConditions() {
        return conditions;
    }
    public void setConditions(List<PrereqConditionResponse> conditions) {
        this.conditions = conditions;
    }
}