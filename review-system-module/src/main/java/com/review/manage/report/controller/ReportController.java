package com.review.manage.report.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.review.front.frontReport.service.IReviewReportVariateService;
import com.review.manage.report.entity.ReviewReportEntity;
import com.review.manage.report.entity.ReviewReportGradeEntity;
import com.review.manage.report.entity.ReviewReportVariateEntity;
import com.review.manage.report.service.IReportGradeService;
import com.review.manage.report.service.IReportService;
import com.review.manage.report.vo.ReportVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private IReviewReportVariateService reviewReportVariateService;

    @ApiOperation(value="维度列表-分页列表查询", notes="维度列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<List<ReviewReportEntity>> queryPageList(ReviewReportEntity reviewReport,HttpServletRequest req) {
        Long tenantId = null;
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
        if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
            tenantId = oConvertUtils.getLong(TenantContext.getTenant(),-1);
        }
        List<ReviewReportEntity> list = reportService.list();
        //量表信息
        reportService.getClassNameByClassId(list);
        List<ReviewReportEntity> listNew = new ArrayList<>();
        if (tenantId != null && tenantId != -1) {
            listNew = reportService.filterData(tenantId,list);
            return Result.OK(listNew);
        }else {
            return Result.OK(list);
        }
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
    public Result<List<ReviewReportGradeEntity>> gradeList(ReviewReportGradeEntity reviewReportGrade, HttpServletRequest req) {
        if (StringUtils.isNotBlank(reviewReportGrade.getReportId())){
            QueryWrapper<ReviewReportGradeEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("report_id",reviewReportGrade.getReportId());
            List<ReviewReportGradeEntity> list = iReportGradeService.list(queryWrapper);
            return Result.OK(list);
        }else {
            List<ReviewReportGradeEntity> list = new ArrayList<>();
            for(int i=0; i<3; i++){
                list.add(new ReviewReportGradeEntity());
            }
            return Result.OK(list);
        }
    }

    @ApiOperation(value="添加维度", notes="添加维度")
    @PostMapping(value = "/addReport")
    public Result<?> addReport(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportVo report) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<ReviewReportGradeEntity> list = report.getReportGradeList();
        ReviewReportEntity reportEntity = new ReviewReportEntity();
        reportEntity.setClassId(report.getClassId());
        reportEntity.setCreateBy(sysUser.getUsername());
        reportEntity.setCreateTime(new Date());
        reportEntity.setReportName(report.getReportName());
        reportService.save(reportEntity);

        for(ReviewReportGradeEntity reportGrade : list) {
            reportGrade.setReportId(reportEntity.getReportId());
            iReportGradeService.save(reportGrade);
        }
        return Result.OK("添加成功");
    }

    @ApiOperation(value="编辑维度", notes="编辑维度")
    @PostMapping(value = "/editReport")
    public Result<?> editReport(HttpServletRequest request,HttpServletResponse response, @RequestBody ReportVo report) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<ReviewReportGradeEntity> list = report.getReportGradeList();
        ReviewReportEntity reportEntity = reportService.getById(report.getReportId());
        reportEntity.setClassId(report.getClassId());
        reportEntity.setCreateBy(sysUser.getUsername());
        reportEntity.setCreateTime(new Date());
        reportEntity.setReportName(report.getReportName());

        //先删除变量题目关联表
        QueryWrapper<ReviewReportGradeEntity> reviewReportGrade = new QueryWrapper<>();
        reviewReportGrade.eq("report_id",report.getReportId());
        iReportGradeService.remove(reviewReportGrade);
        for(ReviewReportGradeEntity reportGrade : list) {
            reportGrade.setReportId(report.getReportId());
            iReportGradeService.save(reportGrade);
        }
        reportService.saveOrUpdate(reportEntity);
        return Result.OK("更新成功");
    }

    @ApiOperation(value="保存维度设置", notes="保存维度设置")
    @PostMapping(value = "/saveReportSet")
    public Result<String> saveReportSet(@RequestBody ReportVo report) {
        List<ReviewReportVariateEntity> reportVariateList = report.getReportVariateList();
        QueryWrapper<ReviewReportVariateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id",report.getReportId());
        reviewReportVariateService.remove(queryWrapper);
        List<ReviewReportVariateEntity> list = null;
        for(ReviewReportVariateEntity reportVariate : reportVariateList) {
            if(!"".equals(StringUtils.trimToEmpty(reportVariate.getVariateId()))) {
                QueryWrapper<ReviewReportVariateEntity> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("variate_id",reportVariate.getVariateId());
                queryWrapper1.eq("report_id",report.getReportId());
                list = reviewReportVariateService.list(queryWrapper1);
                if(list.size() == 0) {
                    reportVariate.setReportId(report.getReportId());
                    reviewReportVariateService.save(reportVariate);
                }
            }
        }
        return Result.OK("添加成功");
    }

    @ApiOperation(value="获取维度设置", notes="获取维度设置")
    @GetMapping(value = "/reportSetList")
    public Result<List<ReviewReportVariateEntity>> reportSetList(@RequestParam(name="reportId",required=true) String reportId) {
        QueryWrapper<ReviewReportVariateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id",reportId);
        List<ReviewReportVariateEntity> list = reviewReportVariateService.list(queryWrapper);
        return Result.OK(list);
    }
}
