package com.review.manage.userManage.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * @author javabage
 * @date 2023/2/28
 */
@Data
public class ReviewResultVo {
    private String userId;
    private String resultId;
    private String title;
    private Date createTime;
    private String resultExplain;
    private double totalGrade;
}
