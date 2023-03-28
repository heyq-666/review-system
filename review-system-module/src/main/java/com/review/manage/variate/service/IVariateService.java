package com.review.manage.variate.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.variate.entity.ReviewVariateEntity;

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
}
