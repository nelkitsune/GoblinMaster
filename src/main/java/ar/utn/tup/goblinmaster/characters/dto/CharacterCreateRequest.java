package ar.utn.tup.goblinmaster.characters.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CharacterCreateRequest(
        Long userId,
        @NotBlank @Size(max = 120) String name,
        @NotNull Integer maxHp,
        @NotNull Integer baseInitiative,
        Boolean isNpc
) {}

