package ar.utn.tup.goblinmaster.campaigns.xplog.dto;

public record SessionParticipantResponse(
        String participantType,
        Long userId,
        String displayName
) {}

