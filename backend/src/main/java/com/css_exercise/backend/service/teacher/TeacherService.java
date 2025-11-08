
package com.css_exercise.backend.service.teacher;

import java.util.Map;

public interface TeacherService {
    /**
     * 获取所有学生及其做题统计
     */
    Map<String, Object> getAllStudentsWithStats();

    /**
     * 获取指定学生的所有提交记录
     */
    Map<String, Object> getStudentSubmissions(Integer studentId);

    /**
     * 获取指定学生在某个练习题上的所有提交记录
     */
    Map<String, Object> getStudentExerciseSubmissions(Integer studentId, String exerciseId);

    /**
     * 获取所有练习题的完成情况统计
     */
    Map<String, Object> getExercisesStats();
}