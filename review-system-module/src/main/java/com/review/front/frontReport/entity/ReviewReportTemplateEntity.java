package com.review.front.frontReport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Title: Entity
 * @Description: 测评量表报告模板
 * @author zhangdaihao
 * @date 2022-05-06 22:01:49
 * @version V1.0   
 *
 */
@Data
@TableName("review_report_template")
@ApiModel(value="review_report_template对象", description="review_report_template")
public class ReviewReportTemplateEntity implements Serializable {
	/**id*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键")
	private Integer id;

	/**量表id*/
	@ApiModelProperty(value = "量表id")
	private String classId;

	/**标题*/
	@ApiModelProperty(value = "标题")
	private String title;

	/**描述*/
	@ApiModelProperty(value = "描述")
	private String explanation;

	/**排序号码*/
	@ApiModelProperty(value = "排序号码")
	private Integer orderNum;

	/**操作时间*/
	@ApiModelProperty(value = "操作时间")
	private Date operateTime;

	/**操作人*/
	@ApiModelProperty(value = "操作人")
	private String operator;

	private transient String reportTips;
}
