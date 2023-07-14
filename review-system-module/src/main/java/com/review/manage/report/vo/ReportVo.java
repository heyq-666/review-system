package com.review.manage.report.vo;

import com.review.manage.report.entity.ReviewReportGradeEntity;
import com.review.manage.report.entity.ReviewReportVariateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javabage
 * @date 2023/3/22
 */
@Data
@ApiModel(value="review_report对象", description="维度列表")
public class ReportVo {

    @ApiModelProperty(value = "reportId")
    private String reportId;

    @ApiModelProperty(value = "classId")
    private String classId;

    @ApiModelProperty(value = "reportName")
    private String reportName;

    @ApiModelProperty(value = "createTime")
    private String createTime;

    @ApiModelProperty(value = "crateBy")
    private String crateBy;

    @ApiModelProperty(value = "reportNum")
    private Integer reportNum;

    @ApiModelProperty(value = "curGradeId")
    private String curGradeId;

    @ApiModelProperty(value = "reportList")
    private List<ReportVo> reportList = new ArrayList<ReportVo>();

    @ApiModelProperty(value = "grade")
    private Double grade;

    @ApiModelProperty(value = "reportGradeList")
    private List<ReviewReportGradeEntity> reportGradeList = new ArrayList<ReviewReportGradeEntity>();

    @ApiModelProperty(value = "reportVariateList")
    private List<ReviewReportVariateEntity> reportVariateList = new ArrayList<ReviewReportVariateEntity>();
}
