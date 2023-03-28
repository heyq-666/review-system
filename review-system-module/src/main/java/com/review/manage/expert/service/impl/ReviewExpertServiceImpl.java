package com.review.manage.expert.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.expert.entity.ReviewExpert;
import com.review.manage.expert.entity.ReviewExpertCalendarEntity;
import com.review.manage.expert.mapper.ReviewExpertMapper;
import com.review.manage.expert.service.IReviewExpertService;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: review_expert
 * @Author: jeecg-boot
 * @Date:   2023-03-10
 * @Version: V1.0
 */
@Service
public class ReviewExpertServiceImpl extends ServiceImpl<ReviewExpertMapper, ReviewExpert> implements IReviewExpertService {

    @Autowired
    private ReviewExpertMapper reviewExpertMapper;

    @Override
    public List<ReviewExpertCalendarVo> getCalendarDetailList(ReviewExpertCalendarEntity expertCalendarEntity) {
        return reviewExpertMapper.getCalendarDetailList(expertCalendarEntity);
    }
}
