package ar.utn.tup.goblinmaster.rules.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RuleCreateRequest(
        @NotBlank @Size(max=200) String name,
        @Size(max=200) String originalName,
        @NotBlank String description,
        @Size(max=100) String pages,
        @Size(max=200) String books
) {}

