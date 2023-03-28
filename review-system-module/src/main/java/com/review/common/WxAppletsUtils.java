package com.review.common;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信小程序工具类
 * @author javabage
 * @date 2023/3/24
 */
public class WxAppletsUtils {

    public final static String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    public final static String appId = "wx3d03d72c4fc3614f";

    public final static String appSecret = "f95e7676a11f1fbcd2be34f8ea01fed5";

    public final static String qrCodeUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

    public static String geneAppletsQrCode(String pagePath, String params) {
        String accessToken = geneAccessToken();
        JSONObject paramObj = new JSONObject();
        paramObj.put("scene", params);
        paramObj.put("page", pagePath);
        paramObj.put("width", 200);
        paramObj.put("is_hyaline", true);
        paramObj.put("auto_color", true);
        String filePath = "qrcode/%s/" + params.split("=")[1] + "_" + System.currentTimeMillis() + ".jpg";
        return HttpUtils.postFile(String.format(qrCodeUrl, accessToken), paramObj.toString(), filePath);
    }

    private static String geneAccessToken() {
        String tokenStr = HttpUtils.postString(String.format(accessTokenUrl, appId, appSecret), "");
        JSONObject tokenObj = JSONObject.parseObject(tokenStr);
        if (tokenObj.containsKey("access_token")) {
            return tokenObj.getString("access_token");
        }
        return null;
    }

}
