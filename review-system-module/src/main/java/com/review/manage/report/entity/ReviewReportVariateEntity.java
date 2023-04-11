package com.review.manage.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author yourname
 * 
 */
@Data
@TableName("review_report_variate")
@ApiModel(value="review_report_variate对象", description="review_report_variate")
public class ReviewReportVariateEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "主键")
	private String reportVariateId;

	@ApiModelProperty(value = "reportId")
	private String reportId;

	@ApiModelProperty(value = "variateId")
	private String variateId;

	@ApiModelProperty(value = "weight")
    private Integer weight;

	@ApiModelProperty(value = "ruleExplain")
    private String ruleExplain;

	@ApiModelProperty(value = "calSymbol")
    private String calSymbol;
    
}
