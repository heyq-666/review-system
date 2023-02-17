package com.review.manage.notice.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.notice.entity.ReviewNoticeEntity;
import com.review.manage.notice.service.IReviewNoticeService;
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
 * @author javabage
 * @date 2023/2/16
 */
@Api(tags="公告管理")
@RestController
@RequestMapping("/reviewNotice/reviewNotice")
@Slf4j
public class ReviewNoticeController extends JeecgController<ReviewNoticeEntity, IReviewNoticeService> {

    @Autowired
    private IReviewNoticeService reviewNoticeService;

    /**
     * 公告列表-分页列表查询
     * @param reviewNotice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value="公告列表-分页列表查询", notes="公告列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ReviewNoticeEntity>> queryPageList(ReviewNoticeEntity reviewNotice,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            HttpServletRequest req) {
        QueryWrapper<ReviewNoticeEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewNotice, req.getParameterMap());
        Page<ReviewNoticeEntity> page = new Page<ReviewNoticeEntity>(pageNo, pageSize);
        IPage<ReviewNoticeEntity> pageList = reviewNoticeService.page(page, queryWrapper);
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
        reviewNoticeService.delMain(id);
        return Result.OK("删除成功");
    }

    /**
     * 添加
     * @param reviewNotice
     * @return
     */
    @AutoLog(value = "公告管理-添加")
    @ApiOperation(value="公告管理-添加", notes="公告管理-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ReviewNoticeEntity reviewNotice) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        reviewNotice.setOperator(sysUser.getUsername());
        reviewNoticeService.save(reviewNotice);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     * @param reviewNotice
     * @return
     */
    @AutoLog(value = "公告管理-编辑")
    @ApiOperation(value="公告管理-编辑", notes="公告管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody ReviewNoticeEntity reviewNotice) {
        reviewNoticeService.updateById(reviewNotice);
        return Result.OK("编辑成功!");
    }
    /**
     * 通过id批量or单个发布/停用公告
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/publishBatch", method = RequestMethod.PUT)
    public Result<ReviewNoticeEntity> publishBatch(@RequestBody JSONObject jsonObject){
        Result<ReviewNoticeEntity> result = new Result<ReviewNoticeEntity>();
        try {
            String ids = jsonObject.getString("ids");
            String status = jsonObject.getString("status");
            String[] arr = ids.split(",");
            for (String id : arr) {
                if(oConvertUtils.isNotEmpty(id)) {
                    this.reviewNoticeService.update(new ReviewNoticeEntity().setStatus(Integer.parseInt(status)),
                            new UpdateWrapper<ReviewNoticeEntity>().lambda().eq(ReviewNoticeEntity::getId,id));
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
