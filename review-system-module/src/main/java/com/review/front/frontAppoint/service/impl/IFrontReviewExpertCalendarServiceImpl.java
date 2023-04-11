package com.review.front.frontAppoint.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontAppoint.mapper.FrontReviewExpertCalendarMapper;
import com.review.front.frontAppoint.service.IFrontReviewExpertCalendarService;
import com.review.manage.expert.entity.ReviewExpertCalendarEntity;
import org.springframework.stereotype.Service;

/**
 * @author javabage
 * @date 2023/4/4
 */
@Service
public class IFrontReviewExpertCalendarServiceImpl extends
        ServiceImpl<FrontReviewExpertCalendarMapper, ReviewExpertCalendarEntity>
        implements IFrontReviewExpertCalendarService {
}
