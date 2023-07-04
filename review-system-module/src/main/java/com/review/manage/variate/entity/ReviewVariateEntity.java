package com.review.manage.variate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author yourname
 * 
 */
@Data
@TableName("review_variate")
@ApiModel(value="review_variate对象", description="因子列表")
public class ReviewVariateEntity implements Serializable {


	private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_UUID)
	@ApiModelProperty(value = "主键")
	private String variateId;

	@ApiModelProperty(value = "量表Id")
 	private String classId;

	@ApiModelProperty(value = "因子名称")
    private String variateName;

	@ApiModelProperty(value = "calSymbol")
    private String calSymbol;

	@ApiModelProperty(value = "calTotal")
    private Double calTotal;

	@ApiModelProperty(value = "calSymbol1")
    private String calSymbol1;

	@ApiModelProperty(value = "calTotal1")
    private Double calTotal1;

	@ApiModelProperty(value = "curGradeId")
    private String curGradeId;

	@ApiModelProperty(value = "创建时间")
    private Date createTime;

	@ApiModelProperty(value = "创建人")
    private String createBy;

	@ApiModelProperty(value = "序号")
    private Integer sortNum;

	private transient String className;

	/**
	 * 租户ID
	 */
	private Long tenantId;
} 