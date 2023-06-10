package com.review.front.frontAppoint.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.common.Constants;
import com.review.front.frontAppoint.entity.ReviewExpertReserveEntity;
import com.review.front.frontAppoint.service.IAppointExpertService;
import com.review.front.frontAppoint.service.IFrontReviewExpertCalendarService;
import com.review.front.frontAppoint.service.IReviewExpertReserveService;
import com.review.front.frontAppoint.vo.ConsultationVO;
import com.review.front.frontReviewClass.service.IFrontReviewClassService;
import com.review.manage.expert.entity.ExpertLongDistanceTrain;
import com.review.manage.expert.entity.ReviewExpert;
import com.review.manage.expert.entity.ReviewExpertCalendarEntity;
import com.review.manage.expert.service.IExpertLongDistanceTrainService;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;
import com.review.manage.userManage.entity.ReviewUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/4
 */
@Api(tags="预约咨询管理")
@RestController
@RequestMapping("/reviewFront/appointExpert")
@Slf4j
public class AppointExpertController extends JeecgController<ReviewExpert, IAppointExpertService> {

    @Autowired
    private IAppointExpertService appointExpertService;

    @Autowired
    private IFrontReviewExpertCalendarService reviewExpertCalendarService;

    @Autowired
    private IReviewExpertReserveService reviewExpertReserveService;

    @Autowired
    private IFrontReviewClassService reviewClassService;

    @Autowired
    private IExpertLongDistanceTrainService distanceTrainService;

    /**
     * 小程序-专家列表查询
     * @param reviewExpert
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "小程序-专家列表查询")
    @PostMapping(value = "list")
    public Result<IPage<ReviewExpert>> list(ReviewExpert reviewExpert,
                                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                     HttpServletRequest req) {
        QueryWrapper<ReviewExpert> queryWrapper = QueryGenerator.initQueryWrapper(reviewExpert, req.getParameterMap());
        Page<ReviewExpert> page = new Page<ReviewExpert>(pageNo, pageSize);
        IPage<ReviewExpert> pageList = appointExpertService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 小程序-专家详情查询
     * @param reviewExpert
     * @return
     */
    @AutoLog(value = "小程序-专家详情查询")
    @PostMapping(value = "detail")
    public Result<ReviewExpert> detail(@RequestBody ReviewExpert reviewExpert) {
        ReviewExpert reviewExpert1 = appointExpertService.getById(reviewExpert.getId());
        return Result.OK("查询成功",reviewExpert1);
    }

    /**
     * 小程序-专家日历查询
     * @param reviewExpertCalendar
     * @return
     */
    @AutoLog(value = "小程序-专家日历查询")
    @PostMapping(value = "listCalendar")
    public Result<List<ReviewExpertCalendarVo>> listCalendar(@RequestBody ReviewExpertCalendarVo reviewExpertCalendar) {
        List<ReviewExpertCalendarVo> reviewExpertCalendarList =
                appointExpertService.getReviewExpertCalendars(reviewExpertCalendar);
        return Result.OK("查询成功",reviewExpertCalendarList);
    }

    /**
     * 小程序-预约专家
     * @param reviewExpertCalendar
     * @return
     */
    @AutoLog(value = "小程序-预约专家")
    @PostMapping(value = "orderExpert")
    public Result<?> orderExpert(@RequestBody ReviewExpertCalendarVo reviewExpertCalendar) {
        ReviewExpertCalendarEntity reviewExpertCalendarEntity = new ReviewExpertCalendarEntity();
        reviewExpertCalendarEntity.setStatus(2);
        reviewExpertCalendarEntity.setId(reviewExpertCalendar.getId());
        reviewExpertCalendarService.updateById(reviewExpertCalendarEntity);
        return Result.OK("预约成功",reviewExpertCalendar.getId());
    }

    /**
     * 小程序-保存预约人信息
     * @param reviewExpertReserveEntity
     * @return
     */
    @AutoLog(value = "小程序-保存预约人信息")
    @PostMapping(value = "saveOrderInfo")
    public Result<ReviewExpertReserveEntity> saveOrderInfo(@RequestBody ReviewExpertReserveEntity[] reviewExpertReserveEntity) {
        reviewExpertReserveEntity[0].setDelFlag(1);
        boolean success = reviewExpertReserveService.save(reviewExpertReserveEntity[0]);
        return success ? Result.OK("预约成功",reviewExpertReserveEntity[0]) : Result.error("预约失败");
    }

    /**
     * 小程序-我的预约列表
     * @param consultationVO
     * @return
     */
    @AutoLog(value = "小程序-我的预约列表")
    @PostMapping(value = "queryMyConsultation")
    public Result<List<ConsultationVO>> queryMyConsultation(@RequestBody ConsultationVO consultationVO) {
        List<ConsultationVO> reviewExpertReserveList = reviewExpertReserveService.getMyConsultation(consultationVO.getUserId());
        return Result.OK("查询成功",reviewExpertReserveList);
    }

