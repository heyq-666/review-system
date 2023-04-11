package com.review.front.msgManage.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author javabage
 * @date 2022/7/22
 */
@Data
public class WxSubscribeMsg {
    // 接收人id
    private String touser;
    // 模板id
    private String template_id;
    // 跳转小程序
    private String page;
    // 模板数据
    private Map<String, WxTemplateValue> data;
    // 跳转小程序类型 默认正式版
    private String miniprogram_state;
    // 语言类型 默认中文
    private String lang = "zh_CN";

    //模板内容
    private String visitDate;
    private String beginTime;
    private String endTime;
    private String expertName;

    private String consulId;
    private String mobilePhone;

    private String userId;
}
