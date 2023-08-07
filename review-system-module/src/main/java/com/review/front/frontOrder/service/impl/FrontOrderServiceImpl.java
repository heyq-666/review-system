package com.review.front.frontOrder.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.common.Constants;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.HttpUtils;
import org.jeecg.common.util.PayUtils;
import org.jeecg.common.util.WxAppletsUtils;
import com.review.front.dongliangReview.entity.EvalCodeEntity;
import com.review.front.dongliangReview.service.IDongLiangReviewService;
import com.review.front.frontAppoint.service.IAppointExpertService;
import com.review.front.frontOrder.entity.ReviewPayLogEntity;
import com.review.front.frontOrder.mapper.FrontOrderMapper;
import com.review.front.frontOrder.service.IFrontOrderService;
import com.review.front.frontOrder.service.IReviewPayLogService;
import com.review.front.frontOrder.vo.PreOrderVO;
import com.review.front.frontReviewClass.service.IFrontReviewClassService;
import com.review.manage.expert.entity.ReviewExpert;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewOrder.entity.ReviewOrder;
import com.review.manage.reviewOrder.vo.ReviewOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.base.entity.SysTenantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author javabage
 * @date 2023/4/7
 */
@Service
@Slf4j
public class FrontOrderServiceImpl extends ServiceImpl<FrontOrderMapper, ReviewOrder> implements IFrontOrderService {

    @Autowired
    private IFrontReviewClassService frontReviewClassService;

    @Autowired
    private IReviewPayLogService reviewPayLogService;

    @Autowired
    private FrontOrderMapper frontOrderMapper;

    @Autowired
    private IAppointExpertService appointExpertService;

    @Autowired
    private IDongLiangReviewService dongLiangReviewService;

