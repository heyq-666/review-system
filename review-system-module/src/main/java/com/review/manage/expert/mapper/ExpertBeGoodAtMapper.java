package com.review.manage.expert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.review.manage.expert.entity.ExpertBeGoodAt;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

/**
 * @author javabage
 * @date 2023/5/17
 */
public interface ExpertBeGoodAtMapper extends BaseMapper<ExpertBeGoodAt> {
    @Select("SELECT count(*) FROM expert_be_good_at where del_flag ='0' AND parent_id = #{parentId,jdbcType=VARCHAR}")
    Integer queryCountByPid(@Param("parentId")String parentId);
}
