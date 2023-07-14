package com.review.manage.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.question.entity.ReviewQuestion;
import com.review.manage.question.entity.ReviewQuestionClassEntity;

import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/7/10
 */
public interface ReviewQuestionClassMapper extends BaseMapper<ReviewQuestionClassEntity> {
    List<ReviewQuestion> getQuestionByQnum(Map map);
}
