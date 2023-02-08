package com.review.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.entity.ReviewClass;
import com.review.manage.entity.ReviewQuestion;
import com.review.manage.service.IReviewClassService;
import com.review.manage.service.IReviewQuestionService;
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
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
   private IReviewQuestionService reviewQuestionService;


   /*---------------------------------主表处理-begin1-------------------------------------*/

   /**
    * 分页列表查询
    * @param reviewClass
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "测评量表-分页列表查询")
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
    *   添加
    * @param reviewClass
    * @return
    */
   @AutoLog(value = "测评量表-添加")
   @ApiOperation(value="测评量表-添加", notes="测评量表-添加")
   //@RequiresPermissions("reviewClass:review_class:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody ReviewClass reviewClass) {
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
   //@RequiresPermissions("reviewClass:review_class:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody ReviewClass reviewClass) {
       reviewClassService.updateById(reviewClass);
       return Result.OK("编辑成功!");
   }

   /**
    * 通过id删除
    * @param id
    * @return
    */
   @AutoLog(value = "测评量表-通过id删除")
   @ApiOperation(value="测评量表-通过id删除", notes="测评量表-通过id删除")
   //@RequiresPermissions("reviewClass:review_class:delete")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       reviewClassService.delMain(id);
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
           String ids = jsonObject.getString("ids");
           String status = jsonObject.getString("status");
           String[] arr = ids.split(",");
           for (String id : arr) {
               if(oConvertUtils.isNotEmpty(id)) {
                   this.reviewClassService.update(new ReviewClass().setStatus(Integer.parseInt(status)),
                           new UpdateWrapper<ReviewClass>().lambda().eq(ReviewClass::getId,id));
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
   //@RequiresPermissions("reviewClass:review_class:importExcel")
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, ReviewClass.class);
   }
   /*---------------------------------主表处理-end-------------------------------------*/


   /*--------------------------------子表处理-问题列表-begin----------------------------------------------*/
   /**
    * 通过主表ID查询
    * @return
    */
   //@AutoLog(value = "问题列表-通过主表ID查询")
   @ApiOperation(value="问题列表-通过主表ID查询", notes="问题列表-通过主表ID查询")
   @GetMapping(value = "/listReviewQuestionByMainId")
   public Result<IPage<ReviewQuestion>> listReviewQuestionByMainId(ReviewQuestion reviewQuestion,
                                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                   HttpServletRequest req) {
       QueryWrapper<ReviewQuestion> queryWrapper = QueryGenerator.initQueryWrapper(reviewQuestion, req.getParameterMap());
       Page<ReviewQuestion> page = new Page<ReviewQuestion>(pageNo, pageSize);
       IPage<ReviewQuestion> pageList = reviewQuestionService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    * 添加
    * @param reviewQuestion
    * @return
    */
   @AutoLog(value = "问题列表-添加")
   @ApiOperation(value="问题列表-添加", notes="问题列表-添加")
   @PostMapping(value = "/addReviewQuestion")
   public Result<String> addReviewQuestion(@RequestBody ReviewQuestion reviewQuestion) {
       //查找该量表题目中最大的题目序号
       Integer questionNum = reviewQuestionService.getMaxQuestionId(reviewQuestion.getClassId());
       reviewQuestion.setQuestionNum(questionNum);
       reviewQuestionService.save(reviewQuestion);
       return Result.OK("添加成功！");
   }

   /**
    * 编辑
    * @param reviewQuestion
    * @return
    */
   @AutoLog(value = "问题列表-编辑")
   @ApiOperation(value="问题列表-编辑", notes="问题列表-编辑")
   @RequestMapping(value = "/editReviewQuestion", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> editReviewQuestion(@RequestBody ReviewQuestion reviewQuestion) {
       reviewQuestionService.updateById(reviewQuestion);
       return Result.OK("编辑成功!");
   }

   /**
    * 通过id删除
    * @param id
    * @return
    */
   @AutoLog(value = "问题列表-通过id删除")
   @ApiOperation(value="问题列表-通过id删除", notes="问题列表-通过id删除")
   @DeleteMapping(value = "/deleteReviewQuestion")
   public Result<String> deleteReviewQuestion(@RequestParam(name="id",required=true) String id) {
       reviewQuestionService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    * 批量删除
    * @param ids
    * @return
    */
   @AutoLog(value = "问题列表-批量删除")
   @ApiOperation(value="问题列表-批量删除", notes="问题列表-批量删除")
   @DeleteMapping(value = "/deleteBatchReviewQuestion")
   public Result<String> deleteBatchReviewQuestion(@RequestParam(name="ids",required=true) String ids) {
       this.reviewQuestionService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 导出
    * @return
    */
   @RequestMapping(value = "/exportReviewQuestion")
   public ModelAndView exportReviewQuestion(HttpServletRequest request, ReviewQuestion reviewQuestion) {
        // Step.1 组装查询条件
        QueryWrapper<ReviewQuestion> queryWrapper = QueryGenerator.initQueryWrapper(reviewQuestion, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<ReviewQuestion> pageList = reviewQuestionService.list(queryWrapper);
        List<ReviewQuestion> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //此处设置的filename无效,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.FILE_NAME, "问题列表");
        mv.addObject(NormalExcelConstants.CLASS, ReviewQuestion.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("问题列表报表", "导出人:" + sysUser.getRealname(), "问题列表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
   }

   /**
    * 导入
    * @return
    */
   @RequestMapping(value = "/importReviewQuestion/{mainId}")
   public Result<?> importReviewQuestion(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
      // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ReviewQuestion> list = ExcelImportUtil.importExcel(file.getInputStream(), ReviewQuestion.class, params);
                for (ReviewQuestion temp : list) {
                   temp.setClassId(mainId);
                }
                long start = System.currentTimeMillis();
                reviewQuestionService.saveBatch(list);
                log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                return Result.OK("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
   }

   /*--------------------------------子表处理-问题列表-end----------------------------------------------*/




}
