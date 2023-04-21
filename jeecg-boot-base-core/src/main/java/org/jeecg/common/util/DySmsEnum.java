package org.jeecg.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 短信枚举类
 * @author: jeecg-boot
 */
public enum DySmsEnum {

    /**登录短信模板编码*/
	LOGIN_TEMPLATE_CODE("SMS_175435174","JEECG","code"),
    /**忘记密码短信模板编码*/
	FORGET_PASSWORD_TEMPLATE_CODE("SMS_175435174","JEECG","code"),
    /**注册账号短信模板编码*/
	REGISTER_TEMPLATE_CODE("SMS_175430166","JEECG","code"),
	/**会议通知*/
	MEET_NOTICE_TEMPLATE_CODE("SMS_201480469","H5活动之家","username,title,minute,time"),
	/**我的计划通知*/
	PLAN_NOTICE_TEMPLATE_CODE("SMS_201470515","H5活动之家","username,title,time"),

	/**预约接诊通知*/
	SMS_APPOINT_RECEIVE_CODE("SMS_272080188","心宅同行心理健康服务平台","name"),
	/**视频咨询房间号*/
	VIDEO_ROOMID_CODE("SMS_272590072","心宅同行心理健康服务平台","name,roomId"),
	/**会议码通知*/
	MEETING_CODE("SMS_272635881","心宅同行心理健康服务平台","name,txNumber"),
	/**会议码通知2*/
	MEETING2_CODE("SMS_272431129","心宅同行心理健康服务平台","name,visitDate,beginTime,txNumber"),
	/**未支付通知*/
	SMS_REMINDER_PAY_CODE("SMS_272615935","心宅同行心理健康服务平台","name"),
	/**确认通知*/
	CONFIRM_NOTICE_CODE("SMS_272410720","心宅同行心理健康服务平台","name"),
	/**短信验证码*/
	SMS_VERIFICATION_CODE("SMS_259430450","心宅同行","code");
	/**
	 * 短信模板编码
	 */
	private String templateCode;
	/**
	 * 签名
	 */
	private String signName;
	/**
	 * 短信模板必需的数据名称，多个key以逗号分隔，此处配置作为校验
	 */
	private String keys;
	
	private DySmsEnum(String templateCode,String signName,String keys) {
		this.templateCode = templateCode;
		this.signName = signName;
		this.keys = keys;
	}
	
	public String getTemplateCode() {
		return templateCode;
	}
	
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	public String getSignName() {
		return signName;
	}
	
	public void setSignName(String signName) {
		this.signName = signName;
	}
	
	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public static DySmsEnum toEnum(String templateCode) {
		if(StringUtils.isEmpty(templateCode)){
			return null;
		}
		for(DySmsEnum item : DySmsEnum.values()) {
			if(item.getTemplateCode().equals(templateCode)) {
				return item;
			}
		}
		return null;
	}
}
