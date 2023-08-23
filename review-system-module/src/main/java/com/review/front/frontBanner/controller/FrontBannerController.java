package com.review.front.frontBanner.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.common.Constants;
import com.review.front.frontBanner.service.IFrontBannerService;
import com.review.manage.banner.entity.ReviewBannerEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javabage
 * @date 2023/4/7
 */
@Api(tags="小程序-banner管理")
@RestController
@RequestMapping("/reviewFront/banner")
@Slf4j
public class FrontBannerController extends JeecgController<ReviewBannerEntity, IFrontBannerService> {

    @Autowired
    private IFrontBannerService frontBannerService;

    @AutoLog(value = "小程序-banner列表查询")
    @PostMapping(value = "list")
    public Result<IPage<ReviewBannerEntity>> list(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                          HttpServletRequest req){
        Long tenantId = null;
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
        if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
            tenantId = oConvertUtils.getLong(TenantContext.getTenant(),-1);
        }
        System.out.println("租户id："+ tenantId);
        ReviewBannerEntity reviewBanner = new ReviewBannerEntity().setStatus(Constants.StatusPublish);
        QueryWrapper<ReviewBannerEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewBanner, req.getParameterMap());
        Page<ReviewBannerEntity> page = new Page<ReviewBannerEntity>(pageNo, pageSize);
        IPage<ReviewBannerEntity> pageList = frontBannerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
}
