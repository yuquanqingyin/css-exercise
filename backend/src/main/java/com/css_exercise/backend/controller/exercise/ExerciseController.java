// ExerciseController.java
package com.css_exercise.backend.controller.exercise;

import com.css_exercise.backend.pojo.User;
import com.css_exercise.backend.service.exercise.ExerciseService;
import com.css_exercise.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/exercise")
@CrossOrigin
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/list/")
    public Map<String, Object> getAllExercises() {
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

        return exerciseService.getAllExercises(userId);
    }

    @GetMapping("/detail/{id}/")
    public Map<String, Object> getExerciseById(@PathVariable String id) {
        return exerciseService.getExerciseById(id);
    }
}