package com.review.manage.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.question.entity.ReviewQuestion;
import com.review.manage.question.vo.QuestionVO;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/21
 */
public interface ReviewQuestionMapper extends BaseMapper<ReviewQuestion> {

    List<ReviewQuestion> getQuestionListByClassId(String classId);

    List<QuestionVO> getAnswersByQuestionId(String questionId);
}
