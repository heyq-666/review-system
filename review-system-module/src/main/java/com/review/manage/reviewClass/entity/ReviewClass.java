package com.review.manage.reviewClass.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 测评量表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Data
@TableName("review_class")
@ApiModel(value="review_class对象", description="测评量表")
@Accessors(chain = true)
public class ReviewClass implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**排序ID*/
    @Excel(name = "排序ID", width = 15)
    @ApiModelProperty(value = "排序ID")
    private Integer sortId;
	/**量表名称*/
    @Excel(name = "量表名称", width = 15)
    @ApiModelProperty(value = "量表名称")
    private String title;
	/**量表简介*/
    @Excel(name = "量表简介", width = 15)
    @ApiModelProperty(value = "量表简介")
    private String classDesc;
	/**引导语*/
    @Excel(name = "引导语", width = 15)
    @ApiModelProperty(value = "引导语")
    private String guide;
	/**是否计费*/
    @Excel(name = "是否计费", width = 15, dicCode = "yn")
    @ApiModelProperty(value = "是否计费")
    @Dict(dicCode = "yn")
    private Integer charge;
	/**量表价格*/
    @Excel(name = "量表价格", width = 15)
    @ApiModelProperty(value = "量表价格")
    private java.math.BigDecimal orgPrice;
	/**优惠金额*/
    @Excel(name = "优惠金额", width = 15)
    @ApiModelProperty(value = "优惠金额")
    private java.math.BigDecimal dicountPrice;
	/**封面图片*/
    @Excel(name = "封面图片", width = 15)
    @ApiModelProperty(value = "封面图片")
    private String bannerImg;
	/**是否视频测评*/
    @Excel(name = "是否视频测评", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否视频测评")
    private Integer videoAnalysis;
	/**状态*/
    @Excel(name = "状态", width = 15, dicCode = "send_status")
    //@ApiModelProperty(value = "状态")
    @Dict(dicCode = "send_status")
    private Integer status;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**是否热门*/
    @Excel(name = "是否热门", width = 15, dicCode = "hot")
    @Dict(dicCode = "hot")
    @ApiModelProperty(value = "是否热门")
    private Integer type;
	/**报告提示语*/
    @Excel(name = "报告提示语", width = 15)
    @ApiModelProperty(value = "报告提示语")
    private String reportTips;
}
