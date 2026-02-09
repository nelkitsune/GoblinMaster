package ar.utn.tup.goblinmaster.rules.dto;

public record RuleResponse(
        Long id,
        String name,
        String originalName,
        String description,
        String pages,
        String books,
        boolean isCustom,
        Long ownerUserId
) {}

