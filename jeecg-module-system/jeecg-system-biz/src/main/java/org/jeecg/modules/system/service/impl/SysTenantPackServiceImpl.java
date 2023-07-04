package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.TenantConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.aop.TenantLog;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.service.ISysTenantPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 租户产品包
 * @Author: jeecg-boot
 * @Date: 2022-12-31
 * @Version: V1.0
 */
@Service
public class SysTenantPackServiceImpl extends ServiceImpl<SysTenantPackMapper, SysTenantPack> implements ISysTenantPackService {

    @Autowired
    private SysTenantPackMapper sysTenantPackMapper;

    @Autowired
    private SysTenantPackUserMapper sysTenantPackUserMapper;

    @Autowired
    private SysPackPermissionMapper sysPackPermissionMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    private final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    @Override
    public void addPackPermission(SysTenantPack sysTenantPack) {
        //生成随机码码
        String code = RandomUtil.randomString(BASE_CHECK_CODES,6);
        sysTenantPack.setPackCode(code);

        //为该租户创建一个默认角色
        SysRole sysRole = new SysRole();
        sysRole.setRoleCode(getRoleCode(sysTenantPack));
        sysRole.setRoleName("租户产品管理员");
        sysRole.setTenantId(sysTenantPack.getTenantId());
        sysRoleMapper.insert(sysRole);

        //创建租户产品包 产品包 应该单独抽象成一个实体
        sysTenantPackMapper.insert(sysTenantPack);
        String permissionIds = sysTenantPack.getPermissionIds();
        if (oConvertUtils.isNotEmpty(permissionIds)) {
            String packId = sysTenantPack.getId();
            String[] permissionIdArray = permissionIds.split(SymbolConstant.COMMA);
            for (String permissionId : permissionIdArray) {
                //添加套餐与菜单关联权限 同时添加默认角色与
                this.addPermission(packId, permissionId, sysRole.getId(), sysTenantPack.getTenantId());

            }
        }
    }

    @Override
    public List<SysTenantPack> setPermissions(List<SysTenantPack> records) {
        for (SysTenantPack pack : records) {
            List<String> permissionIds = sysPackPermissionMapper.getPermissionsByPackId(pack.getId());
            if (null != permissionIds && permissionIds.size() > 0) {
                String ids = String.join(SymbolConstant.COMMA, permissionIds);
                pack.setPermissionIds(ids);
            }
        }
        return records;
    }

    private String getRoleCode(SysTenantPack sysTenantPack) {
        return String.format("%s_%s_admin", sysTenantPack.getPackCode(), sysTenantPack.getTenantId(), "_admin");
    }

