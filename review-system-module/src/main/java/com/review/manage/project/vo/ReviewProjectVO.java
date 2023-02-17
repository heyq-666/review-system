package com.review.manage.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="review_projectVO对象", description="项目表")
public class ReviewProjectVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目描述")
    private String projectDesc;

    @ApiModelProperty(value = "appletsQrCodeLink")
    private String appletsQrCodeLink;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "量表ID")
    private String classIds;

    @ApiModelProperty(value = "是否显示报告")
    private Integer showReport;

    @ApiModelProperty(value = "封面图片")
    private String cover;

    @ApiModelProperty(value = "是否开放")
    private Integer isOpen;

    @ApiModelProperty(value = "用户组ID")
    private String groupId;
}
