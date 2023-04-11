package com.review.front.frontOrder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.reviewOrder.entity.ReviewOrder;
import com.review.manage.reviewOrder.vo.ReviewOrderVO;

import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/7
 */
public interface FrontOrderMapper extends BaseMapper<ReviewOrder> {

    /**
     * 个人中心我的订购
     * @param param
     * @return
     */
    List<ReviewOrderVO> getOrderList(Map param);
}
