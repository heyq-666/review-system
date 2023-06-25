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
        //小程序端不需要拦截的url
        /*List<String> excludePathPatternsList = new ArrayList<String>();
        excludePathPatternsList.add("review/reviewFront/user/register");
        excludePathPatternsList.add("review/reviewFront/user/getUserInfoByOpenid");
        excludePathPatternsList.add("review/reviewFront/reviewClass/getReviewClass");
        excludePathPatternsList.add("review/reviewFront/user/getOpenid");
        excludePathPatternsList.add("review/reviewFront/project/getReviewProjectDetail");
        excludePathPatternsList.add("review/reviewFront/subject/getReviewSubjectClass");
        excludePathPatternsList.add("review/reviewFront/sendMsg/SendMsgCode");
        excludePathPatternsList.add("review/reviewFront/notice/list");
        excludePathPatternsList.add("review/reviewFront/notice/detail");
        excludePathPatternsList.add("review/reviewFront/banner/list");
        registry.addInterceptor(reviewAuthInterceptor()).excludePathPatterns(excludePathPatternsList);*/
    }
}
