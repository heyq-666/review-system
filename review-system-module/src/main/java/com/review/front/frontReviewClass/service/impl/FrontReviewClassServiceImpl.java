package com.review.front.frontReviewClass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.common.Constants;
import com.review.common.MyBeanUtils;
import com.review.front.frontProject.service.IFrontProjectService;
import com.review.front.frontReport.entity.ReviewReportResultEntity;
import com.review.front.frontReport.service.IFrontReviewResultService;
import com.review.front.frontReport.service.IReviewReportResultService;
import com.review.front.frontReport.service.IReviewReportVariateService;
import com.review.front.frontReviewClass.mapper.FrontReviewClassMapper;
import com.review.front.frontReviewClass.service.IFrontReviewClassService;
import com.review.front.frontReviewClass.vo.ReviewResultVO;
import com.review.front.frontReviewClass.vo.SelectVO;
import com.review.manage.project.entity.ReviewProjectEntity;
import com.review.manage.question.entity.ReviewQuestionAnswerEntity;
import com.review.manage.question.service.IReviewQuestionAnswerService;
import com.review.manage.question.vo.QuestionVO;
import com.review.manage.report.entity.ReviewReportEntity;
import com.review.manage.report.entity.ReviewReportGradeEntity;
import com.review.manage.report.entity.ReviewReportVariateEntity;
import com.review.manage.report.service.IReportGradeService;
import com.review.manage.report.service.IReportService;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.vo.ReviewClassPage;
import com.review.manage.reviewOrder.vo.ReviewOrderVO;
import com.review.manage.userManage.entity.ReviewResult;
import com.review.manage.userManage.entity.ReviewUser;
import com.review.manage.variate.entity.ReviewVariateEntity;
import com.review.manage.variate.entity.ReviewVariateGradeEntity;
import com.review.manage.variate.service.IVariateGradeService;
import com.review.manage.variate.service.IVariateService;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.Arith;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Service
public class FrontReviewClassServiceImpl extends ServiceImpl<FrontReviewClassMapper, ReviewClass> implements IFrontReviewClassService {

    @Autowired
    private FrontReviewClassMapper frontReviewClassMapper;

    @Autowired
    private IFrontProjectService frontProjectService;

    @Autowired
    private IFrontReviewResultService frontReviewResultService;

    @Autowired
    private IReviewQuestionAnswerService reviewQuestionAnswerService;

    @Autowired
    private IReportService reportService;

    @Autowired
    private IVariateService variateService;

    @Autowired
    private IVariateGradeService variateGradeService;

    @Autowired
    private IReviewReportResultService reviewReportResultService;

    @Autowired
    private IReviewReportVariateService reviewReportVariateService;

    @Autowired
    private IReportGradeService reportGradeService;

    @Override
    public List<QuestionVO> getQuestionVOList(String classId) {
        return frontReviewClassMapper.getQuestionVOList(classId);
    }

    @Override
    public List<SelectVO> getSelectVOList(Integer questionId) {
        return frontReviewClassMapper.getSelectVOList(questionId);
    }

