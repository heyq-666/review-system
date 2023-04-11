package com.review.front.videoAnalysis.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * oss临时授权结果
 */
@Data
public class OssTmpAuth implements Serializable {

    private Boolean ret;

    private AuthResult result;

    private String token;

    private Long tokenExpiryTime;

    @Data
    public static class AuthResult  implements Serializable{
        private String ossEndPoint;
        private String ossBucket;
        private String ossObjName;

        private JSONObject ossCredentials;
    }
}
