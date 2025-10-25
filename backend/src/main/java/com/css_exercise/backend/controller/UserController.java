package com.css_exercise.backend.controller;

import com.css_exercise.backend.mapper.UserMapper;
import com.css_exercise.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/all")
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }
}
