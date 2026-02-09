package ar.utn.tup.goblinmaster.rules;

import ar.utn.tup.goblinmaster.users.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "rules")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "original_name", length = 200)
    private String originalName;

    // Usar LONGTEXT explícito para alinear con MySQL y evitar mismatch de CLOB
    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "pages", length = 100)
    private String pages;

    @Column(name = "books", length = 200)
    private String books;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private User owner; // null => oficial, no null => personalizada

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    void prePersist() { this.createdAt = Instant.now(); }

    @PreUpdate
    void preUpdate() { this.updatedAt = Instant.now(); }
}
