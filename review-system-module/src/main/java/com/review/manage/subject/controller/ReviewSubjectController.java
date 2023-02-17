package com.review.manage.subject.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.subject.entity.ReviewSubjectEntity;
import com.review.manage.subject.service.IReviewSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javabage
 * @date 2023/2/16
 */
@Api(tags="专题管理")
@RestController
@RequestMapping("/reviewSubject/reviewSubject")
public class ReviewSubjectController extends JeecgController<ReviewSubjectEntity, IReviewSubjectService> {

    @Autowired
    private IReviewSubjectService reviewSubjectService;

    /**
     * 专题列表-分页列表查询
     * @param reviewSubject
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value="专题列表-分页列表查询", notes="专题列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ReviewSubjectEntity>> queryPageList(ReviewSubjectEntity reviewSubject,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            HttpServletRequest req) {
        QueryWrapper<ReviewSubjectEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewSubject, req.getParameterMap());
        Page<ReviewSubjectEntity> page = new Page<ReviewSubjectEntity>(pageNo, pageSize);
        IPage<ReviewSubjectEntity> pageList = reviewSubjectService.page(page, queryWrapper);
        //获取专题-量表信息
        reviewSubjectService.getSubjectClassInfo(pageList);
        return Result.OK(pageList);
    }

    /**
     * 专题管理-通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "专题管理-通过id删除")
    @ApiOperation(value="专题管理-通过id删除", notes="专题管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        reviewSubjectService.delMain(id);
        return Result.OK("删除成功");
    }

    /**
     * 添加
     * @param reviewSubject
     * @return
     */
    @AutoLog(value = "专题管理-添加")
    @ApiOperation(value="专题管理-添加", notes="专题管理-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ReviewSubjectEntity reviewSubject) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        reviewSubject.setOperator(sysUser.getUsername());
        reviewSubjectService.save(reviewSubject);
        reviewSubjectService.insert(reviewSubject);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     * @param reviewSubject
     * @return
     */
    @AutoLog(value = "专题管理-编辑")
    @ApiOperation(value="专题管理-编辑", notes="专题管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody ReviewSubjectEntity reviewSubject) {
        //更新专题相关信息
        reviewSubjectService.updateById(reviewSubject);
        //更新专题关联的测评量表信息
        reviewSubjectService.insert(reviewSubject);
        return Result.OK("编辑成功!");
    }
}
