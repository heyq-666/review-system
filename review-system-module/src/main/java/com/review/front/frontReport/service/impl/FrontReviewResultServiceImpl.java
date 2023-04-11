package com.review.front.frontReport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontReport.mapper.FrontReviewResultMapper;
import com.review.front.frontReport.service.IFrontReviewResultService;
import com.review.front.frontReviewClass.vo.ReviewResultVO;
import com.review.manage.userManage.entity.ReviewResult;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Service
public class FrontReviewResultServiceImpl extends ServiceImpl<FrontReviewResultMapper, ReviewResult> implements IFrontReviewResultService {

    @Autowired
    private FrontReviewResultMapper frontReviewResultMapper;

    @Override
    public List<ReviewResultVO> getReviewResult(ReviewResultVO reviewResult) {
        return frontReviewResultMapper.getReviewResult(reviewResult);
    }

    @Override
    public List<ReviewResultVO> getReviewReports(String userId, Long projectId) {
        Map param = new HashMap();
        param.put("userId",userId);
        param.put("projectId",projectId);
        return frontReviewResultMapper.getReviewReports(param);
    }

    @Override
    public List<ReviewResultVO> getProjectReviewResult(ReviewResult reviewResult,Long pCount, HttpServletRequest req) {
        req.getParameterMap().put("column",new String[]{"createTime"});
        req.getParameterMap().put("order",new String[]{"DESC"});
        QueryWrapper<ReviewResult> queryWrapper = QueryGenerator.initQueryWrapper(reviewResult, req.getParameterMap());
        List<ReviewResult> result = this.list(queryWrapper);
        //该人员该项目下测评总量表数
        int resultCount = result.size();
        //该项目测评次数
        int limit = (int) Math.ceil(resultCount / pCount.intValue());
        List<ReviewResultVO> resultVOS = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            ReviewResultVO reviewResultVO = new ReviewResultVO();
            reviewResultVO.setLimitId(i * pCount.intValue());
            reviewResultVO.setClassId(result.get(i * pCount.intValue()).getClassId());
            reviewResultVO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(result.get(i * pCount.intValue()).getCreateTime()));
            resultVOS.add(reviewResultVO);
        }
        return resultVOS;
    }
}
