// ExerciseServiceImpl.java
package com.css_exercise.backend.service.impl.exercise;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css_exercise.backend.mapper.ExerciseMapper;
import com.css_exercise.backend.mapper.ExerciseLinkMapper;
import com.css_exercise.backend.mapper.SubmissionRecordMapper;
import com.css_exercise.backend.pojo.Exercise;
import com.css_exercise.backend.pojo.ExerciseLink;
import com.css_exercise.backend.pojo.SubmissionRecord;
import com.css_exercise.backend.service.exercise.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Autowired
    private ExerciseLinkMapper exerciseLinkMapper;

    @Autowired
    private SubmissionRecordMapper submissionRecordMapper;

    @Override
    public Map<String, Object> getAllExercises(Integer userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取所有练习题
            List<Exercise> exercises = exerciseMapper.selectList(null);

            // 获取用户已完成的练习题ID列表
            Set<String> completedExerciseIds = new HashSet<>();
            if (userId != null) {
                QueryWrapper<SubmissionRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                List<SubmissionRecord> submissions = submissionRecordMapper.selectList(queryWrapper);

                // 提取已完成的练习题ID
                completedExerciseIds = submissions.stream()
                        .map(SubmissionRecord::getExerciseId)
                        .collect(Collectors.toSet());
            }

            // 转换为前端需要的格式
            List<Map<String, Object>> nodes = new ArrayList<>();
            for (Exercise exercise : exercises) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", exercise.getExerciseId());
                node.put("name", exercise.getName());

                // 添加完成状态
                boolean completed = completedExerciseIds.contains(exercise.getExerciseId());
                node.put("completed", completed);

                // 包含完整的练习数据
                Map<String, Object> exerciseData = new HashMap<>();
                exerciseData.put("id", exercise.getExerciseId());
                exerciseData.put("name", exercise.getName());
                exerciseData.put("title", exercise.getTitle());
                exerciseData.put("description", exercise.getDescription());
                exerciseData.put("htmlContent", exercise.getHtmlContent());
                exerciseData.put("hint", exercise.getHint());
                exerciseData.put("answer", exercise.getAnswer());

                node.put("exerciseData", exerciseData);
                nodes.add(node);
            }

            // 获取所有关系
            List<ExerciseLink> exerciseLinks = exerciseLinkMapper.selectList(null);
            List<Map<String, String>> links = new ArrayList<>();
            for (ExerciseLink link : exerciseLinks) {
                Map<String, String> linkMap = new HashMap<>();
                linkMap.put("source", link.getSourceId());
                linkMap.put("target", link.getTargetId());
                links.add(linkMap);
            }

            result.put("error_message", "success");
            result.put("nodes", nodes);
            result.put("links", links);

        } catch (Exception e) {
            result.put("error_message", "获取练习题失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> getExerciseById(String exerciseId) {
        Map<String, Object> result = new HashMap<>();

        try {
            QueryWrapper<Exercise> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("exercise_id", exerciseId);
            Exercise exercise = exerciseMapper.selectOne(queryWrapper);

            if (exercise == null) {
                result.put("error_message", "练习题不存在");
                return result;
            }

            result.put("error_message", "success");
            result.put("id", exercise.getExerciseId());
            result.put("name", exercise.getName());
            result.put("title", exercise.getTitle());
            result.put("description", exercise.getDescription());
            result.put("htmlContent", exercise.getHtmlContent());
            result.put("hint", exercise.getHint());
            result.put("answer", exercise.getAnswer());

        } catch (Exception e) {
            result.put("error_message", "获取练习题失败: " + e.getMessage());
        }

        return result;
    }
}