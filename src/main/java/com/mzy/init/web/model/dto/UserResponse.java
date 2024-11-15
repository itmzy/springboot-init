package com.mzy.init.web.model.dto;

import com.mzy.init.web.model.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private Long id;
    private String username;
    private String phoneNumber;
    private User.Role role;

    public UserResponse(Long id, String username, String phoneNumber, User.Role role) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getPhoneNumber(), user.getRole());
    }

    // Getters only for response data (no setters to avoid modification)

}
