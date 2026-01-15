package com.weslley.ssi_api.controller;

import com.weslley.ssi_api.dto.user.UserRoleDTO;
import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.service.UserService;
import com.weslley.ssi_api.dto.user.UserResponseDTO;
import com.weslley.ssi_api.dto.user.UserCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserResponseDTO create(@RequestBody UserCreateDTO userDto) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        return UserResponseDTO.from(userService.save(userModel));
    }

    @GetMapping
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userService.findAll(pageable)
                .map(UserResponseDTO::from);
    }

    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable Long id) {
        return UserResponseDTO.from(userService.findById(id));
    }

    @PutMapping("/{id}")
    public UserResponseDTO update (@PathVariable Long id, @RequestBody UserCreateDTO userDto) {
        return UserResponseDTO.from(userService.update(id, userDto));
    }

    @PatchMapping("/{id}")
    public UserResponseDTO partialUpdate (@PathVariable Long id, @RequestBody UserCreateDTO userDto) {
        return UserResponseDTO.from(userService.partialUpdate(id, userDto));
    }

    @PatchMapping("/{id}/role")
    public UserResponseDTO changeRole (@PathVariable Long id, @RequestBody UserRoleDTO roleDTO) {
        return UserResponseDTO.from(userService.changeRole(id, roleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
