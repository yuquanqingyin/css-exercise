// ExerciseController.java
package com.css_exercise.backend.controller.exercise;

import com.css_exercise.backend.service.exercise.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return exerciseService.getAllExercises();
    }

    @GetMapping("/detail/{id}/")
    public Map<String, Object> getExerciseById(@PathVariable String id) {
        return exerciseService.getExerciseById(id);
    }
}