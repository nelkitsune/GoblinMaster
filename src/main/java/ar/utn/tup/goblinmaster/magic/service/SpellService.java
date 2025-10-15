package ar.utn.tup.goblinmaster.magic.service;

import ar.utn.tup.goblinmaster.magic.dto.SpellListItem;
import ar.utn.tup.goblinmaster.magic.dto.SpellRequest;
import ar.utn.tup.goblinmaster.magic.dto.SpellResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpellService {
    SpellResponse create(SpellRequest request);
    SpellResponse get(Long id);
    Page<SpellListItem> search(String q, Pageable pageable);
    SpellResponse update(Long id, SpellRequest request);
    void delete(Long id);
}

