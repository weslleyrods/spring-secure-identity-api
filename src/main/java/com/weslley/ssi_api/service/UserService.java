package com.weslley.ssi_api.service;

import com.weslley.ssi_api.dto.user.UserCreateDTO;
import com.weslley.ssi_api.dto.user.UserRoleDTO;
import com.weslley.ssi_api.exception.UserAlreadyExistsException;
import com.weslley.ssi_api.exception.UserNotFoundException;
import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.repository.RefreshTokenRepository;
import com.weslley.ssi_api.repository.UserRepository;
import com.weslley.ssi_api.utils.UpdateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;



    public UserModel save(UserModel user){
       var userExists = userRepository.findByEmail(user.getEmail());
       if (userExists != null) throw new UserAlreadyExistsException("Email already exists");
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       return userRepository.save(user);
    }

    public List<UserModel> findAll(){
        return userRepository.findAll();
    }

    public UserModel findById(Long id){
        Optional<UserModel> user = userRepository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserModel update(Long id, UserCreateDTO userDto) {
        UserModel user = userRepository.findById(id).orElseThrow(() ->new UserNotFoundException("User not found"));
        BeanUtils.copyProperties(userDto, user, "id");
        return userRepository.save(user);
    }

    public UserModel partialUpdate(Long id, UserCreateDTO userDto) {
        UserModel user = userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User not found")
        );
        UpdateUtil.copyNonNullProperties(userDto, user);
        return userRepository.save(user);
    }

    public UserModel changeRole (Long id, UserRoleDTO roleDTO) {
        UserModel user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        UpdateUtil.copyNonNullProperties(roleDTO, user);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id){
        Optional<UserModel> user = userRepository.findById(id);

        user.orElseThrow(() -> new UserNotFoundException("User not found"));
        refreshTokenRepository.deleteByUser(user);
        userRepository.deleteById(id);
    }

}