    @Override
    @Transactional
    public PreOrderVO createPrePayOrder(ReviewOrderVO reviewOrder) {
        if (StrUtil.isBlank(reviewOrder.getClassId()) || StrUtil.isBlank(reviewOrder.getUserId())) {
            log.warn("classId or userID is null");
            return null;
        }
        //判断订单是否已存在
        ReviewOrderVO reviewOrderVO = frontReviewClassService.findOneOrder(reviewOrder.getClassId(), reviewOrder.getUserId());
        if (reviewOrderVO != null && StrUtil.isNotBlank(reviewOrderVO.getPayId()) && reviewOrderVO.getStatus() != Constants.OrderStatus.PAY_EXPIRED) {
            PreOrderVO preOrder = new PreOrderVO();
            preOrder.setPrePayID(reviewOrderVO.getPayId());
            preOrder.setPackageStr("prepay_id=" + preOrder.getPrePayID());
            if (Constants.OrderStatus.SUCCESS == reviewOrderVO.getStatus() || Constants.OrderStatus.PRE_SUCCESS == reviewOrderVO.getStatus()) {
                preOrder.setReturnCode("FAIL");
                preOrder.setReturnMsg("订单已支付，无需重复创建");
                return preOrder;
            } else {
                Date now = new Date();
                Date createTime = DateUtil.parse(reviewOrderVO.getCreateTime());
                long diffMinutes = DateUtil.between(createTime, now, DateUnit.MINUTE);
                if (diffMinutes >= 60) { //超时1个小时 该订单就已过期 重新创建订单
                    //更新订单状态为已过期
                    ReviewOrder reviewOrderE = new ReviewOrder();
                    reviewOrderE.setId(reviewOrderVO.getId().intValue());
                    reviewOrderE.setStatus(Constants.OrderStatus.PAY_EXPIRED);
                    reviewOrderE.setOperateTime(now);
                    this.updateById(reviewOrderE);
                } else {
                    String nonceStr = IdUtil.simpleUUID();
                    preOrder.setNonceStr(nonceStr);
                    long timestamp = System.currentTimeMillis() / 1000;
                    preOrder.setTimeStamp(timestamp + "");
                    preOrder.setPaySign(WxAppletsUtils.paySign(nonceStr, preOrder.getPrePayID(), timestamp));
                    preOrder.setReturnCode("SUCCESS");
                    return preOrder;
                }
            }
        }
        //获取测评量表
        ReviewClass reviewClass = frontReviewClassService.getById(reviewOrder.getClassId());
        if (reviewClass == null) {
            log.warn("classId {} is not exists", reviewClass.getClassId());
            return null;
        }else if (reviewClass.getOrgPrice() == null){
            reviewClass.setOrgPrice(BigDecimal.valueOf(0));
            reviewClass.setDicountPrice(BigDecimal.valueOf(0));
        }
        //生成订单号
        long orderNo = IdUtil.getSnowflake(0,0).nextId();
        String key = reviewOrder.getClassId() + reviewOrder.getUserId();
        synchronized (key.intern()) {
            try{
                ReviewOrder orderEntity = new ReviewOrder();
                BeanUtils.copyProperties(reviewOrder,orderEntity);
                orderEntity.setOrderNo(orderNo);
                orderEntity.setClassName(reviewClass.getTitle());
                orderEntity.setCreateTime(new Date());
                orderEntity.setOperateTime(orderEntity.getCreateTime());
                orderEntity.setOrgAmount(reviewClass.getOrgPrice());
                orderEntity.setOrderAmount(reviewClass.getOrgPrice().subtract(reviewClass.getDicountPrice()));
                //如果金额为0
                if (orderEntity.getOrderAmount() == null || orderEntity.getOrderAmount().doubleValue() == 0) {
                    orderEntity.setStatus(Constants.OrderStatus.SUCCESS);
                    orderEntity.setPayId("000");
                    this.save(orderEntity);
                    PreOrderVO preOrderVO = new PreOrderVO();
                    preOrderVO.setPrePayID("000");
                    return preOrderVO;
                }
                orderEntity.setStatus(Constants.OrderStatus.PRE_PAY);

                //创建微信预支付订单
                PreOrderVO preOrder = this.generatePrePayOrder(reviewOrder.getOpenid(), reviewOrder.getIpAddr(),
                        orderEntity.getOrderNo(), orderEntity.getOrderAmount(), orderEntity.getClassName());

                if (Constants.WX_PAY_STATUS_SUCCESS.equals(preOrder.getResultCode()) && Constants.WX_PAY_STATUS_SUCCESS.equals(preOrder.getResultCode())) {
                    orderEntity.setPayId(preOrder.getPrePayID());
                    this.save(orderEntity);
                    //记录支付日志
                    preOrder.getReviewPayLog().setOrderId(Long.valueOf(orderEntity.getId()));
                    preOrder.getReviewPayLog().setBroswer(reviewOrder.getBroswer());
                    preOrder.getReviewPayLog().setUserId(reviewOrder.getUserId());
                    preOrder.getReviewPayLog().setOperator(reviewOrder.getOperator());
                    reviewPayLogService.save(preOrder.getReviewPayLog());
                    return preOrder;
                }
            } catch (Exception e) {
                log.error("createPrePayOrder error, ", e);
            }
        }
        return null;
    }

