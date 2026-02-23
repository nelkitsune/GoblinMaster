package ar.utn.tup.goblinmaster.magic.repository;

import ar.utn.tup.goblinmaster.magic.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpellRepository extends JpaRepository<Spell, Long>, JpaSpecificationExecutor<Spell> {
    @Query("""
         select s from Spell s
         where (:q is null or lower(s.name) like lower(concat('%', :q, '%')))
         order by s.name
         """)
    List<Spell> search(@Param("q") String q);

    @Query("""
         select s from Spell s
         where s.owner is null
           and (:q is null or lower(s.name) like lower(concat('%', :q, '%')))
         order by s.name
         """)
    List<Spell> searchOfficial(@Param("q") String q);


    @Query("SELECT s FROM Spell s JOIN SpellClassLevel scl ON s.id = scl.spell.id WHERE scl.spellClass.id = :classId ORDER BY s.name")
    List<Spell> findBySpellClassId(Long classId);

    @Query("SELECT s FROM Spell s JOIN SpellClassLevel scl ON s.id = scl.spell.id WHERE scl.spellClass.id = :classId AND scl.level = :level ORDER BY s.name")
    List<Spell> findBySpellClassIdAndLevel(@Param("classId") Long classId, @Param("level") Integer level);

    @Query("SELECT s FROM Spell s WHERE s.owner.email = :email")
    List<Spell> findByOwnerEmail(@Param("email") String email);

    @Query("""
        SELECT s FROM Spell s
        JOIN SpellClassLevel scl ON s.id = scl.spell.id
        WHERE scl.spellClass.id = :classId
          AND (s.owner IS NULL OR s.owner.email = :email)
        ORDER BY s.name
    """)
    List<Spell> findBySpellClassIdFiltered(@Param("classId") Long classId, @Param("email") String email);

    @Query("""
        SELECT s FROM Spell s
        JOIN SpellClassLevel scl ON s.id = scl.spell.id
        WHERE scl.spellClass.id = :classId
          AND scl.level = :level
          AND (s.owner IS NULL OR s.owner.email = :email)
        ORDER BY s.name
    """)
    List<Spell> findBySpellClassIdAndLevelFiltered(@Param("classId") Long classId, @Param("level") Integer level, @Param("email") String email);

    // Solo oficiales (owner null)
    @Query("SELECT s FROM Spell s JOIN SpellClassLevel scl ON s.id = scl.spell.id WHERE scl.spellClass.id = :classId AND s.owner IS NULL ORDER BY s.name")
    List<Spell> findOfficialBySpellClassId(@Param("classId") Long classId);

    @Query("SELECT s FROM Spell s JOIN SpellClassLevel scl ON s.id = scl.spell.id WHERE scl.spellClass.id = :classId AND scl.level = :level AND s.owner IS NULL ORDER BY s.name")
    List<Spell> findOfficialBySpellClassIdAndLevel(@Param("classId") Long classId, @Param("level") Integer level);
}
