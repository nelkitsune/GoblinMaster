package ar.utn.tup.goblinmaster.feats.dto;

import ar.utn.tup.goblinmaster.feats.entity.Feats;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatResponseDTO {
    private Long id;
    private String name;
    private Feats.Tipo tipo;
    private String source;
    private String code;
}
