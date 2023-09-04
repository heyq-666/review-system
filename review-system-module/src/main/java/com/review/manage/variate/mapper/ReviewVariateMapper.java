package com.review.manage.variate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.variate.entity.ReviewVariateEntity;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/22
 */
public interface ReviewVariateMapper extends BaseMapper<ReviewVariateEntity> {
    /**
     * 通过classId获取量表名称
     * @return
     */
    String getClassNameByClassId(String classId);

    /**
     * 删除变量题目关联表
     * @param variateId
     */
    void delCariateGrade(String variateId);

    /**
     * 删除变量-报告关联表数据
     * @param variateId
     */
    void delReportVariate(String variateId);

    /**
     * 获取某分类下的因子最大编号
     * @param classId
     * @return
     */
    Integer getMaxSortNum(String classId);

    List<String> getVariateIds(String classId);
}
