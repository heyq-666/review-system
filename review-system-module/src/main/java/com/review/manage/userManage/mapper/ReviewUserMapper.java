package com.review.manage.userManage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.front.frontReport.entity.ReviewReportResultEntity;
import com.review.manage.userManage.entity.ReviewUser;
import com.review.manage.userManage.vo.QuestionAnswerVo;
import com.review.manage.userManage.vo.ReviewQuestionAnswerVO;
import com.review.manage.userManage.vo.ReviewResultVo;
import com.review.manage.userManage.vo.SysUserDepVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 测评用户
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
public interface ReviewUserMapper extends BaseMapper<ReviewUser> {

    List<SysUserDepVo> getDepNamesByUserIds(@Param("userIds")List<String> userIds);

    List<String> getDepartNameList(@Param("groupList")List<String> groupList);

    List<SysUserDepVo> getSysUserDepVoList(@Param("userIds")List<String> userIds);

    void updateByUserId(ReviewUser reviewUser);

    List<ReviewResultVo> getResultByUserId(String userId);

    void removeRecord(String resultId);

    List<ReviewQuestionAnswerVO> getListByGroupId(QuestionAnswerVo questionAnswerVo);

    List<ReviewReportResultEntity> getReviewReportResult(@Param("resultId")String resultId);
}
