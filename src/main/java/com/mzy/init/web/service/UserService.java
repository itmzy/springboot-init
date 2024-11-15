package com.mzy.init.web.service;

import com.mzy.init.web.model.dto.UserRequest;
import com.mzy.init.web.model.entity.User;
import com.mzy.init.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        // 使用 MD5 加密密码
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        // 设置默认值
        user.setRole(User.Role.USER);                       // 默认角色为 USER
        user.setCreateTime(LocalDateTime.now());            // 设置创建时间
        user.setUpdateTime(LocalDateTime.now());            // 设置更新时间
        user.setDelete(false);                            // 默认未删除

        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRequest userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDetails.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(userDetails.getPassword().getBytes()));
        user.setPhoneNumber(userDetails.getPhoneNumber());
//        user.setRole(userDetails.getRole());
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDelete(true);
        userRepository.save(user);
    }

    // 验证用户名和密码
    public Optional<User> validateUser(String username, String password) {
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        return userRepository.findByUsernameAndPassword(username, encryptedPassword);
    }
}
