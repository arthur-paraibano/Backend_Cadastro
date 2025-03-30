package com.duett.api.controllers.dto;

public record AuthenticatorDto(
    String name,
    String password
) {
}