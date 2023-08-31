package org.jeecg.modules.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 测评订单
 */
@Data
@ApiModel(value="review_order对象", description="测评订单表")
public class ReviewOrderVO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户手机号")
    private String mobilePhone;

    @ApiModelProperty(value = "微信")
    private String openid;

    @ApiModelProperty(value = "支付ip地址")
    private String ipAddr;

    @ApiModelProperty(value = "测评量表id")
    private String classId;

    @ApiModelProperty(value = "量表名称")
    private String className;

    @ApiModelProperty(value = "量表封面")
    private String bannerImg;

    @ApiModelProperty(value = "订单实付金额")
    private String orderAmount;

    @ApiModelProperty(value = "原始价格")
    private String orgAmount;

    @ApiModelProperty(value = "订单号")
    private Long orderNo;

    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty(value = "外部支付id")
    private String payId;

    @ApiModelProperty(value = "支付方式")
    private Integer payType;

    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "测评专题id")
    private Long subjectId;

    @ApiModelProperty(value = "用户组id")
    private String groupId;

    @ApiModelProperty(value = "支付时间")
    private String payTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "操作时间")
    private String operateTime;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "broswer")
    private String broswer;

    @ApiModelProperty(value = "微信订单id")
    private String transactionId;

    @ApiModelProperty(value = "expertId")
    private Long expertId;

    @ApiModelProperty(value = "isExistClass")
    private Integer isExistClass;
}
