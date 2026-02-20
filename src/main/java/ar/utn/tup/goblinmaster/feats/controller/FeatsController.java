package ar.utn.tup.goblinmaster.feats.controller;


import ar.utn.tup.goblinmaster.feats.dto.FeatsRequest;
import ar.utn.tup.goblinmaster.feats.dto.FeatsResponse;
import ar.utn.tup.goblinmaster.feats.entity.Feats;
import ar.utn.tup.goblinmaster.feats.sevice.FeatsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feats")
public class FeatsController {
    private final FeatsService service;

    public FeatsController(FeatsService service) {
        this.service = service;
    }

    @PostMapping
    public void createFeat(@RequestBody FeatsRequest request, Authentication auth) {
        service.createFeat(request, auth);
    }

    @GetMapping
    public Page<FeatsResponse> getFeats(
            @PageableDefault(sort = {"name"}, direction = org.springframework.data.domain.Sort.Direction.ASC) Pageable pageable,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "tipos", required = false) String tiposCsv) {
        int size = pageable.getPageSize();
        if (size > 100) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "size máximo permitido: 100");
        }
        Optional<List<Feats.Tipo>> tiposOpt = Optional.empty();
        List<Feats.Tipo> tiposList = new ArrayList<>();
        try {
            if (tiposCsv != null && !tiposCsv.isBlank()) {
                for (String t : tiposCsv.split(",")) {
                    if (!t.isBlank()) tiposList.add(Feats.Tipo.valueOf(t.trim()));
                }
            }
            if (tipo != null && !tipo.isBlank()) {
                tiposList.add(Feats.Tipo.valueOf(tipo.trim()));
            }
            if (!tiposList.isEmpty()) tiposOpt = Optional.of(tiposList);
        } catch (IllegalArgumentException ex) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo inválido: use valores de enum Tipo");
        }
        return service.getFeats(pageable, tiposOpt);
    }

    @GetMapping("/{id}")
    public FeatsResponse getFeatById(@PathVariable Long id) {
        return service.getFeatById(id);
    }

    @GetMapping("/mine")
    public List<FeatsResponse> mine(Authentication auth) {
        return service.mine(auth);
    }

    @PostMapping("/{id}/campaigns/{campaignId}")
    public ResponseEntity<Void> enableInCampaign(@PathVariable Long id, @PathVariable Long campaignId,
                                                 Authentication auth) {
        service.enableInCampaign(id, campaignId, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/campaigns/{campaignId}/homebrew")
    public List<FeatsResponse> listCampaignHomebrew(@PathVariable Long campaignId,
                                                    Authentication auth) {
        return service.listCampaignHomebrew(campaignId, auth);
    }

    @DeleteMapping("/campaigns/{campaignId}/{featId}")
    public ResponseEntity<Void> disableInCampaign(@PathVariable Long campaignId, @PathVariable Long featId,
                                                  Authentication auth) {
        service.disableInCampaign(campaignId, featId, auth);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public FeatsResponse updateFeat(@PathVariable Long id, @RequestBody FeatsRequest request, Authentication auth) {
        return service.updateFeat(id, request, auth);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeat(@PathVariable Long id, Authentication auth) {
        service.deleteFeat(id, auth);
        return ResponseEntity.noContent().build();
    }
}
