package ar.utn.tup.goblinmaster.magic.service;

import ar.utn.tup.goblinmaster.magic.dto.SpellListItem;
import ar.utn.tup.goblinmaster.magic.dto.SpellRequest;
import ar.utn.tup.goblinmaster.magic.dto.SpellResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface SpellService {
    SpellResponse create(SpellRequest req, Authentication auth);
    SpellResponse get(Long id);
    List<SpellListItem> search(String q);
    List<SpellListItem> getBySpellClass(Long spellClassId);
    List<SpellListItem> getBySpellClassAndLevel(Long spellClassId, Integer level);
    SpellResponse update(Long id, SpellRequest req, Authentication auth);
    void delete(Long id, Authentication auth);
    List<SpellListItem> mine(Authentication auth);
    void enableInCampaign(Long spellId, Long campaignId, Authentication auth);
    List<SpellListItem> listCampaignHomebrew(Long campaignId, Authentication auth);
    void disableInCampaign(Long campaignId, Long spellId, Authentication auth);
}