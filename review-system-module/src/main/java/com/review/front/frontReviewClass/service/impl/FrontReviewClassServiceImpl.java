package com.review.front.frontReviewClass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.common.Constants;
import com.review.common.MathCalculatorUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private final static String classIdFinal = "7116ab76c491b5f7c489d5fdd9eda944";

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
        List<ReviewClass> reviewClassList = frontReviewClassMapper.getPsychoMetrics();
        for (int i = 0; i < reviewClassList.size(); i++) {
            if (reviewClassList.get(i).getCharge() != null && reviewClassList.get(i).getCharge() == Constants.ClassCharge) {
                reviewClassList.get(i).setRealPrice(reviewClassList.get(i).getOrgPrice().subtract(reviewClassList.get(i).getDicountPrice()));
            }else {
                reviewClassList.get(i).setRealPrice(BigDecimal.valueOf(0.0));
            }
            Integer count = this.getReviewClassNumber(reviewClassList.get(i).getClassId());
            reviewClassList.get(i).setReviewCount(count);
        }
        return reviewClassList;
    }

    @Override
    public Integer getReviewClassNumber(String classId) {
        return frontReviewClassMapper.getReviewClassNumber(classId);
    }

    @Override
    public ReviewResult completeReviewNew(List<QuestionVO> resultList, String classId, org.jeecg.modules.base.entity.ReviewUser user) {
        /******************保存测评结果start*********************/
        //量表对应的因子设置
        QueryWrapper<ReviewVariateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId);
        List<ReviewVariateEntity> variateGradeConfList = variateService.list(queryWrapper);
        //因子按序号排序
        variateGradeConfList.sort(((o1, o2) -> o1.getSortNum().compareTo(o2.getSortNum())));
        String regex = "#(.*?)#";//匹配题目序号的正则
        List<ReviewResult> resultList1 = new ArrayList<>();
        for (int i = 0; i < variateGradeConfList.size(); i++) {
            String variateGradeConf = variateGradeConfList.get(i).getVariateGradeConf();//#1#*2+#2#/2
            //因子表达式中的题目序号集合
            List<String> selectGradeList = new ArrayList<>();
            //if (classId.equals(classIdFinal)) {
                //List<Map<String,Boolean>> questionNumListF = getGradeTotalF(variateGradeConf);
                List<Map<String,Boolean>> questionNumListF = getSubUtilF(variateGradeConf,regex);
                for (int j = 0; j < questionNumListF.size(); j++) {
                    Integer questionNum = 0;
                    //获取key
                    Map<String,Boolean> map = questionNumListF.get(j);
                    Set<String> keySet = map.keySet();
                    for (String key : keySet) {
                        questionNum = Integer.valueOf(key);
                    }
                    Integer finalQuestionNum = questionNum;
                    List<String> selectGrade = resultList.stream().filter(item -> item.getQuestionNum().equals(finalQuestionNum)).map(QuestionVO::getSelectGrade).collect(Collectors.toList());
                    if (questionNumListF.get(j).get(String.valueOf(finalQuestionNum))) {
                        selectGradeList.add(selectGrade.get(0).equals("1.0") ? "0.0" : "1.0");
                    }else {
                        selectGradeList.add(selectGrade.get(0));
                    }
                }
                variateGradeConf = variateGradeConf.replaceAll("@","#");
            /*} else {
                List<String> questionNumList = getSubUtil(variateGradeConf,regex);
                //根据题目序号获取分值
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = 0; j < questionNumList.size(); j++) {
                    Integer questionNum = Integer.valueOf(questionNumList.get(j));
                    List<String> selectGrade = resultList.stream().filter(item -> item.getQuestionNum() == questionNum).map(QuestionVO::getSelectGrade).collect(Collectors.toList());
                    selectGradeList.add(selectGrade.get(0));
                }
            }*/
            //替换题目序号为对应的分值
            StringBuffer gradeTotal = replacement(variateGradeConf,regex,selectGradeList);
            //计算结果
            //double tradeTotalTemp = getGradeTotal(gradeTotal.toString());
            double tradeTotalTemp = MathCalculatorUtil.calculator(gradeTotal.toString());
            double tradeTotalD = new BigDecimal(tradeTotalTemp).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            //保存计算结果
            ReviewResult reviewResult = new ReviewResult();
            reviewResult.setGradeTotal(tradeTotalD);
            //根据该因子的计算结果匹配是哪个因子项
            //1、先获取该因子分值范围
            QueryWrapper<ReviewVariateGradeEntity> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("variate_id", variateGradeConfList.get(i).getVariateId());
            List<ReviewVariateGradeEntity> reviewVariateGradeList = variateGradeService.list(queryWrapper1);
            for (int j = 0; j < reviewVariateGradeList.size(); j++) {
                double gradeSmall = reviewVariateGradeList.get(j).getGradeSamll();
                double gradeBig = reviewVariateGradeList.get(j).getGradeBig();
                if (tradeTotalD >= gradeSmall && tradeTotalD <= gradeBig) {
                    reviewResult.setReviewResult(reviewVariateGradeList.get(j).getResultExplain());
                }
            }
            resultList1.add(reviewResult);
        }
        StringBuilder resultExplain = new StringBuilder();
        for (int i = 0; i < resultList1.size(); i++) {
            if(resultExplain.length() == 0) {
                resultExplain.append(variateGradeConfList.get(i).getVariateName())
                        .append("得分:").append(resultList1.get(i).getGradeTotal())
                        .append("; 结果:").append(resultList1.get(i).getReviewResult());
            }else {
                resultExplain.append("<br>").append(variateGradeConfList.get(i).getVariateName())
                        .append("得分:").append(resultList1.get(i).getGradeTotal())
                        .append("; 结果:").append(resultList1.get(i).getReviewResult());
            }
        }
        ReviewResult reviewResult = new ReviewResult();
        reviewResult.setUserId(user.getUserId());
        reviewResult.setClassId(classId);
        reviewResult.setCreateTime(new Date());
        reviewResult.setCreateBy(user.getUserName());
        reviewResult.setProjectId(resultList.get(0).getProjectId()); //项目id
        reviewResult.setGroupId(user.getGroupId());
        reviewResult.setReviewResult(resultExplain.toString());
        //保存测评结果
        frontReviewResultService.save(reviewResult);
        /******************保存测评结果end*********************/

        /******************保存答题记录start*********************/
        ReviewProjectEntity reviewProject = null;
        String groupId = "";
        if(reviewResult.getProjectId() != null && reviewResult.getProjectId() > 0) {
            QueryWrapper<ReviewProjectEntity> queryWrapper1 = new QueryWrapper<>();
            reviewProject = frontProjectService.getById(reviewResult.getProjectId());
            groupId = reviewProject.getGroupId();
        }else {
            groupId = user.getGroupId().split(",")[0];
        }
        List<ReviewQuestionAnswerEntity> reviewQuestionAnswerList = new ArrayList<>(1200);
        Date now = new Date();
        QuestionVO question = null;
        for (int i = 0; i < resultList.size(); i++) {
            question = resultList.get(i);
            ReviewQuestionAnswerEntity reviewQuestionAnswer = new ReviewQuestionAnswerEntity();
            try {
                MyBeanUtils.copyBean2Bean(reviewQuestionAnswer, question);
                reviewQuestionAnswer.setGroupId(groupId);
                reviewQuestionAnswer.setUserId(user.getUserId());
                reviewQuestionAnswer.setUserName(user.getUserName());
                reviewQuestionAnswer.setMobilePhone(user.getMobilePhone());
                reviewQuestionAnswer.setSex(user.getSex());
                reviewQuestionAnswer.setAge(user.getAge());
                reviewQuestionAnswer.setCreateTime(now);
                reviewQuestionAnswer.setResultId(reviewResult.getResultId());
                reviewQuestionAnswerList.add(reviewQuestionAnswer);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (StringUtils.isBlank(question.getSelectGrade())) {
                continue;
            }
        }
        if (reviewQuestionAnswerList.size() > 0) {
            reviewQuestionAnswerService.saveBatch(reviewQuestionAnswerList);
            reviewQuestionAnswerList.clear();
        }
        /******************保存答题记录end*********************/

        /******************保存因子对应维度的结果start*********************/
        //查询某分类下的维度设置并保存到报告结果中
        QueryWrapper<ReviewReportEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("class_id",classId);
        List<ReviewReportEntity> reportList = reportService.list(queryWrapper1);
        //维度-因子
        List<ReviewReportVariateEntity> reportVariateList = null;
        //维度结果分值范围
        List<ReviewReportGradeEntity> gradeList = null;
        ReviewReportResultEntity reportResult = null;
        for(ReviewReportEntity report : reportList) {
            Double grade = 0.0;
            reportResult = new ReviewReportResultEntity();
            QueryWrapper<ReviewReportVariateEntity> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("report_id", report.getReportId());
            reportVariateList = reviewReportVariateService.list(queryWrapper2);
            for (int i = 0; i < reportVariateList.size(); i++) {
                grade = calVariateGrade(reportVariateList.get(i).getCalSymbol(),grade,resultList1.get(i).getGradeTotal());
            }
            grade = new BigDecimal(grade).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            QueryWrapper<ReviewReportGradeEntity> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("report_id", report.getReportId());
            gradeList = reportGradeService.list(queryWrapper3);
            int levelGrade = 0;
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
        /******************保存因子对应维度的结果end*********************/
        return reviewResult;
    }

    private List<Map<String,Boolean>> getGradeTotalF(String variateGradeConf) {
        final Pattern p = Pattern.compile("F#(.*?)#F");
        final Matcher m = p.matcher(variateGradeConf);
        List<String> listF = new ArrayList<>();
        while (m.find()) {
            int i = 1;
            listF.add(m.group(i));
        }
        String variateGradeConfNew = variateGradeConf.replaceAll("F","");
        final Pattern p1 = Pattern.compile("#(.*?)#");
        final Matcher m1 = p1.matcher(variateGradeConfNew);
        List<String> listAll = new ArrayList<>();
        while (m1.find()) {
            int i = 1;
            listAll.add(m1.group(i));
        }
        List<Map<String,Boolean>> mapList = new ArrayList<>();
        for (int i = 0; i < listAll.size(); i++) {
            Map<String,Boolean> map = new HashMap<>();
            for (int j = 0; j < listF.size(); j++) {
                if (listAll.get(i).equals(listF.get(j))) {
                    map.put(listAll.get(i),true);
                    mapList.add(map);
                }
                break;
            }
        }
        return mapList;
    }

    @Override
    public List<ReviewClassPage> getReviewClassTenant(ReviewClassPage reviewClass) {
        //所有量表
        List<ReviewClassPage> list = this.getReviewClassByProjectId(reviewClass.getProjectId());
        //筛选共享量表
        List<ReviewClassPage> shareClassList = list.stream().filter(reviewClassPage -> reviewClassPage.getIsShare() == 1)
                .collect(Collectors.toList());
        //获取给租户绑定的量表
        List<ReviewClassPage> ownClassList = new ArrayList<>();
        List<String> classIds = frontReviewClassMapper.getClassIds(reviewClass.getTenantId());
        for (int i = 0; i < classIds.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (classIds.get(i).equals(list.get(j).getClassId())) {
                    ownClassList.add(list.get(j));
                }
            }
        }
        shareClassList.addAll(ownClassList);
        return shareClassList;
    }

    @Override
    public List<ReviewClass> getReviewClassTenantF(List<ReviewClass> reviewClassList,Long tenantId) {
        List<ReviewClass> shareClassList = reviewClassList.stream().filter(reviewClassPage -> reviewClassPage.getIsShare() == 1)
                .collect(Collectors.toList());
        //是否给租户绑定
        List<ReviewClass> ownClassList = new ArrayList<>();
        List<String> classIds = frontReviewClassMapper.getClassIds(tenantId);
        for (int i = 0; i < classIds.size(); i++) {
            for (int j = 0; j < reviewClassList.size(); j++) {
                if (classIds.get(i).equals(reviewClassList.get(j).getClassId())) {
                    ownClassList.add(reviewClassList.get(j));
                }
            }
        }
        shareClassList.addAll(ownClassList);
        return shareClassList;
    }

    @Override
    public Integer getQuestionsNum(String classId) {
        return frontReviewClassMapper.getQuestionsNum(classId);
    }

    /**
     * 正则匹配指定符号之间的数字
     * @param str
     * @param regex
     * @return
     */
    private List<String> getSubUtil(String str,String regex){
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(str);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
        }
        return list;
    }
    private List<Map<String,Boolean>> getSubUtilF(String str,String regex){
        //匹配正反向题目
        final Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        final Pattern p1 = Pattern.compile("@(.*?)@");
        Matcher m1 = p1.matcher(str);
        List<Map<Integer,Map<String,Boolean>>> list = new ArrayList<>();
        List<Map<String,Boolean>> mapList = new ArrayList<>();
        while (m.find()) {
            int i = 1;
            Map<String,Boolean> map = new HashMap<>();
            map.put(m.group(i),false);
            Map<Integer,Map<String,Boolean>> mapMap = new HashMap<>();
            mapMap.put(m.start(),map);
            list.add(mapMap);
        }
        while (m1.find()) {
            int i = 1;
            Map<String,Boolean> map = new HashMap<>();
            map.put(m1.group(i),true);
            Map<Integer,Map<String,Boolean>> mapMap = new HashMap<>();
            mapMap.put(m1.start(),map);
            list.add(mapMap);
        }
        Collections.sort(list, new Comparator<Map<Integer, Map<String, Boolean>>>() {
            @Override
            public int compare(Map<Integer, Map<String, Boolean>> o1, Map<Integer, Map<String, Boolean>> o2) {
                Set s = o1.keySet();
                Set ss = o2.keySet();
                Integer key1 = null;
                Integer key2 = null;
                for (Object key : s) {
                    key1 = (Integer) key;
                }
                for (Object key : ss) {
                    key2 = (Integer) key;
                }
                return key1.compareTo(key2);
            }
        });
        List<Map<String,Boolean>> listend = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Set k = list.get(i).keySet();
            Integer keyw = null;
            for (Object key: k) {
                keyw = (Integer) key;
            }
            listend.add(list.get(i).get(keyw));
        }
        return listend;
        /*final Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        final Pattern p1 = Pattern.compile("@(.*?)@");
        Matcher m1 = p1.matcher(str);
        List<Map<String,Boolean>> list = new ArrayList<>();
        while (m.find()) {
            int i = 1;
            Map<String,Boolean> map = new HashMap<>();
            map.put(m.group(i),false);
            list.add(map);
        }
        while (m1.find()) {
            int i = 1;
            Map<String,Boolean> map = new HashMap<>();
            map.put(m1.group(i),true);
            list.add(map);
        }
        return list;*/
    }

    private StringBuffer replacement(String str,String regex,List<String> selectGradeList){
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (m.find()) {
            String sub = str.substring(m.start(),m.end());
            m.appendReplacement(stringBuffer,m.group().replace(sub,selectGradeList.get(i)));
            i++;
        }
        m.appendTail(stringBuffer);
        return stringBuffer;
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
    private double getGradeTotal(String str) {
        String ReversePolish = toReversePolish(str);
        //6 10 3 40.2 + 8 + - 5 * - 8 +
        String[] expression = ReversePolish.split(" ");
        Stack<Double> stack = new Stack<>();
        for (int i = 0; i < expression.length; i++) {
            switch (expression[i]) {
                case "+" : {
                    double num1 = stack.pop();
                    double num2 = stack.pop();
                    double result = num1+num2;
                    stack.push(result);
                    break;
                }
                case "-" : {
                    double num1 = stack.pop();
                    double num2 = stack.pop();
                    double result = num2-num1;
                    stack.push(result);
                    break;
                }
                case "*" : {
                    double num1 = stack.pop();
                    double num2 = stack.pop();
                    double result = num1*num2;
                    stack.push(result);
                    break;
                }
                case "/" : {
                    double num1 = stack.pop();
                    double num2 = stack.pop();
                    double result = num2/num1;
                    stack.push(result);
                    break;
                }
                default: {
                    Double in = Double.parseDouble(expression[i]);
                    stack.push(in);
                }
            }
        }
        double gradeTotal = Double.valueOf(stack.pop());
        return gradeTotal;
    }
    public static String toReversePolish(String expression) {
        Stack<String> stack1 = new Stack<>();
        Stack<String> stack2 = new Stack<>();

        String number = "";
        for (int i = 0; i < expression.length();) {
            boolean isAll = false;
            while((expression.charAt(i)>='0' && expression.charAt(i) <= '9') || expression.charAt(i)=='.') {
                number += expression.charAt(i);
                i++;
                if (i == expression.length()) {
                    isAll = true;
                    break;
                }
            }
            if (!number.isEmpty()) {
                stack2.push(new StringBuffer(number).reverse().toString());
                number = "";
            }
            if (isAll) {
                break;
            }
            if (expression.charAt(i) == ')') {
                while (!stack1.peek().equals("(")) {
                    stack2.push(stack1.pop());
                }
                stack1.pop();
                i++;
                continue;
            }

            while(!(expression.charAt(i)>='0' && expression.charAt(i) <= '9')) {
                char symbol = expression.charAt(i);
                if ((stack1.size() == 0) || (stack1.peek().equals("(")) || (symbol=='(')) {
                    stack1.push(symbol+"");
                    i++;
                    break;
                }
                if ((symbol == '+') || (symbol == '-')) {
                    stack2.push(stack1.pop());
                    continue;
                } else {
                    if ((stack1.peek().equals("+")) || (stack1.peek().equals("-"))) {
                        stack1.push(symbol+"");
                        i++;
                        break;
                    }
                    if ((stack1.peek().equals("*")) || (stack1.peek().equals("/"))) {
                        stack2.push(stack1.pop());
                        continue;
                    }
                }
            }
        }
        while(stack1.size() != 0) {
            stack2.push(stack1.pop());
        }
        String s1 = "";
        while(stack2.size() != 0) {
            s1 += stack2.pop();
            if (stack2.size() != 0) {
                s1 += " ";
            }
        }
        return new StringBuffer(s1).reverse().toString();
    }
}
