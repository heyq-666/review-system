package com.review.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.entity.ReviewQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 问题列表
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface ReviewQuestionMapper extends BaseMapper<ReviewQuestion> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public List<ReviewQuestion> selectByMainId(@Param("mainId") String mainId);

	Integer getMaxQuestionId(@Param("classId") String classId);
}
