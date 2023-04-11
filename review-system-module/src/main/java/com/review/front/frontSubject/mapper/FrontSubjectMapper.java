package com.review.front.frontSubject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.reviewClass.vo.ReviewClassPage;
import com.review.manage.subject.entity.ReviewSubjectEntity;

import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/3
 */
public interface FrontSubjectMapper extends BaseMapper<ReviewSubjectEntity> {

    /**
     * 小程序-获取测评专题分类
     * @param param
     * @return
     */
    List<ReviewClassPage> getReviewSubjectClass(Map param);
}
