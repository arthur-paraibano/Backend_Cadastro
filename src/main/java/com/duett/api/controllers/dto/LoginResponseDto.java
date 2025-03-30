package com.duett.api.controllers.dto;

public record LoginResponseDto(
    String token,
    Integer id,
    String name,
    String email,
    String profile,
    boolean firstLogin
) {}