    @Override
    public boolean updateStatusByPayId(Long orderNo,String payId, Integer status, String transactionId, String payResultCode,
                                   String payResultMsg, Integer totalFee) {
        //检查订单是否存在
        QueryWrapper<ReviewOrder> queryWrapper = new QueryWrapper<ReviewOrder>();
        QueryWrapper<ReviewOrder> queryWrapper1 = new QueryWrapper<ReviewOrder>();
        queryWrapper.eq("order_no",orderNo);
        queryWrapper1.eq("pay_id",payId);
        List<ReviewOrder> map = this.list(queryWrapper);
        List<ReviewOrder> map1 = this.list(queryWrapper1);
        if (map.size() == 0 || map == null) {
            if (map1.size() == 0 || map1 == null) {
                log.warn("payId:{} is not exist", payId);
                log.warn("orderNo:{} is not exist", orderNo);
                return false;
            }
        }
        //处理成功的订单 不再重复处理
        Integer orderStatus = map1.get(0).getStatus();
        if (orderStatus == Constants.OrderStatus.SUCCESS) {
            log.warn("order_no:{}  had process success", map1.get(0).getOrderNo());
            return false;
        }
        //check支付金额
        int orderAmount = BigDecimal.valueOf(Double.valueOf(map1.get(0).getOrderAmount().toString())).multiply(BigDecimal.valueOf(100)).intValue();
        if (Constants.OrderStatus.SUCCESS == status && (totalFee == null || totalFee != orderAmount)) {
            log.warn("totalFee:{} not equals orderAmount:{} ", totalFee == null ? "null" : totalFee, orderAmount);
            return false;
        }
        ReviewOrder reviewOrder = new ReviewOrder();
        reviewOrder.setId(map1.get(0).getId());
        reviewOrder.setStatus(status);
        reviewOrder.setOperateTime(new Date());
        reviewOrder.setPayTime(Constants.OrderStatus.SUCCESS == status || Constants.OrderStatus.PRE_SUCCESS == status ? new Date() : null);
        reviewOrder.setTransactionId(StrUtil.isNotBlank(transactionId) ? transactionId : null);
        reviewOrder.setPayResultCode(StrUtil.isNotBlank(payResultCode) ? payResultCode : null);
        reviewOrder.setPayResultMsg(StrUtil.isNotBlank(payResultMsg) ? payResultMsg : null);
        if (status != Constants.OrderStatus.SUCCESS) {
            int updateCount = frontOrderMapper.updateById(reviewOrder);
            return updateCount == 1 ? true : false;
        }else {
            return false;
        }
    }

    @Override
    public List<ReviewOrderVO> getOrderList(ReviewOrderVO reviewOrder) {
        Map param = new HashMap();
        param.put("userId",reviewOrder.getUserId());
        param.put("preSuccess",Constants.OrderStatus.PRE_SUCCESS);
        param.put("success",Constants.OrderStatus.SUCCESS);
        return frontOrderMapper.getOrderList(param);
    }

