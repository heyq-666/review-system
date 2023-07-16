package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.entity.SysUserTenant;
import org.jeecg.modules.system.mapper.SysRoleMapper;
import org.jeecg.modules.system.mapper.SysUserRoleMapper;
import org.jeecg.modules.system.mapper.SysUserTenantMapper;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysUserTenantMapper sysUserTenantMapper;

    @Override
    public void saveUserRoleByTenant(SysUserRole sysUserRole) {
        //保存用户角色关联
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>();
        queryWrapper.eq("role_id", sysUserRole.getRoleId()).eq("user_id",sysUserRole.getUserId());
        SysUserRole one = this.getOne(queryWrapper);

        if (one == null) {
            this.save(sysUserRole);
        }

        SysRole role = roleMapper.selectById(sysUserRole.getRoleId());
        if (role.getTenantId() != -1) {

            SysUser sysUser = new SysUser();
            sysUser.setId(sysUserRole.getUserId());
            sysUser.setTenantId(role.getTenantId());
            UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", sysUserRole.getUserId());
            sysUserService.update(sysUser, updateWrapper);

            //查询用户租户关联
            QueryWrapper<SysUserTenant> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("user_id", sysUserRole.getUserId()).eq("tenant_id", role.getTenantId());
            SysUserTenant sysUserTenant = sysUserTenantMapper.selectOne(queryWrapper1);

            //如果没有 就插入一条
            if (sysUserTenant == null) {
                SysUserTenant sysUserTenant1 = new SysUserTenant();
                sysUserTenant1.setTenantId(role.getTenantId());
                sysUserTenant1.setUserId(sysUserRole.getUserId());
                sysUserTenant1.setStatus(CommonConstant.STATUS_1);
                sysUserTenantMapper.insert(sysUserTenant1);
            }
        }
    }
}
