package com.review.manage.banner.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author javabage
 * @date 2023/2/16
 */
@Data
@ApiModel(value="review_bannerVO对象", description="轮播图")
public class ReviewBannerVo {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图片路径")
    private String imgUrl;

    @ApiModelProperty(value = "目标跳转url")
    private String targetUrl;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date operateTime;

    @ApiModelProperty(value = "创建人")
    private String operator;

}
