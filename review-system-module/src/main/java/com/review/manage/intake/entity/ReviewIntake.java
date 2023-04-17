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
 * @Description: review_intake
 * @Author: jeecg-boot
 * @Date:   2023-04-11
 * @Version: V1.0
 */
@Data
@TableName("review_intake")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="review_intake对象", description="review_intake")
public class ReviewIntake implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

	/**来电记录编号*/
	@Excel(name = "来电记录编号", width = 15)
    @ApiModelProperty(value = "来电记录编号")
    private String callRecordNumber;

	/**挂断时间*/
	@Excel(name = "挂断时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "挂断时间")
    private Date hangupTime;

	/**呈现问题描述*/
	@Excel(name = "呈现问题描述", width = 15)
    @ApiModelProperty(value = "呈现问题描述")
    private String problemDescription;

	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;

	/**来电类型*/
    @Dict(dicCode = "call_type")
	@Excel(name = "来电类型", width = 15)
    @ApiModelProperty(value = "来电类型")
    private String callType;

	/**是否重点个案*/
    @Dict(dicCode = "yn")
	@Excel(name = "是否重点个案", width = 15)
    @ApiModelProperty(value = "是否重点个案")
    private String keyCases;

	/**公司名称*/
	@Excel(name = "公司名称", width = 15)
    @ApiModelProperty(value = "公司名称")
    private String companyId;

	/**对自己或他人有危险*/
    @Dict(dicCode = "yn")
	@Excel(name = "对自己或他人有危险", width = 15)
    @ApiModelProperty(value = "对自己或他人有危险")
    private String dangerousSituation;

	/**分支机构*/
	@Excel(name = "分支机构", width = 15)
    @ApiModelProperty(value = "分支机构")
    private Integer branch;

	/**个案紧急程度*/
    @Dict(dicCode = "case_urgency")
	@Excel(name = "个案紧急程度", width = 15)
    @ApiModelProperty(value = "个案紧急程度")
    private String caseUrgency;

	/**是否使用过EAP*/
    @Dict(dicCode = "yn")
	@Excel(name = "是否使用过EAP", width = 15)
    @ApiModelProperty(value = "是否使用过EAP")
    private String usedEap;

	/**对EAP认识*/
    @Dict(dicCode = "eap_cognition")
	@Excel(name = "对EAP认识", width = 15)
    @ApiModelProperty(value = "对EAP认识")
    private String eapCognition;

	/**员工姓名*/
	@Excel(name = "员工姓名", width = 15)
    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

	/**婚姻状态*/
    @Dict(dicCode = "marital_status")
	@Excel(name = "婚姻状态", width = 15)
    @ApiModelProperty(value = "婚姻状态")
    private String maritalStatus;

	/**员工生日*/
	@Excel(name = "员工生日", width = 15)
    @ApiModelProperty(value = "员工生日")
    private String employeeBirthday;

	/**员工工号*/
	@Excel(name = "员工工号", width = 15)
    @ApiModelProperty(value = "员工工号")
    private String employeeJobNumber;

	/**工作电话*/
	@Excel(name = "工作电话", width = 15)
    @ApiModelProperty(value = "工作电话")
    private String jobPhone;

	/**员工职位*/
	@Excel(name = "员工职位", width = 15)
    @ApiModelProperty(value = "员工职位")
    private String employeePosition;

	/**咨客性别*/
    @Dict(dicCode = "sex")
	@Excel(name = "咨客性别", width = 15)
    @ApiModelProperty(value = "咨客性别")
    private String clientGender;

	/**家庭电话*/
	@Excel(name = "家庭电话", width = 15)
    @ApiModelProperty(value = "家庭电话")
    private String familyPhone;

	/**电子邮件*/
	@Excel(name = "电子邮件", width = 15)
    @ApiModelProperty(value = "电子邮件")
    private String clientEmail;

	/**工作种类*/
    @Dict(dicCode = "work_type")
	@Excel(name = "工作种类", width = 15)
    @ApiModelProperty(value = "工作种类")
    private String workType;

	/**咨客地区*/
	@Excel(name = "咨客地区", width = 15)
    @ApiModelProperty(value = "咨客地区")
    private String clientArea;

	/**岗位*/
	@Excel(name = "岗位", width = 15)
    @ApiModelProperty(value = "岗位")
    private String station;

	/**转介来源*/
    @Dict(dicCode = "referral_source")
	@Excel(name = "转介来源", width = 15)
    @ApiModelProperty(value = "转介来源")
    private String referralSource;

	/**希望预约时间*/
	@Excel(name = "希望预约时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "希望预约时间")
    private Date expectAppointTime;

	/**呈现问题类型*/
    @Dict(dicCode = "problem_type")
	@Excel(name = "呈现问题类型", width = 15)
    @ApiModelProperty(value = "呈现问题类型")
    private String problemType;

	/**对咨询师的期待*/
	@Excel(name = "对咨询师的期待", width = 15)
    @ApiModelProperty(value = "对咨询师的期待")
    private String expectationsForConsultants;

    private transient String companyName;
}
