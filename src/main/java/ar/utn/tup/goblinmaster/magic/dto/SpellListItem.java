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
    private String summary;
    private Map<String, Integer> classLevels;
}
