package com.review.front.frontOrder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**   
 * @Title: Entity
 * @Description: 用户支付日志
 * @author zhangdaihao
 * @date 2021-11-23 17:43:38
 * @version V1.0   
 *
 */
@Data
@TableName("review_pay_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="review_pay_log对象", description="review_pay_log")
@NoArgsConstructor
public class ReviewPayLogEntity implements Serializable {

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private Long id;

	/**订单id*/
	@ApiModelProperty(value = "订单id")
	private Long orderId;

	/**用户id*/
	@ApiModelProperty(value = "用户id")
	private String userId;

	/**订单号*/
	@ApiModelProperty(value = "订单号")
	private Long orderNo;

	/**支付请求*/
	@ApiModelProperty(value = "支付请求")
	private String reqParam;

	/**支付请求响应*/
	@ApiModelProperty(value = "支付请求响应")
	private String prePayResp;

	/**回调响应*/
	@ApiModelProperty(value = "回调响应")
	private String callbackResp;

	/**ip地址*/
	@ApiModelProperty(value = "ip地址")
	private String ipAddr;

	/**操作类型*/
	@ApiModelProperty(value = "操作类型")
	private Integer operateType;

	/**浏览器*/
	@ApiModelProperty(value = "浏览器")
	private String broswer;

	/**操作时间*/
	@ApiModelProperty(value = "操作时间")
	private Date operateTime;

	/**操作人*/
	@ApiModelProperty(value = "操作人")
	private String operator;

	/**扩展字段*/
	private String extra;
}
