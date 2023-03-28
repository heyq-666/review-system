package com.review.manage.report.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.report.entity.ReviewReportGradeEntity;
import com.review.manage.report.mapper.ReviewReportGradeMapper;
import com.review.manage.report.service.IReportGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author javabage
 * @date 2023/3/24
 */
@Service
public class ReportGradeImpl extends ServiceImpl<ReviewReportGradeMapper, ReviewReportGradeEntity> implements IReportGradeService {

    @Autowired
    private ReviewReportGradeMapper reviewReportGradeMapper;

    @Override
    public void delReviewReportGrade(String reportId) {
        reviewReportGradeMapper.delReviewReportGrade(reportId);
    }
}
