package org.jeecg.modules.base.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.modules.base.entity.ReviewUser;
import org.jeecg.modules.base.entity.SysTenantVO;

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
}
