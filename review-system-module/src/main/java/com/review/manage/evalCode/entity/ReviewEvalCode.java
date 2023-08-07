package com.review.manage.evalCode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: review_eval_code
 * @Author: jeecg-boot
 * @Date:   2023-07-28
 * @Version: V1.0
 */
@Data
@TableName("review_eval_code")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="review_eval_code对象", description="review_eval_code")
public class ReviewEvalCode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private String userId;
	/**测评码*/
	@Excel(name = "测评码", width = 15)
    @ApiModelProperty(value = "测评码")
    private String evalCode;
	/**测评码价格*/
	@Excel(name = "测评码价格", width = 15)
    @ApiModelProperty(value = "测评码价格")
    private BigDecimal price;
	/**测评码来源(1:线下导入；2:用户购买)*/
	@Excel(name = "测评码来源(1:线下导入；2:用户购买)", width = 15)
    @ApiModelProperty(value = "测评码来源(1:线下导入；2:用户购买)")
    private Integer source;
	/**测评码状态(1:未使用；2:已使用；3:已购买；4：已删除)*/
	@Excel(name = "测评码状态(1:未使用；2:已使用；3:已购买；4：已删除)", width = 15, dicCode = "review_eval_code_status")
	@Dict(dicCode = "review_eval_code_status")
    @ApiModelProperty(value = "测评码状态(1:未使用；2:已使用；3:已购买；4：已删除)")
    private Integer status;
	/**操作时间*/
	@Excel(name = "操作时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;
	/**测评人*/
	@Excel(name = "测评人", width = 15)
    @ApiModelProperty(value = "测评人")
    private String userName;
	/**返回信息*/
	@Excel(name = "返回信息", width = 15)
    @ApiModelProperty(value = "返回信息")
    private String msg;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;
}
