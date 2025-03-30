package com.duett.api.controllers.dto;

public record UserDto(
                Integer id,
                String name,
                String email,
                String password,
                String cpf,
                String profile) {

}
