package com.review.manage.reviewClass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.reviewClass.entity.ReviewClass;

/**
 * @Description: 测评量表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface ReviewClassMapper extends BaseMapper<ReviewClass> {

    void reviewStopById(String id);

    void reviewPublishById(String id);
}
