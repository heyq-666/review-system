package com.review.front.frontAppoint.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.front.frontAppoint.entity.ReviewExpertReserveEntity;
import com.review.front.frontAppoint.vo.BeGoodAt;
import com.review.front.frontAppoint.vo.ConsultationVO;
import com.review.manage.expert.entity.ReviewExpert;

import java.util.List;

/**
 * @author javabage
 * @date 2023/4/4
 */
public interface ReviewExpertReserveMapper extends BaseMapper<ReviewExpertReserveEntity> {

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
     * 获取咨询师擅长领域id
     * @param expertId
     * @return
     */
    String getExpertFieldGroup(String expertId);

    String getDictId(String expertFieldGroup);

    List<String> getDictText(String dictId,List<Integer> dictIdList);

    List<BeGoodAt> getBeGoodAtList(Integer expertId);

    List<BeGoodAt> getBeGoodAtNameList(List<String> beGoodAtListNew);
}
