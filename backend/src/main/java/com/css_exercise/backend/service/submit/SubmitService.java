// SubmitService.java
package com.css_exercise.backend.service.submit;

import java.util.Map;

public interface SubmitService {
    /**
     * 评测CSS代码
     * @param exerciseId 练习题ID
     * @param cssCode CSS代码
     * @param userId 用户ID
     * @return 评测结果
     */
    Map<String, Object> evaluateCss(String exerciseId, String cssCode, Integer userId);
}