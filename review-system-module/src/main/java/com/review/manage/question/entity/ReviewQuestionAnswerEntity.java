package com.review.manage.question.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("review_question_answer")
@ApiModel(value="review_question_answer对象", description="review_question_answer")
public class ReviewQuestionAnswerEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**主键*/
	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "分类id")
	private String classId;

	@ApiModelProperty(value = "结果id")
	private String resultId;

	@ApiModelProperty(value = "关联项目ID")
	private Long projectId;

	@ApiModelProperty(value = "问题id")
	private Integer questionId;

	@ApiModelProperty(value = "题目编号")
	private Integer questionNum;

	@ApiModelProperty(value = "题目内容")
	private String content;

	@ApiModelProperty(value = "用户id")
	private String userId;

	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty(value = "手机号")
	private String mobilePhone;

	@ApiModelProperty(value = "性别")
	private String sex;

	@ApiModelProperty(value = "年龄")
	private Integer age;

	@ApiModelProperty(value = "用户分组id")
	private String groupId;

	@ApiModelProperty(value = "选择答案")
	private String rightAnswer;

	@ApiModelProperty(value = "所选选项code")
	private String selCode;

	@ApiModelProperty(value = "选项分数")
	private String selectGrade;

	@ApiModelProperty(value = "变量id")
	private String variateId;

	@ApiModelProperty(value = "因子名称")
	private String variateName;

	@ApiModelProperty(value = "变量因子得分")
	private String variateGrade;

	@ApiModelProperty(value = "变量因子总得分")
	private String variateTotalGrade;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;
}
