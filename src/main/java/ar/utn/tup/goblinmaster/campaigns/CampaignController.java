// campaigns/CampaignController.java
package ar.utn.tup.goblinmaster.campaigns;

import ar.utn.tup.goblinmaster.campaigns.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService service;

    public CampaignController(CampaignService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CampaignResponse> create(@Valid @RequestBody CreateCampaignRequest req,
                                                   Authentication auth) {
        return ResponseEntity.ok(service.create(req, auth));
    }

    @GetMapping
    public ResponseEntity<List<CampaignResponse>> mine(Authentication auth) {
        return ResponseEntity.ok(service.myCampaigns(auth));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addMember(@PathVariable Long id,
                                          @Valid @RequestBody AddMemberRequest req,
                                          Authentication auth) {
        service.addMember(id, req, auth);
        return ResponseEntity.noContent().build();
    }
}
