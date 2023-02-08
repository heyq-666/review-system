package com.review.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.entity.ReviewQuestion;
import com.review.manage.mapper.ReviewQuestionMapper;
import com.review.manage.service.IReviewQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 问题列表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Service
public class ReviewQuestionServiceImpl extends ServiceImpl<ReviewQuestionMapper, ReviewQuestion> implements IReviewQuestionService {
	
	@Autowired
	private ReviewQuestionMapper reviewQuestionMapper;
	
	@Override
	public List<ReviewQuestion> selectByMainId(String mainId) {
		return reviewQuestionMapper.selectByMainId(mainId);
	}

	@Override
	public Integer getMaxQuestionId(String classId) {
		Integer questionNum = reviewQuestionMapper.getMaxQuestionId(classId);
		if (questionNum == null){
			questionNum = 1;
		}else {
			questionNum++;
		}
		return questionNum;
	}
}
