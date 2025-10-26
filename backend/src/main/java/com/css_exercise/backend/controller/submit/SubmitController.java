// SubmitController.java
package com.css_exercise.backend.controller.submit;

import com.css_exercise.backend.service.submit.SubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/submit")
@CrossOrigin
public class SubmitController {

    @Autowired
    private SubmitService submitService;

    /**
     * 提交CSS代码进行评测
     * @param requestData 包含exerciseId和cssCode的请求数据
     * @param principal 用户认证信息
     * @return 评测结果
     */
    @PostMapping("/evaluate/")
    public Map<String, Object> submitCss(@RequestBody Map<String, String> requestData, Principal principal) {
        String exerciseId = requestData.get("exerciseId");
        String cssCode = requestData.get("cssCode");

        // 获取用户ID（如果有认证系统的话）
        Integer userId = null;
        if (principal != null && principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            if (token.getPrincipal() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> userDetails = (Map<String, Object>) token.getPrincipal();
                userId = (Integer) userDetails.get("id");
            }
        }

        return submitService.evaluateCss(exerciseId, cssCode, userId);
    }
}