// SubmissionRecord.java
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
@TableName("submission_record")
public class SubmissionRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String exerciseId;
    private String cssCode;
    private Integer score;
    private LocalDateTime submitTime;
}