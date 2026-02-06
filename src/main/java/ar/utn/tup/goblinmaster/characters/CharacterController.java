package ar.utn.tup.goblinmaster.characters;

import ar.utn.tup.goblinmaster.characters.dto.CharacterCreateRequest;
import ar.utn.tup.goblinmaster.characters.dto.CharacterResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final CharacterService service;

    public CharacterController(CharacterService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CharacterResponse> create(@Valid @RequestBody CharacterCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping
    public ResponseEntity<List<CharacterResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CharacterResponse> update(@PathVariable Long id,
                                                    @RequestBody CharacterCreateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/campaigns/{campaignId}")
    public ResponseEntity<Void> addToCampaign(@PathVariable Long id, @PathVariable Long campaignId) {
        service.addToCampaign(id, campaignId);
        return ResponseEntity.noContent().build();
    }
}

