package com.review.front.frontAppoint.entity;

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
 * @Description: 测评专家预约
 * @author zhangdaihao
 * @date 2021-10-21 11:42:44
 * @version V1.0   
 *
 */
@Data
@TableName("review_expert_reserve")
@ApiModel(value="review_expert_reserve对象", description="review_expert_reserve")
public class ReviewExpertReserveEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键")
	private Long id;

	/**专家id*/
	@ApiModelProperty(value = "专家id")
	private Integer expertId;

	/**测评用户id*/
	@ApiModelProperty(value = "测评用户id")
	private String userId;

	/**日历id*/
	@ApiModelProperty(value = "日历id")
	private Integer calendarId;

	/**预约状态(1:待问诊；2:问诊结束；3:取消预约)*/
	@ApiModelProperty(value = "预约状态")
	private Integer status;

	/**预约类型(1:专家视频问诊；2:专家电话问诊)*/
	@ApiModelProperty(value = "预约类型")
	private Integer type;

	/**就诊人姓名*/
	@ApiModelProperty(value = "就诊人姓名")
	private String patientName;

	/**就诊人性别*/
	@ApiModelProperty(value = "就诊人性别")
	private Integer patientSex;

	/**就诊人年龄*/
	@ApiModelProperty(value = "就诊人年龄")
	private Integer patientAge;

	/**就诊附件*/
	@ApiModelProperty(value = "就诊附件")
	private String attachFiles;

	/**备注*/
	@ApiModelProperty(value = "备注")
	private String note;

	/**删除标记：(1:未删除；2:已删除)*/
	@ApiModelProperty(value = "删除标记")
	private Integer delFlag;

	/**创建时间*/
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**房间id*/
	@ApiModelProperty(value = "房间id")
	private Integer roomId;

	@ApiModelProperty(value = "是否确认")
	private Integer confirmFlag;
}
