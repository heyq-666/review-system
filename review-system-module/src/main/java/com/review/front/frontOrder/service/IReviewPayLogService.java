package com.review.front.frontOrder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.frontOrder.entity.ReviewPayLogEntity;

/**
 * @author javabage
 * @date 2023/3/30
 */
public interface IReviewPayLogService extends IService<ReviewPayLogEntity> {
    void updatePayLog(String jsonString, String now, int success, String outTradeNo);
}
