package com.review.front.frontOrder.vo;

import com.review.front.frontOrder.entity.ReviewPayLogEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 预支付订单信息
 */
@Data
public class PreOrderVO implements Serializable {

    private String prePayID;

    private Long orderID;

    private Long orderNO;

    private String paySign;

    private String returnCode;

    private String resultCode;

    private String returnMsg;

    private String mchId;

    private String nonceStr;

    private String tradeType;

    private String appId;

    private String timeStamp;

    private String signType;

    private String packageStr;

    private ReviewPayLogEntity reviewPayLog;
}
