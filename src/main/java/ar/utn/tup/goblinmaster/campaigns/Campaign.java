package ar.utn.tup.goblinmaster.campaigns;

import ar.utn.tup.goblinmaster.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Builder.Default
    @Column(nullable=false)
    private Boolean active = true;

    @Builder.Default
    @Column(name = "created_at", nullable=false, updatable=false)
    private Instant createdAt = Instant.now();

    @Column(name = "game_system", length = 120)
    private String system;

    @Column(length = 120)
    private String setting;

    @Column(length = 500)
    private String imageUrl;

    @Column(name = "join_code", length = 16, nullable = false, unique = true)
    private String joinCode;

    @Column(name = "updated_at", nullable=false, insertable=false, updatable=false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "image_public_id", length = 200)
    private String imagePublicId; // nullable

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CampaignMember> members = new HashSet<>();

    @PrePersist
    void prePersist(){
        if (createdAt==null) createdAt = Instant.now();
        if (active==null) active = true;
        // joinCode debe estar seteado por el servicio antes de persistir
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

}