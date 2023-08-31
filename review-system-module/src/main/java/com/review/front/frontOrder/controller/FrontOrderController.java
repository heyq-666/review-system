package com.review.front.frontOrder.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.review.common.Constants;
import com.review.front.frontOrder.service.IFrontOrderService;
import com.review.front.frontOrder.service.IReviewPayLogService;
import com.review.front.frontOrder.vo.PreOrderVO;
import com.review.front.frontUser.service.IFrontUserService;
import com.review.manage.reviewOrder.entity.ReviewOrder;
import com.review.manage.reviewOrder.vo.ReviewOrderVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/7
 */
@Api(tags="测评订单")
@RestController
@RequestMapping("reviewFront/order")
@Slf4j
public class FrontOrderController extends JeecgController<ReviewOrder, IFrontOrderService> {

    private final static Logger logger= LoggerFactory.getLogger(FrontOrderController.class);

    @Autowired
    private IFrontOrderService frontOrderService;
    @Autowired
    private IFrontUserService frontUserService;

    @Autowired
    private IReviewPayLogService reviewPayLogService;
    /**
     * 小程序-创建预支付订单
     * @param reviewOrder
     * @return
     */
    @AutoLog(value = "小程序-创建预支付订单")
    @PostMapping(value = "createPrePayOrder")
    public Result<PreOrderVO> createPrePayOrder(@RequestBody ReviewOrderVO reviewOrder){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        org.jeecg.modules.base.entity.ReviewUser reviewUserEntity = (org.jeecg.modules.base.entity.ReviewUser) request.getSession().getAttribute(CommonConstant.REVIEW_LOGIN_USER);
        reviewOrder.setUserId(reviewUserEntity.getUserId());
        reviewOrder.setOperator(reviewUserEntity.getUserName());
        reviewOrder.setGroupId(reviewUserEntity.getGroupId());
        reviewOrder.setOpenid(reviewUserEntity.getOpenid());
        reviewOrder.setIpAddr(IpUtils.getIpAddr(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setBroswer(BrowserUtils.checkBrowse(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setMobilePhone(reviewUserEntity.getMobilePhone());
        //创建预支付订单
        PreOrderVO preOrderVO = frontOrderService.createPrePayOrder(reviewOrder);
        return preOrderVO == null ? Result.error(400,"创建失败") : Result.OK("创建成功",preOrderVO);
    }

    /**
     * 小程序-更新订单状态
     * @param reviewOrder
     * @return
     */
    @AutoLog(value = "小程序-更新订单状态")
    @PostMapping(value = "updateOrderStatus")
    public Result<?> updateOrderStatus(@RequestBody ReviewOrderVO reviewOrder){

        Integer total_fee = BigDecimal.valueOf(Double.valueOf(reviewOrder.getOrderAmount())).multiply(BigDecimal.valueOf(100)).intValue();
        boolean isSuccess = frontOrderService.updateStatusByPayId(reviewOrder.getOrderNo(),reviewOrder.getPayId(), Constants.OrderStatus.PRE_SUCCESS, "",
                "", "", total_fee);
        return isSuccess ? Result.OK("更新成功") : Result.error(300,"状态已更新");
    }

    /**
     * 小程序-个人中心我的订购
     * @param reviewOrder
     * @return
     */
    @AutoLog(value = "小程序-个人中心我的订购")
    @PostMapping(value = "myOrder")
    public Result<?> myOrder(@RequestBody ReviewOrderVO reviewOrder){
        if (StrUtil.isBlank(reviewOrder.getUserId())) {
            return Result.error(300,"用户id为空");
        } else {
            List<ReviewOrderVO> orderList = frontOrderService.getOrderList(reviewOrder);
            return Result.OK("查询成功",orderList);
        }
    }

    /**
     * 小程序咨询预约-创建预支付订单
     * @param reviewOrder
     * @return
     */
    @AutoLog(value = "小程序咨询预约-创建预支付订单")
    @PostMapping(value = "createPrePayConsultationOrder")
    public Result<PreOrderVO> createPrePayConsultationOrder(@RequestBody ReviewOrderVO reviewOrder){
        //LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        org.jeecg.modules.base.entity.ReviewUser reviewUserEntity = (org.jeecg.modules.base.entity.ReviewUser) request.getSession().getAttribute(CommonConstant.REVIEW_LOGIN_USER);
        reviewOrder.setUserId(reviewUserEntity.getUserId());
        reviewOrder.setOperator(reviewUserEntity.getUserName());
        reviewOrder.setGroupId(reviewUserEntity.getGroupId());
        reviewOrder.setOpenid(reviewUserEntity.getOpenid());
        reviewOrder.setIpAddr(IpUtils.getIpAddr(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setBroswer(BrowserUtils.checkBrowse(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setMobilePhone(reviewUserEntity.getMobilePhone());
        //创建预支付订单
        PreOrderVO preOrderVO = frontOrderService.createPrePayConsultationOrder(reviewOrder);
        return preOrderVO == null ? Result.error(400,"创建失败") : Result.OK("创建成功",preOrderVO);
    }

    /**
     * 创建预支付订单--栋梁测评码购买
     * @param reviewOrder
     * @return
     */
    @AutoLog(value = "创建预支付订单--栋梁测评码购买")
    @PostMapping(value = "createEvalCodePrePayOrder")
    public Result<PreOrderVO> createEvalCodePrePayOrder(@RequestBody ReviewOrderVO reviewOrder){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        org.jeecg.modules.base.entity.ReviewUser reviewUserEntity = (org.jeecg.modules.base.entity.ReviewUser) request.getSession().getAttribute(CommonConstant.REVIEW_LOGIN_USER);
        reviewOrder.setUserId(reviewUserEntity.getUserId());
        reviewOrder.setOperator(reviewUserEntity.getUserName());
        reviewOrder.setGroupId(reviewUserEntity.getGroupId());
        reviewOrder.setOpenid(reviewUserEntity.getOpenid());
        reviewOrder.setIpAddr(IpUtils.getIpAddr(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setBroswer(BrowserUtils.checkBrowse(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setMobilePhone(reviewUserEntity.getMobilePhone());
        //创建预支付订单
        PreOrderVO preOrderVO = frontOrderService.createEvalCodePrePayOrder(reviewOrder);
        return preOrderVO == null ? Result.error(400,"创建失败") : Result.OK("创建成功",preOrderVO);
    }

    @AutoLog(value = "查询订单详情")
    @PostMapping(value = "getReviewOrderDetail")
    public Result<ReviewOrder> getReviewOrderDetail(@RequestBody ReviewOrder reviewOrder) {
        if (StringUtils.isNotBlank(reviewOrder.getPayId())){
            QueryWrapper<ReviewOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("pay_id",reviewOrder.getPayId());
            List<ReviewOrder> reviewOrder1 = frontOrderService.list(queryWrapper);
            return  reviewOrder1 != null ? Result.OK(reviewOrder1.get(0)) : Result.error("无订单");
        }else {
            return Result.error("订单id为空");
        }
    }

    @PostMapping(value = "getIsPayCalendar")
    public Result<?> getIsPayCalendar(@RequestBody ReviewOrderVO reviewOrder) {
        if (StringUtils.isNotBlank(reviewOrder.getClassId())){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            org.jeecg.modules.base.entity.ReviewUser reviewUserEntity = (org.jeecg.modules.base.entity.ReviewUser) request.getSession().getAttribute(CommonConstant.REVIEW_LOGIN_USER);
            QueryWrapper<ReviewOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("class_id",reviewOrder.getClassId());
            queryWrapper.eq("user_id",reviewUserEntity.getUserId());
            queryWrapper.eq("status",2);
            List<ReviewOrder> reviewOrderList = frontOrderService.list(queryWrapper);
            if (reviewOrderList != null && reviewOrderList.size() > 0) {
                return Result.OK(true);
            }else {
                return Result.OK(false);
            }
        }else {
            return Result.error("量表id为空");
        }
    }

    @RequestMapping(value = "wxPayNotify",method = RequestMethod.POST)
    public void wxPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(response.getOutputStream());

            String notityXml = IoUtil.read(request.getInputStream(), Charset.defaultCharset().name());
            String resXml = "";

            logger.info("接收到的报文：" + notityXml);

            Map map = PayUtils.doXMLParse(notityXml);
            String returnCode = (String) map.get("return_code");
            String err_code = (String) map.get("err_code");
            String err_code_des = (String) map.get("err_code_des");
            String out_trade_no = (String) map.get("out_trade_no");

            if(Constants.WX_PAY_STATUS_SUCCESS.equals(returnCode)){
                String resultCode = (String) map.get("result_code");
                String sign = (String)map.get("sign");
                map.remove("sign");
                //验证签名是否正确
                if(PayUtils.verify(PayUtils.createLinkString(map), sign, WxAppletsUtils.payKey, Charset.defaultCharset().name())){

                    /**此处添加自己的业务逻辑代码start**/
                    String transaction_id = map.get("transaction_id") == null ? "" : map.get("transaction_id").toString();

                    //支付费用
                    Integer total_fee = Integer.parseInt((String)map.get("total_fee"));
                    Integer status = Constants.WX_PAY_STATUS_SUCCESS.equals(resultCode) ? Constants.OrderStatus.SUCCESS : Constants.OrderStatus.PAY_FAIL;

                    //更新顶单状态
                    boolean updNum = frontOrderService.updateStatusByPayId(Long.valueOf(out_trade_no),null, status, transaction_id, err_code, err_code_des, total_fee);
                    if (updNum) {
                        //通知微信服务器已经支付成功
                        resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code>"
                                + "<return_msg><![CDATA[OK]]></return_msg></xml> ";
                    } else {
                        resXml = "<xml><return_code><![CDATA[FAIL]]></return_code>"
                                + "<return_msg><![CDATA[订单状态更新失败]]></return_msg></xml> ";
                    }

                    //更新支付日志
                    /*reviewPayLogService.executeSql("update review_pay_log set callback_resp=?, operate_time=?, operate_type=? where order_no=?",
                            new Object[]{com.alibaba.fastjson.JSONObject.toJSONString(map), DateUtil.now(), Constants.OrderStatus.SUCCESS, out_trade_no});*/
                    reviewPayLogService.updatePayLog(com.alibaba.fastjson.JSONObject.toJSONString(map), DateUtil.now(),Constants.OrderStatus.SUCCESS,out_trade_no);
                    /**此处添加自己的业务逻辑代码end**/
                } else{
                    resXml = "<xml><return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[签名校验失败]]></return_msg></xml> ";
                }
            } else{
                resXml = "<xml><return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg></xml> ";
            }
            logger.info(resXml);
            logger.info("微信支付回调数据结束");

            out.write(resXml.getBytes());
            out.flush();
        } catch (Exception e) {
            out.write("<xml><return_code><![CDATA[ERROR]]></return_code><return_msg><![CDATA[订单状态更新异常]]></return_msg></xml>".getBytes());
            out.flush();
            logger.error("wxPayNotify error, ", e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
