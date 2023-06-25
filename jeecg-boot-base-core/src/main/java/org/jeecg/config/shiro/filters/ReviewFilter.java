package org.jeecg.config.shiro.filters;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.base.entity.ReviewUser;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.base.service.IFrontUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author javabage
 * @date 2023/6/25
 */
@Slf4j
public class ReviewFilter extends BasicHttpAuthenticationFilter{

    List<String> exList = new ArrayList<String>();

    public ReviewFilter(){};

    public ReviewFilter(List<String> exList){
        this.exList = exList;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        System.out.println("自定义拦截器执行了");
        String requestPath = httpServletRequest.getRequestURI();
        if (requestPath.startsWith("css/") || requestPath.startsWith("images/") || requestPath.startsWith("plug-in/") || requestPath.startsWith("upload2/")) {
            return true;
        }
        if (exList.contains(requestPath)) {
            return true;
        } else {
            if (requestPath.indexOf("reviewFront/") > -1) { //前端拦截
                if (httpServletRequest.getSession().getAttribute("reviewUser") != null) {
                    return true;
                } else {
                    String userId = httpServletRequest.getHeader("userId");
                    if (StringUtils.isBlank(userId)) {
                        return false;
                    }
                    //ReviewUser reviewUser = this.getById(userId);
                    if (userId != null) {
                        httpServletRequest.getSession().setAttribute("reviewUser", userId);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
