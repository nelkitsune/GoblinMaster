package ar.utn.tup.goblinmaster.campaigns.xplog.dto;

import jakarta.validation.constraints.*;

public record CreateSessionXpLogRequest(
        @NotNull @Min(0) Integer xpGained,
        @Size(max = 5000) String description,
        @NotBlank String participantsText
) {}

