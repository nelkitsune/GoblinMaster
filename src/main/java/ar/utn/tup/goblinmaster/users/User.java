package ar.utn.tup.goblinmaster.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "user_code", length = 50, unique = true, nullable = false, updatable = false)
    @NotNull
    private String userCode;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl; // nullable

    @Column(name = "avatar_public_id", length = 200)
    private String avatarPublicId; // nullable

    @PrePersist
    void prePersist() {
        // Generar un código único tipo USR-XXXXXXXX (hex de UUID truncado)
        if (this.userCode == null || this.userCode.isBlank()) {
            String rand = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            this.userCode = "USR-" + rand;
        }
        // active se inicializa por defecto en true vía @Builder.Default y default de DB
    }

    public enum Role { USER, ADMIN }
}
