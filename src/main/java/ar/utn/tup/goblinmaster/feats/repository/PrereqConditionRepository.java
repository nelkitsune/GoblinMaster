package ar.utn.tup.goblinmaster.feats.repository;

import ar.utn.tup.goblinmaster.feats.entity.PrereqCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrereqConditionRepository extends JpaRepository<PrereqCondition, Long> {
}
