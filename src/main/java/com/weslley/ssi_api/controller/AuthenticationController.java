package com.weslley.ssi_api.controller;

import com.weslley.ssi_api.dto.auth.AuthenticationDTO;
import com.weslley.ssi_api.dto.auth.RefreshTokenDTO;
import com.weslley.ssi_api.infra.security.TokenService;
import com.weslley.ssi_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        logger.info("Login request: {}", authenticationDTO);
        var tokenResponse = authenticationService.login(authenticationDTO);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody @Valid RefreshTokenDTO refreshTokenDTO){
        logger.info("Refresh request: {}", refreshTokenDTO);
        var tokenResponse = authenticationService.refreshToken(refreshTokenDTO);
        return ResponseEntity.ok(tokenResponse);
    }
}
