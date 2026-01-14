package com.weslley.ssi_api.dto.auth;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class AuthenticationDTO {

    @NotBlank(message = "E-mail is required")
    @Email(message = "Invalid e-mail format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}