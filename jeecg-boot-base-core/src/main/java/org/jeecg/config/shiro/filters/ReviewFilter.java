package org.jeecg.config.shiro.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.entity.ReviewUser;
import org.jeecg.modules.base.entity.SysTenantVO;
import org.jeecg.modules.base.service.BaseCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

        String tenantId = httpServletRequest.getHeader(CommonConstant.TENANT_ID);
        TenantContext.setTenant(tenantId);

        Long tenantIdL =  oConvertUtils.getLong(tenantId, -1);
        if (!baseCommonService.checkSysTenant(tenantIdL)) {
            return false;
        }

        String requestPath = httpServletRequest.getRequestURI();
        if (requestPath.startsWith("css/") || requestPath.startsWith("images/") || requestPath.startsWith("plug-in/") || requestPath.startsWith("upload2/")) {
            return true;
        }
        if (requestPath.indexOf("reviewFront/") > -1) { //前端拦截
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
        return true;
    }
}