    @Override
    public PreOrderVO createPrePayConsultationOrder(ReviewOrderVO reviewOrder) {
        if (StrUtil.isBlank(reviewOrder.getClassId()) || StrUtil.isBlank(reviewOrder.getUserId())) {
            log.warn("classId or userID is null");
            return null;
        }
        //判断订单是否已存在
        ReviewOrderVO reviewOrderVO = frontReviewClassService.findOneOrder(reviewOrder.getClassId(), reviewOrder.getUserId());
        if (reviewOrderVO != null && StrUtil.isNotBlank(reviewOrderVO.getPayId()) && reviewOrderVO.getStatus() != Constants.OrderStatus.PAY_EXPIRED) {
            PreOrderVO preOrder = new PreOrderVO();
            preOrder.setPrePayID(reviewOrderVO.getPayId());
            preOrder.setPackageStr("prepay_id=" + preOrder.getPrePayID());
            if (Constants.OrderStatus.SUCCESS == reviewOrderVO.getStatus() || Constants.OrderStatus.PRE_SUCCESS == reviewOrderVO.getStatus()) {
                preOrder.setReturnCode("FAIL");
                preOrder.setReturnMsg("订单已支付，无需重复创建");
                return preOrder;
            } else {
                Date now = new Date();
                Date date = new Date();
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH).parse(reviewOrderVO.getCreateTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diffMinutes = DateUtil.between(date, now, DateUnit.MINUTE);
                if (diffMinutes >= 60) { //超时1个小时 该订单就已过期 重新创建订单
                    //更新订单状态为已过期
                    ReviewOrder reviewOrderE = new ReviewOrder();
                    reviewOrderE.setId(reviewOrderVO.getId().intValue());
                    reviewOrderE.setStatus(Constants.OrderStatus.PAY_EXPIRED);
                    reviewOrderE.setOperateTime(now);
                    this.updateById(reviewOrderE);
                } else {
                    String nonceStr = IdUtil.simpleUUID();
                    preOrder.setNonceStr(nonceStr);
                    long timstamp = System.currentTimeMillis() / 1000;
                    preOrder.setTimeStamp(timstamp + "");
                    preOrder.setPaySign(WxAppletsUtils.paySign(nonceStr, preOrder.getPrePayID(), timstamp));
                    preOrder.setReturnCode("SUCCESS");
                    return preOrder;
                }
            }
        }
        //查询专家详情
        ReviewExpert reviewExpertEntity = appointExpertService.getById(reviewOrder.getExpertId());
        if (reviewExpertEntity == null) {
            log.warn("expertId {} is not exists", reviewOrder.getExpertId());
            return null;
        }
        //生成订单号
        long orderNo = IdUtil.getSnowflake(0, 0).nextId();
        String key = reviewOrder.getClassId() + reviewOrder.getUserId();
        synchronized (key.intern()) {
            try {
                //封装订单信息
                ReviewOrder orderEntity = new ReviewOrder();
                BeanUtils.copyProperties(reviewOrder, orderEntity);
                orderEntity.setOrderNo(orderNo);
                orderEntity.setClassName("预约专家订单");
                orderEntity.setCreateTime(new Date());
                orderEntity.setOperateTime(orderEntity.getCreateTime());
                orderEntity.setOrgAmount(reviewExpertEntity.getOrgPrice());
                orderEntity.setOrderAmount(reviewExpertEntity.getOrgPrice().subtract(reviewExpertEntity.getDicountPrice()));
                //如果金额为0
                if (orderEntity.getOrderAmount() == null || orderEntity.getOrderAmount().doubleValue() == 0) {
                    orderEntity.setStatus(Constants.OrderStatus.SUCCESS);
                    orderEntity.setPayId("000");
                    this.save(orderEntity);
                    PreOrderVO preOrderVO = new PreOrderVO();
                    preOrderVO.setPrePayID("000");
                    return preOrderVO;
                }
                orderEntity.setStatus(Constants.OrderStatus.PRE_PAY);
                //创建微信预支付订单
                PreOrderVO preOrder = this.generatePrePayOrder(reviewOrder.getOpenid(), reviewOrder.getIpAddr(),
                        orderEntity.getOrderNo(), orderEntity.getOrderAmount(), orderEntity.getClassName());
                if (Constants.WX_PAY_STATUS_SUCCESS.equals(preOrder.getResultCode()) && Constants.WX_PAY_STATUS_SUCCESS.equals(preOrder.getReturnCode())) {
                    orderEntity.setPayId(preOrder.getPrePayID());
                    this.save(orderEntity);
                    //记录支付日志
                    preOrder.getReviewPayLog().setOrderId(Long.valueOf(orderEntity.getId()));
                    preOrder.getReviewPayLog().setBroswer(reviewOrder.getBroswer());
                    preOrder.getReviewPayLog().setUserId(reviewOrder.getUserId());
                    preOrder.getReviewPayLog().setOperator(reviewOrder.getOperator());
                    reviewPayLogService.save(preOrder.getReviewPayLog());
                    return preOrder;
                }
            } catch (Exception e) {
                log.error("createPrePayOrder error, ", e);
            }
        }
        return null;
    }

