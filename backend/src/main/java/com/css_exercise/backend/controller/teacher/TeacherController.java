package com.css_exercise.backend.controller.teacher;

import com.css_exercise.backend.pojo.User;
import com.css_exercise.backend.service.impl.utils.UserDetailsImpl;
import com.css_exercise.backend.service.teacher.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * 验证当前用户是否为教师
     */
    private Map<String, Object> checkTeacherRole() {
        Map<String, Object> result = new HashMap<>();
        try {
            UsernamePasswordAuthenticationToken token =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) token.getPrincipal();
            User user = loginUser.getUser();

            if (!"teacher".equals(user.getRole())) {
                result.put("error_message", "无权限访问");
                return result;
            }
            result.put("valid", true);
            return result;
        } catch (Exception e) {
            result.put("error_message", "获取用户信息失败");
            return result;
        }
    }

    /**
     * 获取所有学生列表及其做题统计
     */
    @GetMapping("/students/")
    public Map<String, Object> getAllStudents() {
        Map<String, Object> authCheck = checkTeacherRole();
        if (!authCheck.containsKey("valid")) {
            return authCheck;
        }
        return teacherService.getAllStudentsWithStats();
    }

    /**
     * 获取指定学生的详细做题情况
     */
    @GetMapping("/student/{studentId}/submissions/")
    public Map<String, Object> getStudentSubmissions(@PathVariable Integer studentId) {
        Map<String, Object> authCheck = checkTeacherRole();
        if (!authCheck.containsKey("valid")) {
            return authCheck;
        }
        return teacherService.getStudentSubmissions(studentId);
    }

    /**
     * 获取指定学生在某个练习题上的所有提交记录
     */
    @GetMapping("/student/{studentId}/exercise/{exerciseId}/")
    public Map<String, Object> getStudentExerciseSubmissions(
            @PathVariable Integer studentId,
            @PathVariable String exerciseId) {
        Map<String, Object> authCheck = checkTeacherRole();
        if (!authCheck.containsKey("valid")) {
            return authCheck;
        }
        return teacherService.getStudentExerciseSubmissions(studentId, exerciseId);
    }

    /**
     * 获取所有练习题的完成情况统计
     */
    @GetMapping("/exercises/stats/")
    public Map<String, Object> getExercisesStats() {
        Map<String, Object> authCheck = checkTeacherRole();
        if (!authCheck.containsKey("valid")) {
            return authCheck;
        }
        return teacherService.getExercisesStats();
    }
}