package ar.utn.tup.goblinmaster.magic.service;

import ar.utn.tup.goblinmaster.magic.dto.SpellListItem;
import ar.utn.tup.goblinmaster.magic.dto.SpellRequest;
import ar.utn.tup.goblinmaster.magic.dto.SpellResponse;
import java.util.List;

public interface SpellService {
    SpellResponse create(SpellRequest request);
    SpellResponse get(Long id);
    List<SpellListItem> search(String q);
    SpellResponse update(Long id, SpellRequest request);
    void delete(Long id);

    List<SpellListItem> getBySpellClass(Long spellClassId);
    List<SpellListItem> getBySpellClassAndLevel(Long spellClassId, Integer level);
}