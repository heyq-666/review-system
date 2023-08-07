package com.review.manage.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.report.entity.ReviewReportEntity;
import com.review.manage.report.mapper.ReviewReportMapper;
import com.review.manage.report.service.IReportService;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.service.IReviewClassService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javabage
 * @date 2023/3/22
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReviewReportMapper, ReviewReportEntity> implements IReportService {

    @Autowired
    private ReviewReportMapper reviewReportMapper;

    @Autowired
    private IReviewClassService reviewClassService;

    @Override
    public void getClassNameByClassId(List<ReviewReportEntity> pageList) {
        String classId = "";
        String className = "";
        for (int i = 0; i < pageList.size(); i++) {
            classId = pageList.get(i).getClassId();
            if (StringUtils.isNotBlank(classId)){
                className = reviewReportMapper.getClassNameByClassId(classId);
                pageList.get(i).setClassName(className);
            }
        }
    }

    @Override
    public void delReviewReportVariate(String reportId) {
        reviewReportMapper.delReviewReportVariate(reportId);
    }

    @Override
    public List<ReviewReportEntity> filterData(Long tenantId, List<ReviewReportEntity> list) {
        //先获取租户绑定的量表
        //获取给该租户开放的量表
        List<String> classIds = reviewClassService.getClassIds(tenantId);
        //获取共享量表
        QueryWrapper<ReviewClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_share",1);
        List<ReviewClass> shareClassList = reviewClassService.list(queryWrapper);
        for (int i = 0; i < shareClassList.size(); i++) {
            classIds.add(shareClassList.get(i).getClassId());
        }
        List<ReviewReportEntity> listNew = new ArrayList<>();
        for (int i = 0; i < classIds.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (classIds.get(i).equals(list.get(j).getClassId())){
                    listNew.add(list.get(j));
                }
            }
        }
        return listNew;
    }
}