    @Override
    public ReviewResult completeReview(List<QuestionVO> resultList, String classId, ReviewUser reviewUser) {
        //按照因子排序
        sort(resultList);

        //存放变量因子对应的分值
        Map<String, Double> map = new HashMap<String, Double>();
        QuestionVO question = null;
        Double totalGrade = 0.0;
        String[] idArr = null;

        //添加测评结果
        ReviewResult reviewResult = new ReviewResult();
        reviewResult.setUserId(reviewUser.getUserId());
        reviewResult.setClassId(classId);
        reviewResult.setCreateTime(new Date());
        reviewResult.setCreateBy(reviewUser.getUserName());
        reviewResult.setProjectId(resultList.get(0).getProjectId());
        reviewResult.setGroupId(reviewUser.getGroupId());

        ReviewProjectEntity reviewProject = null;
        String groupId = "";
        if(reviewResult.getProjectId() != null && reviewResult.getProjectId() > 0) {
            reviewProject = frontProjectService.getById(reviewResult.getProjectId());
            groupId = reviewProject.getGroupId();
        } else {
            groupId = reviewUser.getGroupId().split(",")[0];
        }
        //保存测评结果
        frontReviewResultService.save(reviewResult);
        List<ReviewQuestionAnswerEntity> reviewQuestionAnswerList = new ArrayList<>(1200);
        Date now = new Date();
        //计算因子对应的分值
        for (int i = 0; i < resultList.size(); i++) {
            question = resultList.get(i);
            ReviewQuestionAnswerEntity reviewQuestionAnswer = new ReviewQuestionAnswerEntity();
            try {
                MyBeanUtils.copyBean2Bean(reviewQuestionAnswer,question);
                reviewQuestionAnswer.setGroupId(groupId);
                reviewQuestionAnswer.setUserId(reviewUser.getUserId());
                reviewQuestionAnswer.setUserName(reviewUser.getUserName());
                reviewQuestionAnswer.setMobilePhone(reviewUser.getMobilePhone());
                reviewQuestionAnswer.setSex(reviewUser.getSex());
                reviewQuestionAnswer.setAge(reviewUser.getAge());
                reviewQuestionAnswer.setCreateTime(now);
                reviewQuestionAnswer.setResultId(reviewResult.getResultId());
                reviewQuestionAnswerList.add(reviewQuestionAnswer);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (StringUtils.isBlank(question.getSelectGrade())) {
                continue;
            }
            double grade = Double.valueOf(question.getSelectGrade());
            if(!"".equals(StringUtils.trimToEmpty(question.getVariateId()))) {
                idArr = question.getVariateId().split(",");
                for(int j = 0; j < idArr.length; j++) {
                    if(map.get(idArr[j]) != null) {
                        map.put(idArr[j], Arith.add(map.get(idArr[j]), grade));
                    } else {
                        map.put(idArr[j], grade);
                    }
                }
            }
            totalGrade = Arith.add(totalGrade, grade);
        }
        reviewResult.setGradeTotal(totalGrade);
        //保存答题记录
        if (reviewQuestionAnswerList.size() > 0) {
            reviewQuestionAnswerService.saveBatch(reviewQuestionAnswerList);
            reviewQuestionAnswerList.clear();
        }
        //查询某分类下的报告
        QueryWrapper<ReviewReportEntity> queryWrapper = new QueryWrapper<ReviewReportEntity>();
        queryWrapper.eq("class_id",classId);
        List<ReviewReportEntity> reportList = reportService.list(queryWrapper);

        //报告因子
        List<ReviewReportVariateEntity> reportVariateList = null;

        //报告结果分值范围
        List<ReviewReportGradeEntity> gradeList = null;

        ReviewReportResultEntity reportResult = null;
        Double grade = null;

        //将所有报告的结果拼装起来成字符串
        StringBuilder resultExplain = new StringBuilder();

        String variateResultExplain = "";
        List<ReviewVariateGradeEntity> variateGradeList = null;
        ReviewVariateGradeEntity variateGrade = null;
        ReviewVariateEntity variateEntity = null;
        Double variateTotalGrade = 0.0;

        List<String> resultCombine = new ArrayList<>();
        Integer levelGradeTotal = 0;

        List<Double> variateTotalGradeTemp = new ArrayList<>();

        //遍历因子
        for(Map.Entry<String, Double> entry : map.entrySet()) {
            variateEntity = variateService.getById(entry.getKey());
            reportResult = new ReviewReportResultEntity();
            QueryWrapper<ReviewVariateGradeEntity> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("variate_id",entry.getKey());
            variateGradeList = variateGradeService.list(queryWrapper1);
            //计算因子最后得分
            variateTotalGrade = calVariateGrade(variateEntity.getCalSymbol1(), entry.getValue(), variateEntity.getCalTotal1());

            variateTotalGrade = calVariateGrade(variateEntity.getCalSymbol(), variateTotalGrade, variateEntity.getCalTotal());

            variateTotalGradeTemp.add(variateTotalGrade);

            int levelGrade = 0;

            for(int i=0; i< variateGradeList.size();i++) {
                variateGrade = variateGradeList.get(i);
                if(variateGrade.getGradeSamll() <= variateTotalGrade && variateTotalGrade <= variateGrade.getGradeBig()) {
                    variateResultExplain = variateGrade.getResultExplain();
                    resultCombine.add(variateResultExplain);
                    if (variateGrade.getLevelGrade() != null) {
                        levelGrade = variateGrade.getLevelGrade();
                        levelGradeTotal += levelGrade;
                    }
                    break;
                }
            }
            //拼装因子的结果描述
            if(resultExplain.length() == 0) {
                resultExplain.append(variateEntity.getVariateName())
                        .append("得分:").append(variateTotalGrade)
                        .append("; 结果:").append(variateResultExplain);
            } else {
                resultExplain.append("<br>").append(variateEntity.getVariateName())
                        .append("得分:").append(variateTotalGrade)
                        .append("; 结果:").append(variateResultExplain);
            }
            //添加因子测试结果
            if(variateEntity.getSortNum() != null) {
                reportResult.setCreateTime(DateUtils.addMillSecond(now, variateEntity.getSortNum()* 1000));
            }

            reportResult.setExplainResult(variateResultExplain);
            reportResult.setGrade(variateTotalGrade);
            reportResult.setReportId(entry.getKey());
            reportResult.setReportName(variateEntity.getVariateName());
            reportResult.setResultId(reviewResult.getResultId());
            reportResult.setResultType("1");//因子类型
            reviewReportResultService.save(reportResult);
        }
        //循环报告结果
        for(ReviewReportEntity report : reportList) {
            grade = 0.0;
            reportResult = new ReviewReportResultEntity();
            QueryWrapper<ReviewReportVariateEntity> queryWrapper1 = new QueryWrapper<ReviewReportVariateEntity>();
            queryWrapper1.eq("report_id",report.getReportId());
            reportVariateList = reviewReportVariateService.list(queryWrapper1);
            for (int i = 0; i < reportVariateList.size() - 1; i++) {
                grade = calVariateGrade(reportVariateList.get(i).getCalSymbol(),variateTotalGradeTemp.get(i),variateTotalGradeTemp.get(i+1));
            }
            grade = new BigDecimal(grade).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            int levelGrade = 0;
            QueryWrapper<ReviewReportGradeEntity> queryWrapper2 = new QueryWrapper<ReviewReportGradeEntity>();
            queryWrapper2.eq("report_id",report.getReportId());
            gradeList = reportGradeService.list(queryWrapper2);
            for(ReviewReportGradeEntity reportGrade : gradeList) {
                if(grade <= reportGrade.getGradeBig() && grade >= reportGrade.getGradeSmall()) {
                    reportResult.setExplainResult(reportGrade.getResultExplain());
                    if(reportGrade.getLevelGrade() != null) {
                        levelGrade = reportGrade.getLevelGrade();
                    }
                    //拼装报告的结果描述
                    if(resultExplain.length() == 0) {
                        resultExplain.append(report.getReportName())
                                .append("得分:").append(grade)
                                .append("; 结果:").append(reportGrade.getResultExplain());
                    } else {
                        resultExplain.append("<br>").append(report.getReportName())
                                .append("得分:").append(grade)
                                .append("; 结果:").append(reportGrade.getResultExplain());
                    }
                }
            }
            if(gradeList.size() > 0 && grade > 0) {
                reportResult.setReportId(reviewResult.getResultId());
                reportResult.setGrade(grade);
                reportResult.setLevelGrade(levelGrade);
                reportResult.setCreateTime(report.getCreateTime());
                reportResult.setReportId(report.getReportId());
                reportResult.setReportName(report.getReportName());
                reportResult.setResultId(reviewResult.getResultId());
                reportResult.setResultType("2");//报告类型
                reviewReportResultService.save(reportResult);
            }
        }
        //将结果存起来
        reviewResult.setReviewResult(resultExplain.toString());
        reviewResult.setLevelGrade(levelGradeTotal);
        reviewResult.setCombineResult(StringUtils.join(resultCombine, ","));
        frontReviewResultService.saveOrUpdate(reviewResult);
        return reviewResult;
    }

    @Override
    public List<ReviewClassPage> getReviewClassByProjectId(Long projectId) {
        return frontReviewClassMapper.getReviewClassByProjectId(projectId);
    }

    @Override
    public Boolean userBuy(String classId, String userId) {
        ReviewOrderVO reviewOrder = this.findOneOrder(classId, userId);
        if (reviewOrder != null && (reviewOrder.getStatus() == Constants.OrderStatus.PRE_SUCCESS || reviewOrder.getStatus() == Constants.OrderStatus.SUCCESS)) {
            return true;
        }
        return false;
    }

    @Override
    public ReviewOrderVO findOneOrder(String classId, String userId) {
        Map param = new HashMap();
        param.put("classId",classId);
        param.put("userId",userId);
        param.put("create", Constants.OrderStatus.CREATE);
        param.put("prePay",Constants.OrderStatus.PRE_PAY);
        param.put("preSuccess",Constants.OrderStatus.PRE_SUCCESS);
        param.put("success",Constants.OrderStatus.SUCCESS);
        param.put("payFail",Constants.OrderStatus.PAY_FAIL);
        ReviewOrderVO reviewOrder = frontReviewClassMapper.findOneOrder(param);
        return reviewOrder;
    }

    @Override
    public List<ReviewResultVO> getReportResults(String userId, Long projectId) {
        Map param = new HashMap();
        param.put("userId",userId);
        param.put("projectId",projectId);
        return frontReviewClassMapper.getReportResults(param);
    }

    @Override
    public List<ReviewClass> getPsychoMetrics() {
        return frontReviewClassMapper.getPsychoMetrics();
    }

    @Override
    public Integer getReviewClassNumber(String classId) {
        return frontReviewClassMapper.getReviewClassNumber(classId);
    }

    /**
     * 按照因子排序
     * @param resultList
     */
    private void sort(List<QuestionVO> resultList) {
        Collections.sort(resultList,new Comparator<QuestionVO>(){
            @Override
            public int compare(QuestionVO o1, QuestionVO o2) {
                String str1 = o1.getVariateId();
                String str2 = o2.getVariateId();
                if(str1.equals(str2)){
                    return 0;
                }else{
                    return 1;
                }
            }
        });
    }

    /**
     * 计算因子得分
     * @param calymbol
     * @param grade
     * @param calTotal
     * @return
     */
    private Double calVariateGrade(String calymbol, Double grade,Double calTotal) {
        if(calTotal != null) {
            if(calTotal > 0) {
                if("+".equals(StringUtils.trimToEmpty(calymbol))) {
                    grade += calTotal;
                } else if ("-".equals(StringUtils.trimToEmpty(calymbol))) {
                    grade -= calTotal;
                } else if ("*".equals(StringUtils.trimToEmpty(calymbol))) {
                    grade = grade * calTotal;
                } else if ("/".equals(StringUtils.trimToEmpty(calymbol))) {
                    grade = Arith.div(grade, calTotal, 2);
                }
            }
        }
        return grade;
    }
}
