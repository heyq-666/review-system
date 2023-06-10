package com.review.front.frontReport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontReport.entity.ReviewReportTemplateEntity;
import com.review.front.frontReport.mapper.ReviewReportTemplateMapper;
import com.review.front.frontReport.service.IReviewReportTemplateService;
import com.review.manage.report.entity.ReviewReportEntity;
import com.review.manage.reviewClass.vo.ReviewClassPage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author javabage
 * @date 2023/4/4
 */
@Service
public class ReviewReportTemplateServiceImpl extends ServiceImpl<ReviewReportTemplateMapper, ReviewReportTemplateEntity> implements IReviewReportTemplateService {

    @Override
    public ReviewClassPage getByClassId(String classId) {
        /*List<ReviewReportTemplateEntity> list = this.listByMap(
                    (Map<String, Object>) new HashMap<>().put("class_id",classId));*/
        QueryWrapper<ReviewReportTemplateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",classId);
        List<ReviewReportTemplateEntity> list = this.list(queryWrapper);
        List<ReviewReportTemplateEntity> byOrderNumSortList = list.stream().sorted(Comparator.comparing(
                    ReviewReportTemplateEntity :: getOrderNum)).collect(Collectors.toList());
        ReviewClassPage reviewClassPage = new ReviewClassPage();
        reviewClassPage.setReportTemplateList(byOrderNumSortList);
        reviewClassPage.setClassId(classId);
        return reviewClassPage;
    }
}
