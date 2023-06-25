package com.review.config.review;

import com.review.common.Constants;
import com.review.front.frontUser.service.IFrontUserService;
import com.review.manage.userManage.entity.ReviewUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
    private IFrontUserService iFrontUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("自定义拦截器执行了");
        /*String requestPath = request.getRequestURI();
        HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (requestPath.startsWith("css/") || requestPath.startsWith("images/") || requestPath.startsWith("plug-in/") || requestPath.startsWith("upload2/")) {
            return true;
        }
        if(requestPath.indexOf("reviewFront/") > -1) { //前端拦截
            if(request1.getSession().getAttribute(Constants.REVIEW_LOGIN_USER) != null) {
                return true;
            } else {  //登录失效跳到登陆页面
                String userId = request.getHeader("userId");
                if (StringUtils.isBlank(userId)) {
                    return false;
                }
                ReviewUser reviewUser = iFrontUserService.getById(userId);
                if (reviewUser != null) {
                    request1.getSession().setAttribute(Constants.REVIEW_LOGIN_USER, reviewUser);
                    return true;
                } else {
                    return false;
                }
            }
        }*/
        return true;
    }
}
