package com.review.manage.question.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.question.entity.ReviewAnswerEntity;
import com.review.manage.question.mapper.ReviewAnswerMapper;
import com.review.manage.question.service.IReviewAnswerService;
import org.springframework.stereotype.Service;

/**
 * @author javabage
 * @date 2023/7/10
 */
@Service
public class ReviewAnswerServiceImpl extends ServiceImpl<ReviewAnswerMapper, ReviewAnswerEntity> implements IReviewAnswerService {
}
