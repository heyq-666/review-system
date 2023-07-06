package com.review.front.frontAppoint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.frontAppoint.entity.ReviewExpertReserveEntity;
import com.review.front.frontAppoint.vo.BeGoodAt;
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
    List<ConsultationVO> getMyConsultationDetail(Long id);

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

    /**
     * 获取咨询师擅长领域id
     * @return
     */
    String getExpertFieldGroup(Integer expertId);

    String getDictId(String expertFieldGroup);

    List<BeGoodAt> getDictText(String dictId,List<Integer> dictIdList);

    /**
     * 获取咨询师擅长方向标签
     * @param expertId
     * @return
     */
    List<BeGoodAt> getBeGoodAtList(Integer expertId);

    /**
     * 获取咨询师擅长方向标签名称
     * @param beGoodAtListNew
     * @return
     */
    List<BeGoodAt> getBeGoodAtNameList(List<String> beGoodAtListNew);
}
