package com.weslley.ssi_api.dto.user;

import com.weslley.ssi_api.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "E-mail is required")
    @Email(message = "E-mail format is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be at least 3 characters long")
    private String password;

}