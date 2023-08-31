package com.review.front.frontOrder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontOrder.entity.ReviewPayLogEntity;
import com.review.front.frontOrder.mapper.ReviewPayLogMapper;
import com.review.front.frontOrder.service.IReviewPayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/3/30
 */
@Service
public class ReviewPayLogServiceImpl extends ServiceImpl<ReviewPayLogMapper, ReviewPayLogEntity> implements IReviewPayLogService {

    @Autowired
    private ReviewPayLogMapper reviewPayLogMapper;
    @Override
    public void updatePayLog(String jsonString, String now, int success, String outTradeNo) {
        Map map = new HashMap();
        map.put("callbackResp",jsonString);
        map.put("operateTime",now);
        map.put("operateType",success);
        map.put("orderNo",outTradeNo);
        reviewPayLogMapper.updatePayLog(map);
    }
}