    /**
     * 小程序-我的预约详情
     * @param consultationVO
     * @return
     */
    @AutoLog(value = "小程序-我的预约详情")
    @PostMapping(value = "queryMyConsultationDetail")
    public Result<List<ConsultationVO>> queryMyConsultationDetail(@RequestBody ConsultationVO consultationVO) {
        List<ConsultationVO> reviewExpertReserveList = reviewExpertReserveService.getMyConsultationDetail(consultationVO.getId());
        /*LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = user.getId();*/
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        ReviewUser reviewUserEntity = (ReviewUser)request.getSession().getAttribute(CommonConstant.REVIEW_LOGIN_USER);
        String userId = reviewUserEntity.getUserId();
        if (StrUtil.isNotBlank(reviewExpertReserveList.get(0).getUserId()) && reviewExpertReserveList.get(0).getCharge() == Constants.ClassCharge) {
            //判断用户是否支付了问诊费用
            reviewExpertReserveList.get(0).setBuy(reviewClassService.userBuy(reviewExpertReserveList.get(0).getId().toString(), userId));
        }
        //时间格式处理
        reviewExpertReserveService.beginAndEndTimehandle(reviewExpertReserveList);
        //判断当前时间是否可发起视频咨询
        reviewExpertReserveService.videoConsultCondition(reviewExpertReserveList);
        //判断该预约是否已经被咨询师确认
        reviewExpertReserveService.isConfirmByExpert(consultationVO,reviewExpertReserveList);
        return Result.OK("查询成功",reviewExpertReserveList);
    }

    /**
     * 小程序-取消预约
     * @param consultationVO
     * @return
     */
    @AutoLog(value = "小程序-取消预约")
    @PostMapping(value = "cancelReservation")
    public Result<?> cancelReservation(@RequestBody ConsultationVO consultationVO) {
        //取消预约-将预约人信息status置为3：取消预约
        ReviewExpertReserveEntity reserveEntity = new ReviewExpertReserveEntity();
        reserveEntity.setId(Long.valueOf(consultationVO.getId()));
        reserveEntity.setStatus(3);
        reviewExpertReserveService.updateById(reserveEntity);
        //取消预约-将专家日历状态status置为1：可预约
        ReviewExpertCalendarEntity expertCalendarEntity = new ReviewExpertCalendarEntity();
        expertCalendarEntity.setStatus(1);
        expertCalendarEntity.setId(Long.valueOf(consultationVO.getCalendarId()));
        reviewExpertCalendarService.updateById(expertCalendarEntity);
        return Result.OK("取消预约成功",consultationVO.getId());
    }

    /**
     * 小程序-判断是否为专家
     * @param reviewUser
     * @return
     */
    @AutoLog(value = "小程序-判断是否为专家")
    @PostMapping(value = "isExpert")
    public Result<Boolean> isExpert(@RequestBody ReviewUser reviewUser) {
        List<ReviewExpert> list = appointExpertService.listByMap((Map<String, Object>) new HashMap<>().put("mobilePhone",reviewUser.getMobilePhone()));
        boolean isExpert = list.size() > 0 ? true : false;
        return Result.OK("查询成功",isExpert);
    }

    /**
     * 小程序-咨询师对应的预约列表
     * @param consultationVO
     * @return
     */
    @AutoLog(value = "小程序-咨询师对应的预约列表")
    @PostMapping(value = "queryMyAppoint")
    public Result<List<ConsultationVO>> queryMyAppoint(@RequestBody ConsultationVO consultationVO) {
        List<ConsultationVO> reviewAppointList = appointExpertService.getMyAppointList(consultationVO.getExpertPhone());
        for (int i = 0; i < reviewAppointList.size(); i++) {
            if (StrUtil.isNotBlank(reviewAppointList.get(i).getUserId()) && reviewAppointList.get(i).getCharge() == Constants.ClassCharge) {
                //判断用户是否支付了问诊费用
                reviewAppointList.get(i).setBuy(reviewClassService.userBuy(reviewAppointList.get(i).getId().toString(), reviewAppointList.get(i).getUserId()));
            }
        }
        appointExpertService.handleTime(reviewAppointList);
        return Result.OK("查询成功",reviewAppointList);
    }

    /**
     * 小程序-咨询师确认预约
     * @param consultationVO
     * @return
     */
    @AutoLog(value = "小程序-咨询师确认预约")
    @PostMapping(value = "postConfirmAppoint")
    public Result<Integer> postConfirmAppoint(@RequestBody ConsultationVO consultationVO) throws ClientException {
        //确认预约
        ReviewExpertReserveEntity reserveEntity = new ReviewExpertReserveEntity();
        reserveEntity.setConfirmFlag(1);
        reserveEntity.setId(Long.valueOf(consultationVO.getId()));
        reviewExpertReserveService.updateById(reserveEntity);
        //给咨客发送短信通知：咨询师已确认该预约
        JSONObject obj = new JSONObject();
        obj.put("name",consultationVO.getPatientName());
        DySmsHelper.sendSms(consultationVO.getUserPhone(),obj, DySmsEnum.SMS_REMINDER_PAY_CODE);
        return Result.OK("已确认",consultationVO.getId());
    }
    @AutoLog(value = "小程序-专家长程培训经历列表查询")
    @PostMapping(value = "postLongDistanceTrainList")
    public Result<List<ExpertLongDistanceTrain>> postLongDistanceTrainList(@RequestBody ExpertLongDistanceTrain expertLongDistanceTrain, HttpServletRequest req) {
        QueryWrapper<ExpertLongDistanceTrain> queryWrapper = new QueryWrapper<ExpertLongDistanceTrain>();
        queryWrapper.eq("expert_id",expertLongDistanceTrain.getExpertId());
        List<ExpertLongDistanceTrain> list = distanceTrainService.list(queryWrapper);
        return Result.OK(list);
    }
}
