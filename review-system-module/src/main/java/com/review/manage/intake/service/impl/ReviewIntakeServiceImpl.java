package com.review.manage.intake.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.intake.entity.ReviewIntake;
import com.review.manage.intake.mapper.ReviewIntakeMapper;
import com.review.manage.intake.service.IReviewIntakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: review_intake
 * @Author: jeecg-boot
 * @Date:   2023-04-11
 * @Version: V1.0
 */
@Service
public class ReviewIntakeServiceImpl extends ServiceImpl<ReviewIntakeMapper, ReviewIntake> implements IReviewIntakeService {

    @Autowired
    private ReviewIntakeMapper reviewIntakeMapper;

    @Override
    public List<String> getDepartNameList(List<String> companyIdNew) {
        return reviewIntakeMapper.getDepartNameList(companyIdNew);
    }
}
