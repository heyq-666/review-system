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
@TableName("review_variate_grade")
@ApiModel(value="review_variate_grade对象", description="因子-分值列表")
public class ReviewVariateGradeEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "主键")
	private java.lang.String variateGradeId;

	@ApiModelProperty(value = "因子主键")
 	private String variateId;

	@ApiModelProperty(value = "最小分值")
    private String gradeSmall;

	@ApiModelProperty(value = "最大分值")
	private String gradeBig;

	@ApiModelProperty(value = "描述")
    private String resultExplain;

	@ApiModelProperty(value = "levelGrade")
	private Integer levelGrade;
} 