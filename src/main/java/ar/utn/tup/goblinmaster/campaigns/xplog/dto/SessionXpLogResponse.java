package ar.utn.tup.goblinmaster.campaigns.xplog.dto;

import java.time.Instant;
import java.util.List;

public record SessionXpLogResponse(
        Long id,
        Long campaignId,
        Integer xpGained,
        String description,
        Long createdByUserId,
        Instant createdAt,
        List<SessionParticipantResponse> participants
) {}

