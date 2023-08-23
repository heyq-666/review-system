package org.jeecg.config.shiro.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.jeecg.modules.base.entity.ReviewProjectEntity;
import org.jeecg.modules.base.entity.ReviewUser;
import org.jeecg.modules.base.service.BaseCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javabage
 * @date 2023/8/21
 */
@Slf4j
public class ReviewProjectAuthInterceptor extends BasicHttpAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private BaseCommonService baseCommonService;

    public ReviewProjectAuthInterceptor(){};

    public ReviewProjectAuthInterceptor(BaseCommonService baseCommonService){
        this.baseCommonService = baseCommonService;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestPath = httpServletRequest.getRequestURI();

        //白名单
        List<String> excludeUrls = new ArrayList<>();
        excludeUrls.add("/review/reviewFront/user/getOpenid");
        excludeUrls.add("/review/reviewFront/notice/list");
        excludeUrls.add("/review/reviewFront/reviewClass/getReviewClass");
        excludeUrls.add("/review/reviewFront/project/getReviewProjectDeta");
        excludeUrls.add("/review/reviewFront/user/getUserInfoByOpenid");
        if (excludeUrls.contains(requestPath)) {
            return true;
        }
        if(requestPath.indexOf("reviewFront/") > -1) {
            String projectId = httpServletRequest.getHeader("projectId");
            if (StringUtils.isNotBlank(projectId) && !"0".equals(projectId)) {
                //获取测评用户
                ReviewUser reviewUser = getReviewUser(httpServletRequest);
                //检查用户项目权限
                if (reviewUser != null && checkProjectAuth(projectId, reviewUser.getGroupId())) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }
    private ReviewUser getReviewUser(HttpServletRequest request) {
        ReviewUser reviewUser = (ReviewUser) request.getSession().getAttribute("reviewUser");
        if (reviewUser == null) {
            String userId = request.getHeader("userId");
            if (StringUtils.isNotBlank(userId)) {
                reviewUser = baseCommonService.getUserInfoByUserId(userId);
            }
        }
        return reviewUser;
    }
    private boolean checkProjectAuth(String projectId, String userGroupId) {
        ReviewProjectEntity reviewProject = baseCommonService.getProjectInfo(Long.valueOf(projectId));
        if (reviewProject == null) {
            return false;
        }
        if (StringUtils.isNotBlank(userGroupId) && userGroupId.indexOf(reviewProject.getGroupId()) > -1) {
            return true;
        }
        return false;
    }
}
