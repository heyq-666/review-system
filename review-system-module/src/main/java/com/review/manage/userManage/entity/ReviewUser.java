package com.review.manage.userManage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 测评用户
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Data
@TableName("review_user")
@Accessors(chain = true)
@ApiModel(value="review_user对象", description="测评用户")
public class ReviewUser implements Serializable {
    private static final long serialVersionUID = 1L;

	/**userId*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "userId")
    private String userId;
	/**idCard*/
	//@Excel(name = "idCard", width = 15)
    @ApiModelProperty(value = "idCard")
    private String idCard;
	/**realName*/
	@Excel(name = "真实姓名", width = 15)
    @ApiModelProperty(value = "realName")
    private String realName;
    /**userName*/
    @Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "userName")
    private String userName;
	/**age*/
	@Excel(name = "年龄", width = 15)
    @ApiModelProperty(value = "age")
    private Integer age;
	/**sex*/
	@Excel(name = "性别", width = 15)
    @Dict(dicCode = "sex")
    @ApiModelProperty(value = "sex")
    private String sex;
	/**mobilePhone*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "mobilePhone")
    private String mobilePhone;
	/**password*/
	//@Excel(name = "password", width = 15)
    @ApiModelProperty(value = "password")
    private String password;
	/**userType*/
	//@Excel(name = "userType", width = 15)
    @ApiModelProperty(value = "userType")
    private String userType;
	/**微信openid*/
	@Excel(name = "微信openid", width = 15)
    @ApiModelProperty(value = "微信openid")
    private String openid;
	/**用户组ID*/
	@Excel(name = "用户组ID", width = 15)
    @ApiModelProperty(value = "用户组ID")
    private String groupId;
	/**用户来源(1:系统后台新增；2:系统批量导入；3:前端注册)*/
	//@Excel(name = "用户来源(1:系统后台新增；2:系统批量导入；3:前端注册)", width = 15)
    @ApiModelProperty(value = "用户来源(1:系统后台新增；2:系统批量导入；3:前端注册)")
    private Integer source;
	/**创建时间*/
	//@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    //@DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改时间*/
	//@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
	/**扩展字段*/
	//@Excel(name = "扩展字段", width = 15)
    @ApiModelProperty(value = "扩展字段")
    private String extra;
    /**部门名称*/
    @Excel(name = "用户组", width = 15)
    private transient String departName;
}
