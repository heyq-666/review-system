package com.review.manage.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.report.entity.ReviewReportEntity;

/**
 * @author javabage
 * @date 2023/3/22
 */
public interface ReviewReportMapper extends BaseMapper<ReviewReportEntity> {

    String getClassNameByClassId(String classId);

    void delReviewReportVariate(String reportId);
}
