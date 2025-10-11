// auth/AuthService.java
package ar.utn.tup.goblinmaster.auth;

import ar.utn.tup.goblinmaster.auth.dto.*;
import ar.utn.tup.goblinmaster.users.User;
import ar.utn.tup.goblinmaster.users.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder,
                       AuthenticationManager authManager, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.authManager = authManager; this.jwt = jwt;
    }

    public AuthResponse register(RegisterRequest req) {
        if (users.findByEmail(req.email()).isPresent()) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        var u = User.builder()
                .username(req.username())
                .email(req.email())
                .password(encoder.encode(req.password()))
                .role(User.Role.USER)
                .build();
        users.save(u);

        UserDetails ud = new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
        );
        return new AuthResponse(jwt.generateToken(ud));
    }

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        UserDetails ud = new org.springframework.security.core.userdetails.User(
                req.email(), users.findByEmail(req.email()).orElseThrow().getPassword(),
                java.util.List.of()
        );
        return new AuthResponse(jwt.generateToken(ud));
    }
}
