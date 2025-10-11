package ar.utn.tup.goblinmaster.campaigns.dto;

    public record UpdateCampaignRequest(
            String name,
            String description,
            Boolean active
    ) { }
