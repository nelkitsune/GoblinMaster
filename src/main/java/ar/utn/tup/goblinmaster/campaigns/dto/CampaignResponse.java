// CampaignResponse.java
package ar.utn.tup.goblinmaster.campaigns.dto;

import java.time.Instant;

public record CampaignResponse(
        Long id,
        String name,
        String description,
        boolean active,
        Long ownerId,
        String ownerEmail,
        String game_system,
        String setting,
        String imageUrl,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) { }
