package com.css_exercise.backend.service.impl.ai;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class SiliconFlowAiService {

    @Value("${siliconflow.api.token}")
    private String apiToken;

    private final WebClient webClient;

    public SiliconFlowAiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.siliconflow.cn").build();
    }

    public String chat(String userMessage) {
        // 构造请求体
        Map<String, Object> body = new HashMap<>();
        body.put("model", "Qwen/Qwen2.5-7B-Instruct");
        body.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", userMessage);
                }}
        });

        try {
            String response = webClient.post()
                    .uri("/v1/chat/completions")
                    .header("Authorization", "Bearer " + apiToken)
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();  // Java 8 可以用 block() 阻塞获取结果

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "调用硅基流动 API 失败: " + e.getMessage();
        }
    }
}

