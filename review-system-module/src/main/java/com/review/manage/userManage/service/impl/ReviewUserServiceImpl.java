package com.review.manage.userManage.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontReport.service.IFrontReviewResultService;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.service.IReviewClassService;
import com.review.manage.userManage.entity.ReviewResult;
import com.review.manage.userManage.entity.ReviewUser;
import com.review.manage.userManage.mapper.ReviewUserMapper;
import com.review.manage.userManage.service.IReviewUserService;
import com.review.manage.userManage.vo.QuestionAnswerVo;
import com.review.manage.userManage.vo.ReviewQuestionAnswerVO;
import com.review.manage.userManage.vo.ReviewResultVo;
import com.review.manage.userManage.vo.SysUserDepVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: 测评用户
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Service
public class ReviewUserServiceImpl extends ServiceImpl<ReviewUserMapper, ReviewUser> implements IReviewUserService {

    @Autowired
    private ReviewUserMapper reviewUserMapper;

    @Autowired
    private IReviewClassService iReviewClassService;

    @Autowired
    private IFrontReviewResultService reviewResultService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map<String, String> getDepNamesByUserIds(List<String> userIds) {
        List<SysUserDepVo> list = reviewUserMapper.getDepNamesByUserIds(userIds);

        Map<String, String> res = new HashMap(5);
        list.forEach(item -> {
                    if (res.get(item.getUserId()) == null) {
                        res.put(item.getUserId(), item.getDepartName());
                    } else {
                        res.put(item.getUserId(), res.get(item.getUserId()) + "," + item.getDepartName());
                    }
                }
        );
        return res;
    }

    @Override
    public List<String> getDepartNameList(List<String> groupList) {
        return reviewUserMapper.getDepartNameList(groupList);
    }

    @Override
    public List<SysUserDepVo> getSysUserDepVoList(List<String> userIds) {
        List<SysUserDepVo> sysUserDepVoList = reviewUserMapper.getSysUserDepVoList(userIds);
        return sysUserDepVoList;
    }

    @Override
    public void updateByUserId(ReviewUser reviewUser) {
        reviewUserMapper.updateByUserId(reviewUser);
    }

    @Override
    public List<ReviewResultVo> getResultByUserId(String userId) {
        List<ReviewResultVo> reviewResults = reviewUserMapper.getResultByUserId(userId);
        return reviewResults;
    }

    @Override
    public void removeRecord(String resultId) {
        reviewUserMapper.removeRecord(resultId);
    }

