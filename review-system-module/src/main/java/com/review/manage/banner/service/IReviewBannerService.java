package com.review.manage.banner.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.banner.entity.ReviewBannerEntity;

/**
 * @author javabage
 * @date 2023/2/16
 */
public interface IReviewBannerService extends IService<ReviewBannerEntity> {

    void delMain(String id);
}
