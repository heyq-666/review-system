package com.review.front.frontReviewClass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.frontReviewClass.vo.ReviewResultVO;
import com.review.front.frontReviewClass.vo.SelectVO;
import com.review.manage.question.vo.QuestionVO;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.vo.ReviewClassPage;
import com.review.manage.reviewOrder.vo.ReviewOrderVO;
import com.review.manage.userManage.entity.ReviewResult;
import com.review.manage.userManage.entity.ReviewUser;

import java.util.List;

/**
 * @author javabage
 * @date 2023/4/3
 */
public interface IFrontReviewClassService extends IService<ReviewClass> {

    /**
     * 获取量表下的题目
     * @param classId
     * @return
     */
    List<QuestionVO> getQuestionVOList(String classId);

    /**
     * 获取每道题目下的选项
     * @param questionId
     * @return
     */
    List<SelectVO> getSelectVOList(Integer questionId);

    /**
     * 小程序-完成测试提交
     * @param resultList
     * @param classId
     * @param reviewUser
     * @return
     */
    ReviewResult completeReview(List<QuestionVO> resultList, String classId, ReviewUser reviewUser);

    /**
     * 小程序-获取测评分类
     * @param projectId
     * @return
     */
    List<ReviewClassPage> getReviewClassByProjectId(Long projectId);

    /**
     * 小程序-判断用户是都已经购买了课程
     * @param class_id
     * @param id
     * @return
     */
    Boolean userBuy(String class_id, String id);

    ReviewOrderVO findOneOrder(String classId, String userId);

    /**
     * 查询我的报告/测评记录
     * @param userId
     * @param projectId
     * @return
     */
    List<ReviewResultVO> getReportResults(String userId, Long projectId);

    /**
     * 获取推荐量表（limit 2）
     * @return
     */
    List<ReviewClass> getPsychoMetrics();

    Integer getReviewClassNumber(String classId);

    ReviewResult completeReviewNew(List<QuestionVO> resultList, String classId, org.jeecg.modules.base.entity.ReviewUser user);

    /**
     * 获取所属租户的量表
     * @param reviewClass
     * @return
     */
    List<ReviewClassPage> getReviewClassTenant(ReviewClassPage reviewClass);

    List<ReviewClass> getReviewClassTenantF(List<ReviewClass> reviewClassList,Long tenantId);
}
