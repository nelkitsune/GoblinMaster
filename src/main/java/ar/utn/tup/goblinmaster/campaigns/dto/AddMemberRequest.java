// AddMemberRequest.java
package ar.utn.tup.goblinmaster.campaigns.dto;

public record AddMemberRequest(
        Long userId,
        String role
) { }
