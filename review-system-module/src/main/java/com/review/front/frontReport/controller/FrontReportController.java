package com.review.front.frontReport.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.review.front.frontReport.entity.ReviewReportTemplateEntity;
import com.review.front.frontReport.service.IFrontReviewResultService;
import com.review.front.frontReport.service.IReviewReportTemplateService;
import com.review.front.frontReviewClass.service.IFrontReviewClassService;
import com.review.front.frontReviewClass.vo.ReviewResultVO;
import com.review.manage.reviewClass.vo.ReviewClassPage;
import com.review.manage.userManage.entity.ReviewResult;
import com.review.manage.userManage.entity.ReviewUser;
import com.review.manage.variate.entity.ReviewVariateEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Api(tags="测评报告管理")
@RestController
@RequestMapping("/reviewFront/reviewReport")
@Slf4j
public class FrontReportController extends JeecgController<ReviewResult, IFrontReviewResultService> {

    @Autowired
    private IFrontReviewResultService frontReviewResultService;

    @Autowired
    private IReviewReportTemplateService reviewReportTemplateService;

    @Autowired
    private IFrontReviewClassService reviewClassService;

    /**
     * 小程序-查询报告详情
     * @param reviewResult
     * @return
     */
    @AutoLog(value = "小程序-查询报告详情")
    @PostMapping(value = "/getReviewReportDetail")
    public Result<?> getReviewReportDetail(@RequestBody ReviewResultVO reviewResult) {
        if (reviewResult == null || StringUtils.isBlank(reviewResult.getResultId())) {
            return Result.error(300,"报告id为空");
        }
        if (reviewResult.getProjectId() == null || reviewResult.getProjectId() == 0){
            ReviewResult reviewResultEntity = frontReviewResultService.getById(reviewResult.getResultId());
            return Result.OK(reviewResultEntity);
        } else {
            List<ReviewResultVO> reviewResultVOS = frontReviewResultService.getReviewResult(reviewResult);
            return Result.OK(reviewResultVOS);
        }
    }

    /**
     * 小程序-获取测评报告列表
     * @param reviewUser
     * @return
     */
    @AutoLog(value = "小程序-获取测评报告列表")
    @PostMapping(value = "getReviewReports")
    public Result<List<ReviewResultVO>> getReviewReports(@RequestBody ReviewUser reviewUser) {
        if (reviewUser == null || StringUtils.isBlank(reviewUser.getUserId())) {
            return Result.error(300,"用户信息为空");
        }
        List<ReviewResultVO> reviewResultList = frontReviewResultService.getReviewReports(reviewUser.getUserId(), reviewUser.getProjectId());
        return Result.OK(reviewResultList);
    }

    /**
     * 小程序-查询量表对应的报告模板
     * @param reviewClass
     * @return
     */
    @AutoLog(value = "小程序-查询量表对应的报告模板")
    @PostMapping(value = "getReportTemplate")
    public Result<ReviewClassPage> getReportTemplate(@RequestBody ReviewClassPage reviewClass) {
        if (StrUtil.isBlank(reviewClass.getClassId())) {
            return Result.error(300,"量表ID不能为空");
        } else {
            ReviewClassPage reviewClassVO = reviewReportTemplateService.getByClassId(reviewClass.getClassId());
            reviewClassVO.setReportTips(reviewClassService.getById(reviewClass.getClassId()).getReportTips());
            return Result.OK("查询成功",reviewClassVO);
        }
    }

    /**
     * 小程序-获取项目测评报告
     * @param reviewResult
     * @return
     */
    @AutoLog(value = "小程序-获取项目测评报告")
    @PostMapping(value = "getProjectReviewCount")
    public Result<List<ReviewResultVO>> getProjectReviewCount(@RequestBody ReviewResultVO reviewResult, HttpServletRequest req) {
        ReviewResult reviewResult1 = new ReviewResult();
        reviewResult1.setUserId(reviewResult.getUserId());
        reviewResult1.setProjectId(reviewResult.getProjectId());
        Long pCount = reviewResult.getPCount();
        List<ReviewResultVO> result = frontReviewResultService.getProjectReviewResult(reviewResult1,pCount,req);
        return Result.OK(result);
    }
}
