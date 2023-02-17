package com.review.manage.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author javabage
 * @date 2023/2/15
 */
@Data
@TableName("review_project_class")
@ApiModel(value="review_project_class对象", description="项目表")
public class ReviewProjectClassEntity implements Serializable {
    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "量表ID")
    private String classId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}
