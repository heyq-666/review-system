package com.review.manage.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.review.manage.entity.ReviewQuestion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 测评量表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Data
@ApiModel(value="review_classPage对象", description="测评量表")
public class ReviewClassPage {

	/**主键*/
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
    @Dict(dicCode = "yn")
	@ApiModelProperty(value = "是否计费")
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
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
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

	@ExcelCollection(name="问题列表")
	@ApiModelProperty(value = "问题列表")
	private List<ReviewQuestion> reviewQuestionList;

}
