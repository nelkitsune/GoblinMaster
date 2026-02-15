package ar.utn.tup.goblinmaster.campaigns.xplog.service;

import ar.utn.tup.goblinmaster.campaigns.xplog.dto.CreateSessionXpLogRequest;
import ar.utn.tup.goblinmaster.campaigns.xplog.dto.SessionXpLogResponse;
import ar.utn.tup.goblinmaster.campaigns.xplog.entity.SessionXpLog;
import ar.utn.tup.goblinmaster.campaigns.xplog.mapper.SessionXpLogMapper;
import ar.utn.tup.goblinmaster.campaigns.xplog.repository.SessionXpLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@Transactional
public class SessionXpLogService {

    private final SessionXpLogRepository repo;

    public SessionXpLogService(SessionXpLogRepository repo) { this.repo = repo; }

    public SessionXpLogResponse createLog(Long campaignId, Long createdByUserId, CreateSessionXpLogRequest req) {
        if (req.xpGained() == null || req.xpGained() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "xpGained debe ser >= 0");
        }
        var participants = SessionXpLogMapper.parseParticipantsText(req.participantsText());
        if (participants.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe incluir al menos un participante");
        }
        SessionXpLog log = SessionXpLogMapper.toEntity(req, campaignId, createdByUserId);
        SessionXpLog saved = repo.save(log);
        return SessionXpLogMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<SessionXpLogResponse> listLogs(Long campaignId, Pageable pageable) {
        return repo.findByCampaignIdOrderByCreatedAtDesc(campaignId, pageable)
                .map(SessionXpLogMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<SessionXpLogResponse> listLogsByParticipantUserId(Long campaignId, Long userId, Pageable pageable) {
        return repo.findByCampaignIdAndParticipantUserId(campaignId, userId, pageable)
                .map(SessionXpLogMapper::toResponse);
    }
}

