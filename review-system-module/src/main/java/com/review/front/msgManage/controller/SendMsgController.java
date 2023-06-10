package com.review.front.msgManage.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.review.common.Constants;
import com.review.common.WxAppletsUtils;
import com.review.common.httpclient.HttpClientUtils;
import com.review.front.frontAppoint.entity.ReviewExpertReserveEntity;
import com.review.front.frontAppoint.service.IReviewExpertReserveService;
import com.review.front.frontAppoint.vo.ConsultationVO;
import com.review.front.msgManage.entity.WxSubscribeMsg;
import com.review.front.msgManage.entity.WxTemplateValue;
import com.review.front.msgManage.service.ISendMsgService;
import com.review.manage.userManage.entity.ReviewUser;
import io.swagger.annotations.Api;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/6
 */
@Api(tags="短信通知")
@RestController
@RequestMapping("/reviewFront/sendMsg")
@Slf4j
public class SendMsgController extends JeecgController<ReviewUser, ISendMsgService> {

    @Autowired
    private IReviewExpertReserveService reviewExpertReserveService;

    //模板id
    //private final static String templateId = "tz0qAaZq2v0s3dZbfPnOYwkFy7QOF82XVFNvpLZGTNQ";
    private final static String templateId = "4IVeiK2tYEmXqTcDJ7IVnXduD2CToUiV9Sz7ZHCObfs";
    private final static String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";

    /**
     * 发送短信验证码
     * @param reviewUser
     * @return
     */
    @AutoLog(value = "发送短信验证码")
    @PostMapping(value = "SendMsgCode")
    public Result<String> SendMsgCode(HttpServletRequest request,@RequestBody ReviewUser reviewUser) {

        HttpSession sessoin = request.getSession();
        if (StringUtils.isEmpty(reviewUser.getMobilePhone())) {
            return Result.error(300,"手机号不能为空");
        }else {
            JSONObject obj = new JSONObject();
            String code = RandomUtil.randomNumbers(4);
            obj.put("code",code);
            try {
                boolean isSuccess = DySmsHelper.sendSms(reviewUser.getMobilePhone(),obj, DySmsEnum.SMS_VERIFICATION_CODE);
                //return isSuccess ? Result.OK("验证码发送成功") : Result.error(400,"验证码发送失败");
                if (isSuccess) {
                    //存验证码
                    sessoin.setAttribute(reviewUser.getMobilePhone() + Constants.MSG_CODE_KEY,code);
                    return Result.OK("验证码发送成功");
                }else {
                    return Result.error(400,"验证码发送失败");
                }
            } catch (ClientException e) {
                e.printStackTrace();
                return Result.error(500,"验证码发送异常，请联系管理员");
            }
        }
    }

    /**
     * 微信模板消息推送
     * @param wxSubscribeMsg
     * @return
     */
    @AutoLog(value = "微信模板消息推送")
    @PostMapping(value = "sendSubscribeMessage")
    public Result<?> sendSubscribeMessage(@RequestBody WxSubscribeMsg wxSubscribeMsg) {
        wxSubscribeMsg.setTemplate_id(templateId);
        String roomID = RandomUtil.randomNumbers(4);
        wxSubscribeMsg.setPage("pages/room/room?userID=" + wxSubscribeMsg.getUserId() + "&template=grid&roomID=" + roomID+"&debugMode=false&cloudenv=PRO&consulId="+wxSubscribeMsg.getConsulId()+"&mobilePhone="+wxSubscribeMsg.getMobilePhone());
        //开发版
        //wxSubscribeMsg.setMiniprogram_state("developer");
        //跳转体验版
        //wxSubscribeMsg.setMiniprogram_state("trial");
        //跳转正式版
        wxSubscribeMsg.setMiniprogram_state("formal");
        //模板消息
        Map<String, WxTemplateValue> map = new HashMap<>();
        WxTemplateValue time6 = new WxTemplateValue();
        WxTemplateValue time7 = new WxTemplateValue();
        WxTemplateValue thing2 = new WxTemplateValue();
        //WxTemplateValue thing5 = new WxTemplateValue();
        WxTemplateValue thing6 = new WxTemplateValue();

        time6.setValue(wxSubscribeMsg.getVisitDate() + " " + wxSubscribeMsg.getBeginTime());
        map.put("time6", time6);
        time7.setValue(wxSubscribeMsg.getVisitDate() + " " + wxSubscribeMsg.getEndTime());
        map.put("time7", time7);
        /*thing4.setValue("1小时");
        map.put("thing4", thing4);
        thing5.setValue("视频咨询");*/
        thing2.setValue("视频咨询");
        map.put("thing2", thing2);
        thing6.setValue(wxSubscribeMsg.getExpertName());
        map.put("thing4", thing6);
        // 推送模板参数
        wxSubscribeMsg.setData(map);
        // 参数转json
        String json = JSONObject.toJSONString(wxSubscribeMsg);
        //获取AccessToken
        String accessToken = WxAppletsUtils.geneAccessToken();
        // 调用微信推送模板接口
        String doPostJson = HttpClientUtils.doPost(requestUrl+accessToken, com.alibaba.fastjson.JSONObject.parseObject(json),"utf-8");
        // 将获取到的数据进行判断进行日志写入
        JSONObject jsonObject = JSONObject.parseObject(doPostJson);
        log.info("调用微信模板消息回调结果："+jsonObject);
        return Result.OK(jsonObject);
    }

