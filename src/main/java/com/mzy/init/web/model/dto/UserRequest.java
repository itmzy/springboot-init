package com.mzy.init.web.model.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String phoneNumber;
}
