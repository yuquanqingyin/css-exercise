package com.css_exercise.backend.controller;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.model.ChatCompletionCreateParams;
import ai.z.openapi.service.model.ChatCompletionResponse;
import ai.z.openapi.service.model.ChatMessage;
import ai.z.openapi.service.model.ChatMessageRole;
import com.css_exercise.backend.mapper.UserMapper;
import com.css_exercise.backend.pojo.User;
import com.css_exercise.backend.service.impl.ai.SiliconFlowAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SiliconFlowAiService siliconFlowAiService;

    @GetMapping("/user/all")
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @GetMapping("/user/test")
    public String test() {
        String reply = siliconFlowAiService.chat("你是谁？");
        System.out.println("AI 回复：" + reply);
        return "调用ai完成";
    }
}
