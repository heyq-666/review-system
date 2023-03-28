package com.review.manage.report.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.report.entity.ReviewReportEntity;
import com.review.manage.report.entity.ReviewReportGradeEntity;
import com.review.manage.report.service.IReportGradeService;
import com.review.manage.report.service.IReportService;
import com.review.manage.variate.entity.ReviewVariateGradeEntity;
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
@Api(tags="维度管理")
@RestController
@RequestMapping("/report/report")
public class ReportController extends JeecgController<ReviewReportEntity, IReportService> {

    @Autowired
    private IReportService reportService;

    @Autowired
    private IReportGradeService iReportGradeService;

    @ApiOperation(value="维度列表-分页列表查询", notes="维度列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ReviewReportEntity>> queryPageList(ReviewReportEntity reviewReport,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            HttpServletRequest req) {
        QueryWrapper<ReviewReportEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewReport, req.getParameterMap());
        Page<ReviewReportEntity> page = new Page<ReviewReportEntity>(pageNo, pageSize);
        IPage<ReviewReportEntity> pageList = reportService.page(page, queryWrapper);
        //量表信息
        reportService.getClassNameByClassId(pageList);
        return Result.OK(pageList);
    }

    @AutoLog(value = "维度管理-通过id删除")
    @ApiOperation(value="维度管理-通过id删除", notes="维度管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="reportId",required=true) String reportId) {
        //先删除变量题目关联表
        iReportGradeService.delReviewReportGrade(reportId);
        //删除变量-报告关联表数据
        reportService.delReviewReportVariate(reportId);
        //删除变量
        reportService.removeById(reportId);
        return Result.OK("删除成功!");
    }

    @ApiOperation(value="维度管理-分值列表", notes="维度管理-分值列表")
    @GetMapping(value = "/reportGradeList")
    public Result<IPage<ReviewReportGradeEntity>> gradeList(ReviewReportGradeEntity reviewReportGrade,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="5") Integer pageSize,
                                                            HttpServletRequest req) {
        if (StringUtils.isNotBlank(reviewReportGrade.getReportId())){
            QueryWrapper<ReviewReportGradeEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewReportGrade, req.getParameterMap());
            Page<ReviewReportGradeEntity> page = new Page<ReviewReportGradeEntity>(pageNo, pageSize);
            IPage<ReviewReportGradeEntity> pageList = iReportGradeService.page(page, queryWrapper);
            return Result.OK(pageList);
        }else {
            return Result.OK(null);
        }
    }
}
