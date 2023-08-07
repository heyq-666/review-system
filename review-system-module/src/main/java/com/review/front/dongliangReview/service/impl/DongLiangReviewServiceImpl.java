package com.review.front.dongliangReview.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.dongliangReview.entity.EvalCodeEntity;
import com.review.front.dongliangReview.mapper.DongLiangReviewMapper;
import com.review.front.dongliangReview.service.IDongLiangReviewService;
import com.review.front.dongliangReview.vo.DongliangTestQuestionVO;
import com.review.front.dongliangReview.vo.TestRecord;
import com.review.front.frontReport.service.IFrontReviewResultService;
import com.review.manage.userManage.entity.ReviewResult;
import com.review.manage.userManage.entity.ReviewUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author javabage
 * @date 2023/4/4
 */
@Service
public class DongLiangReviewServiceImpl extends ServiceImpl<DongLiangReviewMapper, EvalCodeEntity> implements IDongLiangReviewService {

    //封面背景图地址
    private static final String coverUrl = "https://pcapi.dilighthealth.com/profile/upload/2022/10/09/9b1ff86c-6a28-4496-8b5f-6a2a21570ad1.png";
    //logo 背景图地址
    //private static final String logoUrl = "https://pcapi.dilighthealth.com/profile/upload/2022/10/09/9b1ff86c-6a28-4496-8b5f-6a2a21570ad1.png";
    private static final String logoUrl = "https://www.xinzhaitongxing.com/review/plug-in/login/images/logo5.png";
    //公司名称
    private static final String companyName = "北京心宅同行管理咨询有限公司";
    //二维码地址
    //private static final String qrCodeUrl = "https://pcapi.dilighthealth.com/profile/upload/2022/10/10/b69f7e67-65bb-43dc-b02b-8cb5b8e042c6.png";
    private static final String qrCodeUrl =  "https://www.xinzhaitongxing.com/review/plug-in/login/images/appLogo.jpg";
    //封面标题
    private static final String indexTitle = "生涯发展评估报告";
    //报告撰写
    private static final String reportWriting = "北京心宅同行管理咨询有限公司";
    //联系方式
    private static final String contactPhone = "18510801311";
    //联系人
    private static final String contactPeople = "北京心宅同行管理咨询有限公司";
    //联系地址
    private static final String contactAddress = "北京市石景山区";

    @Autowired
    private IFrontReviewResultService frontReviewResultService;

    @Override
    public void handleData(DongliangTestQuestionVO[] dongliangTestQuestionVO) {
        //questNo及第二套试题处理
        int I = 1;//第一套试题题号
        int M = 1;//第二套试题题号
        int N = 1;
        int A = 1;//第三套试题题号
        int P = 1;//第四套试题题号
        List<TestRecord> testRecordList = new ArrayList<>();
        List<TestRecord> testRecordListOld = dongliangTestQuestionVO[0].getTestRecord();
        for (int i = 0; i < testRecordListOld.size(); i++) {
            if (i < 162){
                TestRecord testRecord = new TestRecord();
                testRecord.setQuesNo("I" + I);
                testRecord.setAnswer(testRecordListOld.get(i).getAnswer());
                testRecord.setScoreA(StringUtils.isNotBlank(testRecordListOld.get(i).getScoreA()) ? DecimalInputText(testRecordListOld.get(i).getScoreA()) : "1");
                testRecord.setScoreB(StringUtils.isNotBlank(testRecordListOld.get(i).getScoreB()) ? DecimalInputText(testRecordListOld.get(i).getScoreB()) : "1");
                testRecordList.add(testRecord);
                I++;
            } else if (i >= 162 && i < 204){
                this.handleQuestNo(testRecordListOld,i,testRecordList,M,"M");
                M++;
            } else if (i >= 204 && i < 246){
                this.handleQuestNo(testRecordListOld,i,testRecordList,N,"N");
                N++;
            } else if (i >= 246 && i < 336 ){
                TestRecord testRecord = new TestRecord();
                testRecord.setQuesNo("A" + A);
                testRecord.setAnswer(testRecordListOld.get(i).getAnswer());
                testRecord.setScoreA(StringUtils.isNotBlank(testRecordListOld.get(i).getScoreA()) ? DecimalInputText(testRecordListOld.get(i).getScoreA()) : "1");
                testRecord.setScoreB(StringUtils.isNotBlank(testRecordListOld.get(i).getScoreB()) ? DecimalInputText(testRecordListOld.get(i).getScoreB()) : "1");
                testRecordList.add(testRecord);
                A++;
            } else if ( i >= 336 ){
                TestRecord testRecord = new TestRecord();
                testRecord.setQuesNo("P" + P);
                testRecord.setAnswer(testRecordListOld.get(i).getAnswer());
                testRecord.setScoreA(StringUtils.isNotBlank(testRecordListOld.get(i).getScoreA()) ? DecimalInputText(testRecordListOld.get(i).getScoreA()) : "1");
                testRecord.setScoreB(StringUtils.isNotBlank(testRecordListOld.get(i).getScoreB()) ? DecimalInputText(testRecordListOld.get(i).getScoreB()) : "1");
                testRecordList.add(testRecord);
                P++;
            }
        }
        dongliangTestQuestionVO[0].setTestRecord(testRecordList);
        dongliangTestQuestionVO[0].getUserInfo().setSex(dongliangTestQuestionVO[0].getUserInfo().getSex().equals("1") ? "男" : "女");
        String select = dongliangTestQuestionVO[0].getUserInfo().getSelect();
        String[] provinceAndArea = select.split("--");
        dongliangTestQuestionVO[0].getUserInfo().setProvince(provinceAndArea[0]);
        dongliangTestQuestionVO[0].getUserInfo().setCity(provinceAndArea[1]);
        dongliangTestQuestionVO[0].getUserInfo().setArea(provinceAndArea[2]);
        //报告封面元素
        dongliangTestQuestionVO[0].setCoverUrl(coverUrl);
        dongliangTestQuestionVO[0].setLogoUrl(logoUrl);
        dongliangTestQuestionVO[0].setCompanyName(companyName);
        dongliangTestQuestionVO[0].setQrCodeUrl(qrCodeUrl);
        dongliangTestQuestionVO[0].setIndexTitle(indexTitle);
        dongliangTestQuestionVO[0].setReportWriting(reportWriting);
        dongliangTestQuestionVO[0].setContactPhone(contactPhone);
        dongliangTestQuestionVO[0].setContactPeople(contactPeople);
        dongliangTestQuestionVO[0].setContactAddress(contactAddress);
    }

