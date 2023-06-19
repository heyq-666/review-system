package com.review.manage.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.review.front.frontAppoint.vo.BeGoodAt;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: review_expert
 * @Author: jeecg-boot
 * @Date:   2023-03-10
 * @Version: V1.0
 */
@Data
@TableName("review_expert")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="review_expert对象", description="review_expert")
public class ReviewExpert implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**专家姓名*/
	@Excel(name = "专家姓名", width = 15)
    @ApiModelProperty(value = "专家姓名")
    private String expertName;
	/**性别*/
	@Excel(name = "性别", width = 15, dicCode = "sex")
	@Dict(dicCode = "sex")
    @ApiModelProperty(value = "性别")
    private Integer sex;
	/**年龄*/
	@Excel(name = "年龄", width = 15)
    @ApiModelProperty(value = "年龄")
    private Integer age;
	/**关联测评用户id*/
	@Excel(name = "关联测评用户id", width = 15)
    @ApiModelProperty(value = "关联测评用户id")
    private String userId;
	/**专家手机号*/
	@Excel(name = "专家手机号", width = 15)
    @ApiModelProperty(value = "专家手机号")
    private String mobilePhone;
	/**职称*/
	@Excel(name = "职称", width = 15)
    @ApiModelProperty(value = "职称")
    private String jobTitle;
	/**专家介绍*/
	@Excel(name = "专家介绍", width = 15)
    @ApiModelProperty(value = "专家介绍")
    private String introduction;
	/**工作机构名称*/
	@Excel(name = "工作机构名称", width = 15)
    @ApiModelProperty(value = "工作机构名称")
    private String workOrgName;
	/**专家标签*/
	@Excel(name = "专家标签", width = 15)
    @ApiModelProperty(value = "专家标签")
    private String label;
	/**头像*/
	@Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
    private String avatar;
	/**创建时间*/
	//@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    //@DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新时间*/
	//@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    //@DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String creator;
	/**状态(0:未发布；1:已发布)*/
    @Dict(dicCode = "send_status")
	@Excel(name = "状态(0:未发布；1:已发布)", width = 15)
    @ApiModelProperty(value = "状态(0:未发布；1:已发布)")
    private Integer status;
	/**专家排名*/
	@Excel(name = "专家排名", width = 15)
    @ApiModelProperty(value = "专家排名")
    private Integer sortNum;
	/**是否收费(0:免费；1:收费)*/
	@Excel(name = "是否收费(0:免费；1:收费)", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否收费(0:免费；1:收费)")
    private Integer charge;
	/**原始价格*/
	@Excel(name = "原始价格", width = 15)
    @ApiModelProperty(value = "原始价格")
    private BigDecimal orgPrice;
	/**优惠价格*/
	@Excel(name = "优惠价格", width = 15)
    @ApiModelProperty(value = "优惠价格")
    private BigDecimal dicountPrice;
    /**擅长领域-群体*/
    @Excel(name = "擅长领域-群体", width = 15)
    @ApiModelProperty(value = "擅长领域-群体")
    private String expertFieldGroup;

    @Excel(name = "擅长方向-情绪压力", width = 15)
    @ApiModelProperty(value = "擅长方向-情绪压力")
    private String beGoodAtEmotion;

    @Excel(name = "擅长方向-人际关系", width = 15)
    @ApiModelProperty(value = "擅长方向-人际关系")
    private String beGoodAtRelation;

    @Excel(name = "擅长方向-家庭困扰", width = 15)
    @ApiModelProperty(value = "擅长方向-家庭困扰")
    private String beGoodAtFamilyTrouble;

    @Excel(name = "擅长方向-婚姻恋爱", width = 15)
    @ApiModelProperty(value = "擅长方向-婚姻恋爱")
    private String beGoodAtMarriage;

    @Excel(name = "咨询师线下预约地址", width = 15)
    @ApiModelProperty(value = "咨询师线下预约地址")
    private String offlineReservationAddress;

    private transient List<BeGoodAt> beGoodAtList;
}
