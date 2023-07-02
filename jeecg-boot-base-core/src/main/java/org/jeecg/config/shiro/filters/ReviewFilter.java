package org.jeecg.config.shiro.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
        if (requestPath.indexOf("reviewFront/") > -1) { //前端拦截
            if (httpServletRequest.getSession().getAttribute("reviewUser") != null) {
                return true;
            } else {
                String userId = httpServletRequest.getHeader("userId");
                if (userId != null) {

                    httpServletRequest.getSession().setAttribute("reviewUser", userId);
                    return true;
                }
            }
        }
        return true;
    }
}
