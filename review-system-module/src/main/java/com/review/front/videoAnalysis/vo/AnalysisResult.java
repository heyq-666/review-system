package com.review.front.videoAnalysis.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 心理健康分析结果
 */
@Data
public class AnalysisResult implements Serializable {

    private boolean ret;

    private String token;

    private Long tokenExpiryTime;

    private ResultObj result;

    private String ErrorCode;

    @Data
    public static class ResultObj implements Serializable {
    }
}
