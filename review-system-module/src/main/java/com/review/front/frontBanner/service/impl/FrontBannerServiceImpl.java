package com.review.front.frontBanner.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontBanner.mapper.FrontBannerMapper;
import com.review.front.frontBanner.service.IFrontBannerService;
import com.review.manage.banner.entity.ReviewBannerEntity;
import org.springframework.stereotype.Service;

/**
 * @author javabage
 * @date 2023/4/7
 */
@Service
public class FrontBannerServiceImpl extends ServiceImpl<FrontBannerMapper, ReviewBannerEntity> implements IFrontBannerService {
}
