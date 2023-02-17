package com.review.manage.reviewClass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.reviewClass.entity.ReviewQuestion;

import java.util.List;

/**
 * @Description: 问题列表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface IReviewQuestionService extends IService<ReviewQuestion> {

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId
   * @return List<ReviewQuestion>
   */
	public List<ReviewQuestion> selectByMainId(String mainId);

    /**
     * 查找该量表题目中最大的题目序号
     * @param classId
     * @return
     */
    Integer getMaxQuestionId(String classId);
}
