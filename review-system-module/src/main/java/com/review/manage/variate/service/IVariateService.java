package com.review.manage.variate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.variate.entity.ReviewVariateEntity;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/22
 */
public interface IVariateService extends IService<ReviewVariateEntity> {
    /**
     * 因子列表数据存储量表名称
     * @param pageList
     */
    void getClassNameByClassId(IPage<ReviewVariateEntity> pageList);

    void delVariateGrade(String variateId);

    void delReportVariate(String variateId);

    /**
     * 获取某分类下的因子最大编号
     * @param classId
     * @return
     */
    Integer getMaxSortNum(String classId);

    List<ReviewVariateEntity> filterData(Long tenantId,IPage<ReviewVariateEntity> pageList);
}
