package com.review.manage.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.question.entity.ReviewQuestion;
import com.review.manage.question.vo.QuestionVO;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/21
 */
public interface IReviewQuestionService extends IService<ReviewQuestion> {

    List<ReviewQuestion> getQuestionListByClassId(String classId);

    List<QuestionVO> getAnswersByQuestionId(String questionId);

    void deleteQuestion(String classId);

    Integer getMaxQuestionId(String classId);

    ReviewQuestion getQuestionByQnum(String classId, Integer maxQuestionNum);
}
