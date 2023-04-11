package com.review.front.frontSubject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.subject.entity.ReviewSubjectEntity;
import com.review.manage.subject.vo.ReviewSubjectVO;

import java.util.List;

/**
 * @author javabage
 * @date 2023/4/3
 */
public interface IFrontSubjectService extends IService<ReviewSubjectEntity> {

    /**
     * 小程序-获取测评专题分类
     * @param reviewSubject
     * @param page
     * @param rows
     * @return
     */
    List<ReviewSubjectVO> getReviewSubjectClass(ReviewSubjectVO reviewSubject, Integer page, Integer rows);
}
