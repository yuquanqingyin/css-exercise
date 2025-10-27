// ExerciseService.java
package com.css_exercise.backend.service.exercise;

import java.util.Map;

public interface ExerciseService {
    Map<String, Object> getAllExercises(Integer userId);
    Map<String, Object> getExerciseById(String exerciseId);
}