package ar.utn.tup.goblinmaster.magic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpellListItem {
    private Long id;
    private String name;
    private String schoolCode;
    private String schoolName;
    private String summary; // opcional: primeras N palabras de description
}
