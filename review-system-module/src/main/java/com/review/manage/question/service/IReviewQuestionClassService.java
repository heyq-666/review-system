package com.review.manage.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.question.entity.ReviewQuestion;
import com.review.manage.question.entity.ReviewQuestionClassEntity;

/**
 * @author javabage
 * @date 2023/7/10
 */
public interface IReviewQuestionClassService extends IService<ReviewQuestionClassEntity> {
    ReviewQuestion getQuestionByQnum(String classId, Integer questionNum);
}