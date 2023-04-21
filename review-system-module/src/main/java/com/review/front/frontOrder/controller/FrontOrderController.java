package com.review.front.frontOrder.controller;

import cn.hutool.core.util.StrUtil;
import com.review.common.Constants;
import com.review.front.frontOrder.service.IFrontOrderService;
import com.review.front.frontOrder.vo.PreOrderVO;
import com.review.manage.reviewOrder.entity.ReviewOrder;
import com.review.manage.reviewOrder.vo.ReviewOrderVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.BrowserUtils;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author javabage
 * @date 2023/4/7
 */
@Api(tags="测评订单")
@RestController
@RequestMapping("reviewFront/order")
@Slf4j
public class FrontOrderController extends JeecgController<ReviewOrder, IFrontOrderService> {

    @Autowired
    private IFrontOrderService frontOrderService;
    /**
     * 小程序-创建预支付订单
     * @param reviewOrder
     * @return
     */
    @AutoLog(value = "小程序-创建预支付订单")
    @PostMapping(value = "createPrePayOrder")
    public Result<PreOrderVO> createPrePayOrder(@RequestBody ReviewOrderVO reviewOrder){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        reviewOrder.setUserId(sysUser.getId());
        reviewOrder.setOperator(sysUser.getUsername());
        reviewOrder.setGroupId(sysUser.getOrgCode());
        reviewOrder.setOpenid("");
        reviewOrder.setIpAddr(IpUtils.getIpAddr(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setBroswer(BrowserUtils.checkBrowse(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setMobilePhone(sysUser.getPhone());
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
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        reviewOrder.setUserId(sysUser.getId());
        reviewOrder.setOperator(sysUser.getUsername());
        reviewOrder.setGroupId(sysUser.getOrgCode());
        reviewOrder.setOpenid("");
        reviewOrder.setIpAddr(IpUtils.getIpAddr(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setBroswer(BrowserUtils.checkBrowse(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setMobilePhone(sysUser.getPhone());
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
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        reviewOrder.setUserId(sysUser.getId());
        reviewOrder.setOperator(sysUser.getUsername());
        reviewOrder.setGroupId(sysUser.getOrgCode());
        reviewOrder.setOpenid("");
        reviewOrder.setIpAddr(IpUtils.getIpAddr(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setBroswer(BrowserUtils.checkBrowse(SpringContextUtils.getHttpServletRequest()));
        reviewOrder.setMobilePhone(sysUser.getPhone());
        //创建预支付订单
        PreOrderVO preOrderVO = frontOrderService.createEvalCodePrePayOrder(reviewOrder);
        return preOrderVO == null ? Result.error(400,"创建失败") : Result.OK("创建成功",preOrderVO);
    }
}