package ar.utn.tup.goblinmaster.magic.repository;

import ar.utn.tup.goblinmaster.magic.entity.SpellClassLevel;
import ar.utn.tup.goblinmaster.magic.entity.SpellClassLevelId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpellClassLevelRepository extends JpaRepository<SpellClassLevel, SpellClassLevelId> {
    List<SpellClassLevel> findBySpellId(Long spellId);
}
