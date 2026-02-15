package ar.utn.tup.goblinmaster.campaigns.xplog.mapper;

import ar.utn.tup.goblinmaster.campaigns.xplog.dto.CreateSessionXpLogRequest;
import ar.utn.tup.goblinmaster.campaigns.xplog.dto.SessionParticipantResponse;
import ar.utn.tup.goblinmaster.campaigns.xplog.dto.SessionXpLogResponse;
import ar.utn.tup.goblinmaster.campaigns.xplog.entity.ParticipantType;
import ar.utn.tup.goblinmaster.campaigns.xplog.entity.SessionXpLog;
import ar.utn.tup.goblinmaster.campaigns.xplog.entity.SessionXpLogParticipant;

import java.util.ArrayList;
import java.util.List;

public class SessionXpLogMapper {

    public static SessionXpLog toEntity(CreateSessionXpLogRequest req, Long campaignId, Long createdByUserId) {
        SessionXpLog log = SessionXpLog.builder()
                .campaignId(campaignId)
                .xpGained(req.xpGained())
                .description(req.description())
                .createdByUserId(createdByUserId)
                .build();
        List<SessionXpLogParticipant> parts = parseParticipantsText(req.participantsText());
        for (SessionXpLogParticipant p : parts) {
            p.setLog(log);
        }
        log.setParticipants(parts);
        return log;
    }

    public static List<SessionXpLogParticipant> parseParticipantsText(String text) {
        List<SessionXpLogParticipant> out = new ArrayList<>();
        if (text == null) return out;
        for (String raw : text.split(",")) {
            String name = raw.trim();
            if (name.isEmpty()) continue;
            SessionXpLogParticipant p = SessionXpLogParticipant.builder()
                    .participantType(ParticipantType.GUEST)
                    .displayName(name)
                    .build();
            out.add(p);
        }
        return out;
    }

    public static SessionXpLogResponse toResponse(SessionXpLog entity) {
        List<SessionParticipantResponse> participants = entity.getParticipants().stream()
                .map(p -> new SessionParticipantResponse(
                        p.getParticipantType().name(),
                        p.getUserId(),
                        p.getDisplayName()
                ))
                .toList();
        return new SessionXpLogResponse(
                entity.getId(),
                entity.getCampaignId(),
                entity.getXpGained(),
                entity.getDescription(),
                entity.getCreatedByUserId(),
                entity.getCreatedAt(),
                participants
        );
    }
}

