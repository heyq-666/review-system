package com.review.manage.notice.entity;

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
@TableName("review_notice")
@ApiModel(value="review_notice对象", description="通告")
@Accessors(chain = true)
public class ReviewNoticeEntity {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "通告名称")
    private String noticeName;

    @ApiModelProperty(value = "通告描述")
    private String noticeDesc;

    //@ApiModelProperty(value = "状态")
    @Dict(dicCode = "send_status")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private String operator;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 租户ID
     */
    private Long tenantId;
}
