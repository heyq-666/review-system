package com.review.manage.notice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author javabage
 * @date 2023/2/16
 */
@Data
@ApiModel(value="review_noticeVO对象", description="通告")
public class ReviewNoticeVo {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "通告名称")
    private String noticeName;

    @ApiModelProperty(value = "通告描述")
    private String noticeDesc;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String operator;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

}
