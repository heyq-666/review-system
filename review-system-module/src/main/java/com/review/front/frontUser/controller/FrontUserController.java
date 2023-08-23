package com.review.front.frontUser.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.review.common.Constants;
import com.review.common.MyBeanUtils;
import com.review.front.frontProject.service.IFrontProjectService;
import com.review.front.frontUser.service.IFrontUserService;
import com.review.manage.project.entity.ReviewProjectEntity;
import com.review.manage.userManage.entity.ReviewUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.WxAppletsUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Api(tags="用户信息管理")
@RestController
@RequestMapping("/reviewFront/user")
@Slf4j
public class FrontUserController extends JeecgController<ReviewUser,IFrontUserService> {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IFrontUserService frontUserService;

    @Autowired
    private IFrontProjectService frontProjectService;

    /**
     * 小程序-获取openid
     * @param paramJson
     * @return
     */
    @AutoLog(value = "小程序-获取openid")
    @PostMapping(value = "getOpenid")
    public Result<?> getOpenid(@RequestBody JSONObject paramJson,HttpServletRequest request) throws JsonProcessingException {
        String appId = request.getHeader("X-AppId-Id");
        System.out.println(appId);
        if (paramJson == null || paramJson.isEmpty() || !paramJson.containsKey("code")) {
            return Result.error(300,"code为空");
        }
        String code = paramJson.getString("code");
        String openid = WxAppletsUtils.getOpenid(code);
        if (openid == null) {
            return Result.error(301,"openid获取失败");
        } else {
            return Result.OK("openid获取成功",openid);
        }
    }
    /**
     * 小程序-提交用户注册信息
     * @param reviewUser
     * @return
     */
    @AutoLog(value = "小程序-提交用户注册信息")
    @PostMapping(value = "register")
    public Result<?> register(@RequestBody ReviewUser reviewUser) {

        if (reviewUser == null) {
            return Result.error(300,"用户信息为空");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Object object = request.getSession().getAttribute(reviewUser.getMobilePhone() + Constants.MSG_CODE_KEY);
        if(null==object || !reviewUser.getMsgCode().equals(object.toString())) {
            return Result.error("短信验证码不正确或已过期！");
        }
        long tenantId = oConvertUtils.getLong(TenantContext.getTenant(), -1);
        reviewUser.setTenantId(tenantId);
        Result result = frontUserService.register(reviewUser);
        if (result.getCode() == 200) {
            request.getSession().removeAttribute(reviewUser.getMobilePhone() + Constants.MSG_CODE_KEY);
        }
        return result;
    }

    /**
     * 小程序-修改用户信息
     * @param reviewUser
     * @return
     */
    @AutoLog(value = "小程序-修改用户信息")
    @PostMapping(value = "update")
    public Result<?> updUserInfo(HttpServletRequest request, @RequestBody ReviewUser reviewUser) {

        if (reviewUser == null || StrUtil.isBlank(reviewUser.getUserId())) {
            return Result.error(300,"用户信息为空");
        }
        /*Object userId = request.getSession().getAttribute(CommonConstant.REVIEW_LOGIN_USER);
        ReviewUser reviewUserEntity = frontUserService.getById(userId.toString());*/
        ReviewUser reviewUserEntity = (ReviewUser)request.getSession().getAttribute(CommonConstant.REVIEW_LOGIN_USER);
        if (!reviewUser.getUserId().equals(reviewUserEntity.getUserId())) {
            return Result.error(400,"非本人登陆，不允许修改");
        }
        try {
            ReviewUser reviewUserOld = frontUserService.getById(reviewUser.getUserId());
            if (reviewUserOld == null) {
                return Result.error(404,"用户不存在");
            }
            if (!reviewUserOld.getOpenid().equals(reviewUser.getOpenid())) {
                return Result.error(400,"用户openid不可修改");
            }
            //需要修改手机号
            if(!reviewUserOld.getMobilePhone().equals(reviewUser.getMobilePhone())) {
                //check 验证码
                String msgCode = (String)request.getSession().getAttribute(reviewUser.getMobilePhone() + Constants.MSG_CODE_KEY);
                if (StringUtils.isBlank(msgCode) || !msgCode.equals(reviewUser.getMsgCode())) {
                    return Result.error(301,"短信验证码不正确或已过期");
                }
            }
            MyBeanUtils.copyBeanNotNull2Bean(reviewUser, reviewUserOld);
            reviewUserOld.setUpdateTime(new Date());
            frontUserService.saveOrUpdate(reviewUserOld);
            request.getSession().removeAttribute(reviewUser.getMobilePhone() + Constants.MSG_CODE_KEY);
        } catch (Exception e) {
            return Result.error("用户信息修改失败");
        }
        return Result.OK("用户信息修改成功");
    }

    /**
     * 小程序-用户是否注册
     * @param reviewUser
     * @return
     */
    @AutoLog(value = "小程序-用户是否注册")
    @PostMapping(value = "getUserInfoByOpenid")
    public Result<?> getUserInfoByOpenid(@RequestBody ReviewUser reviewUser) {
        Long tenantId = null;
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
        if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
            tenantId = oConvertUtils.getLong(TenantContext.getTenant(),-1);
        }
        System.out.println(tenantId);
        QueryWrapper<ReviewUser> queryWrapper = new QueryWrapper<ReviewUser>();
        queryWrapper.eq("openid",reviewUser.getOpenid());
        ReviewUser reviewUser1 = frontUserService.getOne(queryWrapper);
        return reviewUser1 != null ? Result.OK(reviewUser1) : Result.error("用户未注册");
    }

    /**
     * 小程序-设置用户组
     * @param reviewUser
     * @return
     */
    @AutoLog(value = "小程序-设置用户组")
    @PostMapping(value = "joinUserGroup")
    public Result<?> joinUserGroup(@RequestBody ReviewUser reviewUser) {

        if (reviewUser.getProjectId() == null || reviewUser.getProjectId() == 0 || StringUtils.isBlank(reviewUser.getUserId())) {
            return Result.error(300,"项目/用户ID为空");
        }
        ReviewUser reviewUserEntity = frontUserService.getById(reviewUser.getUserId());
        if (reviewUserEntity == null) {
            return Result.error(301,"用户不存在");
        }
        ReviewProjectEntity reviewProject = frontProjectService.getById(reviewUser.getProjectId());
        if (reviewProject == null) {
            return Result.error(301,"项目不存在");
        }
        if (reviewUserEntity.getGroupId().indexOf(reviewProject.getGroupId()) > -1) {
            return Result.OK("该用户已是项目组成员");
        }
        if (StringUtils.isBlank(reviewProject.getGroupId())) {
            return Result.error(303,"项目未绑定用户组");
        }
        if (reviewProject.getIsOpen() == 1) {
            return Result.error(304,"非开放项目，您没有测评权限");
        }
        if (StringUtils.isBlank(reviewUserEntity.getGroupId())) {
            reviewUserEntity.setGroupId(reviewProject.getGroupId());
            frontUserService.saveOrUpdate(reviewUserEntity);
        } else if (reviewUserEntity.getGroupId().indexOf(reviewProject.getGroupId()) == -1) {
            reviewUserEntity.setGroupId(reviewUserEntity.getGroupId() + "," + reviewProject.getGroupId());
            frontUserService.saveOrUpdate(reviewUserEntity);
        }
        return Result.OK("加入成功");
    }
}
