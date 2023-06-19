package com.review.front.frontAppoint.vo;

import lombok.Data;

/**
 * @author javabage
 * @date 2023/6/19
 */
@Data
public class VisitDate {

    private Long id;
    private Long expertId;
    private Integer status;
    private String visitDateNew;
    private String time;
    private Boolean isChooseFlag;
}
