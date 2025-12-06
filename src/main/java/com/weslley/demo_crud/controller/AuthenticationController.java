package com.weslley.demo_crud.controller;

import com.weslley.demo_crud.dto.auth.AuthenticationDTO;
import com.weslley.demo_crud.dto.auth.LoginResponseDTO;
import com.weslley.demo_crud.dto.auth.RefreshTokenDTO;
import com.weslley.demo_crud.infra.security.TokenService;
import com.weslley.demo_crud.model.RefreshTokenModel;
import com.weslley.demo_crud.model.UserModel;
import com.weslley.demo_crud.repository.RefreshTokenRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


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
        String refreshToken = refreshTokenDTO.getRefreshToken();

        Optional<RefreshTokenModel> optionalToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (optionalToken.isEmpty()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh Token não encontrado!");

        RefreshTokenModel refreshTokenModel = optionalToken.get();
        UserModel user = refreshTokenModel.getUser();
        String newAccessToken = tokenService.generateToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);

        refreshTokenRepository.delete(refreshTokenModel);

        return ResponseEntity.ok(new LoginResponseDTO(newAccessToken, newRefreshToken));
    }

}
