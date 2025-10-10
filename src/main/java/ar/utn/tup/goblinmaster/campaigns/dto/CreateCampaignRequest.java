// CreateCampaignRequest.java
package ar.utn.tup.goblinmaster.campaigns.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCampaignRequest(
        @NotBlank String name,
        String description
) { }
