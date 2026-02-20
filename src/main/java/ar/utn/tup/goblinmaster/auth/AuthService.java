// auth/AuthService.java
package ar.utn.tup.goblinmaster.auth;

import ar.utn.tup.goblinmaster.auth.dto.*;
import ar.utn.tup.goblinmaster.users.User;
import ar.utn.tup.goblinmaster.users.UserRepository;
import ar.utn.tup.goblinmaster.users.dto.UserDto;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper mapper;

    public AuthService(UserRepository users, PasswordEncoder encoder,
                       AuthenticationManager authManager, JwtService jwt, ModelMapper mapper) {
        this.users = users; this.encoder = encoder; this.authManager = authManager; this.jwt = jwt; this.mapper = mapper;
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
        // Resolver posibles colisiones de userCode
        int tries = 0;
        while (tries < 5) {
            if (u.getUserCode() == null || users.findByUserCode(u.getUserCode()).isPresent()) {
                String rand = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
                u.setUserCode("USR-" + rand);
                tries++;
            } else {
                break;
            }
        }
        if (users.findByUserCode(u.getUserCode()).isPresent()) {
            throw new IllegalStateException("No se pudo generar un userCode único");
        }
        users.save(u);

        UserDetails ud = new org.springframework.security.core.userdetails.User(
                u.getEmail(), u.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
        );
        String token = jwt.generateToken(ud, u.getRole().name());
        UserDto dto = mapper.map(u, UserDto.class);
        return new AuthResponse(token, dto);
    }

    public AuthResponse login(LoginRequest req) {
        // verificar que el usuario esté activo
        User u = users.findByEmail(req.email()).orElseThrow();
        if (!u.isActive()) {
            throw new IllegalStateException("Usuario inactivo");
        }
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        UserDetails ud = new org.springframework.security.core.userdetails.User(
                req.email(), u.getPassword(),
                java.util.List.of()
        );
        String token = jwt.generateToken(ud, u.getRole().name());
        UserDto dto = mapper.map(u, UserDto.class);
        return new AuthResponse(token, dto);
    }
}
