package com.weslley.ssi_api.controller;

import com.weslley.ssi_api.dto.auth.AuthenticationDTO;
import com.weslley.ssi_api.dto.auth.LoginResponseDTO;
import com.weslley.ssi_api.dto.auth.RefreshTokenDTO;
import com.weslley.ssi_api.infra.security.TokenService;
import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (UserModel) auth.getPrincipal();
        var token = tokenService.generateToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);
        return  ResponseEntity.ok(new LoginResponseDTO(token, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody @Valid RefreshTokenDTO refreshTokenDTO){
        var tokenResponse = authenticationService.refreshToken(refreshTokenDTO);
        return ResponseEntity.ok(tokenResponse);
    }
}
