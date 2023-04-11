package com.review.front.frontOrder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontOrder.entity.ReviewPayLogEntity;
import com.review.front.frontOrder.mapper.ReviewPayLogMapper;
import com.review.front.frontOrder.service.IReviewPayLogService;
import org.springframework.stereotype.Service;

/**
 * @author javabage
 * @date 2023/3/30
 */
@Service
public class ReviewPayLogServiceImpl extends ServiceImpl<ReviewPayLogMapper, ReviewPayLogEntity> implements IReviewPayLogService {
}
