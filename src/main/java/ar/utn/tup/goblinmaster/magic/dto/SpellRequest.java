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

    @NotBlank private String schoolCode;

    private Long subschoolId; // nuevo campo opcional

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
    private String summary;
    private String description;

    private String target; // nuevo campo

    private Boolean componentsF; // nuevo
    private Boolean componentsDf; // nuevo

    private String focusDesc; // nuevo: descripción del foco
    private String divineFocusDesc; // nuevo: descripción del foco divino

    @NotNull
    private Map<String,Integer> classLevels;
}
