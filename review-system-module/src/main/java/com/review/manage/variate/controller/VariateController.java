package com.review.manage.variate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.question.entity.ReviewQuestion;
import com.review.manage.question.entity.ReviewQuestionClassEntity;
import com.review.manage.question.service.IReviewQuestionClassService;
import com.review.manage.question.service.IReviewQuestionService;
import com.review.manage.variate.entity.ReviewVariateEntity;
import com.review.manage.variate.entity.ReviewVariateGradeEntity;
import com.review.manage.variate.service.IReviewGradeRuleService;
import com.review.manage.variate.service.IVariateGradeService;
import com.review.manage.variate.service.IVariateService;
import com.review.manage.variate.vo.VariateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author javabage
 * @date 2023/3/22
 */
@Api(tags="因子管理")
@RestController
@RequestMapping("/variate/variate")
public class VariateController extends JeecgController<ReviewVariateEntity, IVariateService>  {

    private static Logger logger = LoggerFactory.getLogger(VariateController.class);

    @Autowired
    private IVariateService variateService;

    @Autowired
    private IVariateGradeService variateGradeService;

    @Autowired
    private IReviewGradeRuleService gradeRuleService;

    @Autowired
    private IReviewQuestionService questionService;

    @Autowired
    private IReviewQuestionClassService reviewQuestionClassService;

