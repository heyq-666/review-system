package com.review.manage.subject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.subject.entity.ReviewSubjectClassEntity;
import com.review.manage.subject.entity.ReviewSubjectEntity;

import java.util.List;

/**
 * @author javabage
 * @date 2023/2/16
 */
public interface ReviewSubjectMapper extends BaseMapper<ReviewSubjectEntity> {

    void deleteSubjectClassById(Long id);

    void insertReviewProjectClass(List<ReviewSubjectClassEntity> list);

    List<String> isExitSubjectClass(Long id);
}
