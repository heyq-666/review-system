package com.review.manage.reviewClass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.reviewClass.entity.ReviewClass;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 测评量表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface ReviewClassMapper extends BaseMapper<ReviewClass> {

    void updateByClassId(ReviewClass reviewClass);

    void deleteClassByClassId(String classId);

    public boolean deleteByMainId(@Param("classId") String classId);

    List<String> getClassIds(@Param("tenantId") Long tenantId);
}
