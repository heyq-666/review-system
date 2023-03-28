package com.review.manage.variate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @author yourname
 * 
 */
@Data
@ApiModel(value="review_variate对象", description="因子列表")
public class ReviewVariateVo {

	@ApiModelProperty(value = "主键")
	private String variateId;

	@ApiModelProperty(value = "量表Id")
 	private String classId;

	@ApiModelProperty(value = "因子名称")
    private String variateName;

	@ApiModelProperty(value = "calSymbol")
    private String calSymbol;

	@ApiModelProperty(value = "calTotal")
    private Double calTotal;

	@ApiModelProperty(value = "calSymbol1")
    private String calSymbol1;

	@ApiModelProperty(value = "calTotal1")
    private Double calTotal1;

	@ApiModelProperty(value = "curGradeId")
    private String curGradeId;

	@ApiModelProperty(value = "创建时间")
    private Date createTime;

	@ApiModelProperty(value = "创建人")
    private String createBy;

	@ApiModelProperty(value = "序号")
    private Integer sortNum;
} 