    @Override
    public void editPackPermission(SysTenantPack sysTenantPack) {

        //线查到该产品包+租户对应的默认角色
        LambdaQueryWrapper<SysRole> roleQueryWrapper = new LambdaQueryWrapper<>();
        roleQueryWrapper.eq(SysRole::getRoleCode, getRoleCode(sysTenantPack));
        SysRole sysRole = sysRoleMapper.selectOne(roleQueryWrapper);

        //数据库汇总的id
        List<String> oldPermissionIds = sysPackPermissionMapper.getPermissionsByPackId(sysTenantPack.getId());
        //前台传过来的需要修改的id
        String permissionIds = sysTenantPack.getPermissionIds();
        //如果传过来的菜单id为空，那么就删除数据库中所有菜单
        if (oConvertUtils.isEmpty(permissionIds)) {
            this.deletePackPermission(sysTenantPack.getId(), null);
        } else if (oConvertUtils.isNotEmpty(permissionIds) && oConvertUtils.isEmpty(oldPermissionIds)) {
            //如果传过来的菜单id不为空但是数据库的菜单id为空，那么就新增
            String[] permissionIdArray = permissionIds.split(SymbolConstant.COMMA);
            for (String permissionId : permissionIdArray) {
                this.addPermission(sysTenantPack.getId(), permissionId, sysRole.getId(), sysTenantPack.getTenantId());
            }
        } else {
            //都不为空，需要比较，进行添加或删除
            if (oConvertUtils.isNotEmpty(oldPermissionIds)) {
                //找到新的租户id与原来的租户id不同之处，进行删除
                List<String> permissionList = oldPermissionIds.stream().filter(item -> !permissionIds.contains(item)).collect(Collectors.toList());
                if (permissionList.size() > 0) {
                    for (String permission : permissionList) {
                        this.deletePackPermission(sysTenantPack.getId(), permission);
                    }
                }

                //找到原来菜单id与新的菜单id不同之处,进行新增
                List<String> permissionAddList = Arrays.stream(permissionIds.split(SymbolConstant.COMMA)).filter(item -> !oldPermissionIds.contains(item)).collect(Collectors.toList());
                if (permissionAddList.size() > 0) {
                    for (String permission : permissionAddList) {
                        this.addPermission(sysTenantPack.getId(), permission, "", 0l);
                    }
                }
            }
        }
        sysTenantPackMapper.updateById(sysTenantPack);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePackPermissions(String ids) {
        String[] idsArray = ids.split(SymbolConstant.COMMA);
        for (String id : idsArray) {
            this.deletePackPermission(id,null);
            sysTenantPackMapper.deleteById(id);
        }
    }

    @Override
    public void exitTenant(String tenantId, String userId) {
        this.getById(tenantId);
    }

    @Override
    public void addDefaultTenantPack(Long tenantId) {
        // 创建超级管理员
        SysTenantPack superAdminPack = new SysTenantPack(tenantId, "超级管理员", TenantConstant.SUPER_ADMIN);
        ISysTenantPackService currentService = SpringContextUtils.getApplicationContext().getBean(ISysTenantPackService.class);
        String packId = currentService.saveOne(superAdminPack);

        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysTenantPackUser packUser = new SysTenantPackUser(tenantId, packId, sysUser.getId());
        packUser.setRealname(sysUser.getRealname());
        packUser.setPackName(superAdminPack.getPackName());
        //添加人员和管理员的关系数据
        currentService.savePackUser(packUser);

        // 创建超级管理员
        SysTenantPack accountAdminPack = new SysTenantPack(tenantId, "组织账户管理员", TenantConstant.ACCOUNT_ADMIN);
        currentService.saveOne(accountAdminPack);

        // 创建超级管理员
        SysTenantPack appAdminPack = new SysTenantPack(tenantId, "组织应用管理员", TenantConstant.APP_ADMIN);
        currentService.saveOne(appAdminPack);
        
    }

    @TenantLog(2)
    @Override
    public String saveOne(SysTenantPack sysTenantPack) {
        sysTenantPackMapper.insert(sysTenantPack);
        return sysTenantPack.getId();
    }

    @TenantLog(2)
    @Override
    public void savePackUser(SysTenantPackUser sysTenantPackUser) {
        sysTenantPackUser.setStatus(1);
        sysTenantPackUserMapper.insert(sysTenantPackUser);
    }

    @Override
    public SysTenantPack getSysTenantPack(Long tenantId, String packCode) {
        LambdaQueryWrapper<SysTenantPack> query = new LambdaQueryWrapper<SysTenantPack>()
                .eq(SysTenantPack::getPackCode, packCode)
                .eq(SysTenantPack::getTenantId, tenantId);
        List<SysTenantPack> list = baseMapper.selectList(query);
        if(list!=null && list.size()>0){
            SysTenantPack pack = list.get(0);
            if(pack!=null && pack.getId()!=null){
                return pack;
            }
        }
        return null;
    }

    /**
     * 添加菜单
     * @param packId
     * @param permissionId
     * @param roleId
     */
    public void addPermission(String packId, String permissionId, String roleId, Long tenantId) {

        SysPackPermission permission = new SysPackPermission();
        permission.setPermissionId(permissionId);
        permission.setPackId(packId);
        sysPackPermissionMapper.insert(permission);

        if (StrUtil.isNotBlank(roleId)) {
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setPermissionId(permissionId);
            rolePermission.setTenantId(tenantId);
            rolePermission.setRoleId(roleId);
            rolePermissionMapper.insert(rolePermission);
        }
    }



    /**
     * 根据包名id和菜单id删除关系表
     *
     * @param packId
     * @param permissionId
     */
    public void deletePackPermission(String packId, String permissionId) {
        LambdaQueryWrapper<SysPackPermission> query = new LambdaQueryWrapper<>();
        query.eq(SysPackPermission::getPackId, packId);
        if (oConvertUtils.isNotEmpty(permissionId)) {
            query.eq(SysPackPermission::getPermissionId, permissionId);
        }
        sysPackPermissionMapper.delete(query);
    }
}
