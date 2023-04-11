package com.review.manage.subject.vo;

import com.review.manage.reviewClass.vo.ReviewClassPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javabage
 * @date 2023/2/16
 */
@Data
@ApiModel(value="review_subject对象", description="项目表")
public class ReviewSubjectVO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "专题名称")
    private String subjectName;

    @ApiModelProperty(value = "专题描述")
    private String subjectDesc;

    @ApiModelProperty(value = "专题状态")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "创建人")
    private String operator;

    private List<ReviewClassPage> classList = new ArrayList<>();
}
