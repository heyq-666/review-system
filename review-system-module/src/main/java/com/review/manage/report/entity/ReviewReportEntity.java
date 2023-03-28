package com.review.manage.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author yourname
 * 
 */
@Data
@TableName("review_report")
@ApiModel(value="review_report对象", description="维度列表")
public class ReviewReportEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "主键")
	private String reportId;

	@ApiModelProperty(value = "量表Id")
	private String classId;

	@ApiModelProperty(value = "curGradeId")
    private String curGradeId;

	@ApiModelProperty(value = "维度名称")
	private String reportName;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "创建人")
	private String createBy;

	private transient String className;
}
