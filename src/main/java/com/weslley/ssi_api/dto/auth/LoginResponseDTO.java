package com.weslley.ssi_api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
}
