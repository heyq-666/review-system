package com.review.front.frontAppoint.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.front.frontAppoint.vo.ConsultationVO;
import com.review.manage.expert.entity.ReviewExpert;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;

import java.util.List;

/**
 * @author javabage
 * @date 2023/4/4
 */
public interface AppointExpertMapper extends BaseMapper<ReviewExpert> {

    /**
     * 获取专家日历
     * @param expertId
     * @return
     */
    List<ReviewExpertCalendarVo> getReviewExpertCalendars(Long expertId);

    /**
     * 咨询师对应的预约列表
     * @param expertPhone
     * @return
     */
    List<ConsultationVO> getMyAppointList(String expertPhone);
}
