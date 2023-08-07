package com.review.manage.reviewClass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.mapper.ReviewClassMapper;
import com.review.manage.reviewClass.service.IReviewClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String classId) {
		//删除量表下的题目
		reviewClassMapper.deleteByMainId(classId);
		//删除量表
		reviewClassMapper.deleteClassByClassId(classId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			reviewClassMapper.deleteByMainId(id.toString());
			reviewClassMapper.deleteById(id);
		}
	}

	@Override
	public List<String> getClassIds(Long tenantId) {
		return reviewClassMapper.getClassIds(tenantId);
	}

	/*@Override
	public void updateByClassId(ReviewClass reviewClass) {
		reviewClassMapper.updateByClassId(reviewClass);
	}*/

}
