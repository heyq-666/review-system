package com.review.manage.variate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.variate.entity.ReviewVariateGradeEntity;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/23
 */
public interface IVariateGradeService extends IService<ReviewVariateGradeEntity> {

    void deleteVariateGrade(List<String> variateIds);
}
