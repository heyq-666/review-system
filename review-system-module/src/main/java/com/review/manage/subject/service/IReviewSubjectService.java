package com.review.manage.subject.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.subject.entity.ReviewSubjectEntity;

/**
 * @author javabage
 * @date 2023/2/16
 */
public interface IReviewSubjectService extends IService<ReviewSubjectEntity> {
    void delMain(String id);

    void insert(ReviewSubjectEntity reviewSubject);

    void getSubjectClassInfo(IPage<ReviewSubjectEntity> pageList);
}
