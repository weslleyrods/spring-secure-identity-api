package com.weslley.demo_crud.dto.user;

import com.weslley.demo_crud.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;

    public static UserResponseDTO from(UserModel user) {
        return new UserResponseDTO(user.getId().toString(), user.getName(), user.getEmail());
    }
}
