package com.review.front.frontReviewClass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.front.frontReviewClass.vo.ReviewResultVO;
import com.review.front.frontReviewClass.vo.SelectVO;
import com.review.manage.question.vo.QuestionVO;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.vo.ReviewClassPage;
import com.review.manage.reviewOrder.vo.ReviewOrderVO;

import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/3
 */
public interface FrontReviewClassMapper extends BaseMapper<ReviewClass> {

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
     * 小程序-获取测评分类
     * @param projectId
     * @return
     */
    List<ReviewClassPage> getReviewClassByProjectId(Long projectId);

    /**
     * 小程序-判断用户是都已经购买了课程
     * @param param
     * @return
     */
    ReviewOrderVO findOneOrder(Map param);

    /**
     * 查询我的报告/测评记录
     * @param param
     * @return
     */
    List<ReviewResultVO> getReportResults(Map param);

    List<ReviewClass> getPsychoMetrics();
}
