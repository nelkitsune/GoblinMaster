package ar.utn.tup.goblinmaster.magic.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SpellListItem {
    private Long id;
    private String name;
    private String schoolCode;
    private String schoolName;

    private Long subschoolId;
    private String subschoolName;

    private String summary;
    private String target;
    private Map<String, Integer> classLevels;

    // Métodos explícitos para compatibilidad con análisis estático (si Lombok no es procesado)
    public void setSubschoolId(Long subschoolId) { this.subschoolId = subschoolId; }
    public void setSubschoolName(String subschoolName) { this.subschoolName = subschoolName; }
    public void setTarget(String target) { this.target = target; }
}
