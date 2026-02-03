package com.weslley.ssi_api.service;

import com.weslley.ssi_api.builder.UserBuilder;
import com.weslley.ssi_api.dto.auth.AuthenticationDTO;
import com.weslley.ssi_api.dto.auth.TokenResult;
import com.weslley.ssi_api.infra.security.TokenService;
import com.weslley.ssi_api.model.RefreshTokenModel;
import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)

public class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void loginSuccess(){
        UserModel userModel = UserBuilder.aUser().build();

        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setEmail(userModel.getEmail());
        authenticationDTO.setPassword(userModel.getPassword());

        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword());
        Authentication auth = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(usernamePassword)).thenReturn(auth);
        Mockito.when(auth.getPrincipal()).thenReturn(userModel);

        Mockito.when(tokenService.generateToken(userModel)).thenReturn("token");
        Mockito.when(tokenService.generateRefreshToken(userModel)).thenReturn("refreshToken");

        TokenResult result = authenticationService.login(authenticationDTO);

        assertEquals("token", result.getToken());
        assertEquals("refreshToken", result.getRefreshToken());

        Mockito.verify(tokenService, Mockito.times(1)).generateToken(userModel);
        Mockito.verify(tokenService, Mockito.times(1)).generateRefreshToken(userModel);
        Mockito.verify(authenticationManager, Mockito.times(1))
                .authenticate(ArgumentMatchers.refEq(usernamePassword));
    }


    @Test
    void refreshTokenSuccess(){
        RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
        UserModel userModel = UserBuilder.aUser().build();

        refreshTokenModel.setUser(userModel);

        Mockito.when(refreshTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(refreshTokenModel));
        Mockito.when(tokenService.generateToken(userModel)).thenReturn("token");
        Mockito.when(tokenService.generateRefreshToken(userModel)).thenReturn("newRefreshToken");

        TokenResult result = authenticationService.refreshToken("oldRefreshToken");

        assertEquals("token", result.getToken());
        assertEquals("newRefreshToken", result.getRefreshToken());

        Mockito.verify(refreshTokenRepository, Mockito.times(1)).delete(refreshTokenModel);
        Mockito.verify(tokenService, Mockito.times(1)).generateToken(userModel);
        Mockito.verify(tokenService, Mockito.times(1)).generateRefreshToken(userModel);
    }

    @Test
    void logout(){
        RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
        UserModel userModel = UserBuilder.aUser().build();
        refreshTokenModel.setUser(userModel);

        Mockito.when(refreshTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(refreshTokenModel));
        authenticationService.logout("refreshToken");
        Mockito.verify(refreshTokenRepository, Mockito.times(1)).delete(refreshTokenModel);
    }

}
