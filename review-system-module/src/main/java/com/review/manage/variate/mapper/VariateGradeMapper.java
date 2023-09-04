package com.review.manage.variate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.variate.entity.ReviewVariateGradeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/23
 */
public interface VariateGradeMapper extends BaseMapper<ReviewVariateGradeEntity> {
    void deleteVariateGrade(@Param("variateIds")List<String> variateIds);
}
