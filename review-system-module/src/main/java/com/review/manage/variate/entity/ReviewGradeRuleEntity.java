package com.review.manage.variate.entity;

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
@TableName("review_grade_rule")
@ApiModel(value="review_grade_rule对象", description="review_grade_rule")
public class ReviewGradeRuleEntity implements Serializable{


	private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
	private String ruleId;

    @ApiModelProperty(value = "questionId")
    private Integer questionId;

    @ApiModelProperty(value = "variateId")
    private String variateId;

    @ApiModelProperty(value = "weight")
    private Integer weight;

    @ApiModelProperty(value = "calSymbol")
    private String calSymbol;
       
} 