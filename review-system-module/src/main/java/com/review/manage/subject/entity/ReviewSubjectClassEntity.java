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
 * @date 2023/2/15
 */
@Data
@TableName("review_subject_class")
@ApiModel(value="review_subject_class对象", description="专题表")
public class ReviewSubjectClassEntity implements Serializable {
    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "量表ID")
    private String classId;

    @ApiModelProperty(value = "项目ID")
    private Long subjectId;

    @ApiModelProperty(value = "机构ID")
    private Date operateTime;

}
