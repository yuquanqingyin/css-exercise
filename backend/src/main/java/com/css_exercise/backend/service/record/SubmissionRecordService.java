// SubmissionRecordService.java
package com.css_exercise.backend.service.record;

import java.util.Map;

public interface SubmissionRecordService {
    /**
     * 获取用户的所有提交记录
     * @param userId 用户ID
     * @return 提交记录列表
     */
    Map<String, Object> getUserSubmissions(Integer userId);
}