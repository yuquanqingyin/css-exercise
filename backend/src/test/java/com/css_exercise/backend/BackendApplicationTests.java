package com.css_exercise.backend;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.model.*;
import ai.z.openapi.core.Constants;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        System.out.println(passwordEncoder.encode("123456"));
//        System.out.println(passwordEncoder.matches("123456", passwordEncoder.encode("123456")));
        // 初始化客户端
        ZhipuAiClient client = ZhipuAiClient.builder()
                .apiKey("8a262500f7c34ab5945d349361a72aa7.fSQZTEdgoOV9UEDg")
                .build();

        // 创建聊天完成请求
        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("glm-4.6")
                .messages(Arrays.asList(
                        ChatMessage.builder()
                                .role(ChatMessageRole.USER.value())
                                .content("你好，请介绍一下自己")
                                .build()
                ))
                .build();

        // 发送请求
        ChatCompletionResponse response = client.chat().createChatCompletion(request);

        // 获取回复
        if (response.isSuccess()) {
            Object reply = response.getData().getChoices().get(0).getMessage();
            System.out.println("AI 回复: " + reply);
        } else {
            System.err.println("错误: " + response.getMsg());
        }
    }

}
