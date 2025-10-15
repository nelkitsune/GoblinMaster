package ar.utn.tup.goblinmaster.magic.repository;

import ar.utn.tup.goblinmaster.magic.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpellRepository extends JpaRepository<Spell, Long>, JpaSpecificationExecutor<Spell> {
    @Query("""
         select s from Spell s
         where (:q is null or lower(s.name) like lower(concat('%', :q, '%')))
         """)
    Page<Spell> search(@Param("q") String q, Pageable pageable);
}