    @Override
    public PreOrderVO createEvalCodePrePayOrder(ReviewOrderVO reviewOrder) {
        if (StrUtil.isBlank(reviewOrder.getClassId()) || StrUtil.isBlank(reviewOrder.getUserId())) {
            log.warn("classId or userID is null");
            return null;
        }
        //判断订单是否已存在
        ReviewOrderVO reviewOrderVO = frontReviewClassService.findOneOrder(reviewOrder.getClassId(), reviewOrder.getUserId());
        if (reviewOrderVO != null && StrUtil.isNotBlank(reviewOrderVO.getPayId()) && reviewOrderVO.getStatus() != Constants.OrderStatus.PAY_EXPIRED) {
            PreOrderVO preOrder = new PreOrderVO();
            preOrder.setPrePayID(reviewOrderVO.getPayId());
            preOrder.setPackageStr("prepay_id=" + preOrder.getPrePayID());
            if (Constants.OrderStatus.SUCCESS == reviewOrderVO.getStatus() || Constants.OrderStatus.PRE_SUCCESS == reviewOrderVO.getStatus()) {
                preOrder.setReturnCode("FAIL");
                preOrder.setReturnMsg("订单已支付，无需重复创建");
                return preOrder;
            } else {
                Date now = new Date();
                Date createTime = DateUtil.parse(reviewOrderVO.getCreateTime());
                long diffMinutes = DateUtil.between(createTime, now, DateUnit.MINUTE);
                if (diffMinutes >= 60) { //超时1个小时 该订单就已过期 重新创建订单
                    //更新订单状态为已过期
                    ReviewOrder reviewOrderE = new ReviewOrder();
                    reviewOrderE.setId(reviewOrderVO.getId().intValue());
                    reviewOrderE.setStatus(Constants.OrderStatus.PAY_EXPIRED);
                    reviewOrderE.setOperateTime(now);
                    this.updateById(reviewOrderE);
                } else {
                    String nonceStr = IdUtil.simpleUUID();
                    preOrder.setNonceStr(nonceStr);
                    long timstamp = System.currentTimeMillis() / 1000;
                    preOrder.setTimeStamp(timstamp + "");
                    preOrder.setPaySign(WxAppletsUtils.paySign(nonceStr, preOrder.getPrePayID(), timstamp));
                    preOrder.setReturnCode("SUCCESS");
                    return preOrder;
                }
            }
        }
        //生成订单号
        long orderNo = IdUtil.getSnowflake(0, 0).nextId();
        String key = reviewOrder.getClassId() + reviewOrder.getUserId();
        synchronized (key.intern()) {
            try{
                //封装订单信息
                ReviewOrder orderEntity = new ReviewOrder();
                BeanUtils.copyProperties(reviewOrder, orderEntity);
                orderEntity.setOrderNo(orderNo);
                orderEntity.setClassName("栋梁测评码");
                orderEntity.setCreateTime(new Date());
                orderEntity.setOperateTime(orderEntity.getCreateTime());
                orderEntity.setOrderAmount(orderEntity.getOrgAmount());
                //如果金额为0
                if (orderEntity.getOrderAmount() == null || orderEntity.getOrderAmount().doubleValue() == 0) {
                    orderEntity.setStatus(Constants.OrderStatus.SUCCESS);
                    orderEntity.setPayId("000");
                    this.save(orderEntity);
                    PreOrderVO preOrderVO = new PreOrderVO();
                    preOrderVO.setPrePayID("000");
                    return preOrderVO;
                }
                orderEntity.setStatus(Constants.OrderStatus.PRE_PAY);
                //创建微信预支付订单
                PreOrderVO preOrder = this.generatePrePayOrder(reviewOrder.getOpenid(), reviewOrder.getIpAddr(),
                        orderEntity.getOrderNo(), orderEntity.getOrderAmount(), orderEntity.getClassName());

                if (Constants.WX_PAY_STATUS_SUCCESS.equals(preOrder.getResultCode())) {
                    orderEntity.setPayId(preOrder.getPrePayID());
                    this.save(orderEntity);
                    //记录支付日志
                    preOrder.getReviewPayLog().setOrderId(Long.valueOf(orderEntity.getId()));
                    preOrder.getReviewPayLog().setBroswer(reviewOrder.getBroswer());
                    preOrder.getReviewPayLog().setUserId(reviewOrder.getUserId());
                    preOrder.getReviewPayLog().setOperator(reviewOrder.getOperator());
                    reviewPayLogService.save(preOrder.getReviewPayLog());
                    return preOrder;
                }else {
                    EvalCodeEntity evalCode = new EvalCodeEntity();
                    evalCode.setStatus((byte) 1);
                    dongLiangReviewService.update(evalCode,new UpdateWrapper<EvalCodeEntity>().lambda().eq(EvalCodeEntity::getEvalCode,reviewOrder.getClassId()));
                }
            } catch (Exception e) {
                log.error("createPrePayOrder error, ", e);
            }
        }
        return null;
    }

