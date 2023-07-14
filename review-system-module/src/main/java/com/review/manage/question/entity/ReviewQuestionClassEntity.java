package com.review.manage.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * 
 * @author yourname
 * 
 */
@Data
@TableName("review_question_class")
@Accessors(chain = true)
@ApiModel(value="review_question_class对象", description="量表-问题")
public class ReviewQuestionClassEntity implements Serializable{


	private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "主键")
	private String id;

	@Excel(name = "量表id", width = 15)
	@ApiModelProperty(value = "量表id")
	private String classId;

	@Excel(name = "问题id", width = 15)
	@ApiModelProperty(value = "问题id")
    private Integer questionId;
 
} 