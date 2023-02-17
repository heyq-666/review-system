package com.review.manage.subject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author javabage
 * @date 2023/2/16
 */
@Data
@TableName("review_subject")
@ApiModel(value="review_subject对象", description="专题表")
public class ReviewSubjectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "专题名称")
    private String subjectName;

    @ApiModelProperty(value = "专题描述")
    private String subjectDesc;

    @ApiModelProperty(value = "专题状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String operator;

    private transient String classIds;
}
