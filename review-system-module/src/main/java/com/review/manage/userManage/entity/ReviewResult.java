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
@TableName("review_result")
@Accessors(chain = true)
@ApiModel(value="review_result对象", description="测评记录")
public class ReviewResult implements Serializable {
    private static final long serialVersionUID = 1L;

	/**result_id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "resultId")
    private String resultId;
	/**user_id*/
	//@Excel(name = "user_id", width = 15)
    @ApiModelProperty(value = "userId")
    private String userId;
	/**grade_total*/
	@Excel(name = "grade_total", width = 15)
    @ApiModelProperty(value = "gradeTotal")
    private double gradeTotal;
    /**class_id*/
    @Excel(name = "class_id", width = 15)
    @ApiModelProperty(value = "classId")
    private String classId;
	/**review_result*/
	@Excel(name = "review_result", width = 15)
    @ApiModelProperty(value = "reviewResult")
    private String reviewResult;
	/**create_time*/
	@Excel(name = "create_time", width = 15)
    @ApiModelProperty(value = "createTime")
    private Date createTime;
	/**create_by*/
	@Excel(name = "create_by", width = 15)
    @ApiModelProperty(value = "createBy")
    private String createBy;
	/**projectId*/
	//@Excel(name = "password", width = 15)
    @ApiModelProperty(value = "projectId")
    private Long projectId;
	/**levelGrade*/
	//@Excel(name = "userType", width = 15)
    @ApiModelProperty(value = "levelGrade")
    private Integer levelGrade;
	/**combine_result*/
	@Excel(name = "combine_result", width = 15)
    @ApiModelProperty(value = "combineResult")
    private String combineResult;
	/**group_id*/
	@Excel(name = "group_id", width = 15)
    @ApiModelProperty(value = "groupId")
    private String groupId;

    /**
     * 租户ID
     */
    private Long tenantId;
}
