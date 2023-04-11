package com.review.front.dongliangReview.vo;

import lombok.Data;

import java.util.List;

/**
 * @author javabage
 * @date 2022/8/30
 */
@Data
public class DongliangTestQuestionVO {

    private Integer type;

    /**
     * 1：学生版；2：职业版
     */
    private Integer version;

    private Integer gaugeNum;
    /**
     * 测评码
     */
    private String testCode;
    /**
     * 测评人信息
     */
    private UserInfo userInfo;
    /**
     * 答题信息
     */
    private List<TestRecord> testRecord;
    /**
     * 测评报告地址
     */
    private String reportUrl;
    /**
     * 测评id
     */
    private String classId;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 封面背景图地址
     */
    private String coverUrl;
    /**
     * logo 背景图地址
     */
    private String logoUrl;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 二维码地址
     */
    private String qrCodeUrl;
    /**
     * 封面标题
     */
    private String indexTitle;
    /**
     * 报告撰写
     */
    private String reportWriting;
    /**
     * 联系方式
     */
    private String contactPhone;
    /**
     * 联系人
     */
    private String contactPeople;
    /**
     * 联系地址
     */
    private String contactAddress;

}
