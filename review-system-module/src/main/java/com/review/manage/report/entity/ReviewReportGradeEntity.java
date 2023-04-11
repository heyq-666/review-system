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
@TableName("review_report_grade")
@ApiModel(value="review_report_grade对象", description="维度-分值列表")
public class ReviewReportGradeEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "主键")
	private String reportGradeId;

	@ApiModelProperty(value = "维度id")
	private String reportId;

	@ApiModelProperty(value = "最小分值")
    private Double gradeSmall;

	@ApiModelProperty(value = "最大分值")
	private Double gradeBig;

	@ApiModelProperty(value = "描述")
    private String resultExplain;

	@ApiModelProperty(value = "levelGrade")
	private Integer levelGrade;

}
