package ar.utn.tup.goblinmaster.magic.controller;

import ar.utn.tup.goblinmaster.magic.dto.SpellListItem;
import ar.utn.tup.goblinmaster.magic.dto.SpellRequest;
import ar.utn.tup.goblinmaster.magic.dto.SpellResponse;
import ar.utn.tup.goblinmaster.magic.service.SpellService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public Page<SpellListItem> search(
            @RequestParam(value="q", required=false) String q,
            @PageableDefault(size=20, sort="name") Pageable pageable) {
        return service.search(q, pageable);
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
