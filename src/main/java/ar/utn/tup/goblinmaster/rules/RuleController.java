package ar.utn.tup.goblinmaster.rules;

import ar.utn.tup.goblinmaster.rules.dto.RuleCreateRequest;
import ar.utn.tup.goblinmaster.rules.dto.RuleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService service;

    @PostMapping
    public ResponseEntity<RuleResponse> create(@Valid @RequestBody RuleCreateRequest req, Authentication auth) {
        return ResponseEntity.ok(service.createCustomRule(req, auth));
    }

    @GetMapping("/mine")
    public List<RuleResponse> mine(Authentication auth) {
        return service.listMyRules(auth);
    }

    @GetMapping("/{id}")
    public RuleResponse get(@PathVariable Long id) {
        return service.getRule(id);
    }

    @PatchMapping("/{id}")
    public RuleResponse update(@PathVariable Long id, @RequestBody RuleCreateRequest req, Authentication auth) {
        return service.updateRule(id, req, auth);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        service.deleteRule(id, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public java.util.List<RuleResponse> listOfficial() {
        return service.listOfficialRules();
    }

    @PostMapping("/official")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RuleResponse> createOfficial(@Valid @RequestBody RuleCreateRequest req, Authentication auth) {
        return ResponseEntity.ok(service.createOfficialRule(req, auth));
    }
}
