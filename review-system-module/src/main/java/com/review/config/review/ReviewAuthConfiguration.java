package com.review.config.review;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author javabage
 * @date 2023/4/24
 */
@Configuration
public class ReviewAuthConfiguration implements WebMvcConfigurer {

    @Bean
    public ReviewAuthInterceptor reviewAuthInterceptor() {
        return new ReviewAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //小程序端需要拦截的url
        /*String uniAppUrl = "/reviewFront/notice/list";
        registry.addInterceptor(reviewAuthInterceptor()).addPathPatterns(uniAppUrl);*/
    }
}
