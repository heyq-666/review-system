package com.review.front.videoAnalysis.vo;

/**
 * 分析类型
 */
public enum AnalysisType {
    Health(1),
    Emo(2);
    private int value;

    AnalysisType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}