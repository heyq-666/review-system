package com.review.manage.banner.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;

import java.util.Date;

/**
 * @author javabage
 * @date 2023/2/16
 */
@Data
@TableName("review_banner")
@ApiModel(value="review_banner对象", description="轮播图")
@Accessors(chain = true)
public class ReviewBannerEntity {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图片路径")
    private String imgUrl;

    @ApiModelProperty(value = "目标跳转url")
    private String targetUrl;

    @Dict(dicCode = "send_status")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date operateTime;

    @ApiModelProperty(value = "创建人")
    private String operator;

    /**
     * 租户ID
     */
    private Long tenantId;
}
