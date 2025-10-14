package ar.utn.tup.goblinmaster.campaigns.dto;

public record UpdateCampaignRequest(
        String name,
        String description,
        String game_system,
        String setting,
        String imageUrl,
        Boolean active
) { }