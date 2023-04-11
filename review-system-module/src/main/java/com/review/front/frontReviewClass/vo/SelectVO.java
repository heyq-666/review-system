package com.review.front.frontReviewClass.vo;

import lombok.Data;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Data
public class SelectVO {

	private String selectId;
	
	private String selCode;
    
    private String selectContent;
    
    private String selectGrade;
    
    private String isAttach;

    private String pictureAttach;

	private CommonsMultipartFile contentImg;
}
