package com.review.manage.expert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.expert.entity.ReviewExpertCalendarEntity;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;

/**
 * @author javabage
 * @date 2023/3/28
 */
public interface IReviewExpertCalendarService extends IService<ReviewExpertCalendarEntity> {

    /**
     * 处理日历入参数据
     * @param reviewExpertCalendarVo
     */
    void handleCalendarData(ReviewExpertCalendarVo reviewExpertCalendarVo);
}
