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

    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> getOne(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(service.getOne(id, auth));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CampaignResponse> update(@PathVariable Long id,
                                                   @RequestBody UpdateCampaignRequest req,
                                                   Authentication auth) {
        return ResponseEntity.ok(service.update(id, req, auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id, Authentication auth) {
        service.softDelete(id, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<MemberResponse>> members(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(service.listMembers(id, auth));
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long id, @PathVariable Long userId, Authentication auth) {
        service.removeMember(id, userId, auth);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/transfer-owner")
    public ResponseEntity<Void> transferOwner(@PathVariable Long id,
                                              @RequestParam Long toUserId,
                                              Authentication auth) {
        service.transferOwnership(id, toUserId, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<CampaignResponse>> listMyCampaigns(Authentication auth) {
        var result = service.listUserCampaigns(auth);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/join")
    public ResponseEntity<CampaignResponse> join(@RequestBody JoinByCodeRequest req,
                                                 Authentication auth) {
        return ResponseEntity.ok(service.joinByCode(req.code(), auth));
    }

}
