package com.weslley.ssi_api.service;

import com.weslley.ssi_api.exception.UserAlreadyExistsException;
import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.repository.RefreshTokenRepository;
import com.weslley.ssi_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserSuccess() {
        UserModel user = new UserModel();
        user.setEmail("user@email.com");
        user.setPassword("123456");

        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(null);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        UserModel result = userService.save(user);
        assertEquals(user, result);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(anyString());
    }

    @Test
        void createUserUnsuccess(){
        UserModel user = new UserModel();
        user.setEmail("user2@email.com");
        user.setPassword("123456");

        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(user);
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(user));
        Mockito.verify(userRepository, Mockito.times(0)).save(user);
        Mockito.verify(passwordEncoder, Mockito.times(0)).encode(anyString());
    }

}
