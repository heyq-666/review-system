package org.jeecg.modules.base.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.modules.base.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: BaseCommonMapper
 * @author: jeecg-boot
 */
public interface BaseCommonMapper {

    /**
     * 保存日志
     * @param dto
     */
    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void saveLog(@Param("dto")LogDTO dto);

    ReviewUser getUserInfoByUserId(@Param("userId")String userId);

    SysTenantVO getSysTenantInfo(@Param("id")Long tenantId);

    SysTenantVO getTenantIdByAppId(@Param("appId")String appId);

    ReviewProjectEntity getProjectInfo(@Param("projectId")Long projectId);

    SysTenantVO getTenantInfoByTenantId(@Param("tenantId")String tenantId);

    List<ReviewClass> getReviewClass(@Param("classId")String classId);

    List<ReviewOrder> userBuy(Map map);
}
