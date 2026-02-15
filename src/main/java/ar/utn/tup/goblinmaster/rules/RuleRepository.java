package ar.utn.tup.goblinmaster.rules;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    List<Rule> findByOwnerEmail(String email);
    List<Rule> findByOwnerIsNull();
}

