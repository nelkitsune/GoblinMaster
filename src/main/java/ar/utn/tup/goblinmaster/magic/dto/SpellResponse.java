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

    private Long subschoolId;
    private String subschoolName;

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
    private String summary;
    private String description;

    private String target;

    private Map<String,Integer> classLevels;

    // Métodos explícitos para compatibilidad con análisis estático
    public void setSubschoolId(Long subschoolId) { this.subschoolId = subschoolId; }
    public void setSubschoolName(String subschoolName) { this.subschoolName = subschoolName; }
    public void setTarget(String target) { this.target = target; }

    // nuevos componentes
    private boolean componentsF;
    private boolean componentsDf;

    public void setComponentsF(boolean componentsF) { this.componentsF = componentsF; }
    public void setComponentsDf(boolean componentsDf) { this.componentsDf = componentsDf; }

    // descripciones de foco
    private String focusDesc;
    private String divineFocusDesc;

    public void setFocusDesc(String focusDesc) { this.focusDesc = focusDesc; }
    public void setDivineFocusDesc(String divineFocusDesc) { this.divineFocusDesc = divineFocusDesc; }
}
