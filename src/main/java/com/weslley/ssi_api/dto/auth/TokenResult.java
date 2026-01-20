package com.weslley.ssi_api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResult {

    private String token;
    private String refreshToken;

}
