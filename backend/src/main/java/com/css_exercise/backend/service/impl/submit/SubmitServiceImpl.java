// SubmitServiceImpl.java
package com.css_exercise.backend.service.impl.submit;

import com.css_exercise.backend.service.submit.SubmitService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SubmitServiceImpl implements SubmitService {

    private final Random random = new Random();

    @Override
    public Map<String, Object> evaluateCss(String exerciseId, String cssCode, Integer userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 暂时使用随机函数返回0-3颗星
            int stars = random.nextInt(4); // 生成0到3之间的随机数

            // 模拟评测过程的延迟（可选）
            Thread.sleep(500);

            result.put("error_message", "success");
            result.put("stars", stars);
            result.put("exerciseId", exerciseId);
            result.put("message", getEvaluationMessage(stars));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.put("error_message", "评测过程被中断");
        } catch (Exception e) {
            result.put("error_message", "评测失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 根据星级返回评价信息
     */
    private String getEvaluationMessage(int stars) {
        switch (stars) {
            case 0:
                return "还需努力，继续加油！";
            case 1:
                return "有进步空间，再接再厉！";
            case 2:
                return "做得不错，继续保持！";
            case 3:
                return "完美！你真棒！";
            default:
                return "";
        }
    }
}