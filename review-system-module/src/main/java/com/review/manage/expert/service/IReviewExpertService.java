package com.review.manage.expert.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.expert.entity.ReviewExpert;
import com.review.manage.expert.entity.ReviewExpertCalendarEntity;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;

import java.util.Date;
import java.util.List;

/**
 * @Description: review_expert
 * @Author: jeecg-boot
 * @Date:   2023-03-10
 * @Version: V1.0
 */
public interface IReviewExpertService extends IService<ReviewExpert> {

    List<ReviewExpertCalendarVo> getCalendarDetailList(ReviewExpertCalendarEntity expertCalendarEntity);
}
