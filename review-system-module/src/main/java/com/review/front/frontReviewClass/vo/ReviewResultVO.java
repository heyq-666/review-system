package com.review.front.frontReviewClass.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReviewResultVO implements Serializable {

	private String classId;

	private String classTitle;

	private String createTime;

	private String classCover;

	private String resultId;
	
	private String reportName;
	
	private String reportResult;
	
	private String reportGrade;

	private String userId;

	private String idCard;

	private String userName;

	private String realName;

	private String combineVarResult;

	private Integer levelGrade;

	private Long projectId;

	private Double gradeTotal;

	@JsonProperty(value="pCount")
	private Long pCount;

	private String title;

	private String reviewResult;

	private Integer limitId;
}
