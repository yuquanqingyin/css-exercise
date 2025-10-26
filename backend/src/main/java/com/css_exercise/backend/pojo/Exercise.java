// Exercise.java
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
@TableName("exercise")
public class Exercise {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String exerciseId;
    private String name;
    private String title;
    private String description;
    private String htmlContent;
    private String hint;
    private String answer;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}