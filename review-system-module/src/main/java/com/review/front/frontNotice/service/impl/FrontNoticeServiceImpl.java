package com.review.front.frontNotice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontNotice.mapper.FrontNoticeMapper;
import com.review.front.frontNotice.service.IFrontNoticeService;
import com.review.manage.notice.entity.ReviewNoticeEntity;
import org.springframework.stereotype.Service;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Service
public class FrontNoticeServiceImpl extends ServiceImpl<FrontNoticeMapper, ReviewNoticeEntity> implements IFrontNoticeService {
}
