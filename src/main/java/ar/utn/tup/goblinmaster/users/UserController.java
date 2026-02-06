package ar.utn.tup.goblinmaster.users;

import ar.utn.tup.goblinmaster.users.dto.CreateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserController(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @GetMapping
    public List<User> getAll() {
        return repo.findAllByActiveTrue();
    }

    @GetMapping("/all")
    public List<User> getAllIncludingInactive() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody CreateUserRequest req) {
        // Forzar rol USER y validar unicidad por email/username
        if (repo.findByEmail(req.email()).isPresent()) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        if (repo.findByUsername(req.username()).isPresent()) {
            throw new IllegalArgumentException("Username ya registrado");
        }
        var u = User.builder()
                .username(req.username())
                .email(req.email())
                .password(encoder.encode(req.password()))
                .role(User.Role.USER)
                .build();
        User saved = repo.save(u);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User u = repo.findByIdAndActiveTrue(id).orElseThrow();
        u.setActive(false);
        repo.save(u);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<User> restore(@PathVariable Long id) {
        User u = repo.findById(id).orElseThrow();
        u.setActive(true);
        repo.save(u);
        return ResponseEntity.ok(u);
    }
}
