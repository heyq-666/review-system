package com.review.front.frontReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.frontReviewClass.vo.ReviewResultVO;
import com.review.manage.userManage.entity.ReviewResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author javabage
 * @date 2023/4/3
 */
public interface IFrontReviewResultService extends IService<ReviewResult> {

    /**
     * 小程序-获取报告信息
     * @param reviewResult
     * @return
     */
    List<ReviewResultVO> getReviewResult(ReviewResultVO reviewResult);

    /**
     * 小程序-获取测评报告列表
     * @param userId
     * @param projectId
     * @return
     */
    List<ReviewResultVO> getReviewReports(String userId, Long projectId);

    /**
     * 获取项目测评报告
     * @param reviewResult
     * @param pCount
     * @param req
     * @return
     */
    List<ReviewResultVO> getProjectReviewResult(ReviewResult reviewResult,Long pCount, HttpServletRequest req);
}