    /**
     * 给专家发送房间号
     * @param consultationVO
     * @return
     */
    @AutoLog(value = "给专家发送房间号")
    @PostMapping(value = "sendRoomId")
    public Result<String> sendRoomId(@RequestBody ConsultationVO consultationVO) {
        if (StringUtils.isEmpty(consultationVO.getExpertPhone())) {
            return Result.error(300,"手机号不能为空");
        }else {
            JSONObject obj = new JSONObject();
            obj.put("name",consultationVO.getExpertName());
            obj.put("roomId",consultationVO.getRoomId());
            try {
                boolean isSuccess = DySmsHelper.sendSms(consultationVO.getExpertPhone(),obj, DySmsEnum.VIDEO_ROOMID_CODE);
                return isSuccess ? Result.OK("发送成功") : Result.error(400,"发送失败");
            } catch (ClientException e) {
                e.printStackTrace();
                return Result.error(500,"发送异常，请联系管理员");
            }
        }
    }

    /**
     * 给专家发送咨客预约成功的短信提醒
     * @param consultationVO
     * @return
     */
    @AutoLog(value = "给专家发送咨客预约成功的短信提醒")
    @PostMapping(value = "sendAppointSuccessMsg")
    public Result<String> sendAppointSuccessMsg(@RequestBody ConsultationVO consultationVO) {
        if (StringUtils.isEmpty(consultationVO.getExpertPhone())) {
            return Result.error(300,"手机号不能为空");
        }else {
            JSONObject obj = new JSONObject();
            obj.put("name",consultationVO.getExpertName());
            try {
                boolean isSuccess = DySmsHelper.sendSms(consultationVO.getExpertPhone(),obj, DySmsEnum.SMS_APPOINT_RECEIVE_CODE);
                return isSuccess ? Result.OK("发送成功") : Result.error(400,"发送失败");
            } catch (ClientException e) {
                e.printStackTrace();
                return Result.error(500,"发送异常，请联系管理员");
            }
        }
    }

    /**
     * 提醒用户支付通知
     * @param consultationVO
     * @return
     */
    @AutoLog(value = "提醒用户支付通知")
    @PostMapping(value = "postPayRemind")
    public Result<String> postPayRemind(@RequestBody ConsultationVO consultationVO) {
        if (StringUtils.isEmpty(consultationVO.getUserPhone())) {
            return Result.error(300,"手机号不能为空");
        }else {
            //确认预约
            ReviewExpertReserveEntity reserveEntity = new ReviewExpertReserveEntity();
            reserveEntity.setId(Long.valueOf(consultationVO.getId()));
            reserveEntity.setConfirmFlag(1);
            boolean isSuccess = reviewExpertReserveService.updateById(reserveEntity);
            if (isSuccess) {
                JSONObject obj = new JSONObject();
                obj.put("name",consultationVO.getPatientName());
                try {
                    boolean isSuccess1 = DySmsHelper.sendSms(consultationVO.getUserPhone(),obj, DySmsEnum.SMS_REMINDER_PAY_CODE);
                    return isSuccess1 ? Result.OK("发送成功") : Result.error(400,"发送失败");
                } catch (ClientException e) {
                    e.printStackTrace();
                    return Result.error(500,"发送异常，请联系管理员");
                }
            }else {
                return Result.error(500,"确认失败");
            }
        }
    }
}
