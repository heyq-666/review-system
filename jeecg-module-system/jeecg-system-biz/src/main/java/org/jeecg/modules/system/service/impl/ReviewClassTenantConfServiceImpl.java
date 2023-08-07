package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.ReviewClassTenantConf;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.mapper.ReviewClassTenantConfMapper;
import org.jeecg.modules.system.service.IReviewClassTenantConfService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author javabage
 * @date 2023/8/4
 */
@Service
public class ReviewClassTenantConfServiceImpl extends ServiceImpl<ReviewClassTenantConfMapper, ReviewClassTenantConf> implements IReviewClassTenantConfService {
    @Override
    public void saveClass(SysTenant sysTenant) {
        String[] classIds = sysTenant.getClassIds().split(",");
        for (int i = 0; i < classIds.length; i++) {
            String classId = classIds[i];
            ReviewClassTenantConf reviewClassTenantConf = new ReviewClassTenantConf();
            reviewClassTenantConf.setTenantId(sysTenant.getId());
            reviewClassTenantConf.setClassId(classId);
            this.save(reviewClassTenantConf);
        }
    }

    @Override
    public boolean updateClassConf(SysTenant tenant) {
        //先删除
        QueryWrapper<ReviewClassTenantConf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id",tenant.getId());
        this.remove(queryWrapper);
        this.saveClass(tenant);
        return false;
    }

    @Override
    public void getClassList(IPage<SysTenant> pageList) {
        List<SysTenant> list = pageList.getRecords();
        for (int i = 0; i < list.size(); i++) {
            Long tenantId = list.get(i).getId();
            QueryWrapper<ReviewClassTenantConf> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tenant_id",tenantId);
            List<ReviewClassTenantConf> classIds = this.list(queryWrapper);
            StringBuffer classArr = new StringBuffer();
            for (int j = 0; j < classIds.size(); j++) {
                classArr.append(classIds.get(j).getClassId()).append(",");
            }
            String classSub = "";
            if (classArr.length() > 0) {
                classSub = classArr.substring(0,classArr.length() - 1);
            }
            list.get(i).setClassIds(classSub);
            pageList.setRecords(list);
        }
    }
}
