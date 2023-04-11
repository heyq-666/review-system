package com.review.front.frontUser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.userManage.entity.ReviewUser;
import org.jeecg.common.api.vo.Result;

/**
 * @author javabage
 * @date 2023/4/3
 */
public interface IFrontUserService extends IService<ReviewUser> {

    /**
     * 用户注册
     * @param reviewUser
     * @return
     */
    Result register(ReviewUser reviewUser);
}
