package ar.utn.tup.goblinmaster.magic.mapper;

import ar.utn.tup.goblinmaster.magic.dto.SpellListItem;
import ar.utn.tup.goblinmaster.magic.dto.SpellResponse;
import ar.utn.tup.goblinmaster.magic.entity.Spell;
import ar.utn.tup.goblinmaster.magic.entity.SpellClassLevel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SpellMapper {

    private final ModelMapper mm;

    public SpellResponse toResponse(Spell s, List<SpellClassLevel> scl) {
        SpellResponse dto = mm.map(s, SpellResponse.class);
        dto.setSchoolCode(s.getSchool().getCode());
        dto.setSchoolName(s.getSchool().getName());

        Map<String, Integer> classLevels = new LinkedHashMap<>();
        for (SpellClassLevel x : scl) {
            classLevels.put(x.getSpellClass().getCode(), x.getLevel());
        }
        dto.setClassLevels(classLevels);
        return dto;
    }

    public SpellListItem toListItem(Spell s, List<SpellClassLevel> scl) {
        SpellListItem dto = new SpellListItem();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setSchoolCode(s.getSchool().getCode());
        dto.setSchoolName(s.getSchool().getName());
        dto.setSummary(generarResumen(s.getDescription()));

        Map<String, Integer> levels = new HashMap<>();
        for (SpellClassLevel x : scl) {
            levels.put(x.getSpellClass().getCode(), x.getLevel());
        }
        dto.setClassLevels(levels);
        return dto;
    }

    private String generarResumen(String descripcion) {
        if (descripcion == null) return null;
        return descripcion.length() > 140 ? descripcion.substring(0, 140) + "…" : descripcion;
    }
}