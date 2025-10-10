// CampaignResponse.java
package ar.utn.tup.goblinmaster.campaigns.dto;

public record CampaignResponse(
        Long id,
        String name,
        String description,
        boolean active,
        Long ownerId,
        String ownerEmail
) { }
