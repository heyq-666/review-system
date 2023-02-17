package com.review.manage.project.controller.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.notice.entity.ReviewNoticeEntity;
import com.review.manage.notice.mapper.ReviewNoticeMapper;
import com.review.manage.notice.service.IReviewNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author javabage
 * @date 2023/2/16
 */
@Service
public class ReviewNoticeServiceImpl extends ServiceImpl<ReviewNoticeMapper, ReviewNoticeEntity> implements IReviewNoticeService {

    @Autowired
    private ReviewNoticeMapper reviewNoticeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        reviewNoticeMapper.deleteById(id);
    }
}
