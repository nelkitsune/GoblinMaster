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
    public ResponseEntity<SpellResponse> create(@Valid @RequestBody SpellRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('campaign:read')")
    public SpellResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('campaign:read')")
    public List<SpellListItem> search(
            @RequestParam(value="q", required=false) String q) {
        return service.search(q);
    }

    @GetMapping("/by-class/{spellClassId}")
    @PreAuthorize("hasAuthority('campaign:read')")
    public List<SpellListItem> getBySpellClass(
            @PathVariable Long spellClassId) {
        return service.getBySpellClass(spellClassId);
    }

// SpellController.java
@GetMapping("/by-class/{spellClassId}/level/{level}")
@PreAuthorize("hasAuthority('campaign:read')")
public List<SpellListItem> getBySpellClassAndLevel(
        @PathVariable Long spellClassId,
        @PathVariable Integer level) {
    return service.getBySpellClassAndLevel(spellClassId, level);
}

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('campaign:write')")
    public SpellResponse update(@PathVariable Long id, @Valid @RequestBody SpellRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('campaign:write')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id); // o softDelete si dejaste ese nombre
        return ResponseEntity.noContent().build();
    }
}
