package com.weslley.demo_crud.controller;

import com.weslley.demo_crud.model.UserModel;
import com.weslley.demo_crud.service.UserService;
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
    public UserModel create(@RequestBody UserCreateDTO userDto) {
        UserModel userModel = new UserModel();
        userModel.setName(userDto.getName());
        userModel.setEmail(userDto.getEmail());
        return userService.save(userModel);
    }

    @GetMapping
    public List<UserModel> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserModel findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public UserModel update (@PathVariable Long id, @RequestBody UserCreateDTO userDto) {
        return userService.update(id, userDto);
    }

    @PatchMapping("/{id}")
    public UserModel partialUpdate (@PathVariable Long id, @RequestBody UserCreateDTO userDto) {
        return userService.partialUpdate(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

}
