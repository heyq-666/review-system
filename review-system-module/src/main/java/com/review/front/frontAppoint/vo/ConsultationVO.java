package com.review.front.frontAppoint.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * @author javabage
 * @date 2022/6/2
 */
@Data
public class ConsultationVO implements Serializable {
    /**
        预约id
     */
    private Long id;
    /**
        日历id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long calendarId;
    /**
        用户id
     */
    private String userId;
    /**
     * 专家id
     */
    private Long expertId;
    /**
        专家姓名
     */
    private String expertName;
    /**
        专家性别
     */
    private Integer expertSex;
    /**
        专家职称
     */
    private String expertJobTitle;
    /**
        专家介绍
     */
    private String expertIntroduction;
    /**
        专家标签
     */
    private String expertLable;
    /**
     * 专家电话
     */
    private String expertPhone;
    private String avatar;
    /**
        预约人姓名
     */
    private String patientName;
    /**
        预约人性别
     */
    private String patientSex;
    /**
        预约人年龄
     */
    private Integer patientAge;
    /**
        预约人电话
     */
    private String userPhone;
    /**
        预约人身份证号
     */
    private String userIdCard;
    /**
     * 预约日期
     */
    private String visitDate;
    /**
        预约开始时间
     */
    private String beginTime;
    /**
        预约结束时间
     */
    private String endTime;
    /**
        周几（1，2，3...）
     */
    private Integer weekDay;
    /**
        周几（周一，周二，周三...）
     */
    private String weekDayName;
    /**
        预约状态
     */
    private Integer status;
    private String statusName;
    /**
     *  是否收费(0:免费；1:收费)
     */
    private Integer charge;
    /**
     *  原始价格
     */
    private String orgPrice;
    /**
     *  优惠价格
     */
    private String dicountPrice;
    /**
     *  用户是否支付专家问诊
     */
    private Boolean buy;
    /**
     * 实际价格
     */
    private String realPrice;
    /**
     * 支付状态
     */
    private Integer payStatus;
    private String payStatusName;
    private String roomId;
    private String confirmFlag;
    /**
     * 发送短信提醒标记
     */
    private Integer sendFlag;

    private String createTime;

    /**
     * 腾讯会议码
     */
    private String txNumber;

    private String videoConsultCondition;

    private String isConfirmByExpert;
}
