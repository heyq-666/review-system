package com.review.manage.intake.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.intake.entity.ReviewIntake;

import java.util.List;

/**
 * @Description: review_intake
 * @Author: jeecg-boot
 * @Date:   2023-04-11
 * @Version: V1.0
 */
public interface IReviewIntakeService extends IService<ReviewIntake> {

    List<String> getDepartNameList(List<String> companyIdNew);
}
