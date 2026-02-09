package ar.utn.tup.goblinmaster.rules;

import ar.utn.tup.goblinmaster.rules.dto.RuleCreateRequest;
import ar.utn.tup.goblinmaster.rules.dto.RuleResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RuleService {
    private final RuleRepository rules;
    private final ar.utn.tup.goblinmaster.users.UserRepository users;

    public RuleService(RuleRepository rules, ar.utn.tup.goblinmaster.users.UserRepository users) {
        this.rules = rules; this.users = users;
    }

    private RuleResponse toResponse(Rule r) {
        return new RuleResponse(
                r.getId(), r.getName(), r.getOriginalName(), r.getDescription(), r.getPages(), r.getBooks(),
                r.getOwner() != null,
                r.getOwner() != null ? r.getOwner().getId() : null
        );
    }

    public RuleResponse createCustomRule(RuleCreateRequest req, Authentication auth) {
        var user = users.findByEmail(auth.getName()).orElseThrow();
        Rule r = Rule.builder()
                .name(req.name())
                .originalName(req.originalName())
                .description(req.description())
                .pages(req.pages())
                .books(req.books())
                .owner(user)
                .build();
        r = rules.save(r);
        return toResponse(r);
    }

    @Transactional(readOnly = true)
    public List<RuleResponse> listMyRules(Authentication auth) {
        return rules.findByOwnerEmail(auth.getName()).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RuleResponse getRule(Long id) {
        return rules.findById(id).map(this::toResponse).orElseThrow();
    }

    public RuleResponse updateRule(Long id, RuleCreateRequest req, Authentication auth) {
        Rule r = rules.findById(id).orElseThrow();
        if (r.getOwner() == null || !r.getOwner().getEmail().equals(auth.getName())) {
            throw new SecurityException("No puedes editar esta regla");
        }
        if (req.name() != null) r.setName(req.name());
        if (req.originalName() != null) r.setOriginalName(req.originalName());
        if (req.description() != null) r.setDescription(req.description());
        if (req.pages() != null) r.setPages(req.pages());
        if (req.books() != null) r.setBooks(req.books());
        return toResponse(rules.save(r));
    }

    public void deleteRule(Long id, Authentication auth) {
        Rule r = rules.findById(id).orElseThrow();
        if (r.getOwner() == null || !r.getOwner().getEmail().equals(auth.getName())) {
            throw new SecurityException("No puedes borrar esta regla");
        }
        rules.delete(r);
    }

    @Transactional(readOnly = true)
    public List<RuleResponse> listOfficialRules() {
        return rules.findByOwnerIsNull().stream().map(this::toResponse).toList();
    }
}

