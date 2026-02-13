package ar.utn.tup.goblinmaster.feats.controller;


import ar.utn.tup.goblinmaster.feats.dto.FeatsRequest;
import ar.utn.tup.goblinmaster.feats.dto.FeatsResponse;
import ar.utn.tup.goblinmaster.feats.sevice.FeatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<FeatsResponse> getAllFeats() {
        return service.getAllFeats();
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
}
