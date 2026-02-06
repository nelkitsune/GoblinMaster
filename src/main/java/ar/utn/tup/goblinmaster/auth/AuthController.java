// auth/AuthController.java
package ar.utn.tup.goblinmaster.auth;

import ar.utn.tup.goblinmaster.auth.dto.*;
import ar.utn.tup.goblinmaster.users.UserRepository;
import ar.utn.tup.goblinmaster.users.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {

    private final AuthService auth;
    private final UserRepository users;
    private final ModelMapper mapper;

    public AuthController(AuthService auth, UserRepository users, ModelMapper mapper) {
        this.auth = auth; this.users = users; this.mapper = mapper;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@Validated @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(auth.register(req));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody LoginRequest req) {
        return ResponseEntity.ok(auth.login(req));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserDto> me(Authentication authn) {
        var user = users.findByEmail(authn.getName()).orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        return ResponseEntity.ok(mapper.map(user, UserDto.class));
    }
}
