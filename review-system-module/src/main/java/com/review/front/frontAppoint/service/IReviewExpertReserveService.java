package com.review.front.frontAppoint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.frontAppoint.entity.ReviewExpertReserveEntity;
import com.review.front.frontAppoint.vo.ConsultationVO;

import java.util.List;

/**
 * @author javabage
 * @date 2023/4/4
 */
public interface IReviewExpertReserveService extends IService<ReviewExpertReserveEntity> {

    /**
     * 我的预约列表
     * @param userId
     * @return
     */
    List<ConsultationVO> getMyConsultation(String userId);

    /**
     * 预约详情
     * @param id
     * @return
     */
    List<ConsultationVO> getMyConsultationDetail(Integer id);

    /**
     *  时间格式处理
     * @param reviewExpertReserveList
     */
    List<ConsultationVO> beginAndEndTimehandle(List<ConsultationVO> reviewExpertReserveList);

    /**
     * 判断当前时间是否可发起视频咨询
     * @param reviewExpertReserveList
     */
    void videoConsultCondition(List<ConsultationVO> reviewExpertReserveList);

    /**
     * 判断该预约是否已经被咨询师确认
     * @param consultationVO
     * @return
     */
    void isConfirmByExpert(ConsultationVO consultationVO,List<ConsultationVO> reviewExpertReserveList);
}