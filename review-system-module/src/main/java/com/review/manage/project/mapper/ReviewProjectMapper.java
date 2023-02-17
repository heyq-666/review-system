package com.review.manage.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.project.entity.ReviewProjectClassEntity;
import com.review.manage.project.entity.ReviewProjectEntity;

import java.util.List;

/**
 * @Description: 测评量表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface ReviewProjectMapper extends BaseMapper<ReviewProjectEntity> {

    void deleteProjectClassById(Long id);

    void insertReviewProjectClass(List<ReviewProjectClassEntity> list);

    List<String> isExitProjectClass(Long id);
}
