package com.weslley.ssi_api.service;

import com.weslley.ssi_api.builder.UserBuilder;
import com.weslley.ssi_api.dto.user.UserCreateDTO;
import com.weslley.ssi_api.dto.user.UserRoleDTO;
import com.weslley.ssi_api.exception.UserAlreadyExistsException;
import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.model.enums.UserRole;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

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
        UserModel userModel = UserBuilder.aUser().build();

        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(null);
        Mockito.when(userRepository.save(userModel)).thenReturn(userModel);
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        UserModel result = userService.save(userModel);
        assertEquals(userModel, result);
        Mockito.verify(userRepository, Mockito.times(1)).save(userModel);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(anyString());
    }

    @Test
        void createUserUnsuccess(){
        UserModel userModel = UserBuilder.aUser().build();

        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(userModel);
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(userModel));
        Mockito.verify(userRepository, Mockito.times(0)).save(userModel);
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

    @Test
    void updateSuccess(){
        UserModel userModel = UserBuilder.aUser().build();

        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName("User Test Edited");
        userCreateDTO.setEmail("user_edited@email.com");
        userCreateDTO.setPassword("123456789");

        Mockito.when(userRepository.findById(userModel.getId())).thenReturn(Optional.of(userModel));
        Mockito.when(userRepository.save(userModel)).thenReturn(userModel);
        UserModel result = userService.update(userModel.getId(), userCreateDTO);
        assertEquals(userCreateDTO.getName(), result.getName());
        assertEquals(userCreateDTO.getEmail(), result.getEmail());
        assertEquals(userCreateDTO.getPassword(), result.getPassword());
        Mockito.verify(userRepository, Mockito.times(1)).save(userModel);
    }

    @Test
    void partialUpdateSuccess(){
        UserModel userModel = UserBuilder.aUser().build();

        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName("User Test Partial Edited");
        userCreateDTO.setEmail("user_partial_edited@email.com");

        Mockito.when(userRepository.findById(userModel.getId())).thenReturn(Optional.of(userModel));
        Mockito.when(userRepository.save(userModel)).thenReturn(userModel);
        UserModel result = userService.partialUpdate(userModel.getId(), userCreateDTO);
        assertEquals(userCreateDTO.getName(), result.getName());
        assertEquals(userCreateDTO.getEmail(), result.getEmail());
        assertEquals("123456", result.getPassword());
        Mockito.verify(userRepository, Mockito.times(1)).save(userModel);
    }

    @Test
    void changeRoleSuccess(){
        UserModel userModel = UserBuilder.aUser().build();

        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setRole(UserRole.ADMIN);
        
        Mockito.when(userRepository.findById(userModel.getId())).thenReturn(Optional.of(userModel));
        Mockito.when(userRepository.save(userModel)).thenReturn(userModel);
        UserModel result = userService.changeRole(userModel.getId(), userRoleDTO);
        assertEquals(1L, result.getId());
        assertEquals("User Test", result.getName());
        assertEquals("user@email.com", result.getEmail());
        assertEquals("123456", result.getPassword());
        assertEquals(UserRole.ADMIN, result.getRole());
        Mockito.verify(userRepository, Mockito.times(1)).save(userModel);
    }

}
