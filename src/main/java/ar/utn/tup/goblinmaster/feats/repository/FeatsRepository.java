package ar.utn.tup.goblinmaster.feats.repository;

import ar.utn.tup.goblinmaster.feats.entity.Feats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeatsRepository extends JpaRepository<Feats, Long> {
    Optional<Feats> findByCode(String code);
}

