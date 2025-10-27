// SubmissionRecordServiceImpl.java
package com.css_exercise.backend.service.impl.record;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css_exercise.backend.mapper.ExerciseMapper;
import com.css_exercise.backend.mapper.SubmissionRecordMapper;
import com.css_exercise.backend.pojo.Exercise;
import com.css_exercise.backend.pojo.SubmissionRecord;
import com.css_exercise.backend.service.record.SubmissionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubmissionRecordServiceImpl implements SubmissionRecordService {

    @Autowired
    private SubmissionRecordMapper submissionRecordMapper;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Override
    public Map<String, Object> getUserSubmissions(Integer userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 查询用户的所有提交记录，按提交时间倒序排列
            QueryWrapper<SubmissionRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                    .orderByDesc("submit_time");

            List<SubmissionRecord> records = submissionRecordMapper.selectList(queryWrapper);

            // 构建返回数据，包含练习题信息
            List<Map<String, Object>> submissions = new ArrayList<>();
            for (SubmissionRecord record : records) {
                Map<String, Object> submission = new HashMap<>();
                submission.put("id", record.getId());
                submission.put("exerciseId", record.getExerciseId());
                submission.put("cssCode", record.getCssCode());
                submission.put("score", record.getScore());
                submission.put("submitTime", record.getSubmitTime().toString());

                // 获取练习题名称
                QueryWrapper<Exercise> exerciseQuery = new QueryWrapper<>();
                exerciseQuery.eq("exercise_id", record.getExerciseId());
                Exercise exercise = exerciseMapper.selectOne(exerciseQuery);

                if (exercise != null) {
                    submission.put("exerciseName", exercise.getName());
                    submission.put("exerciseTitle", exercise.getTitle());
                } else {
                    submission.put("exerciseName", "未知练习");
                    submission.put("exerciseTitle", "");
                }

                submissions.add(submission);
            }

            result.put("error_message", "success");
            result.put("submissions", submissions);

        } catch (Exception e) {
            result.put("error_message", "获取提交记录失败: " + e.getMessage());
        }

        return result;
    }
}