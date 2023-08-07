package com.review.manage.reviewClass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.reviewClass.entity.ReviewClass;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 测评量表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface IReviewClassService extends IService<ReviewClass> {

	/**
	 * 删除一对多
	 *
	 * @param classId
	 */
	public void delMain (String classId);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

    List<String> getClassIds(Long tenantId);

    //void updateByClassId(ReviewClass reviewClass);
}
