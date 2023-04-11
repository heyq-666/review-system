package com.review.common;

import org.jeecg.common.system.util.ResourceUtil;

public class VideoAnalysisConfig {

    public final static String PID = ResourceUtil.getConfigByName("qd_pid");

    public final static String APP_ID = ResourceUtil.getConfigByName("qd_appid");

    public final static String SECURE_KEY = ResourceUtil.getConfigByName("qd_securekey");

    public final static String END_POINT = ResourceUtil.getConfigByName("qd_endpoint");

    public final static String OSS_AUTH_URL = END_POINT + "/healthPreAnalysis";

    public final static String HEALTH_ANALYSIS_URL = END_POINT + "/healthAnalysis";

    public final static String EMO_ANALYSIS_URL = END_POINT + "/emoAnalysis";

    public final static String CALLBACK_URI = ResourceUtil.getConfigByName("qd_callback_uri");

}
