package ar.utn.tup.goblinmaster.feats.repository;

import ar.utn.tup.goblinmaster.feats.entity.PrereqGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrereqGroupRepository extends JpaRepository<PrereqGroup, Long> {

}
