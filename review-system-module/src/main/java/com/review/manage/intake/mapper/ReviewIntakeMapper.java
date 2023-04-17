package com.review.manage.intake.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.intake.entity.ReviewIntake;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: review_intake
 * @Author: jeecg-boot
 * @Date:   2023-04-11
 * @Version: V1.0
 */
public interface ReviewIntakeMapper extends BaseMapper<ReviewIntake> {

    List<String> getDepartNameList(@Param("companyId")List<String> companyId);
}
