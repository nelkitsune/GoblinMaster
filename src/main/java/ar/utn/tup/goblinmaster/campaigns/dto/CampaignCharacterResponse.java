package ar.utn.tup.goblinmaster.campaigns.dto;

public record CampaignCharacterResponse(
        Long id,
        Long campaignId,
        Long characterId,
        String characterName,
        Boolean isNpc,
        java.time.Instant deletedAt,
        Boolean isDeleted
) {}
