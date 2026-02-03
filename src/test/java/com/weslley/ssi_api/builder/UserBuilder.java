package com.weslley.ssi_api.builder;

import com.weslley.ssi_api.model.UserModel;
import com.weslley.ssi_api.model.enums.UserRole;

public class UserBuilder {

    private Long id = 1L;
    private String name = "User Test";
    private String email = "user@email.com";
    private String password = "123456";
    private UserRole role = UserRole.USER;
    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withRole(UserRole role) {
        this.role = role;
        return this;
    }

    public UserModel build() {
        UserModel user = new UserModel();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}