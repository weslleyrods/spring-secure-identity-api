package com.weslley.demo_crud.service;
import com.weslley.demo_crud.dto.user.UserCreateDTO;
import com.weslley.demo_crud.model.UserModel;
import com.weslley.demo_crud.repository.UserRepository;
import com.weslley.demo_crud.utils.UpdateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel save(UserModel user){
        return userRepository.save(user);
    }

    public List<UserModel> findAll(){
        return userRepository.findAll();
    }

    public UserModel findById(Long id){
        Optional<UserModel> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }

    public UserModel update(Long id, UserCreateDTO userDto) {
        UserModel user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        BeanUtils.copyProperties(userDto, user, "id");
        return userRepository.save(user);
    }

    public UserModel partialUpdate(Long id, UserCreateDTO userDto) {
        UserModel user = userRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.")
        );
        UpdateUtil.copyNonNullProperties(userDto, user);
        return userRepository.save(user);
    }

    public void  deleteById(Long id){
        Optional<UserModel> user = userRepository.findById(id);
        user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        userRepository.deleteById(id);
    }

}
