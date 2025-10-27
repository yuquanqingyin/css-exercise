// SubmitController.java
package com.css_exercise.backend.controller.submit;

import com.css_exercise.backend.pojo.User;
import com.css_exercise.backend.service.impl.utils.UserDetailsImpl;
import com.css_exercise.backend.service.submit.SubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
     * @return 评测结果
     */
    @PostMapping("/evaluate/")
    public Map<String, Object> submitCss(@RequestBody Map<String, String> requestData) {
        String exerciseId = requestData.get("exerciseId");
        String cssCode = requestData.get("cssCode");

        // 从 SecurityContext 中获取当前登录用户信息
        Integer userId = null;
        try {
            UsernamePasswordAuthenticationToken token =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) token.getPrincipal();
            User user = loginUser.getUser();
            userId = user.getId();
        } catch (Exception e) {
            System.out.println("获取用户信息失败: " + e.getMessage());
        }

        return submitService.evaluateCss(exerciseId, cssCode, userId);
    }
}