package ar.utn.tup.goblinmaster.campaigns.dto;


public record MemberResponse(
        Long id,
        Long userId,
        String email,
        String role
) {}
