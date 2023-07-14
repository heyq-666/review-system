package com.review.manage.question.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.question.entity.ReviewQuestion;
import com.review.manage.question.mapper.ReviewQuestionMapper;
import com.review.manage.question.service.IReviewQuestionService;
import com.review.manage.question.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/3/21
 */
@Service
public class ReviewQuestionServiceImpl extends ServiceImpl<ReviewQuestionMapper, ReviewQuestion> implements IReviewQuestionService {

    @Autowired
    private ReviewQuestionMapper reviewQuestionMapper;

    @Override
    public List<ReviewQuestion> getQuestionListByClassId(String classId) {
        return reviewQuestionMapper.getQuestionListByClassId(classId);
    }

    @Override
    public List<QuestionVO> getAnswersByQuestionId(String questionId) {
        return reviewQuestionMapper.getAnswersByQuestionId(questionId);
    }

    @Override
    public void deleteQuestion(String classId) {
        reviewQuestionMapper.deleteQuestion(classId);
    }

    @Override
    public Integer getMaxQuestionId(String classId) {
        return reviewQuestionMapper.getMaxQuestionId(classId);
    }

    @Override
    public ReviewQuestion getQuestionByQnum(String classId, Integer maxQuestionNum) {
        Map map = new HashMap();
        map.put("classId",classId);
        map.put("questionNum",maxQuestionNum);
        return reviewQuestionMapper.getQuestionByQnum(map);
    }
}
