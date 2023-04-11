package com.review.front.frontReport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("review_report_result")
@ApiModel(value="review_report_result对象", description="review_report_result")
public class ReviewReportResultEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "主键")
	private String resultReportId;

	@ApiModelProperty(value = "resultId")
	private String resultId;

	@ApiModelProperty(value = "reportId")
	private String reportId;

	@ApiModelProperty(value = "explainResult")
	private String explainResult;

	@ApiModelProperty(value = "主键")
	private String reportName;

	@ApiModelProperty(value = "主键")
	private Double grade;

	@ApiModelProperty(value = "主键")
	private Date createTime;

	@ApiModelProperty(value = "主键")
	private String resultType;

	@ApiModelProperty(value = "主键")
	private Integer levelGrade;
}
