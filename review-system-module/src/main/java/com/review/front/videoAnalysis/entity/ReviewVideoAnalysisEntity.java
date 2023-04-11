package com.review.front.videoAnalysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**   
 * @Title: Entity
 * @Description: 视频分析记录
 * @date 2022-02-10 18:22:47
 *
 */
@Data
@TableName("review_video_analysis")
@ApiModel(value="review_video_analysis对象", description="review_video_analysis")
@Accessors(chain = true)
public class ReviewVideoAnalysisEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "视频地址")
	private String videoPath;

	@ApiModelProperty(value = "视频时长")
	private Long videoDuration;

	@ApiModelProperty(value = "分析结果")
	private String healthAnalysisResult;

	@ApiModelProperty(value = "表情分析结果")
	private String emoAnalysisResult;

	@ApiModelProperty(value = "emoStatus")
	private Integer emoStatus;

	@ApiModelProperty(value = "healthStatus")
	private Integer healthStatus;

	@ApiModelProperty(value = "healthMsg")
	private String healthMsg;

	@ApiModelProperty(value = "emoMsg")
	private String emoMsg;

	@ApiModelProperty(value = "用户id")
	private String userId;

	@ApiModelProperty(value = "量表id")
	private String classId;

	@ApiModelProperty(value = "项目id")
	private Long projectId;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;
}
