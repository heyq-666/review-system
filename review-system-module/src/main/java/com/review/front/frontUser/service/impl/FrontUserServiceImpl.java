package com.review.front.frontUser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.common.Constants;
import com.review.front.frontProject.service.IFrontProjectService;
import com.review.front.frontUser.mapper.FrontUserMapper;
import com.review.front.frontUser.service.IFrontUserService;
import com.review.manage.expert.entity.ExpertLongDistanceTrain;
import com.review.manage.project.entity.ReviewProjectEntity;
import com.review.manage.userManage.entity.ReviewUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Service
public class FrontUserServiceImpl extends ServiceImpl<FrontUserMapper,ReviewUser> implements IFrontUserService {

    @Autowired
    private IFrontProjectService frontProjectService;

    @Override
    public Result register(ReviewUser reviewUser) {
        QueryWrapper<ReviewUser> queryWrapper = new QueryWrapper<ReviewUser>();
        queryWrapper.eq("mobile_phone",reviewUser.getMobilePhone());
        List<ReviewUser> reviewUserList = this.list(queryWrapper);
        ReviewUser reviewUserEntity = CollectionUtils.isNotEmpty(reviewUserList) ? reviewUserList.get(0) : new ReviewUser();
        //判断用户是否已经绑定过openid
        if(StringUtils.isNotBlank(reviewUserEntity.getOpenid()) && reviewUserEntity.getOpenid().equals(reviewUser.getOpenid())) {
            return Result.error(1001,"用户已注册");
        }
        //BeanUtils.copyProperties(reviewUserEntity,reviewUser);
        //存储用户额外信息
        /*if (!reviewUser.getExtraObj().isEmpty()) {
            reviewUserEntity.setExtra(com.alibaba.fastjson.JSONObject.toJSONString(reviewUser.getExtraObj()));
        }*/
        if (!reviewUser.getExtraObj().isEmpty()) {
            reviewUser.setExtra(com.alibaba.fastjson.JSONObject.toJSONString(reviewUser.getExtraObj()));
        }
        //设置用户组
        boolean flag = setUserGroup(reviewUser.getProjectId(), reviewUser);
        if (!flag) {
            return Result.error(1000,"用户没有该项目测评权限，请联系管理员");
        }
        if (StringUtils.isBlank(reviewUser.getGroupId())) { //设置默认用户组
            reviewUser.setGroupId("1");
        }
        if (StringUtils.isBlank(reviewUser.getUserId())) {
            reviewUser.setCreateTime(new Date());
            reviewUser.setUpdateTime(reviewUserEntity.getUpdateTime());
            reviewUser.setSource(Constants.UserSource.Register);
            this.save(reviewUser);
        } else {
            reviewUser.setUpdateTime(new Date());
            this.saveOrUpdate(reviewUser);
        }
        return Result.OK(reviewUser.getUserId());
    }

    /**
     * 设置用户组 同时判断 如果是指定项目测评 判断用户是否有测评权限
     * @param projectId
     * @param reviewUserEntity
     * @return
     */
    private boolean setUserGroup(Long projectId, ReviewUser reviewUserEntity) {
        if (projectId != null && projectId > 0) { //是否加入用户组
            ReviewProjectEntity reviewProject = frontProjectService.getById(projectId);
            if (reviewProject == null) {
                return false;
            }
            String userGroupId = reviewUserEntity.getGroupId();
            if (reviewProject.getIsOpen() == 2) {
                if (StringUtils.isBlank(userGroupId)) {
                    reviewUserEntity.setGroupId(reviewProject.getGroupId());
                } else if (userGroupId.indexOf(reviewProject.getGroupId()) == -1) {
                    reviewUserEntity.setGroupId(reviewUserEntity.getGroupId() +"," + reviewProject.getGroupId());
                }
            } else if (StringUtils.isBlank(userGroupId) || userGroupId.indexOf(reviewProject.getGroupId()) == -1) {
                return false;
            }
        }
        return true;
    }
}
