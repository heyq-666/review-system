package com.review.manage.variate.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.variate.entity.ReviewVariateEntity;
import com.review.manage.variate.mapper.ReviewVariateMapper;
import com.review.manage.variate.service.IVariateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/22
 */
@Service
public class VariateServiceImpl extends ServiceImpl<ReviewVariateMapper, ReviewVariateEntity> implements IVariateService {

    @Autowired
    private ReviewVariateMapper reviewVariateMapper;

    /**
     * 因子列表数据存储量表名称
     * @param pageList
     */
    @Override
    public void getClassNameByClassId(IPage<ReviewVariateEntity> pageList) {
        List<ReviewVariateEntity> newPageList = pageList.getRecords();
        String classId = "";
        String className = "";
        for (int i = 0; i < newPageList.size(); i++) {
            classId = newPageList.get(i).getClassId();
            if (StringUtils.isNotBlank(classId)){
                className = reviewVariateMapper.getClassNameByClassId(classId);
                newPageList.get(i).setClassName(className);
            }
        }
    }

    @Override
    public void delVariateGrade(String variateId) {
        reviewVariateMapper.delCariateGrade(variateId);
    }

    @Override
    public void delReportVariate(String variateId) {
        reviewVariateMapper.delReportVariate(variateId);
    }

    @Override
    public Integer getMaxSortNum(String classId) {
        return reviewVariateMapper.getMaxSortNum(classId);
    }
}
