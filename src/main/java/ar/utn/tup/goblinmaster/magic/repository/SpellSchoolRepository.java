package ar.utn.tup.goblinmaster.magic.repository;

import ar.utn.tup.goblinmaster.magic.entity.SpellSchool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpellSchoolRepository extends JpaRepository<SpellSchool, Long> {
    Optional<SpellSchool> findByCode(String code);
}