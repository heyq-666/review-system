package com.review.manage.banner.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.banner.entity.ReviewBannerEntity;
import com.review.manage.banner.service.IReviewBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javabage1
 * @date 2023/2/16
 */
@Api(tags="公告管理")
@RestController
@RequestMapping("/reviewBanner/reviewBanner")
@Slf4j
public class ReviewBannerController extends JeecgController<ReviewBannerEntity, IReviewBannerService> {

    @Autowired
    private IReviewBannerService reviewBannerService;

    /**
     * 轮播图列表-分页列表查询
     * @param reviewBanner
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value="轮播图列表-分页列表查询", notes="轮播图列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ReviewBannerEntity>> queryPageList(ReviewBannerEntity reviewBanner,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            HttpServletRequest req) {
        QueryWrapper<ReviewBannerEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewBanner, req.getParameterMap());
        Page<ReviewBannerEntity> page = new Page<ReviewBannerEntity>(pageNo, pageSize);
        IPage<ReviewBannerEntity> pageList = reviewBannerService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 公告管理-通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "公告管理-通过id删除")
    @ApiOperation(value="公告管理-通过id删除", notes="公告管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        reviewBannerService.delMain(id);
        return Result.OK("删除成功");
    }

    /**
     * 添加
     * @param reviewBanner
     * @return
     */
    @AutoLog(value = "公告管理-添加")
    @ApiOperation(value="公告管理-添加", notes="公告管理-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ReviewBannerEntity reviewBanner) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        reviewBanner.setOperator(sysUser.getUsername());
        reviewBannerService.save(reviewBanner);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     * @param reviewBanner
     * @return
     */
    @AutoLog(value = "公告管理-编辑")
    @ApiOperation(value="公告管理-编辑", notes="公告管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody ReviewBannerEntity reviewBanner) {
        reviewBannerService.updateById(reviewBanner);
        return Result.OK("编辑成功!");
    }
    /**
     * 通过id批量or单个发布/停用公告
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/publishBatch", method = RequestMethod.PUT)
    public Result<ReviewBannerEntity> publishBatch(@RequestBody JSONObject jsonObject){
        Result<ReviewBannerEntity> result = new Result<ReviewBannerEntity>();
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            for (String id : arr) {
                if(oConvertUtils.isNotEmpty(id)) {
                    this.reviewBannerService.update(new ReviewBannerEntity().setStatus(Integer.parseInt(status)),
                            new UpdateWrapper<ReviewBannerEntity>().lambda().eq(ReviewBannerEntity::getId,id));
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            result.error500("操作失败"+e.getMessage());
        }
        result.success("操作成功!");
        return result;
    }
}
