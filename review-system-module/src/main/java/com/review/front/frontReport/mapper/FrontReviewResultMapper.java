package com.review.front.frontReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.front.frontReviewClass.vo.ReviewResultVO;
import com.review.manage.userManage.entity.ReviewResult;

import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/3
 */
public interface FrontReviewResultMapper extends BaseMapper<ReviewResult> {

    /**
     * 小程序-获取报告信息
     * @param reviewResult
     * @return
     */
    List<ReviewResultVO> getReviewResult(ReviewResultVO reviewResult);

    /**
     * 小程序-获取测评报告列表
     * @param param
     * @return
     */
    List<ReviewResultVO> getReviewReports(Map param);
}
