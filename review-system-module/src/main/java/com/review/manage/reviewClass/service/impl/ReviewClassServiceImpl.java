package com.review.manage.reviewClass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.mapper.ReviewClassMapper;
import com.review.manage.reviewClass.mapper.ReviewQuestionMapper;
import com.review.manage.reviewClass.service.IReviewClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Collection;

/**
 * @Description: 测评量表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
@Service
public class ReviewClassServiceImpl extends ServiceImpl<ReviewClassMapper, ReviewClass> implements IReviewClassService {

	@Autowired
	private ReviewClassMapper reviewClassMapper;
	@Autowired
	private ReviewQuestionMapper reviewQuestionMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		reviewQuestionMapper.deleteByMainId(id);
		reviewClassMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			reviewQuestionMapper.deleteByMainId(id.toString());
			reviewClassMapper.deleteById(id);
		}
	}

	@Override
	public void reviewStop(String id) {
		reviewClassMapper.reviewStopById(id);
	}

	@Override
	public void reviewPublish(String id) {
		reviewClassMapper.reviewPublishById(id);
	}

}
