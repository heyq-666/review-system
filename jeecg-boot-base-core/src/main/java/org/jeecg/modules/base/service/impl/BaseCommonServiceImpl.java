package org.jeecg.modules.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.entity.ReviewUser;
import org.jeecg.modules.base.entity.SysTenantVO;
import org.jeecg.modules.base.mapper.BaseCommonMapper;
import org.jeecg.modules.base.service.BaseCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description: common实现类
 * @author: jeecg-boot
 */
@Service
@Slf4j
public class BaseCommonServiceImpl implements BaseCommonService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private BaseCommonMapper baseCommonMapper;

    @Override
    public void addLog(LogDTO logDTO) {
        if(oConvertUtils.isEmpty(logDTO.getId())){
            logDTO.setId(String.valueOf(IdWorker.getId()));
        }
        //保存日志（异常捕获处理，防止数据太大存储失败，导致业务失败）JT-238
        try {
            logDTO.setCreateTime(new Date());
            baseCommonMapper.saveLog(logDTO);
        } catch (Exception e) {
            log.warn(" LogContent length : "+logDTO.getLogContent().length());
            log.warn(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operatetype, LoginUser user) {
        LogDTO sysLog = new LogDTO();
        sysLog.setId(String.valueOf(IdWorker.getId()));
        //注解上的描述,操作日志内容
        sysLog.setLogContent(logContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operatetype);
        try {
            //获取request
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(IpUtils.getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        //获取登录用户信息
        if(user==null){
            try {
                user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        if(user!=null){
            sysLog.setUserid(user.getUsername());
            sysLog.setUsername(user.getRealname());
        }
        sysLog.setCreateTime(new Date());
        //保存日志（异常捕获处理，防止数据太大存储失败，导致业务失败）JT-238
        try {
            baseCommonMapper.saveLog(sysLog);
        } catch (Exception e) {
            log.warn(" LogContent length : "+sysLog.getLogContent().length());
            log.warn(e.getMessage());
        }
    }

    @Override
    public void addLog(String logContent, Integer logType, Integer operateType) {
        addLog(logContent, logType, operateType, null);
    }

    @Override
    public ReviewUser getUserInfoByUserId(String userId) {
        return baseCommonMapper.getUserInfoByUserId(userId);
    }

    @Override
    public SysTenantVO getSysTenantById(Long tenantId) {
        return baseCommonMapper.getSysTenantInfo(tenantId);
    }

    @Override
    public boolean checkSysTenant(Long tenantId) {
        if (tenantId != -1) {
            SysTenantVO sysTenantVO = this.getSysTenantById(tenantId);
            if (sysTenantVO == null) {
                logger.warn("tenantIdL:{} is not exist", tenantId);
                throw new AuthenticationException("租户不存在，请联系管理员");
            }
            Date now = DateUtils.getDate();
            if (sysTenantVO.getBeginDate() != null && sysTenantVO.getEndDate() != null) {
                if (now.before(sysTenantVO.getEndDate()) && now.after(sysTenantVO.getBeginDate())) {
                    return true;
                }
                throw new AuthenticationException("租户已过期，请联系管理员");
            } else {
                logger.warn("tenantIdL:{} is expired", tenantId);
                throw new AuthenticationException("租户已过期，请联系管理员");
            }
        }
        return true;
    }

    @Override
    public SysTenantVO getTenantIdByAppId(String appId) {
        return baseCommonMapper.getTenantIdByAppId(appId);
    }
}
