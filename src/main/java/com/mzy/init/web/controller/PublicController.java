package com.mzy.init.web.controller;

import com.mzy.init.common.ApiResponse;
import com.mzy.init.web.model.dto.LoginUser;
import com.mzy.init.web.model.dto.UserRequest;
import com.mzy.init.web.model.entity.User;
import com.mzy.init.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    // 用户登录
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginUser loginUser, HttpSession session) {
        Optional<User> user = userService.validateUser(loginUser.getUsername(), loginUser.getPassword());
        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            return ApiResponse.success("Login successful");
        } else {
            return ApiResponse.error(401, "Invalid username or password");
        }
    }

    // 用户注销
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success("Logged out successfully");
    }


}