    @ApiOperation(value="因子列表-分页列表查询", notes="因子列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ReviewVariateEntity reviewVariate,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            HttpServletRequest req) {
        Long tenantId = null;
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
        if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
            tenantId = oConvertUtils.getLong(TenantContext.getTenant(),-1);
        }
        QueryWrapper<ReviewVariateEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewVariate, req.getParameterMap());
        Page<ReviewVariateEntity> page = new Page<ReviewVariateEntity>(pageNo, pageSize);
        IPage<ReviewVariateEntity> pageList = variateService.page(page, queryWrapper);
        /*List<ReviewVariateEntity> list = variateService.list();*/
        //只要是开放的量表，该量表关联的因子也一并开放
        //量表信息
        variateService.getClassNameByClassId(pageList);
        List<ReviewVariateEntity> listNew = new ArrayList<>();
        if (tenantId != null && tenantId != -1) {
            listNew = variateService.filterData(tenantId,pageList);
            listNew.stream().collect(Collectors.groupingBy(ReviewVariateEntity :: getClassName));
            return Result.OK(listNew);
        }else {
            pageList.getRecords().stream().collect(Collectors.groupingBy(ReviewVariateEntity :: getClassName));
            return Result.OK(pageList);
        }
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
    public Result<List<ReviewVariateGradeEntity>> gradeList(ReviewVariateGradeEntity reviewVariateGrade, HttpServletRequest req) {
        if (StringUtils.isNotBlank(reviewVariateGrade.getVariateId())){
            QueryWrapper<ReviewVariateGradeEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("variate_id",reviewVariateGrade.getVariateId());
            List<ReviewVariateGradeEntity> list = variateGradeService.list(queryWrapper);
            return Result.OK(list);
        }else {
            return Result.OK("查询失败");
        }
    }

    @ApiOperation(value="添加因子", notes="添加因子")
    @PostMapping(value = "/addVariate")
    public Result<String> addVariate(HttpServletRequest request,HttpServletResponse response, @RequestBody VariateVO variate) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<ReviewVariateGradeEntity> list = variate.getVariateGradeList();
        ReviewVariateEntity variateEntity = new ReviewVariateEntity();
        variateEntity.setClassId(variate.getClassId());
        variateEntity.setCreateBy(sysUser.getUsername());
        variateEntity.setCreateTime(new Date());
        variateEntity.setVariateName(variate.getVariateName());
        Integer sortNum = variateService.getMaxSortNum(variate.getClassId());
        variateEntity.setSortNum(sortNum == null ? 0 : sortNum + 1);
        variateService.save(variateEntity);

        for(ReviewVariateGradeEntity variateGrade : list) {
            variateGrade.setVariateId(variateEntity.getVariateId());
            variateGradeService.save(variateGrade);
        }
        return Result.OK("添加成功");
    }

    @ApiOperation(value="编辑因子", notes="编辑因子")
    @PostMapping(value = "/editVariate")
    public Result<String> editVariate(HttpServletRequest request,HttpServletResponse response, @RequestBody VariateVO variate) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<ReviewVariateGradeEntity> list = variate.getVariateGradeList();
        ReviewVariateEntity variateEntity = variateService.getById(variate.getVariateId());
        variateEntity.setClassId(variate.getClassId());
        variateEntity.setCreateBy(sysUser.getUsername());
        variateEntity.setCreateTime(new Date());
        variateEntity.setVariateName(variate.getVariateName());

        //先删除变量题目关联表
        QueryWrapper<ReviewVariateGradeEntity> reviewVariateGrade = new QueryWrapper<>();
        reviewVariateGrade.eq("variate_id",variate.getVariateId());
        variateGradeService.remove(reviewVariateGrade);
        for(ReviewVariateGradeEntity variateGrade : list) {
            variateGrade.setVariateId(variate.getVariateId());
            variateGradeService.save(variateGrade);
        }
        variateService.saveOrUpdate(variateEntity);
        return Result.OK("更新成功");
    }

    @ApiOperation(value="因子计分设置", notes="因子计分设置")
    @PostMapping(value = "/addScoreSet")
    public Result<String> addScoreSet(@RequestBody VariateVO variate) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ReviewVariateEntity variateEntity = variateService.getById(variate.getVariateId());
        variateEntity.setCreateBy(sysUser.getUsername());
        variateEntity.setVariateGradeConf(variate.getVariateGradeConf());
        variateService.saveOrUpdate(variateEntity);
        /*List<ReviewGradeRuleEntity> gradeRuleList = variate.getGradeRuleList();
        QueryWrapper<ReviewGradeRuleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("variate_id", variate.getVariateId());
        gradeRuleService.remove(queryWrapper);*/
        //更新总条目运算信息
        /*ReviewVariateEntity variateEntity = variateService.getById(variate.getVariateId());
        variateEntity.setCreateBy(sysUser.getUsername());
        variateEntity.setCalSymbol(variate.getGradeAllRule().get(0).getCalSymbol());
        variateEntity.setCalTotal(variate.getGradeAllRule().get(0).getCalTotal());
        variateEntity.setCalSymbol1(variate.getGradeAllRule().get(0).getCalSymbol1());
        variateEntity.setCalTotal1(variate.getGradeAllRule().get(0).getCalTotal1());
        variateService.saveOrUpdate(variateEntity);*/

        /*ReviewQuestion question = null;
        for(ReviewGradeRuleEntity gradeRule : gradeRuleList) {
            question = questionService.getQuestionByQnum(variate.getClassId(), gradeRule.getQuestionId());
            gradeRule.setQuestionId(question.getQuestionId());
            gradeRule.setVariateId(variate.getVariateId());
            gradeRuleService.save(gradeRule);
        }*/
        return Result.OK("更新成功");
    }
    @GetMapping(value = "/getQuestionNumByClassId")
    public Result<List<ReviewQuestion>> getQuestionNumByClassId(@RequestParam(name="classId",required=true) String classId) {
        if (StringUtils.isBlank(classId)) {
            throw new JeecgBootException("量表不存在！");
        }
        QueryWrapper<ReviewQuestionClassEntity> queryWrapper = new QueryWrapper<ReviewQuestionClassEntity>();
        queryWrapper.eq("class_id",classId);
        List<ReviewQuestionClassEntity> questionIdList = reviewQuestionClassService.list(queryWrapper);
        List<Integer> questionIds = questionIdList.stream().map(ReviewQuestionClassEntity::getQuestionId).collect(Collectors.toList());
        List<ReviewQuestion> list = questionService.listByIds(questionIds);
        return Result.OK(list);
    }

    @GetMapping(value = "/getVariateListByClassId")
    public Result<List<ReviewVariateEntity>> getVariateListByClassId(@RequestParam(name="classId",required=true) String classId) {
        QueryWrapper<ReviewVariateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",classId);
        List<ReviewVariateEntity> list = variateService.list(queryWrapper);
        return Result.OK(list);
    }
}
