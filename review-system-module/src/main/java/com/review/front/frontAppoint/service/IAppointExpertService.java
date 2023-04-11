package com.review.front.frontAppoint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.frontAppoint.vo.ConsultationVO;
import com.review.manage.expert.entity.ReviewExpert;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;

import java.util.List;

/**
 * @author javabage
 * @date 2023/4/4
 */
public interface IAppointExpertService extends IService<ReviewExpert> {
    /**
     * 获取专家日历
     * @param reviewExpertCalendar
     * @return
     */
    List<ReviewExpertCalendarVo> getReviewExpertCalendars(ReviewExpertCalendarVo reviewExpertCalendar);

    /**
     * 咨询师对应的预约列表
     * @param expertPhone
     * @return
     */
    List<ConsultationVO> getMyAppointList(String expertPhone);

    void handleTime(List<ConsultationVO> reviewAppointList);
}
