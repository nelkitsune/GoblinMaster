package ar.utn.tup.goblinmaster.magic.repository;

import ar.utn.tup.goblinmaster.magic.entity.SpellClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpellClassRepository extends JpaRepository<SpellClass, Long> {
    Optional<SpellClass> findByCode(String code);
}