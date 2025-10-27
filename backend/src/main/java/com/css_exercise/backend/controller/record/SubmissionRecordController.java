// SubmissionRecordController.java
package com.css_exercise.backend.controller.record;

import com.css_exercise.backend.pojo.User;
import com.css_exercise.backend.service.impl.utils.UserDetailsImpl;
import com.css_exercise.backend.service.record.SubmissionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/record")
@CrossOrigin
public class SubmissionRecordController {

    @Autowired
    private SubmissionRecordService submissionRecordService;

    /**
     * 获取当前用户的提交记录
     */
    @GetMapping("/my-submissions/")
    public Map<String, Object> getMySubmissions() {
        // 从 SecurityContext 中获取当前登录用户信息
        UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) token.getPrincipal();
        User user = loginUser.getUser();

        return submissionRecordService.getUserSubmissions(user.getId());
    }
}