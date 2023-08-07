package com.review.manage.variate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.service.IReviewClassService;
import com.review.manage.variate.entity.ReviewVariateEntity;
import com.review.manage.variate.mapper.ReviewVariateMapper;
import com.review.manage.variate.service.IVariateService;
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
public class VariateServiceImpl extends ServiceImpl<ReviewVariateMapper, ReviewVariateEntity> implements IVariateService {

    @Autowired
    private ReviewVariateMapper reviewVariateMapper;

    @Autowired
    private IReviewClassService reviewClassService;

    /**
     * 因子列表数据存储量表名称
     * @param pageList
     */
    @Override
    public void getClassNameByClassId(List<ReviewVariateEntity> pageList) {
        String classId = "";
        String className = "";
        for (int i = 0; i < pageList.size(); i++) {
            classId = pageList.get(i).getClassId();
            if (StringUtils.isNotBlank(classId)){
                className = reviewVariateMapper.getClassNameByClassId(classId);
                pageList.get(i).setClassName(className);
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

    @Override
    public List<ReviewVariateEntity> filterData(Long tenantId,List<ReviewVariateEntity> list) {
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
        List<ReviewVariateEntity> listNew = new ArrayList<>();
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
