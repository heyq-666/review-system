package com.review.manage.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.report.entity.ReviewReportEntity;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/22
 */
public interface IReportService extends IService<ReviewReportEntity> {

    void getClassNameByClassId(List<ReviewReportEntity> pageList);

    void delReviewReportVariate(String reportId);

    List<ReviewReportEntity> filterData(Long tenantId, List<ReviewReportEntity> list);
}
