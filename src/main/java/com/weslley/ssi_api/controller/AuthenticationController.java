package com.weslley.ssi_api.controller;

import com.weslley.ssi_api.dto.auth.AuthenticationDTO;
import com.weslley.ssi_api.dto.auth.LoginResponseDTO;
import com.weslley.ssi_api.dto.auth.TokenResult;
import com.weslley.ssi_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        logger.info("Login request: {}", authenticationDTO);
        var tokenResponse = authenticationService.login(authenticationDTO);
        return buildAuthenticationResponse(tokenResponse);

    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(@CookieValue("refreshToken") String refreshToken){
        logger.info("Refresh request: {}", refreshToken);
        var tokenResponse = authenticationService.refreshToken(refreshToken);
        return buildAuthenticationResponse(tokenResponse);
    }

    public ResponseEntity<LoginResponseDTO> buildAuthenticationResponse(TokenResult tokenResponse){
        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(604800)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(
                        new LoginResponseDTO(tokenResponse.getToken())
                );
    }

}
