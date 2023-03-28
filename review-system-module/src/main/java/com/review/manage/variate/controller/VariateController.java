package com.review.manage.variate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.variate.entity.ReviewVariateEntity;
import com.review.manage.variate.entity.ReviewVariateGradeEntity;
import com.review.manage.variate.service.IVariateGradeService;
import com.review.manage.variate.service.IVariateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javabage
 * @date 2023/3/22
 */
@Api(tags="因子管理")
@RestController
@RequestMapping("/variate/variate")
public class VariateController extends JeecgController<ReviewVariateEntity, IVariateService>  {

    @Autowired
    private IVariateService variateService;

    @Autowired
    private IVariateGradeService variateGradeService;

    @ApiOperation(value="因子列表-分页列表查询", notes="因子列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ReviewVariateEntity>> queryPageList(ReviewVariateEntity reviewVariate,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            HttpServletRequest req) {
        QueryWrapper<ReviewVariateEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewVariate, req.getParameterMap());
        Page<ReviewVariateEntity> page = new Page<ReviewVariateEntity>(pageNo, pageSize);
        IPage<ReviewVariateEntity> pageList = variateService.page(page, queryWrapper);
        //量表信息
        variateService.getClassNameByClassId(pageList);
        return Result.OK(pageList);
    }

    @AutoLog(value = "因子管理-通过id删除")
    @ApiOperation(value="因子管理-通过id删除", notes="因子管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="variateId",required=true) String variateId) {
        //先删除变量题目关联表
        variateService.delVariateGrade(variateId);
        //删除变量-报告关联表数据
        variateService.delReportVariate(variateId);
        //删除变量
        variateService.removeById(variateId);
        return Result.OK("删除成功!");
    }

    @ApiOperation(value="因子管理-分值列表", notes="因子管理-分值列表")
    @GetMapping(value = "/gradeList")
    public Result<IPage<ReviewVariateGradeEntity>> gradeList(ReviewVariateGradeEntity reviewVariateGrade,
                                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                             @RequestParam(name="pageSize", defaultValue="5") Integer pageSize,
                                                             HttpServletRequest req) {
        if (StringUtils.isNotBlank(reviewVariateGrade.getVariateId())){
            QueryWrapper<ReviewVariateGradeEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewVariateGrade, req.getParameterMap());
            Page<ReviewVariateGradeEntity> page = new Page<ReviewVariateGradeEntity>(pageNo, pageSize);
            IPage<ReviewVariateGradeEntity> pageList = variateGradeService.page(page, queryWrapper);
            return Result.OK(pageList);
        }else {
            return Result.OK(null);
        }
    }
}
