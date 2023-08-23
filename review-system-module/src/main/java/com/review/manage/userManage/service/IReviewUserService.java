package com.review.manage.userManage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.userManage.entity.ReviewUser;
import com.review.manage.userManage.vo.QuestionAnswerVo;
import com.review.manage.userManage.vo.ReviewResultVo;
import com.review.manage.userManage.vo.SysUserDepVo;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * @Description: 测评用户
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
public interface IReviewUserService extends IService<ReviewUser> {

    Map<String, String> getDepNamesByUserIds(List<String> userIds);

    List<String> getDepartNameList(List<String> groupList);

    List<SysUserDepVo> getSysUserDepVoList(List<String> userIds);

    void updateByUserId(ReviewUser reviewUser);

    List<ReviewResultVo> getResultByUserId(String userId);

    void removeRecord(String resultId);

    Workbook getExportWorkbook(QuestionAnswerVo questionAnswerVo);
}
