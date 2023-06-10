package com.review.manage.reviewClass.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.front.frontReport.entity.ReviewReportTemplateEntity;
import com.review.front.frontReport.service.IReviewReportTemplateService;
import com.review.manage.project.entity.ReviewProjectEntity;
import com.review.manage.project.service.IReviewProjectService;
import com.review.manage.reviewClass.entity.ReviewClass;
import com.review.manage.reviewClass.service.IReviewClassService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
* @Description: 测评量表
* @Author: jeecg-boot
* @Date:   2023-01-10
* @Version: V1.0
*/
@Api(tags="测评量表")
@RestController
@RequestMapping("/reviewClass/reviewClass")
@Slf4j
public class ReviewClassController extends JeecgController<ReviewClass, IReviewClassService> {

   @Autowired
   private IReviewClassService reviewClassService;
   @Autowired
   private IReviewReportTemplateService reportTemplateService;
   @Autowired
   private IReviewProjectService reviewProjectService;

   /**
    * 分页列表查询
    * @param reviewClass
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @ApiOperation(value="测评量表-分页列表查询", notes="测评量表-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<ReviewClass>> queryPageList(ReviewClass reviewClass,
                                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                   HttpServletRequest req) {
       QueryWrapper<ReviewClass> queryWrapper = QueryGenerator.initQueryWrapper(reviewClass, req.getParameterMap());
       Page<ReviewClass> page = new Page<ReviewClass>(pageNo, pageSize);
       IPage<ReviewClass> pageList = reviewClassService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

    /**
     * 测评量表下拉框数据
     * @param reviewClass
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
   @RequestMapping(value = "/getReviewClassList", method = RequestMethod.GET)
   public Result<IPage<ReviewClass>> queryPageListTest(ReviewClass reviewClass,
                                                       @RequestParam(name="pageNo", defaultValue="1") Integer pageNo, @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
       Result<IPage<ReviewClass>> result = new Result<>();
       QueryWrapper<ReviewClass> queryWrapper = QueryGenerator.initQueryWrapper(reviewClass, req.getParameterMap());
       Page<ReviewClass> page = new Page<ReviewClass>(pageNo, pageSize);
       IPage<ReviewClass> pageList = reviewClassService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
    *   添加
    * @param reviewClass
    * @return
    */
   @AutoLog(value = "测评量表-添加")
   @ApiOperation(value="测评量表-添加", notes="测评量表-添加")
   //@RequiresPermissions("reviewClass:review_class:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody ReviewClass reviewClass) {
       LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
       reviewClass.setCreateBy(sysUser.getUsername());
       reviewClass.setStatus(1);
       reviewClassService.save(reviewClass);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    * @param reviewClass
    * @return
    */
   @AutoLog(value = "测评量表-编辑")
   @ApiOperation(value="测评量表-编辑", notes="测评量表-编辑")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody ReviewClass reviewClass) {
       reviewClassService.updateByClassId(reviewClass);
       return Result.OK("编辑成功!");
   }

   /**
    * 通过id删除
    * @param classId
    * @return
    */
   @AutoLog(value = "测评量表-通过id删除")
   @ApiOperation(value="测评量表-通过id删除", notes="测评量表-通过id删除")
   //@RequiresPermissions("reviewClass:review_class:delete")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="classId",required=true) String classId) {
       reviewClassService.delMain(classId);
       return Result.OK("删除成功!");
   }

    /**
     * 通过id批量or单个发布/停用量表
     * @param jsonObject
     * @return
     */
   @RequestMapping(value = "/publishBatch", method = RequestMethod.PUT)
   public Result<ReviewClass> publishBatch(@RequestBody JSONObject jsonObject){
       Result<ReviewClass> result = new Result<ReviewClass>();
       try {
           String classIds = jsonObject.getString("classIds");
           String status = jsonObject.getString("status");
           String[] arr = classIds.split(",");
           for (String classId : arr) {
               if(oConvertUtils.isNotEmpty(classId)) {
                   this.reviewClassService.update(new ReviewClass().setStatus(Integer.parseInt(status)),
                           new UpdateWrapper<ReviewClass>().lambda().eq(ReviewClass::getClassId,classId));
               }
           }
       }catch (Exception e){
           log.error(e.getMessage(), e);
           result.error500("操作失败"+e.getMessage());
       }
       result.success("操作成功!");
       return result;
   }

   /**
    * 批量删除
    * @param ids
    * @return
    */
   @AutoLog(value = "测评量表-批量删除")
   @ApiOperation(value="测评量表-批量删除", notes="测评量表-批量删除")
   //@RequiresPermissions("reviewClass:review_class:deleteBatch")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.reviewClassService.delBatchMain(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 导出
    * @return
    */
   //@RequiresPermissions("reviewClass:review_class:exportXls")
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, ReviewClass reviewClass) {
       return super.exportXls(request, reviewClass, ReviewClass.class, "测评量表");
   }

   /**
    * 导入
    * @return
    */
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, ReviewClass.class);
   }

    /**
     * 测评量表下拉框动态渲染
     * @param reviewClass
     * @param req
     * @return
     */
    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public Result<?> queryPageListTest(ReviewClass reviewClass,HttpServletRequest req) {
        QueryWrapper<ReviewClass> queryWrapper = QueryGenerator.initQueryWrapper(reviewClass, req.getParameterMap());
        List<ReviewClass> pageList = reviewClassService.list(queryWrapper);
        JSONArray array = new JSONArray(pageList.size());
        for (ReviewClass item : pageList) {
            JSONObject option = new JSONObject(3);
            option.put("value", item.getClassId());
            option.put("label", item.getTitle());
            option.put("text", item.getTitle());
            array.add(option);
        }
        return Result.ok(array);
    }

    /**
     * 添加报告设置
     * @param reviewClass
     * @return
     */
    @AutoLog(value = "测评量表-添加报告提示")
    @ApiOperation(value="测评量表-添加报告提示", notes="测评量表-添加报告提示")
    //@RequiresPermissions("reviewClass:review_class:add")
    @PostMapping(value = "/saveReportConf")
    public Result<String> saveReportConf(@RequestBody ReviewClass reviewClass) {
        reviewClassService.updateById(reviewClass);
        ReviewReportTemplateEntity reviewReportTemplateEntity = new ReviewReportTemplateEntity();
        reviewReportTemplateEntity.setClassId(reviewClass.getClassId());
        QueryWrapper<ReviewReportTemplateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",reviewClass.getClassId());
        Integer count = reportTemplateService.list(queryWrapper).size();
        reviewReportTemplateEntity.setOrderNum(count + 1);
        reviewReportTemplateEntity.setTitle(reviewClass.getReportTitle());
        reviewReportTemplateEntity.setExplanation(reviewClass.getExplanation());
        reportTemplateService.save(reviewReportTemplateEntity);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "测评量表-报告提示列表")
    @ApiOperation(value="测评量表-报告提示列表", notes="测评量表-报告提示列表")
    @GetMapping(value = "/reviewReportList")
    public Result<?> reviewReportList(ReviewReportTemplateEntity reviewReportTemplate) {
        QueryWrapper<ReviewReportTemplateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",reviewReportTemplate.getClassId());
        List<ReviewReportTemplateEntity> list = reportTemplateService.list(queryWrapper);
        QueryWrapper<ReviewClass> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("class_id",reviewReportTemplate.getClassId());
        List<ReviewClass> list1 = reviewClassService.list(queryWrapper1);
        if (list1 != null && list1.size() > 0) {
            list.forEach(temp -> temp.setReportTips(list1.get(0).getReportTips()));
        }
        return Result.OK(list);
    }
    @AutoLog(value = "测评量表-通过id删除报告提示")
    @ApiOperation(value="测评量表-通过id删除报告提示", notes="测评量表-通过id删除报告提示")
    @DeleteMapping(value = "/deleteReportConf")
    public Result<String> deleteReportConf(@RequestParam(name="id",required=true) String id) {
        reportTemplateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *
     * @param reviewProject
     * @param req
     * @return
     */
    @ApiOperation(value="测评项目下拉框数据源", notes="测评项目下拉框数据源")
    @GetMapping(value = "/projectOptions")
    public Result<JSONArray> projectOptions(ReviewProjectEntity reviewProject,HttpServletRequest req) {
        QueryWrapper<ReviewProjectEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewProject, req.getParameterMap());
        List<ReviewProjectEntity> pageList = reviewProjectService.list(queryWrapper);
        JSONArray array = new JSONArray(pageList.size());
        for (ReviewProjectEntity item : pageList) {
            JSONObject option = new JSONObject(3);
            option.put("value", item.getId());
            option.put("label", item.getProjectName());
            option.put("text", item.getProjectName());
            array.add(option);
        }
        return Result.OK(array);
    }
}