    @Override
    public void handleBusinessData(Integer flag,String date,String json,
                                   DongliangTestQuestionVO[] dongliangTestQuestionVO, ReviewUser reviewUser) {
        EvalCodeEntity updateEntity = new EvalCodeEntity();
        updateEntity.setStatus((byte) 2);
        updateEntity.setUserName(dongliangTestQuestionVO[0].getUserInfo().getName());
        updateEntity.setUserId(dongliangTestQuestionVO[0].getUserInfo().getUserId());
        updateEntity.setOperateTime(new Date());
        updateEntity.setMsg(json);
        this.update(updateEntity,new UpdateWrapper<EvalCodeEntity>().lambda().eq(EvalCodeEntity :: getEvalCode,dongliangTestQuestionVO[0].getTestCode()));
        if (flag == 1) {
            //添加测评结果
            ReviewResult reviewResult = new ReviewResult();
            reviewResult.setUserId(dongliangTestQuestionVO[0].getUserInfo().getUserId());
            reviewResult.setClassId(dongliangTestQuestionVO[0].getClassId());
            reviewResult.setCreateTime(new Date());
            reviewResult.setCreateBy(dongliangTestQuestionVO[0].getUserInfo().getName());
            reviewResult.setProjectId(dongliangTestQuestionVO[0].getProjectId()); //项目id
            reviewResult.setReviewResult(dongliangTestQuestionVO[0].getReportUrl());
            frontReviewResultService.save(reviewResult);
        }
    }

    public void handleQuestNo(List<TestRecord> testRecordListOld, int i, List<TestRecord> testRecordList,int M,String MNtype) {
        if (testRecordListOld.get(i).getAnswer().equals("A")){
            TestRecord testRecord = new TestRecord();
            testRecord.setQuesNo(MNtype + M);
            testRecord.setAnswer("A");
            testRecord.setScoreA("0");
            testRecord.setScoreB("3");
            testRecordList.add(testRecord);
        }else if (testRecordListOld.get(i).getAnswer().equals("B")){
            TestRecord testRecord = new TestRecord();
            testRecord.setQuesNo(MNtype + M);
            testRecord.setAnswer("B");
            testRecord.setScoreA("1");
            testRecord.setScoreB("2");
            testRecordList.add(testRecord);
        }else if (testRecordListOld.get(i).getAnswer().equals("C")){
            TestRecord testRecord = new TestRecord();
            testRecord.setQuesNo(MNtype + M);
            testRecord.setAnswer("C");
            testRecord.setScoreA("2");
            testRecord.setScoreB("1");
            testRecordList.add(testRecord);
        }else if (testRecordListOld.get(i).getAnswer().equals("D")){
            TestRecord testRecord = new TestRecord();
            testRecord.setQuesNo(MNtype + M);
            testRecord.setAnswer("D");
            testRecord.setScoreA("3");
            testRecord.setScoreB("0");
            testRecordList.add(testRecord);
        }
    }
    public static String DecimalInputText(String text) {
        if (text.endsWith(".")) {
            text = text.substring(0, text.length() - 1);
        }else if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        return text;
    }
}
