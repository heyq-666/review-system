package com.review.front.dongliangReview.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author javabage
 * @date 2022/8/31
 */
@Data
@TableName("review_eval_code")
@ApiModel(value="review_eval_code对象", description="测评码")
@Accessors(chain = true)
public class EvalCodeEntity {

    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "测评码")
    private String evalCode;

    @ApiModelProperty(value = "测评码价格")
    private BigDecimal price;

    @ApiModelProperty(value = "测评码来源(1:线下导入；2:用户购买)")
    private Byte source;

    @ApiModelProperty(value = "测评码状态(1:未使用；2:已使用；3:已购买；4：已删除)")
    private Byte status;

    @ApiModelProperty(value = "操作时间")
    private Date operateTime;

    @ApiModelProperty(value = "测评人")
    private String userName;

    @ApiModelProperty(value = "返回信息")
    private String msg;

}
