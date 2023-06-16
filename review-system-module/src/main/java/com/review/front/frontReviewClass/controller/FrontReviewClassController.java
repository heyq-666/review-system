package com.review.front.frontReviewClass.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.review.common.Constants;
import com.review.front.frontReviewClass.service.IFrontReviewClassService;
import com.review.front.frontReviewClass.vo.ReviewResultVO;
import com.review.manage.question.vo.QuestionVO;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.vo.ReviewClassPage;
import com.review.manage.userManage.entity.ReviewResult;
import com.review.manage.userManage.entity.ReviewUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Api(tags="测评量表管理")
@RestController
@RequestMapping("/reviewFront/reviewClass")
@Slf4j
public class FrontReviewClassController extends JeecgController<ReviewClass, IFrontReviewClassService> {

    @Autowired
    private IFrontReviewClassService frontReviewClassService;

    /**
     * 小程序-根据分类ID查询分类下的问题及选项
     * @param questionVO
     * @return
     */
    @AutoLog(value = "小程序-根据分类ID查询分类下的问题及选项")
    @PostMapping(value = "/getQuestionsByClassID")
    public Result<?> getQuestionsByClassID(@RequestBody QuestionVO questionVO) {
        if (StringUtils.isBlank(questionVO.getClassId())) {
            return Result.error(300,"classID不能为空");
        }
        List<QuestionVO> questionVOList = frontReviewClassService.getQuestionVOList(questionVO.getClassId());
        for (int i = 0; i < questionVOList.size(); i++) {
            QuestionVO questionVO1 = questionVOList.get(i);
            questionVO1.setSelectList(frontReviewClassService.getSelectVOList(questionVO1.getQuestionId()));
        }
        return Result.OK("查询成功",questionVOList);
    }

    /**
     * 小程序-完成测试
     * @param resultArr
     * @param request
     * @return
     */
    @AutoLog(value = "小程序-完成测试")
    @PostMapping(value = "/completeReview")
    public Result<ReviewResult> completeReview(@RequestBody QuestionVO[] resultArr, HttpServletRequest request) {

        ReviewUser user = (ReviewUser) request.getSession().getAttribute(Constants.REVIEW_LOGIN_USER);
        ReviewResult reviewResult = null;
        List<QuestionVO> resultList = Arrays.asList(resultArr);
        if (user == null) {
            reviewResult = frontReviewClassService.completeReview(resultList,resultArr[0].getClassId(),new ReviewUser());
        } else {
            reviewResult = frontReviewClassService.completeReview(resultList, resultArr[0].getClassId(), user);
        }
        reviewResult.setReviewResult(null);
        return Result.OK(reviewResult);
    }

    /**
     * 获取测评分类
     * @param reviewClass
     * @return
     */
    @AutoLog(value = "小程序-获取测评分类")
    @PostMapping(value = "/getReviewClass")
    public Result<List<ReviewClassPage>> getReviewClassByProjectId(@RequestBody ReviewClassPage reviewClass) {
        List<ReviewClassPage> reviewClassList = frontReviewClassService.getReviewClassByProjectId(reviewClass.getProjectId());
        return Result.OK("查询成功",reviewClassList);
    }

    /**
     * 小程序-查询测评分类详情
     * @param reviewClass
     * @return
     */
    @AutoLog(value = "小程序-查询测评分类详情")
    @PostMapping(value = "/getReviewClassDetail")
    public Result<ReviewClassPage> getReviewClassDetail(HttpServletRequest request,@RequestBody ReviewClassPage reviewClass) {
        if (reviewClass == null || StringUtils.isBlank(reviewClass.getClassId())) {
            return Result.error(300,"分类ID为空");
        }
        ReviewClass reviewClassInfo = frontReviewClassService.getById(reviewClass.getClassId());
        if (reviewClassInfo == null) {
            return Result.error(404,"量表不存在");
        }
        if (reviewClassInfo.getStatus() == null || reviewClassInfo.getStatus() != 1) {
            return Result.error(401,"量表未发布，暂不能测评");
        }
        if (reviewClassInfo.getCharge() != null && reviewClassInfo.getCharge() == Constants.ClassCharge) {
            reviewClassInfo.setRealPrice(reviewClassInfo.getOrgPrice().subtract(reviewClassInfo.getDicountPrice()));
        }
        ReviewClassPage reviewClassVO = new ReviewClassPage();
        BeanUtils.copyProperties(reviewClassInfo, reviewClassVO);
        //session获取userId
        //LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ReviewUser reviewUser = (ReviewUser)request.getSession().getAttribute(CommonConstant.REVIEW_LOGIN_USER);
        if (reviewUser != null && StrUtil.isNotBlank(reviewUser.getUserId()) && reviewClassInfo.getCharge() == Constants.ClassCharge) {
            //判断用户是否已经购买了课程
            reviewClassVO.setBuy(frontReviewClassService.userBuy(reviewClass.getClassId(), reviewUser.getUserId()));
        }
        return Result.OK(reviewClassVO);
    }

    /**
     * 小程序-获取我的测评记录/测评报告
     * @param reviewUser
     * @return
     */
    @AutoLog(value = "小程序-获取我的测评记录/测评报告")
    @PostMapping(value = "/getReviewRecords")
    public Result<List<ReviewResultVO>> getReviewRecords(@RequestBody ReviewUser reviewUser) {
        if (reviewUser == null || StringUtils.isBlank(reviewUser.getUserId())) {
            return Result.error(300,"用户信息为空");
        }
        List<ReviewResultVO> reviewResultList = frontReviewClassService.getReportResults(reviewUser.getUserId(), reviewUser.getProjectId());
        return Result.OK(reviewResultList);
    }

    /**
     * 获取推荐量表（limit 2）
     * @return
     */
    @AutoLog(value = "小程序-获取推荐量表")
    @PostMapping(value = "/getPsychoMetrics")
    public Result<List<ReviewClass>> getPsychoMetrics() {
        List<ReviewClass> reviewClassList = frontReviewClassService.getPsychoMetrics();
        return Result.OK(reviewClassList);
    }
    @AutoLog(value = "小程序-模糊查询量表")
    @PostMapping(value = "/getReviewClassByLike")
    public Result<List<ReviewClass>> getReviewClassByLike(@RequestBody ReviewClass reviewClass) {
        QueryWrapper<ReviewClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title",reviewClass.getTitle());
        List<ReviewClass> classList = frontReviewClassService.list(queryWrapper);
        classList.removeIf(reviewClass1 -> reviewClass1.getStatus() == 0);
        return Result.OK(classList);
    }
}
