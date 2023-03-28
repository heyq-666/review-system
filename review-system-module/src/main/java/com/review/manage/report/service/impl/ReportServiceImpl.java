package com.review.manage.report.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.report.entity.ReviewReportEntity;
import com.review.manage.report.mapper.ReviewReportMapper;
import com.review.manage.report.service.IReportService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/22
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReviewReportMapper, ReviewReportEntity> implements IReportService {

    @Autowired
    private ReviewReportMapper reviewReportMapper;

    @Override
    public void getClassNameByClassId(IPage<ReviewReportEntity> pageList) {
        List<ReviewReportEntity> newPageList = pageList.getRecords();
        String classId = "";
        String className = "";
        for (int i = 0; i < newPageList.size(); i++) {
            classId = newPageList.get(i).getClassId();
            if (StringUtils.isNotBlank(classId)){
                className = reviewReportMapper.getClassNameByClassId(classId);
                newPageList.get(i).setClassName(className);
            }
        }
    }

    @Override
    public void delReviewReportVariate(String reportId) {
        reviewReportMapper.delReviewReportVariate(reportId);
    }
}
