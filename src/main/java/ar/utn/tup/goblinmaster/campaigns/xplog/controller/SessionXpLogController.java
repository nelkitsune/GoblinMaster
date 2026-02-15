package ar.utn.tup.goblinmaster.campaigns.xplog.controller;

import ar.utn.tup.goblinmaster.campaigns.xplog.dto.CreateSessionXpLogRequest;
import ar.utn.tup.goblinmaster.campaigns.xplog.dto.SessionXpLogResponse;
import ar.utn.tup.goblinmaster.campaigns.xplog.service.SessionXpLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaigns/{campaignId}/xp-logs")
@Validated
public class SessionXpLogController {

    private final SessionXpLogService service;

    public SessionXpLogController(SessionXpLogService service) { this.service = service; }

    // Nota: createdByUserId viene por query param por ahora
    @PostMapping
    public ResponseEntity<SessionXpLogResponse> create(@PathVariable Long campaignId,
                                                       @RequestParam Long createdByUserId,
                                                       @RequestBody @Validated CreateSessionXpLogRequest req) {
        var resp = service.createLog(campaignId, createdByUserId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public Page<SessionXpLogResponse> list(@PathVariable Long campaignId, Pageable pageable) {
        return service.listLogs(campaignId, pageable);
    }

    @GetMapping("/by-participant/{userId}")
    public Page<SessionXpLogResponse> listByParticipant(@PathVariable Long campaignId,
                                                        @PathVariable Long userId,
                                                        Pageable pageable) {
        return service.listLogsByParticipantUserId(campaignId, userId, pageable);
    }
}

