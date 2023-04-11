package com.review.front.dongliangReview.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.dongliangReview.entity.EvalCodeEntity;
import com.review.front.dongliangReview.vo.DongliangTestQuestionVO;
import com.review.manage.userManage.entity.ReviewUser;
import org.jeecg.common.util.oConvertUtils;

/**
 * @author javabage
 * @date 2023/4/4
 */
public interface IDongLiangReviewService extends IService<EvalCodeEntity> {

    /**
     * 答题数据处理
     * @param dongliangTestQuestionVO
     */
    void handleData(DongliangTestQuestionVO[] dongliangTestQuestionVO);

    /**
     * 业务数据处理
     * @param flag
     * @param currentTime
     * @param toString
     * @param dongliangTestQuestionVO
     * @param reviewUser
     */
    void handleBusinessData(Integer flag, String currentTime, String toString,
                            DongliangTestQuestionVO[] dongliangTestQuestionVO, ReviewUser reviewUser);

}
