// ExerciseLink.java
package com.css_exercise.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("exercise_link")
public class ExerciseLink {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String sourceId;
    private String targetId;
    private LocalDateTime createTime;
}