package com.mzy.init.web.controller;

import com.mzy.init.annotation.LoginRequired;
import com.mzy.init.annotation.RoleRequired;
import com.mzy.init.common.ApiResponse;
import com.mzy.init.web.model.dto.UserRequest;
import com.mzy.init.web.model.dto.UserResponse;
import com.mzy.init.web.model.entity.User;
import com.mzy.init.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@LoginRequired
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 获取所有用户（需要管理员权限）
    @GetMapping
    @RoleRequired(value = User.Role.ADMIN)
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
        return ApiResponse.success(users);
    }

    // 根据 ID 获取用户信息
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ApiResponse.success(UserResponse.fromUser(value)))
                .orElseGet(() -> ApiResponse.error(404, "User not found"));
    }

    // 创建用户
    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        User newUser = new User();
        newUser.setUsername(userRequest.getUsername());
        newUser.setPassword(userRequest.getPassword());  // 应确保在 UserService 中对密码加密处理
        newUser.setPhoneNumber(userRequest.getPhoneNumber());
        newUser.setRole(User.Role.USER);  // 默认创建普通用户

        User createdUser = userService.createUser(newUser);
        return ApiResponse.success(UserResponse.fromUser(createdUser));
    }

    // 更新用户信息
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ApiResponse.success(UserResponse.fromUser(updatedUser));
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }


}
