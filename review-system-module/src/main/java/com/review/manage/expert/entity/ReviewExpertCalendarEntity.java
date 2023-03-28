package com.review.manage.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("review_expert_calendar")
@ApiModel(value="review_expert_calendar对象", description="review_expert_calendar对象")
@Accessors(chain = true)
public class ReviewExpertCalendarEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private Long id;

	@ApiModelProperty(value = "专家id")
	private Long expertId;

	@ApiModelProperty(value = "出诊日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	//@JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
	private Date visitDate;

	@ApiModelProperty(value = "周几")
	private Integer weekDay;

	@ApiModelProperty(value = "周期")
	private Integer cycle;

	@ApiModelProperty(value = "beginTime")
	private Date beginTime;

	@ApiModelProperty(value = "endTime")
	private Date endTime;

	@ApiModelProperty(value = "日历状态(1:已发布；2:未发布)")
	private Integer status;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	private Date updateTime;

	@ApiModelProperty(value = "创建人")
	private String creator;

}
