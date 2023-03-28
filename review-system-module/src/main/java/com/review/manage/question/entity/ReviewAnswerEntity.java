package com.review.manage.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * 测评试题答案实体类
 * @author yourname
 *
 */
@Data
@TableName("review_answer")
@ApiModel(value="review_answer对象", description="测评试题答案")
public class ReviewAnswerEntity implements Serializable{


    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
    private String answerId;

    @Excel(name = "题目id", width = 15)
    @ApiModelProperty(value = "题目id")
    private Integer questionId;

    @Excel(name = "选项内容", width = 15)
    @ApiModelProperty(value = "选项内容")
    private String answerContent;

    @Excel(name = "选项编码", width = 15)
    @ApiModelProperty(value = "选项编码")
    private String answerCode;

    @Excel(name = "分值", width = 15)
    @ApiModelProperty(value = "分值")
    private Double grade;

    @Excel(name = "是否正确", width = 15)
    @ApiModelProperty(value = "是否正确")
    private String isRight;

    @Excel(name = "", width = 15)
    @ApiModelProperty(value = "")
    private String prepare;

    @Excel(name = "图片地址", width = 15)
    @ApiModelProperty(value = "图片地址")
    private String pictureAttach;
}