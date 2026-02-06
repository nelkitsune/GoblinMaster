package ar.utn.tup.goblinmaster.auth.dto;

import ar.utn.tup.goblinmaster.users.dto.UserDto;

public record AuthResponse(String token, UserDto user) {}
