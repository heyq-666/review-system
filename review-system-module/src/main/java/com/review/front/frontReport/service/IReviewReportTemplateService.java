package com.review.front.frontReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.frontReport.entity.ReviewReportTemplateEntity;
import com.review.manage.reviewClass.vo.ReviewClassPage;

/**
 * @author javabage
 * @date 2023/4/4
 */
public interface IReviewReportTemplateService extends IService<ReviewReportTemplateEntity> {

    /**
     * 查询量表对应的报告模板
     * @param class_id
     * @return
     */
    ReviewClassPage getByClassId(String class_id);
}
