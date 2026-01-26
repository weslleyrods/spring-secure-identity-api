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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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

    @Test
    void findAllSuccess(){
        UserModel user = new UserModel();
        List<UserModel> userList = List.of(user);
        Page<UserModel> page = new PageImpl<>(userList);
        Pageable pageable = Pageable.unpaged();

        Mockito.when(userRepository.findAll(pageable)).thenReturn(page);
        assertEquals(page, userService.findAll(pageable));
        Mockito.verify(userRepository, Mockito.times(1)).findAll(pageable);
    }


}
