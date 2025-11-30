package com.weslley.demo_crud.controller;

import com.weslley.demo_crud.model.UserModel;
import com.weslley.demo_crud.service.UserService;
import com.weslley.demo_crud.dto.user.UserResponseDTO;
import com.weslley.demo_crud.dto.user.UserCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
        return UserResponseDTO.from(userModel);
    }

    @GetMapping
    public List<UserResponseDTO> findAll() {
        return userService.findAll().stream()
                .map(UserResponseDTO::from)
                .toList();
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

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

}
