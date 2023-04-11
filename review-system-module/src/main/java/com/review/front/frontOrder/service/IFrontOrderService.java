package com.review.front.frontOrder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.frontOrder.vo.PreOrderVO;
import com.review.manage.reviewOrder.entity.ReviewOrder;
import com.review.manage.reviewOrder.vo.ReviewOrderVO;

import java.util.List;

/**
 * @author javabage
 * @date 2023/4/7
 */
public interface IFrontOrderService extends IService<ReviewOrder> {

    /**
     * 创建预支付订单
     * @param reviewOrder
     * @return
     */
    PreOrderVO createPrePayOrder(ReviewOrderVO reviewOrder);

    /**
     * 更新订单状态
     * @param orderNo
     * @param payId
     * @param preSuccess
     * @param s
     * @param s1
     * @param s2
     * @param total_fee
     * @return
     */
    boolean updateStatusByPayId(Long orderNo, String payId, Integer preSuccess, String s, String s1, String s2, Integer total_fee);

    /**
     * 个人中心我的订购
     * @param reviewOrder
     * @return
     */
    List<ReviewOrderVO> getOrderList(ReviewOrderVO reviewOrder);

    /**
     * 小程序咨询预约-创建预支付订单
     * @param reviewOrder
     * @return
     */
    PreOrderVO createPrePayConsultationOrder(ReviewOrderVO reviewOrder);

    /**
     * 创建预支付订单--栋梁测评码购买
     * @param reviewOrder
     * @return
     */
    PreOrderVO createEvalCodePrePayOrder(ReviewOrderVO reviewOrder);
}