    @Override
    public Workbook getExportWorkbook(QuestionAnswerVo questionAnswerVo) {
        long begin = System.currentTimeMillis();
        List<ReviewQuestionAnswerVO> list = reviewUserMapper.getListByGroupId(questionAnswerVo);
        long end = System.currentTimeMillis();
        logger.info("getListByGroupId cost time: "+ (end - begin) + " ms");

        Map<String, TreeMap<String, Map<String, Object>>> classMap = new HashMap<>();
        Map<String, TreeMap<String, String>> headerClassMap = new HashMap<>();
        Map<String, String> nameMap = new HashMap<>();
        Map<String, StringBuilder> prefixCLassMap = new HashMap<>();

        //封装导出列表数据
        for (int i=0; i < list.size(); i++) {
            ReviewQuestionAnswerVO answerQuestion = list.get(i);
            String classId = answerQuestion.getClassId();

            TreeMap<String, Map<String, Object>> singleClassMap = null;

            StringBuilder sortStr = prefixCLassMap.get(classId);
            if (sortStr == null) {
                sortStr = new StringBuilder("0");
                prefixCLassMap.put(classId, sortStr);
            }

            String qNum = sortStr.append("a").toString();

            TreeMap<String, String> headerAlias = headerClassMap.get(classId);
            if (headerAlias == null) {
                headerAlias = getBaseHeader();
                headerClassMap.put(classId, headerAlias);
            }
            headerAlias.put(qNum, answerQuestion.getContent());

            String userKey = answerQuestion.getUserId() + answerQuestion.getCreateTime();
            Map<String, Object> userClassMap = null;
            if (classMap.containsKey(classId)) { //是否包含量表id
                singleClassMap = classMap.get(classId);
                if(singleClassMap.get(userKey) != null) {
                    userClassMap = singleClassMap.get(userKey);
                } else {
                    userClassMap = geneUserRow(answerQuestion, headerAlias); //生成用户行
                    singleClassMap.put(userKey, userClassMap);
                }
            } else {
                singleClassMap = new TreeMap<>();
                classMap.put(classId, singleClassMap);
                ReviewClass reviewClass = iReviewClassService.getById(classId);
                nameMap.put(classId, reviewClass.getTitle());

                userClassMap = geneUserRow(answerQuestion, headerAlias); //生成用户行
                singleClassMap.put(userKey, userClassMap);
            }
            if (StrUtil.isBlank(answerQuestion.getSelectGrade())) {
                userClassMap.put(qNum, answerQuestion.getRightAnswer());
            } else {
                userClassMap.put(qNum, answerQuestion.getSelectGrade());
            }
        }

        ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        excelWriter.getSheets().clear();
        for (String classId : classMap.keySet()) {
            excelWriter.setSheet(nameMap.get(classId));
            excelWriter.setHeaderAlias(headerClassMap.get(classId));
            excelWriter.setColumnWidth(1, 40);
            for (int i = 4; i< headerClassMap.get(classId).size(); i++) {
                excelWriter.setColumnWidth(i, 60);
            }
            excelWriter.write(classMap.get(classId).values(), true);
        }
        if (classMap.size() > 0) {
            excelWriter.getWorkbook().removeSheetAt(0);
        }
        return excelWriter.getWorkbook();
    }
    private TreeMap<String, String> getBaseHeader() {
        TreeMap<String, String> headerAlias = new TreeMap<>();
        headerAlias.put("00_姓名", "姓名");
        headerAlias.put("01_真实姓名", "真实姓名");
        headerAlias.put("02_性别", "性别");
        headerAlias.put("03_年龄", "年龄");
        headerAlias.put("04_手机号", "手机号");
        headerAlias.put("05_完成时间", "完成时间");
        //headerAlias.put("06_因子得分", "因子得分");
        return headerAlias;
    }

    private Map<String, Object> geneUserRow(ReviewQuestionAnswerVO answerQuestion, TreeMap<String, String> headerAlias) {
        Map<String, Object> userClassMap = new HashMap<>();
        userClassMap.put("00_姓名", answerQuestion.getUserName());
        userClassMap.put("01_真实姓名", answerQuestion.getRealName());
        userClassMap.put("02_性别", "1".equals(answerQuestion.getSex()) ? "男":"女");
        userClassMap.put("03_年龄", answerQuestion.getAge());
        userClassMap.put("04_手机号", answerQuestion.getMobilePhone());
        userClassMap.put("05_完成时间", answerQuestion.getCreateTime());

        //导出报告因子得分
        /*List<ReviewReportResultEntity> reportResultList = reviewUserMapper.getReviewReportResult(answerQuestion.getResultId());
        if(CollectionUtils.isNotEmpty(reportResultList)) {
            for (ReviewReportResultEntity reviewReportResult : reportResultList) {
                userClassMap.put("0_" + reviewReportResult.getReportName(), reviewReportResult.getGrade());
                headerAlias.put("0_" + reviewReportResult.getReportName(), reviewReportResult.getReportName());
            }
        }*/
        ReviewResult reviewResult = reviewResultService.getById(answerQuestion.getResultId());
        if(reviewResult != null) {
            String[] reviewResultExplain = reviewResult.getReviewResult().split("<br>");
            String grade = "";
            String reportName = "";
            for (int i = 0; i < reviewResultExplain.length; i++) {
                reportName = reviewResultExplain[i].split(":")[0];
                grade = reviewResultExplain[i].split(":")[1].split(";")[0];
                userClassMap.put("0_" + reportName,grade);
                headerAlias.put("0_" + reportName,reportName);
            }
        }
        return userClassMap;
    }
}
