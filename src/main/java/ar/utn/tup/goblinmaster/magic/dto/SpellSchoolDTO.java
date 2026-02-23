// new file
package ar.utn.tup.goblinmaster.magic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpellSchoolDTO {
    private Long id;
    private String code;
    private String name;
    private List<SpellSubschoolDTO> subschools;
}

