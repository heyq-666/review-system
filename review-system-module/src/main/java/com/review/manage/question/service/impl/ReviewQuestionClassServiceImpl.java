package com.review.manage.question.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.question.entity.ReviewQuestion;
import com.review.manage.question.entity.ReviewQuestionClassEntity;
import com.review.manage.question.mapper.ReviewQuestionClassMapper;
import com.review.manage.question.service.IReviewQuestionClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/7/10
 */
@Service
public class ReviewQuestionClassServiceImpl extends ServiceImpl<ReviewQuestionClassMapper, ReviewQuestionClassEntity>
        implements IReviewQuestionClassService {

    @Autowired
    private ReviewQuestionClassMapper reviewQuestionClassMapper;
    @Override
    public ReviewQuestion getQuestionByQnum(String classId, Integer questionNum) {
        Map map = new HashMap();
        map.put("classId",classId);
        map.put("questionNum",questionNum);
        List<ReviewQuestion> reviewQuestion = reviewQuestionClassMapper.getQuestionByQnum(map);
        ReviewQuestion questionEntity = null;
        if (reviewQuestion != null && reviewQuestion.size() > 0) {
            if (reviewQuestion.get(0).getQuestionId() != null) {
                questionEntity = new ReviewQuestion();
                questionEntity.setQuestionId(reviewQuestion.get(0).getQuestionId());
            }
        }
        return questionEntity;
    }
}
