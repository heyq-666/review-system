package org.jeecg.common.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.system.vo.OpenIdJson;
import org.jeecg.modules.base.entity.SysTenantVO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序工具类
 * @author javabage
 * @date 2023/3/24
 */
public class WxAppletsUtils {

    public final static String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    public final static String appId = "wx5c0cb023244b6007";//沃莲纪
    //public final static String appId = "wxe1e9802d7d62c6d4";//zxk

    //public final static String appSecret = "f95e7676a11f1fbcd2be34f8ea01fed5";
    //public final static String appSecret = "994d504be2dc39634df03fe9889701b7";
    public final static String appSecret = "53b3c8f98a8b8e6351a0f23f028f5eb0";//沃莲纪
    //public final static String appSecret = "f8ce119deb600c324de09ced464272ce";//zxk

    public final static String qrCodeUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

    public final static String openidUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&grant_type=authorization_code&&js_code=%s";

    //public final static String payKey = "xinzhaitongxing18510801311paykey";
    public final static String payKey = "wolianji18510801311wolianji01311";
    //public final static String payKey = "Zxkkj111111111111111111111111111";

    //public final static String mchID = "1635457862";

    public final static String mchID = "1647976610";

    public final static String notifyUrl = "https://wlj.xinzhaitongxing.com/review/reviewFront/order/wxPayNotify";

    public final static String tradeType = "JSAPI";

    public final static String prePayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public final static String prePayParamFormat = "<xml><appid>%s</appid>"
            + "<body><![CDATA[%s]]></body>"
            + "<mch_id>%s</mch_id>"
            + "<nonce_str>%s</nonce_str>"
            + "<notify_url>%s</notify_url>"
            + "<openid>%s</openid>"
            + "<out_trade_no>%s</out_trade_no>"
            + "<spbill_create_ip>%s</spbill_create_ip>"
            + "<total_fee>%s</total_fee>"
            + "<trade_type>%s</trade_type>"
            + "<sign>%s</sign>"
            + "</xml>";

    public static String geneAppletsQrCode(String pagePath, String params) {
        String accessToken = geneAccessToken();
        JSONObject paramObj = new JSONObject();
        paramObj.put("scene", params);
        paramObj.put("page", pagePath);
        paramObj.put("width", 200);
        paramObj.put("is_hyaline", true);
        paramObj.put("auto_color", true);
        //String filePath = "qrcode/%s/" + params.split("=")[1] + "_" + System.currentTimeMillis() + ".jpg";
        String filePath = "qrcode/%s/" + params.split("/")[1] + "_" + System.currentTimeMillis() + ".jpg";
        return HttpUtils.postFile(String.format(qrCodeUrl, accessToken), paramObj.toString(), filePath);
    }

    public static String geneAccessToken() {
        String tokenStr = HttpUtils.postString(String.format(accessTokenUrl, appId, appSecret), "");
        JSONObject tokenObj = JSONObject.parseObject(tokenStr);
        if (tokenObj.containsKey("access_token")) {
            return tokenObj.getString("access_token");
        }
        return null;
    }

    /**
     * 获取openid
     * @param code
     * @return
     */
    /*public static String getOpenid(String code) {
        String responseStr = HttpUtils.postString(String.format(openidUrl, appId, appSecret, code), "");
        if (StringUtils.isNotBlank(responseStr)) {
            JSONObject resJson = JSONObject.parseObject(responseStr);
            if (resJson.containsKey("openid")) {
                return resJson.getString("openid");
            }
        }
        return null;
    }*/

    public static String getOpenid(String code) throws JsonProcessingException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        SysTenantVO sysTenantVO = (SysTenantVO) request.getSession().getAttribute("appSession");
        String result = "";
        Map<String,String> map = new HashMap();
        if (sysTenantVO != null && StringUtils.isNotEmpty(sysTenantVO.getAppId()) && StringUtils.isNotEmpty(sysTenantVO.getAppSecret())){
            map.put("appid",sysTenantVO.getAppId());
            map.put("secret",sysTenantVO.getAppSecret());
        } else {
            map.put("appid",appId);
            map.put("secret",appSecret);
        }
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        result = HttpClientUtils.doGet("https://api.weixin.qq.com/sns/jscode2session",map,null);
        ObjectMapper mapper = new ObjectMapper();
        OpenIdJson openIdJson = mapper.readValue(result,OpenIdJson.class);
        return openIdJson.getOpenid();
    }

    /**
     * 生成支付签名
     * @param nonce_str
     * @param prepay_id
     * @param timeStamp
     * @return
     */
    public static String paySign(String nonce_str, String prepay_id, long timeStamp) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        SysTenantVO sysTenantVO = (SysTenantVO) request.getSession().getAttribute("appSession");
        if (sysTenantVO != null && StringUtils.isNotEmpty(sysTenantVO.getAppId()) && StringUtils.isNotEmpty(sysTenantVO.getAppSecret())){
            String stringSignTemp = String.format("appId=%s&nonceStr=%s&package=prepay_id=%s&signType=MD5&timeStamp=%s",
                    sysTenantVO.getAppId(), nonce_str, prepay_id, timeStamp);
            //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
            return PayUtils.sign(stringSignTemp, sysTenantVO.getPayKey(), "utf-8").toUpperCase();
        }else {
            String stringSignTemp = String.format("appId=%s&nonceStr=%s&package=prepay_id=%s&signType=MD5&timeStamp=%s",
                    appId, nonce_str, prepay_id, timeStamp);
            return PayUtils.sign(stringSignTemp, payKey, "utf-8").toUpperCase();
        }
    }

    public static String geneAppletsQrCodeTenant(String pagePath, String params, List<Map<String, String>> list) {
        String[] sss = params.split("\\*");
        String ss1 = sss[0];
        String ss2 = sss[1];
        String accessToken = geneAccessTenant(list);
        JSONObject paramObj = new JSONObject();
        paramObj.put("scene", params);
        paramObj.put("page", pagePath);
        paramObj.put("width", 200);
        paramObj.put("is_hyaline", true);
        paramObj.put("auto_color", true);
        String filePath = "qrcode/%s/" + ss1.split("/")[1] + "_" + ss2.split("/")[1] + "_" + System.currentTimeMillis() + ".jpg";
        return HttpUtils.postFile(String.format(qrCodeUrl, accessToken), paramObj.toString(), filePath);
    }

    private static String geneAccessTenant(List<Map<String, String>> list) {
        String tokenStr = "";
        if (list != null && list.size() > 0) {
            String appId = list.get(0).get("appId");
            String appSecret = list.get(0).get("appSecret");
            if (StringUtils.isNotEmpty(appId) && StringUtils.isNotEmpty(appSecret)) {
                tokenStr = HttpUtils.postString(String.format(accessTokenUrl, appId, appSecret), "");
            }
        }else {
            tokenStr = HttpUtils.postString(String.format(accessTokenUrl, appId, appSecret), "");
        }
        JSONObject tokenObj = JSONObject.parseObject(tokenStr);
        if (tokenObj.containsKey("access_token")) {
            return tokenObj.getString("access_token");
        }
        return null;
    }
}
