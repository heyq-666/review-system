package com.review.manage.expert.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author javabage
 * @date 2023/3/27
 */
@Data
@ApiModel(value="review_expert_calendar对象", description="专家日历")
public class ReviewExpertCalendarVo {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "专家id")
    private Long expertId;

    @ApiModelProperty(value = "出诊日期")
    private String visitDate;

    @ApiModelProperty(value = "周几")
    private Integer weekDay;

    @ApiModelProperty(value = "beginTime")
    private String beginTime;

    @ApiModelProperty(value = "endTime")
    private String endTime;

    @ApiModelProperty(value = "日历状态(1:已发布；2:未发布)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "weekDayName")
    private String weekDayName;

    @ApiModelProperty(value = "时间段")
    private String timeSlot;

    @ApiModelProperty(value = "周期")
    private Integer cycle;
}
