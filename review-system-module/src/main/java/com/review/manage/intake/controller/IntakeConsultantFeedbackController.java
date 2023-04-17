package com.review.manage.intake.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.intake.entity.IntakeConsultantFeedback;
import com.review.manage.intake.service.IIntakeConsultantFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: intake_consultant_feedback
* @Author: jeecg-boot
* @Date:   2023-04-17
* @Version: V1.0
*/
@Api(tags="intake_consultant_feedback")
@RestController
@RequestMapping("/intakeConsultantFeedback/intakeConsultantFeedback")
@Slf4j
public class IntakeConsultantFeedbackController extends JeecgController<IntakeConsultantFeedback, IIntakeConsultantFeedbackService> {
   @Autowired
   private IIntakeConsultantFeedbackService intakeConsultantFeedbackService;

   /**
    * 分页列表查询
    *
    * @param intakeConsultantFeedback
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "intake_consultant_feedback-分页列表查询")
   @ApiOperation(value="intake_consultant_feedback-分页列表查询", notes="intake_consultant_feedback-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<IntakeConsultantFeedback>> queryPageList(IntakeConsultantFeedback intakeConsultantFeedback,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<IntakeConsultantFeedback> queryWrapper = QueryGenerator.initQueryWrapper(intakeConsultantFeedback, req.getParameterMap());
       Page<IntakeConsultantFeedback> page = new Page<IntakeConsultantFeedback>(pageNo, pageSize);
       IPage<IntakeConsultantFeedback> pageList = intakeConsultantFeedbackService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param intakeConsultantFeedback
    * @return
    */
   @AutoLog(value = "intake_consultant_feedback-添加")
   @ApiOperation(value="intake_consultant_feedback-添加", notes="intake_consultant_feedback-添加")
   //@RequiresPermissions("intakeConsultantFeedback:intake_consultant_feedback:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody IntakeConsultantFeedback intakeConsultantFeedback) {
       intakeConsultantFeedbackService.save(intakeConsultantFeedback);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param intakeConsultantFeedback
    * @return
    */
   @AutoLog(value = "intake_consultant_feedback-编辑")
   @ApiOperation(value="intake_consultant_feedback-编辑", notes="intake_consultant_feedback-编辑")
   //@RequiresPermissions("intakeConsultantFeedback:intake_consultant_feedback:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody IntakeConsultantFeedback intakeConsultantFeedback) {
       intakeConsultantFeedbackService.updateById(intakeConsultantFeedback);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "intake_consultant_feedback-通过id删除")
   @ApiOperation(value="intake_consultant_feedback-通过id删除", notes="intake_consultant_feedback-通过id删除")
   //@RequiresPermissions("intakeConsultantFeedback:intake_consultant_feedback:delete")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       intakeConsultantFeedbackService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "intake_consultant_feedback-批量删除")
   @ApiOperation(value="intake_consultant_feedback-批量删除", notes="intake_consultant_feedback-批量删除")
   //@RequiresPermissions("intakeConsultantFeedback:intake_consultant_feedback:deleteBatch")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.intakeConsultantFeedbackService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "intake_consultant_feedback-通过id查询")
   @ApiOperation(value="intake_consultant_feedback-通过id查询", notes="intake_consultant_feedback-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<IntakeConsultantFeedback> queryById(@RequestParam(name="id",required=true) String id) {
       IntakeConsultantFeedback intakeConsultantFeedback = intakeConsultantFeedbackService.getById(id);
       if(intakeConsultantFeedback==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(intakeConsultantFeedback);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param intakeConsultantFeedback
   */
   //@RequiresPermissions("intakeConsultantFeedback:intake_consultant_feedback:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, IntakeConsultantFeedback intakeConsultantFeedback) {
       return super.exportXls(request, intakeConsultantFeedback, IntakeConsultantFeedback.class, "intake_consultant_feedback");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   //@RequiresPermissions("intakeConsultantFeedback:intake_consultant_feedback:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, IntakeConsultantFeedback.class);
   }

}
