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
    // nuevos repos
    private final ar.utn.tup.goblinmaster.campaigns.CampaignRepository campaignRepo;
    private final ar.utn.tup.goblinmaster.campaigns.CampaignMemberRepository campaignMemberRepo;
    private final ar.utn.tup.goblinmaster.rules.repository.CampaignRuleRepository campaignRuleRepo;

    public RuleService(RuleRepository rules, ar.utn.tup.goblinmaster.users.UserRepository users,
                       ar.utn.tup.goblinmaster.campaigns.CampaignRepository campaignRepo,
                       ar.utn.tup.goblinmaster.campaigns.CampaignMemberRepository campaignMemberRepo,
                       ar.utn.tup.goblinmaster.rules.repository.CampaignRuleRepository campaignRuleRepo) {
        this.rules = rules; this.users = users;
        this.campaignRepo = campaignRepo; this.campaignMemberRepo = campaignMemberRepo; this.campaignRuleRepo = campaignRuleRepo;
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

    public RuleResponse createOfficialRule(RuleCreateRequest req, Authentication auth) {
        var user = users.findByEmail(auth.getName()).orElseThrow();
        if (user.getRole() != ar.utn.tup.goblinmaster.users.User.Role.ADMIN) {
            throw new SecurityException("Solo ADMIN puede crear reglas oficiales");
        }
        Rule r = Rule.builder()
                .name(req.name())
                .originalName(req.originalName())
                .description(req.description())
                .pages(req.pages())
                .books(req.books())
                .owner(null)
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

    public void enableInCampaign(Long ruleId, Long campaignId, Authentication auth) {
        var campaign = campaignRepo.findById(campaignId).orElseThrow(() -> new java.util.NoSuchElementException("Campaña no encontrada"));
        boolean isOwner = campaignMemberRepo.existsByCampaignIdAndUserEmailAndRole(campaignId, auth.getName(), ar.utn.tup.goblinmaster.campaigns.CampaignMember.CampaignRole.OWNER);
        if (!isOwner) throw new SecurityException("No autorizado para modificar la campaña");
        Rule r = rules.findById(ruleId).orElseThrow(() -> new java.util.NoSuchElementException("Regla no encontrada"));
        if (r.getOwner() == null) throw new IllegalArgumentException("Solo se puede habilitar homebrew");
        if (!r.getOwner().getEmail().equals(auth.getName())) throw new SecurityException("Homebrew ajeno");
        if (!campaignRuleRepo.existsByCampaignIdAndRuleId(campaignId, ruleId)) {
            ar.utn.tup.goblinmaster.rules.entity.CampaignRule cr = ar.utn.tup.goblinmaster.rules.entity.CampaignRule.builder()
                    .campaign(campaign)
                    .rule(r)
                    .build();
            campaignRuleRepo.save(cr);
        }
    }

    @Transactional(readOnly = true)
    public java.util.List<ar.utn.tup.goblinmaster.rules.dto.RuleResponse> listCampaignHomebrew(Long campaignId, Authentication auth) {
        boolean isMember = campaignMemberRepo.findByCampaignIdAndUserEmail(campaignId, auth.getName()).isPresent();
        if (!isMember) throw new SecurityException("No perteneces a la campaña");
        return campaignRuleRepo.findAllByCampaignId(campaignId).stream()
                .map(cr -> this.toResponse(cr.getRule()))
                .toList();
    }

    public void disableInCampaign(Long campaignId, Long ruleId, Authentication auth) {
        boolean isOwner = campaignMemberRepo.existsByCampaignIdAndUserEmailAndRole(campaignId, auth.getName(), ar.utn.tup.goblinmaster.campaigns.CampaignMember.CampaignRole.OWNER);
        if (!isOwner) throw new SecurityException("No autorizado para modificar la campaña");
        campaignRuleRepo.deleteByCampaignIdAndRuleId(campaignId, ruleId);
    }
}
