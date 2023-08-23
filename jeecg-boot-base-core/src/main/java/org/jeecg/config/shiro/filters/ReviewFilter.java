package org.jeecg.config.shiro.filters;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.entity.ReviewProjectEntity;
import org.jeecg.modules.base.entity.ReviewUser;
import org.jeecg.modules.base.entity.SysTenantVO;
import org.jeecg.modules.base.service.BaseCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author javabage
 * @date 2023/6/25
 */
@Slf4j
/*@Component*/
public class ReviewFilter extends BasicHttpAuthenticationFilter{

    private Logger logger = LoggerFactory.getLogger(getClass());

    private BaseCommonService baseCommonService;
    public ReviewFilter(){};
    public ReviewFilter(BaseCommonService baseCommonService){
        this.baseCommonService = baseCommonService;
    }
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String tenantId = httpServletRequest.getHeader(CommonConstant.TENANT_ID);
        if (StringUtils.isNotEmpty(tenantId)) {
            TenantContext.setTenant(tenantId);
        } else {
            String appId = httpServletRequest.getHeader(CommonConstant.APP_ID);
            SysTenantVO sysTenantVO = baseCommonService.getTenantIdByAppId(appId);
            if (sysTenantVO != null && sysTenantVO.getId() != null) {
                TenantContext.setTenant(sysTenantVO.getId().toString());
                httpServletRequest.getSession().setAttribute("appSession", sysTenantVO);
            }else {
                TenantContext.setTenant(String.valueOf(oConvertUtils.getLong(tenantId, -1)));
            }
        }
        Long tenantIdL =  oConvertUtils.getLong(tenantId, -1);
        if (!baseCommonService.checkSysTenant(tenantIdL)) {
            return false;
        }

        String requestPath = httpServletRequest.getRequestURI();
        if (requestPath.startsWith("css/") || requestPath.startsWith("images/") || requestPath.startsWith("plug-in/") || requestPath.startsWith("upload2/")) {
            return true;
        }
        if (requestPath.indexOf("reviewFront/") > -1) { //前端拦截
            //项目测评权限拦截
            String projectId = httpServletRequest.getHeader("projectId");
            if (StringUtils.isNotBlank(projectId) && !"0".equals(projectId)) {
                //获取测评用户
                ReviewUser reviewUser = getReviewUser(httpServletRequest);
                //检查用户项目权限
                if (reviewUser != null) {
                    if (checkProjectAuth(projectId, reviewUser.getGroupId())) {
                        return true;
                    }else {
                        JSONObject json = new JSONObject();
                        json.put("code", 401);
                        json.put("message", "用户没有该项目权限");
                        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        httpServletResponse.setHeader("Cache-Control", "no-store");
                        httpServletResponse.setCharacterEncoding("UTF-8");
                        PrintWriter pw = null;
                        try {
                            pw = response.getWriter();
                            pw.write(json.toString());
                            pw.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            IOUtils.closeQuietly(pw);
                        }
                        return false;
                    }
                }else {
                    return true;
                }
            }else {
                if (httpServletRequest.getSession().getAttribute("reviewUser") != null) {
                    return true;
                } else {
                    String userId = httpServletRequest.getHeader("userId");
                    if (userId != null) {
                        ReviewUser reviewUser = baseCommonService.getUserInfoByUserId(userId);
                        httpServletRequest.getSession().setAttribute("reviewUser", reviewUser);
                        return true;
                    }
                }
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
