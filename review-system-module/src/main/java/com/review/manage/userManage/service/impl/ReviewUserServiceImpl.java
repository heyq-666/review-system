package com.review.manage.userManage.service.impl;

import com.review.manage.userManage.entity.ReviewResult;
import com.review.manage.userManage.entity.ReviewUser;
import com.review.manage.userManage.mapper.ReviewUserMapper;
import com.review.manage.userManage.service.IReviewUserService;
import com.review.manage.userManage.vo.ReviewResultVo;
import com.review.manage.userManage.vo.SysUserDepVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 测评用户
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Service
public class ReviewUserServiceImpl extends ServiceImpl<ReviewUserMapper, ReviewUser> implements IReviewUserService {

    @Autowired
    private ReviewUserMapper reviewUserMapper;

    @Override
    public Map<String, String> getDepNamesByUserIds(List<String> userIds) {
        List<SysUserDepVo> list = reviewUserMapper.getDepNamesByUserIds(userIds);

        Map<String, String> res = new HashMap(5);
        list.forEach(item -> {
                    if (res.get(item.getUserId()) == null) {
                        res.put(item.getUserId(), item.getDepartName());
                    } else {
                        res.put(item.getUserId(), res.get(item.getUserId()) + "," + item.getDepartName());
                    }
                }
        );
        return res;
    }

    @Override
    public List<String> getDepartNameList(List<String> groupList) {
        return reviewUserMapper.getDepartNameList(groupList);
    }

    @Override
    public List<SysUserDepVo> getSysUserDepVoList(List<String> userIds) {
        List<SysUserDepVo> sysUserDepVoList = reviewUserMapper.getSysUserDepVoList(userIds);
        return sysUserDepVoList;
    }

    @Override
    public void updateByUserId(ReviewUser reviewUser) {
        reviewUserMapper.updateByUserId(reviewUser);
    }

    @Override
    public List<ReviewResultVo> getResultByUserId(String userId) {
        List<ReviewResultVo> reviewResults = reviewUserMapper.getResultByUserId(userId);
        return reviewResults;
    }

    @Override
    public void removeRecord(String resultId) {
        reviewUserMapper.removeRecord(resultId);
    }
}
