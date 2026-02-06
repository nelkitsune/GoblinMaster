// auth/AppUserDetailsService.java
package ar.utn.tup.goblinmaster.auth;

import ar.utn.tup.goblinmaster.users.User;
import ar.utn.tup.goblinmaster.users.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    public AppUserDetailsService(UserRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found"));

        List<SimpleGrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()));
        // permisos finos por rol
        switch (u.getRole()) {
            case ADMIN -> {
                auths.add(new SimpleGrantedAuthority("campaign:read"));
                auths.add(new SimpleGrantedAuthority("campaign:write"));
            }
            case USER -> {
                auths.add(new SimpleGrantedAuthority("campaign:read"));
            }
        }

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getPassword(),
                auths
        );
    }
}
