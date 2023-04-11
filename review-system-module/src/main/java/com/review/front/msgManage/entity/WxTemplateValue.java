package com.review.front.msgManage.entity;

/**
 * @author javabage
 * @date 2022/7/22
 */
public class WxTemplateValue {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "WxTemplateValue{" +
                "value='" + value + '\'' +
                '}';
    }
}
