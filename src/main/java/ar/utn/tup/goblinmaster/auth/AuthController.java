// auth/AuthController.java
package ar.utn.tup.goblinmaster.auth;

import ar.utn.tup.goblinmaster.auth.dto.*;
import ar.utn.tup.goblinmaster.users.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {

    private final AuthService auth;
    private final UserRepository users;

    public AuthController(AuthService auth, UserRepository users) {
        this.auth = auth; this.users = users;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@Validated @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(auth.register(req));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody LoginRequest req) {
        return ResponseEntity.ok(auth.login(req));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authn) {
        // authn.getName() == email del usuario logueado
        return ResponseEntity.ok(users.findByEmail(authn.getName()).orElseThrow());
    }
}
