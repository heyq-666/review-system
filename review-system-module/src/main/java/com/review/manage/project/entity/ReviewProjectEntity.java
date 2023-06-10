package com.review.manage.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;
import java.util.Date;

/**
 * @author javabage
 * @date 2023/2/15
 */
@Data
@TableName("review_project")
@ApiModel(value="review_project对象", description="项目表")
public class ReviewProjectEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目描述")
    private String projectDesc;

    @ApiModelProperty(value = "是否显示报告")
    private Integer showReport;

    @ApiModelProperty(value = "项目封面")
    private String cover;

    @ApiModelProperty(value = "是否开放项目")
    private Integer isOpen;

    @Dict(dicCode = "group")
    @ApiModelProperty(value = "用户组id")
    private String groupId;

    @ApiModelProperty(value = "appletsQrCodeLink")
    private String appletsQrCodeLink;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "首页是否显示咨询师列表")
    private Integer showExpert;

    /*private transient List<String> classIds;*/

    private transient String classIds;
}
