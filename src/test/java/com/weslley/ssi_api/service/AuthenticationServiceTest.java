package com.weslley.ssi_api.service;

import com.weslley.ssi_api.dto.auth.TokenResult;
import com.weslley.ssi_api.infra.security.TokenService;
import com.weslley.ssi_api.model.RefreshTokenModel;
import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void refreshTokenSuccess(){
        RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
        UserModel user = new UserModel();
        user.setId(1L);
        user.setName("User Test");
        user.setEmail("user@email.com");
        user.setPassword("123456");
        refreshTokenModel.setUser(user);

        Mockito.when(refreshTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(refreshTokenModel));
        Mockito.when(tokenService.generateToken(user)).thenReturn("token");
        Mockito.when(tokenService.generateRefreshToken(user)).thenReturn("newRefreshToken");

        TokenResult result = authenticationService.refreshToken("oldRefreshToken");

        assertEquals("token", result.getToken());
        assertEquals("newRefreshToken", result.getRefreshToken());

        Mockito.verify(refreshTokenRepository, Mockito.times(1)).delete(refreshTokenModel);
        Mockito.verify(tokenService, Mockito.times(1)).generateToken(user);
        Mockito.verify(tokenService, Mockito.times(1)).generateRefreshToken(user);


    }
}
