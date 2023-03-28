package com.review.manage.report.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.report.entity.ReviewReportEntity;

/**
 * @author javabage
 * @date 2023/3/22
 */
public interface IReportService extends IService<ReviewReportEntity> {

    void getClassNameByClassId(IPage<ReviewReportEntity> pageList);

    void delReviewReportVariate(String reportId);
}
