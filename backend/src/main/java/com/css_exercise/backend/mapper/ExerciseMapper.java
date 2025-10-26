// ExerciseMapper.java
package com.css_exercise.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.css_exercise.backend.pojo.Exercise;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExerciseMapper extends BaseMapper<Exercise> {
}