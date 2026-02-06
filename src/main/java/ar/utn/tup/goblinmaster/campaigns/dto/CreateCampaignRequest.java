// CreateCampaignRequest.java
package ar.utn.tup.goblinmaster.campaigns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCampaignRequest(
        @NotBlank @Size(min = 2, max = 60) String name,
        @Size(max = 500) String description
) { }
