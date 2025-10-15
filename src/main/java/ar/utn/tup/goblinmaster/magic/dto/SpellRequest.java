package ar.utn.tup.goblinmaster.magic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SpellRequest {
    @NotBlank
    private String name;
    private String originalName;

    @NotBlank private String schoolCode; // EVOCATION, etc.

    private String castingTime;
    private String rangeText;
    private String areaText;
    private String durationText;
    private String savingThrow;
    private Boolean spellResistance;

    private Boolean componentsV;
    private Boolean componentsS;
    private Boolean componentsM;
    private String materialDesc;

    private String source;
    private String description;

    // niveles por clase: {"WIZARD":3,"SORCERER":3,"MAGUS":3,"OCCULTIST":3,"BLOODRAGER":3}
    @NotNull
    private Map<String,Integer> classLevels;
}
