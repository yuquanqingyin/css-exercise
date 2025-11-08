// backend/src/main/java/com/css_exercise/backend/service/impl/teacher/TeacherServiceImpl.java
package com.css_exercise.backend.service.impl.teacher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.css_exercise.backend.mapper.ExerciseMapper;
import com.css_exercise.backend.mapper.SubmissionRecordMapper;
import com.css_exercise.backend.mapper.UserMapper;
import com.css_exercise.backend.pojo.Exercise;
import com.css_exercise.backend.pojo.SubmissionRecord;
import com.css_exercise.backend.pojo.User;
import com.css_exercise.backend.service.teacher.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubmissionRecordMapper submissionRecordMapper;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Override
    public Map<String, Object> getAllStudentsWithStats() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取所有学生
            QueryWrapper<User> userQuery = new QueryWrapper<>();
            userQuery.eq("role", "student");
            List<User> students = userMapper.selectList(userQuery);

            List<Map<String, Object>> studentList = new ArrayList<>();
            for (User student : students) {
                Map<String, Object> studentInfo = new HashMap<>();
                studentInfo.put("id", student.getId());
                studentInfo.put("username", student.getUsername());

                // 统计该学生的提交情况
                QueryWrapper<SubmissionRecord> recordQuery = new QueryWrapper<>();
                recordQuery.eq("user_id", student.getId());
                List<SubmissionRecord> records = submissionRecordMapper.selectList(recordQuery);

                // 总提交次数
                studentInfo.put("totalSubmissions", records.size());

                // 已完成的练习题数（去重）
                Set<String> completedExercises = records.stream()
                        .map(SubmissionRecord::getExerciseId)
                        .collect(Collectors.toSet());
                studentInfo.put("completedExercises", completedExercises.size());

                // 满分次数
                long perfectCount = records.stream()
                        .filter(r -> r.getScore() == 3)
                        .count();
                studentInfo.put("perfectCount", perfectCount);

                // 平均分
                double avgScore = records.isEmpty() ? 0 :
                        records.stream()
                                .mapToInt(SubmissionRecord::getScore)
                                .average()
                                .orElse(0);
                studentInfo.put("avgScore", Math.round(avgScore * 10) / 10.0);

                // 最后提交时间
                if (!records.isEmpty()) {
                    SubmissionRecord lastRecord = records.stream()
                            .max(Comparator.comparing(SubmissionRecord::getSubmitTime))
                            .orElse(null);
                    if (lastRecord != null) {
                        studentInfo.put("lastSubmitTime", lastRecord.getSubmitTime().toString());
                    }
                } else {
                    studentInfo.put("lastSubmitTime", null);
                }

                studentList.add(studentInfo);
            }

            // 按最后提交时间倒序排列
            studentList.sort((a, b) -> {
                String timeA = (String) a.get("lastSubmitTime");
                String timeB = (String) b.get("lastSubmitTime");
                if (timeA == null && timeB == null) return 0;
                if (timeA == null) return 1;
                if (timeB == null) return -1;
                return timeB.compareTo(timeA);
            });

            result.put("error_message", "success");
            result.put("students", studentList);
            result.put("totalStudents", students.size());
        } catch (Exception e) {
            result.put("error_message", "获取学生列表失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getStudentSubmissions(Integer studentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 验证学生是否存在
            User student = userMapper.selectById(studentId);
            if (student == null || !"student".equals(student.getRole())) {
                result.put("error_message", "学生不存在");
                return result;
            }

            // 获取学生信息
            result.put("studentId", student.getId());
            result.put("studentName", student.getUsername());

            // 获取所有提交记录
            QueryWrapper<SubmissionRecord> recordQuery = new QueryWrapper<>();
            recordQuery.eq("user_id", studentId)
                    .orderByDesc("submit_time");
            List<SubmissionRecord> records = submissionRecordMapper.selectList(recordQuery);

            List<Map<String, Object>> submissions = new ArrayList<>();
            for (SubmissionRecord record : records) {
                Map<String, Object> submission = new HashMap<>();
                submission.put("id", record.getId());
                submission.put("exerciseId", record.getExerciseId());
                submission.put("cssCode", record.getCssCode());
                submission.put("score", record.getScore());
                submission.put("submitTime", record.getSubmitTime().toString());

                // 获取练习题信息
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
            result.put("totalSubmissions", submissions.size());
        } catch (Exception e) {
            result.put("error_message", "获取学生提交记录失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getStudentExerciseSubmissions(Integer studentId, String exerciseId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 验证学生是否存在
            User student = userMapper.selectById(studentId);
            if (student == null || !"student".equals(student.getRole())) {
                result.put("error_message", "学生不存在");
                return result;
            }

            // 获取练习题信息
            QueryWrapper<Exercise> exerciseQuery = new QueryWrapper<>();
            exerciseQuery.eq("exercise_id", exerciseId);
            Exercise exercise = exerciseMapper.selectOne(exerciseQuery);
            if (exercise == null) {
                result.put("error_message", "练习题不存在");
                return result;
            }

            result.put("studentId", student.getId());
            result.put("studentName", student.getUsername());
            result.put("exerciseId", exercise.getExerciseId());
            result.put("exerciseName", exercise.getName());
            result.put("exerciseTitle", exercise.getTitle());

            // 获取该学生在该练习题上的所有提交记录
            QueryWrapper<SubmissionRecord> recordQuery = new QueryWrapper<>();
            recordQuery.eq("user_id", studentId)
                    .eq("exercise_id", exerciseId)
                    .orderByDesc("submit_time");
            List<SubmissionRecord> records = submissionRecordMapper.selectList(recordQuery);

            List<Map<String, Object>> submissions = new ArrayList<>();
            for (SubmissionRecord record : records) {
                Map<String, Object> submission = new HashMap<>();
                submission.put("id", record.getId());
                submission.put("cssCode", record.getCssCode());
                submission.put("score", record.getScore());
                submission.put("submitTime", record.getSubmitTime().toString());
                submissions.add(submission);
            }

            result.put("error_message", "success");
            result.put("submissions", submissions);
            result.put("totalSubmissions", submissions.size());

            // 最高分
            if (!records.isEmpty()) {
                int maxScore = records.stream()
                        .mapToInt(SubmissionRecord::getScore)
                        .max()
                        .orElse(0);
                result.put("maxScore", maxScore);
            }
        } catch (Exception e) {
            result.put("error_message", "获取提交记录失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getExercisesStats() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 获取所有练习题
            List<Exercise> exercises = exerciseMapper.selectList(null);

            // 统计学生总数
            QueryWrapper<User> userQuery = new QueryWrapper<>();
            userQuery.eq("role", "student");
            long totalStudents = userMapper.selectCount(userQuery);

            List<Map<String, Object>> exerciseStatsList = new ArrayList<>();
            for (Exercise exercise : exercises) {
                Map<String, Object> stats = new HashMap<>();
                stats.put("exerciseId", exercise.getExerciseId());
                stats.put("exerciseName", exercise.getName());
                stats.put("exerciseTitle", exercise.getTitle());

                // 获取该练习题的所有提交记录
                QueryWrapper<SubmissionRecord> recordQuery = new QueryWrapper<>();
                recordQuery.eq("exercise_id", exercise.getExerciseId());
                List<SubmissionRecord> records = submissionRecordMapper.selectList(recordQuery);

                // 提交总次数
                stats.put("totalSubmissions", records.size());

                // 完成人数（至少提交过一次的不同学生）
                long completedStudents = records.stream()
                        .map(SubmissionRecord::getUserId)
                        .distinct()
                        .count();
                stats.put("completedStudents", completedStudents);

                // 完成率
                double completionRate = totalStudents > 0 ?
                        (completedStudents * 100.0 / totalStudents) : 0;
                stats.put("completionRate", Math.round(completionRate * 10) / 10.0);

                // 满分人数
                Set<Integer> perfectStudents = records.stream()
                        .filter(r -> r.getScore() == 3)
                        .map(SubmissionRecord::getUserId)
                        .collect(Collectors.toSet());
                stats.put("perfectStudents", perfectStudents.size());

                // 平均分
                double avgScore = records.isEmpty() ? 0 :
                        records.stream()
                                .mapToInt(SubmissionRecord::getScore)
                                .average()
                                .orElse(0);
                stats.put("avgScore", Math.round(avgScore * 10) / 10.0);

                exerciseStatsList.add(stats);
            }

            // 按完成人数降序排列
            exerciseStatsList.sort((a, b) ->
                    Long.compare((Long) b.get("completedStudents"),
                            (Long) a.get("completedStudents")));

            result.put("error_message", "success");
            result.put("exercises", exerciseStatsList);
            result.put("totalExercises", exercises.size());
            result.put("totalStudents", totalStudents);
        } catch (Exception e) {
            result.put("error_message", "获取练习题统计失败: " + e.getMessage());
        }
        return result;
    }
}