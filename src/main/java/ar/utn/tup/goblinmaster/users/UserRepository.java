package ar.utn.tup.goblinmaster.users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    // Activos por defecto
    Optional<User> findByIdAndActiveTrue(Long id);
    List<User> findAllByActiveTrue();
    Optional<User> findByEmailAndActiveTrue(String email);
    Optional<User> findByUserCode(String userCode);
}
