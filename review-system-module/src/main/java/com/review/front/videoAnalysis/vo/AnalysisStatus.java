package com.review.front.videoAnalysis.vo;

public enum AnalysisStatus {

    Success(1),
    Failed(2),
    Analysising(3),
    Error(4);

    private int value;
    AnalysisStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
