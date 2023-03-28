package com.review.manage.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.report.entity.ReviewReportGradeEntity;

/**
 * @author javabage
 * @date 2023/3/24
 */
public interface ReviewReportGradeMapper extends BaseMapper<ReviewReportGradeEntity> {
    void delReviewReportGrade(String reportId);
}
