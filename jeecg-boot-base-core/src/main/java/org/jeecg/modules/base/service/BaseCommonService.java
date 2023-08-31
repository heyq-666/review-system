package org.jeecg.modules.base.service;

import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.base.entity.ReviewClass;
import org.jeecg.modules.base.entity.ReviewProjectEntity;
import org.jeecg.modules.base.entity.ReviewUser;
import org.jeecg.modules.base.entity.SysTenantVO;

import java.util.List;

/**
 * common接口
 * @author: jeecg-boot
 */
public interface BaseCommonService {

    /**
     * 保存日志
     * @param logDTO
     */
    void addLog(LogDTO logDTO);

    /**
     * 保存日志
     * @param logContent
     * @param logType
     * @param operateType
     * @param user
     */
    void addLog(String logContent, Integer logType, Integer operateType, LoginUser user);

    /**
     * 保存日志
     * @param logContent
     * @param logType
     * @param operateType
     */
    void addLog(String logContent, Integer logType, Integer operateType);

    /**
     * 通过userId获取用户信息
     * @param userId
     * @return
     */
    ReviewUser getUserInfoByUserId(String userId);

    /**
     * 通过租户ID获取租户信息
     * @param tenantId
     * @return
     */
    SysTenantVO getSysTenantById(Long tenantId);

    /**
     * 检查租户是否过期/删除
     * @param tenantId
     * @return
     */
    boolean checkSysTenant(Long tenantId);

    /**
     * 通过小程序appid获取租户id
     * @param appId
     * @return
     */
    SysTenantVO getTenantIdByAppId(String appId);

    /**
     * 获取测评项目信息
     * @param projectId
     * @return
     */
    ReviewProjectEntity getProjectInfo(Long projectId);

    SysTenantVO getTenantInfoByTenantId(String tenantId);

    List<ReviewClass> getReviewClass(String classId);

    boolean userBuy(String classId, String userId);
}
