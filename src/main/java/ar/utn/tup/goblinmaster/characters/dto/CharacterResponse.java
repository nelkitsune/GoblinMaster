package ar.utn.tup.goblinmaster.characters.dto;

public record CharacterResponse(
        Long id,
        Long userId,
        String name,
        Integer maxHp,
        Integer baseInitiative,
        Boolean isNpc
) {}