    public PreOrderVO generatePrePayOrder(String openid, String ip, Long orderNo, BigDecimal orderAmount, String body) {
        PreOrderVO preOrder = new PreOrderVO();
        try{
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            SysTenantVO sysTenantVO = (SysTenantVO) request.getSession().getAttribute("appSession");
            //生成的随机字符串
            String nonce_str = IdUtil.simpleUUID();
            //商品名称
            //获取本机的ip地址
            long money = orderAmount.multiply(BigDecimal.valueOf(100)).longValue();//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败
            Map<String, String> packageParams = new HashMap<String, String>();
            if (sysTenantVO != null && StringUtils.isNotEmpty(sysTenantVO.getAppId()) && StringUtils.isNotEmpty(sysTenantVO.getAppSecret())){
                packageParams.put("appid",sysTenantVO.getAppId());
            } else {
                packageParams.put("appid",WxAppletsUtils.appId);
            }
            packageParams.put("appid", WxAppletsUtils.appId);
            packageParams.put("mch_id", WxAppletsUtils.mchID);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no", orderNo+"");//商户订单号
            packageParams.put("total_fee", money+"");
            packageParams.put("spbill_create_ip", ip);
            packageParams.put("notify_url", WxAppletsUtils.notifyUrl);
            packageParams.put("trade_type", WxAppletsUtils.tradeType);
            packageParams.put("openid", openid);

            // 除去数组中的空值和签名参数
            packageParams = PayUtils.paraFilter(packageParams);
            String prestr = PayUtils.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = PayUtils.sign(prestr, WxAppletsUtils.payKey, "utf-8").toUpperCase();
            log.info("=======================第一次签名：" + mysign + "=====================");

            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String paramXmlStr = String.format(WxAppletsUtils.prePayParamFormat, WxAppletsUtils.appId, body, WxAppletsUtils.mchID, nonce_str, WxAppletsUtils.notifyUrl, openid,
                    orderNo, ip, money, WxAppletsUtils.tradeType, mysign);
            log.info("调试模式_统一下单接口 请求XML数据：{}", paramXmlStr);
            //调用统一下单接口，并接受返回的结果
            String result = HttpUtils.postString(WxAppletsUtils.prePayUrl, paramXmlStr);
            log.info("调试模式_统一下单接口 返回XML数据：{}", result);
            // 将解析结果存储在HashMap中
            Map map = PayUtils.doXMLParse(result);

            String return_code = (String) map.get("return_code");//返回状态码
            String result_code = (String) map.get("result_code");//返回状态码
            String result_msg = (String) map.get("result_msg");//返回状态码

            preOrder.setReturnCode(return_code);
            preOrder.setReturnMsg(result_msg);
            preOrder.setResultCode(result_code);

            //返回给移动端需要的参数
            if(Constants.WX_PAY_STATUS_SUCCESS.equals(return_code) && Constants.WX_PAY_STATUS_SUCCESS.equals(result_code)){
                // 业务结果
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                preOrder.setPrePayID(prepay_id);
                preOrder.setNonceStr(nonce_str);
                preOrder.setPackageStr("prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                //这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                preOrder.setTimeStamp(timeStamp.toString());
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = WxAppletsUtils.paySign(nonce_str, prepay_id, timeStamp);
                log.info("=======================第二次签名：" + paySign + "=====================");
                preOrder.setPaySign(paySign);

                //这里记录支付日志
                ReviewPayLogEntity reviewPayLog = new ReviewPayLogEntity();
                reviewPayLog.setIpAddr(ip);
                reviewPayLog.setPrePayResp(JSONObject.toJSONString(map));
                reviewPayLog.setReqParam(JSONObject.toJSONString(packageParams));
                reviewPayLog.setOperateTime(new Date());
                reviewPayLog.setOrderNo(orderNo);
                reviewPayLog.setOperateType(Constants.OrderStatus.PRE_PAY);
                preOrder.setReviewPayLog(reviewPayLog);
            }

        } catch(Exception e) {
            throw new RuntimeException("generatePrePayOrder error, ", e);
        }
        return preOrder;
    }
}
