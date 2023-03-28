package com.review.manage.expert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ReviewExpertMapper extends BaseMapper<ReviewExpert> {

    List<ReviewExpertCalendarVo> getCalendarDetailList(ReviewExpertCalendarEntity expertCalendarEntity);
}
