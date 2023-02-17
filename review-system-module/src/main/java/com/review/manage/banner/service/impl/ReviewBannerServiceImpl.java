package com.review.manage.banner.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.banner.entity.ReviewBannerEntity;
import com.review.manage.banner.mapper.ReviewBannerMapper;
import com.review.manage.banner.service.IReviewBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author javabage
 * @date 2023/2/16
 */
@Service
public class ReviewBannerServiceImpl extends ServiceImpl<ReviewBannerMapper, ReviewBannerEntity> implements IReviewBannerService {

    @Autowired
    private ReviewBannerMapper reviewBannerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        reviewBannerMapper.deleteById(id);
    }
}
