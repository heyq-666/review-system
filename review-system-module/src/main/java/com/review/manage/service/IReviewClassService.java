package com.review.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.entity.ReviewClass;

import java.io.Serializable;
import java.util.Collection;

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
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


	void reviewStop(String id);

	void reviewPublish(String id);
}
