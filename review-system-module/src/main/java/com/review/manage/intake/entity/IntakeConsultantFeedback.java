package com.review.manage.intake.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: intake_consultant_feedback
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Data
@TableName("intake_consultant_feedback")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="intake_consultant_feedback对象", description="intake_consultant_feedback")
public class IntakeConsultantFeedback implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**服务日期*/
	@Excel(name = "服务日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "服务日期")
    private Date serviceDate;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
	/**持续分钟*/
	@Excel(name = "持续分钟", width = 15)
    @ApiModelProperty(value = "持续分钟")
    private String durationMinutes;
	/**联系类型*/
	@Excel(name = "联系类型", width = 15,dicCode = "contact_type")
    @ApiModelProperty(value = "联系类型")
    @Dict(dicCode = "contact_type")
    private String contactType;
	/**评估主要问题*/
	@Excel(name = "评估主要问题", width = 15,dicCode = "evaluate_main_Issues")
    @ApiModelProperty(value = "评估主要问题")
    @Dict(dicCode = "evaluate_main_Issues")
    private String evaluateMainIssues;
	/**服务类型*/
	@Excel(name = "服务类型", width = 15,dicCode = "service_type")
    @ApiModelProperty(value = "服务类型")
    @Dict(dicCode = "service_type")
    private String serviceType;
	/**评估次要问题*/
	@Excel(name = "评估次要问题", width = 15,dicCode = "evaluate_secondary_issues")
    @ApiModelProperty(value = "评估次要问题")
    @Dict(dicCode = "evaluate_secondary_issues")
    private String evaluateSecondaryIssues;
	/**个案状态*/
	@Excel(name = "个案状态", width = 15,dicCode = "case_status")
    @ApiModelProperty(value = "个案状态")
    @Dict(dicCode = "case_status")
    private String caseStatus;
	/**咨客主诉*/
	@Excel(name = "咨客主诉", width = 15)
    @ApiModelProperty(value = "咨客主诉")
    private String clientsComplaints;
	/**咨客目标*/
	@Excel(name = "咨客目标", width = 15)
    @ApiModelProperty(value = "咨客目标")
    private String clientsGoal;
	/**解决过程*/
	@Excel(name = "解决过程", width = 15)
    @ApiModelProperty(value = "解决过程")
    private String resolutionProcess;
	/**咨客行动计划*/
	@Excel(name = "咨客行动计划", width = 15)
    @ApiModelProperty(value = "咨客行动计划")
    private String clientsActionPlan;
	/**咨客反馈*/
	@Excel(name = "咨客反馈", width = 15)
    @ApiModelProperty(value = "咨客反馈")
    private String clientsFeedback;
	/**危机评估*/
	@Excel(name = "危机评估", width = 15,dicCode = "crisis_assessment")
    @ApiModelProperty(value = "危机评估")
    @Dict(dicCode = "crisis_assessment")
    private String crisisAssessment;
	/**危机程度*/
	@Excel(name = "危机程度", width = 15,dicCode = "crisis_level")
    @ApiModelProperty(value = "危机程度")
    @Dict(dicCode = "crisis_level")
    private String crisisLevel;
	/**紧急联系人*/
	@Excel(name = "紧急联系人", width = 15)
    @ApiModelProperty(value = "紧急联系人")
    private String emergencyContact;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private String telephone;
	/**与咨客关系*/
	@Excel(name = "与咨客关系", width = 15,dicCode = "client_relation")
    @ApiModelProperty(value = "与咨客关系")
    @Dict(dicCode = "client_relation")
    private String clientRelation;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String creator;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
