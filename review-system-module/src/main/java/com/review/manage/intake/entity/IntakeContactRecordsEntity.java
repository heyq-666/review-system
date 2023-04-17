package com.review.manage.intake.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("intake_contact_record")
@ApiModel(value="intake_contact_record对象", description="intake_contact_record对象")
@Accessors(chain = true)
public class IntakeContactRecordsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "个案id")
	private Long intakeId;

	@Dict(dicCode = "contact_record_type")
	@ApiModelProperty(value = "联系记录类型")
	private String contactRecordType;

	@ApiModelProperty(value = "联系人姓名")
	private String contactName;

	@ApiModelProperty(value = "服务人员姓名")
	private String servicePersonName;

	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "联系时间")
	private Date contactTime;

	@ApiModelProperty(value = "持续分钟")
	private String durationMinutes;

	@Dict(dicCode = "contact_type")
	@ApiModelProperty(value = "联系类型")
	private String contactType;

	@Dict(dicCode = "service_type")
	@ApiModelProperty(value = "服务类型")
	private String serviceType;

	@ApiModelProperty(value = "备注")
	private String contactRecordNote;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	private Date updateTime;

	@ApiModelProperty(value = "创建人")
	private String creator;

	private transient String employeeName;

}
