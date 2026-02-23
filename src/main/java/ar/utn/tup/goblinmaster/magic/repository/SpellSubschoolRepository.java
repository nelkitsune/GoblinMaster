package ar.utn.tup.goblinmaster.magic.repository;

import ar.utn.tup.goblinmaster.magic.entity.SpellSubschool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpellSubschoolRepository extends JpaRepository<SpellSubschool, Long> {
    List<SpellSubschool> findBySchoolId(Long schoolId);
}

