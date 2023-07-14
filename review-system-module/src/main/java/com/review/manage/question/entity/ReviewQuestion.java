package com.review.manage.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 问题列表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Data
@TableName("review_question")
@Accessors(chain = true)
@ApiModel(value="review_question对象", description="问题列表")
public class ReviewQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer questionId;
	/**是否导入*/
	@Excel(name = "是否导入", width = 15)
    @ApiModelProperty(value = "是否导入")
    private Integer isImportant;
	/**题目类型*/
	@Excel(name = "题目类型", width = 15)
    @ApiModelProperty(value = "题目类型")
    private String questionType;
	/**题目内容*/
	@Excel(name = "题目内容", width = 15)
    @ApiModelProperty(value = "题目内容")
    private String content;
	/**题目序号*/
	@Excel(name = "题目序号", width = 15)
    @ApiModelProperty(value = "题目序号")
    private Integer questionNum;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**选项*/
	@Excel(name = "选项", width = 15)
    @ApiModelProperty(value = "选项")
    private String rightAnswer;
	/**图片*/
	@Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private String pictureAttach;

    private transient String className;

    private transient String classId;

    /**
     * 租户ID
     */
    private Long tenantId;
}
