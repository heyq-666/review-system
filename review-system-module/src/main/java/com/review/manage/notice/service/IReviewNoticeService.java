package com.review.manage.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.notice.entity.ReviewNoticeEntity;

/**
 * @author javabage
 * @date 2023/2/16
 */
public interface IReviewNoticeService extends IService<ReviewNoticeEntity> {

    void delMain(String id);
}
