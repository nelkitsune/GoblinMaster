package ar.utn.tup.goblinmaster.magic.controller;

import ar.utn.tup.goblinmaster.magic.dto.SpellListItem;
import ar.utn.tup.goblinmaster.magic.dto.SpellRequest;
import ar.utn.tup.goblinmaster.magic.dto.SpellResponse;
import ar.utn.tup.goblinmaster.magic.service.SpellService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/magic/spells")
@RequiredArgsConstructor
public class SpellController {

    private final SpellService service;

    @PostMapping
    public ResponseEntity<SpellResponse> create(@Valid @RequestBody SpellRequest req, org.springframework.security.core.Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req, auth));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public SpellResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<SpellListItem> search(
            @RequestParam(value="q", required=false) String q) {
        return service.search(q);
    }

    @GetMapping("/by-class/{spellClassId}")
    @PreAuthorize("permitAll()")
    public List<SpellListItem> getBySpellClass(
            @PathVariable Long spellClassId) {
        return service.getBySpellClass(spellClassId);
    }

    // SpellController.java
    @GetMapping("/by-class/{spellClassId}/level/{level}")
    @PreAuthorize("permitAll()")
    public List<SpellListItem> getBySpellClassAndLevel(
            @PathVariable Long spellClassId,
            @PathVariable Integer level) {
        return service.getBySpellClassAndLevel(spellClassId, level);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('campaign:write')")
    public SpellResponse update(@PathVariable Long id, @Valid @RequestBody SpellRequest req, org.springframework.security.core.Authentication auth) {
        return service.update(id, req, auth);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('campaign:write')")
    public ResponseEntity<Void> delete(@PathVariable Long id, org.springframework.security.core.Authentication auth) {
        service.delete(id, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public List<SpellListItem> mine(org.springframework.security.core.Authentication auth) {
        return service.mine(auth);
    }

    @PostMapping("/{id}/campaigns/{campaignId}")
    public ResponseEntity<Void> enableInCampaign(@PathVariable Long id, @PathVariable Long campaignId,
                                                 org.springframework.security.core.Authentication auth) {
        service.enableInCampaign(id, campaignId, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/campaigns/{campaignId}/homebrew")
    public List<SpellListItem> listCampaignHomebrew(@PathVariable Long campaignId,
                                                    org.springframework.security.core.Authentication auth) {
        return service.listCampaignHomebrew(campaignId, auth);
    }

    @DeleteMapping("/campaigns/{campaignId}/{spellId}")
    public ResponseEntity<Void> disableInCampaign(@PathVariable Long campaignId, @PathVariable Long spellId,
                                                  org.springframework.security.core.Authentication auth) {
        service.disableInCampaign(campaignId, spellId, auth);
        return ResponseEntity.noContent().build();
    }
}
