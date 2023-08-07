package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.ReviewClassTenantConf;
import org.jeecg.modules.system.entity.SysTenant;

/**
 * @author javabage
 * @date 2023/8/4
 */
public interface IReviewClassTenantConfService extends IService<ReviewClassTenantConf> {
    void saveClass(SysTenant sysTenant);

    boolean updateClassConf(SysTenant tenant);

    void getClassList(IPage<SysTenant> pageList);
}
