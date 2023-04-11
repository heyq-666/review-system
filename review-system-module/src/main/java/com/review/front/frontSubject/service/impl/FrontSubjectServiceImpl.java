package com.review.front.frontSubject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.common.Constants;
import com.review.front.frontSubject.mapper.FrontSubjectMapper;
import com.review.front.frontSubject.service.IFrontSubjectService;
import com.review.manage.reviewClass.vo.ReviewClassPage;
import com.review.manage.subject.entity.ReviewSubjectEntity;
import com.review.manage.subject.vo.ReviewSubjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Service
public class FrontSubjectServiceImpl extends ServiceImpl<FrontSubjectMapper, ReviewSubjectEntity> implements IFrontSubjectService {

    @Autowired
    private FrontSubjectMapper frontSubjectMapper;

    @Override
    public List<ReviewSubjectVO> getReviewSubjectClass(ReviewSubjectVO reviewSubject, Integer page, Integer rows) {
        Map param = new HashMap();
        param.put("status", Constants.StatusPublish);
        param.put("subjectId",reviewSubject.getId());
        param.put("page",page > 0 ? (page - 1) * rows : 1);
        param.put("rows",rows > 0 ? rows : 10);
        List<ReviewClassPage> resultList = frontSubjectMapper.getReviewSubjectClass(param);
        Map<Long, ReviewSubjectVO> resultMap = new HashMap<>();
        for (ReviewClassPage reviewClass : resultList) {
            ReviewSubjectVO subject = resultMap.get(reviewClass.getSubjectId());
            if (subject == null) {
                subject = new ReviewSubjectVO();
                subject.setSubjectName(reviewClass.getSubjectName());
                resultMap.put(reviewClass.getSubjectId(), subject);
            }
            subject.getClassList().add(reviewClass);
        }
        return new ArrayList<>(resultMap.values());
    }
}
