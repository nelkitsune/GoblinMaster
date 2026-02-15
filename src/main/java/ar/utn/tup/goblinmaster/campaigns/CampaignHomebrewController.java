package ar.utn.tup.goblinmaster.campaigns;

import ar.utn.tup.goblinmaster.magic.dto.SpellListItem;
import ar.utn.tup.goblinmaster.magic.service.SpellService;
import ar.utn.tup.goblinmaster.feats.dto.FeatsResponse;
import ar.utn.tup.goblinmaster.feats.sevice.FeatsService;
import ar.utn.tup.goblinmaster.rules.RuleService;
import ar.utn.tup.goblinmaster.rules.dto.RuleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns/{campaignId}/homebrew")
@RequiredArgsConstructor
public class CampaignHomebrewController {

    private final SpellService spellService;
    private final FeatsService featsService;
    private final RuleService ruleService;

    @GetMapping
    public HomebrewBundle list(@PathVariable Long campaignId, Authentication auth) {
        List<SpellListItem> spells = spellService.listCampaignHomebrew(campaignId, auth);
        List<FeatsResponse> feats = featsService.listCampaignHomebrew(campaignId, auth);
        List<RuleResponse> rules = ruleService.listCampaignHomebrew(campaignId, auth);
        return new HomebrewBundle(spells, feats, rules);
    }

    @PostMapping("/spells/{spellId}")
    public ResponseEntity<Void> addSpell(@PathVariable Long campaignId, @PathVariable Long spellId, Authentication auth) {
        spellService.enableInCampaign(spellId, campaignId, auth);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/spells/{spellId}")
    public ResponseEntity<Void> removeSpell(@PathVariable Long campaignId, @PathVariable Long spellId, Authentication auth) {
        spellService.disableInCampaign(campaignId, spellId, auth);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/feats/{featId}")
    public ResponseEntity<Void> addFeat(@PathVariable Long campaignId, @PathVariable Long featId, Authentication auth) {
        featsService.enableInCampaign(featId, campaignId, auth);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/feats/{featId}")
    public ResponseEntity<Void> removeFeat(@PathVariable Long campaignId, @PathVariable Long featId, Authentication auth) {
        featsService.disableInCampaign(campaignId, featId, auth);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rules/{ruleId}")
    public ResponseEntity<Void> addRule(@PathVariable Long campaignId, @PathVariable Long ruleId, Authentication auth) {
        ruleService.enableInCampaign(ruleId, campaignId, auth);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rules/{ruleId}")
    public ResponseEntity<Void> removeRule(@PathVariable Long campaignId, @PathVariable Long ruleId, Authentication auth) {
        ruleService.disableInCampaign(campaignId, ruleId, auth);
        return ResponseEntity.noContent().build();
    }

    public record HomebrewBundle(List<SpellListItem> spells, List<FeatsResponse> feats, List<RuleResponse> rules) {}
}

