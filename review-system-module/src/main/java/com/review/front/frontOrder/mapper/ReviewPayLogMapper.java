package com.review.front.frontOrder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.front.frontOrder.entity.ReviewPayLogEntity;

import java.util.Map;

/**
 * @author javabage
 * @date 2023/3/30
 */
public interface ReviewPayLogMapper extends BaseMapper<ReviewPayLogEntity> {
    void updatePayLog(Map map);
}
