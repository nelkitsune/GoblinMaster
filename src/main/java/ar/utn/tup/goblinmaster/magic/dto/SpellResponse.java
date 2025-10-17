package ar.utn.tup.goblinmaster.magic.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SpellResponse {
    private Long id;
    private String name;
    private String originalName;
    private String schoolCode;
    private String schoolName;

    private String castingTime;
    private String rangeText;
    private String areaText;
    private String durationText;
    private String savingThrow;
    private boolean spellResistance;

    private boolean componentsV;
    private boolean componentsS;
    private boolean componentsM;
    private String materialDesc;

    private String source;
    private String description;

    private Map<String,Integer> classLevels;
}
