package com.review.manage.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.report.entity.ReviewReportGradeEntity;

/**
 * @author javabage
 * @date 2023/3/24
 */
public interface IReportGradeService extends IService<ReviewReportGradeEntity> {
    void delReviewReportGrade(String reportId);
}
