package com.review.manage.userManage.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author javabage
 * @date 2023/6/7
 */
@Data
public class QuestionAnswerVo {

    private String groupId;
    private Long projectId;
    private String startTime;
    private String endTime;
}
