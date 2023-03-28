package com.review.manage.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author javabage
 * @date 2023/3/22
 */
@Data
@ApiModel(value="review_report对象", description="维度列表")
public class ReportVo {

    @ApiModelProperty(value = "主键")
    private String reportId;

    @ApiModelProperty(value = "主键")
    private String classId;

    @ApiModelProperty(value = "主键")
    private String curGradeId;

    @ApiModelProperty(value = "主键")
    private String reportName;

    @ApiModelProperty(value = "主键")
    private String createTime;

    @ApiModelProperty(value = "主键")
    private String crateBy;
}
