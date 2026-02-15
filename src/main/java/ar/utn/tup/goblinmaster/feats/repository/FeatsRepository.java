package ar.utn.tup.goblinmaster.feats.repository;

import ar.utn.tup.goblinmaster.feats.entity.Feats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeatsRepository extends JpaRepository<Feats, Long> {

    @Query("""
      select distinct f
      from Feats f
      left join fetch f.prereqGroups pg
      left join fetch pg.conditions c
      where f.id = :id
      """)
    Optional<Feats> findByIdConPrereqs(@Param("id") Long id);

    @Query("""
      select distinct f
      from Feats f
      left join fetch f.prereqGroups pg
      left join fetch pg.conditions c
      """)
    List<Feats> findAllConPrereqs();

    @Query("SELECT f FROM Feats f WHERE f.owner.email = :email")
    List<Feats> findByOwnerEmail(@Param("email") String email);

    @Query("""
      select distinct f
      from Feats f
      left join fetch f.prereqGroups pg
      left join fetch pg.conditions c
      where f.owner.email = :email
      """)
    List<Feats> findByOwnerEmailConPrereqs(@Param("email") String email);

    @Query("""
      select distinct f
      from Feats f
      left join fetch f.prereqGroups pg
      left join fetch pg.conditions c
      where f.owner is null
      """)
    List<Feats> findAllOfficialConPrereqs();

    @Query("select f.owner.id from Feats f where f.id = :id")
    Long findOwnerUserIdByFeatId(@Param("id") Long id);
}
