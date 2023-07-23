package com.review.manage.variate.vo;

import com.review.manage.variate.entity.ReviewGradeRuleEntity;
import com.review.manage.variate.entity.ReviewVariateGradeEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yourname
 * 
 */
@Data
@ApiModel(value="review_variate对象", description="因子列表")
public class VariateVO {

	private String variateId;

	private String classId;

	private String variateName;

	private String ruleExplain;

	private String questionIds;

	private String createTime;

	private String curGradeId;

	private String createBy;

	private Integer variateNum;

	private Double calTotal;

	private String calSymbol;

	private Double calTotal1;

	private String calSymbol1;

	private Double grade;

	private List<ReviewVariateGradeEntity> variateGradeList = new ArrayList<ReviewVariateGradeEntity>();

	private List<VariateVO> variateList = new ArrayList<VariateVO>();

	private List<ReviewGradeRuleEntity> gradeRuleList = new ArrayList<ReviewGradeRuleEntity>();

    private Integer sortNum;

	private List<GradeAllVo> gradeAllRule = new ArrayList<>();

	private String variateGradeConf;
} 