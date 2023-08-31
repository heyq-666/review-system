package org.jeecg.modules.base.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;
import java.util.Date;

/**
 * 租户信息
 * @author: jeecg-boot
 */
@Data
@TableName("sys_tenant")
public class SysTenantVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    private Long id;
    
    /**
     * 名称
     */
    private String name;
    

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 状态 1正常 0冻结
     */
    @Dict(dicCode = "tenant_status")
    private Integer status;

    /**
     * 所属行业
     */
    @Dict(dicCode = "trade")
    private String trade;

    /**
     * 公司规模
     */
    @Dict(dicCode = "company_size")
    private String companySize;

    /**
     * 公司地址
     */
    private String companyAddress;

    /**
     * 公司logo
     */
    private String companyLogo;

    /**
     * 门牌号
     */
    private String houseNumber;

    /**
     * 工作地点
     */
    private String workPlace;

    /**
     * 二级域名(暂时无用,预留字段)
     */
    private String secondaryDomain;

    /**
     * 登录背景图片(暂时无用，预留字段)
     */
    private String loginBkgdImg;

    /**
     * 职级
     */
    @Dict(dicCode = "company_rank")
    private String position;

    /**
     * 部门
     */
    @Dict(dicCode = "company_department")
    private String department;
    
    @TableLogic
    private Integer delFlag;

    /**更新人登录名称*/
    private String updateBy;
    
    /**更新日期*/
    private Date updateTime;

    /**
     * 允许申请管理员 1允许 0不允许
     */
    private Integer applyStatus;

    /**
     * 小程序二维码路径
     */
    private String appletsQrCodePath;

    private String appId;

    private String appSecret;

    private String mchId;

    private String payKey;
    
}
