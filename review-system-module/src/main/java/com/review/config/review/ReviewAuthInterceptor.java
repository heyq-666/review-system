package com.review.config.review;

import com.review.front.frontUser.service.IFrontUserService;
import com.review.manage.userManage.entity.ReviewUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author javabage
 * @date 2023/4/24
 */
@Slf4j
public class ReviewAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IFrontUserService iFrontUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("ReviewAuth Interceptor request URI = " + request.getRequestURI());
        System.out.println("自定义拦截器执行了");
        HttpSession sessoin = request.getSession();
        String userId = request.getHeader("userId");
        System.out.println(userId);
        if (StringUtils.isNotBlank(userId)) {
            ReviewUser reviewUser = iFrontUserService.getById(userId);
            /*String token = JwtUtil.sign(reviewUser.getUserName(),"123456");
            redisUtil.set(CommonConstant.REVIEW_LOGIN_USER + token,reviewUser);*/
            sessoin.setAttribute(CommonConstant.REVIEW_LOGIN_USER,reviewUser);
        }
        return true;
    }
}
