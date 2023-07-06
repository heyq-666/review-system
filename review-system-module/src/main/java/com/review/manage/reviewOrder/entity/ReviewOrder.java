package com.review.manage.reviewOrder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: review_order
 * @Author: jeecg-boot
 * @Date:   2023-02-24
 * @Version: V1.0
 */
@Data
@TableName("review_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="review_order对象", description="review_order")
@NoArgsConstructor
public class ReviewOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private Integer id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private String userId;
	/**测评量表id*/
	@Excel(name = "测评量表id", width = 15)
    @ApiModelProperty(value = "测评量表id")
    private String classId;
	/**量表名称*/
	@Excel(name = "量表名称", width = 15)
    @ApiModelProperty(value = "量表名称")
    private String className;
	/**订单实付金额*/
	@Excel(name = "订单实付金额", width = 15)
    @ApiModelProperty(value = "订单实付金额")
    private BigDecimal orderAmount;
	/**原始价格*/
	@Excel(name = "原始价格", width = 15)
    @ApiModelProperty(value = "原始价格")
    private BigDecimal orgAmount;
	/**移动手机号*/
	@Excel(name = "移动手机号", width = 15)
    @ApiModelProperty(value = "移动手机号")
    private String mobilePhone;
	/**订单号*/
	@Excel(name = "订单号", width = 15)
    @ApiModelProperty(value = "订单号")
    private Long orderNo;
	/**订单状态*/
    @Dict(dicCode = "order")
	@Excel(name = "订单状态", width = 15)
    @ApiModelProperty(value = "订单状态")
    private Integer status;
	/**外部支付id*/
	@Excel(name = "外部支付id", width = 15)
    @ApiModelProperty(value = "外部支付id")
    private String payId;
	/**支付方式*/
	@Excel(name = "支付方式", width = 15)
    @ApiModelProperty(value = "支付方式")
    private Integer payType;
	/**项目id*/
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
	/**测评专题id*/
	@Excel(name = "测评专题id", width = 15)
    @ApiModelProperty(value = "测评专题id")
    private Integer subjectId;
	/**用户组id*/
	@Excel(name = "用户组id", width = 15)
    @ApiModelProperty(value = "用户组id")
    private String groupId;
	/**支付时间*/
	@Excel(name = "支付时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**操作时间*/
	@Excel(name = "操作时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
	/**操作人*/
	@Excel(name = "操作人", width = 15)
    @ApiModelProperty(value = "操作人")
    private String operator;
	/**微信支付订单id*/
	@Excel(name = "微信支付订单id", width = 15)
    @ApiModelProperty(value = "微信支付订单id")
    private String transactionId;
	/**支付结果code*/
	@Excel(name = "支付结果code", width = 15)
    @ApiModelProperty(value = "支付结果code")
    private String payResultCode;
	/**支付结果信息*/
	@Excel(name = "支付结果信息", width = 15)
    @ApiModelProperty(value = "支付结果信息")
    private String payResultMsg;

    /**
     * 租户ID
     */
    private Long tenantId;
}
