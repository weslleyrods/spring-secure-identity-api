package com.weslley.ssi_api.service;

import com.weslley.ssi_api.dto.auth.AuthenticationDTO;
import com.weslley.ssi_api.dto.auth.LoginResponseDTO;
import com.weslley.ssi_api.dto.auth.RefreshTokenDTO;
import com.weslley.ssi_api.exception.InvalidTokenException;
import com.weslley.ssi_api.infra.security.TokenService;
import com.weslley.ssi_api.model.RefreshTokenModel;
import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private TokenService tokenService;

    public LoginResponseDTO login(AuthenticationDTO authenticationDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var user = (UserModel) auth.getPrincipal();
        var token = tokenService.generateToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);
        return  new LoginResponseDTO(token, refreshToken);
    }

    public LoginResponseDTO refreshToken(RefreshTokenDTO refreshToken) {
        String token = refreshToken.getRefreshToken();

        Optional<RefreshTokenModel> optionalToken = refreshTokenRepository.findByRefreshToken(token);
        if (optionalToken.isEmpty()) throw new InvalidTokenException("Refresh Token not found!");

        RefreshTokenModel refreshTokenModel = optionalToken.get();
        UserModel user = refreshTokenModel.getUser();
        String newAccessToken = tokenService.generateToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);

        refreshTokenRepository.delete(refreshTokenModel);
        return new LoginResponseDTO(newAccessToken, newRefreshToken);
    }
}