package com.weslley.demo_crud.dto.user;

import com.weslley.demo_crud.model.UserModel;
import com.weslley.demo_crud.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private UserRole role;

    public static UserResponseDTO from(UserModel user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
