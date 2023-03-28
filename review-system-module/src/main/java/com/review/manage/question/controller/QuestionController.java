package com.review.manage.question.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.review.manage.question.entity.ReviewQuestion;
import com.review.manage.question.service.IReviewQuestionService;
import com.review.manage.question.vo.QuestionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
 * @author javabage
 * @date 2023/3/21
 */
@Api(tags="问题列表")
@RestController
@RequestMapping("/reviewQuestion/reviewQuestion")
@Slf4j
public class QuestionController extends JeecgController<ReviewQuestion, IReviewQuestionService> {

    @Autowired
    private IReviewQuestionService reviewQuestionService;

    /**
     * 通过量表ID查询对应的题目列表
     * @param classId
     * @param req
     * @return
     */
    @ApiOperation(value="问题列表-通过量表ID查询", notes="问题列表-通过主表ID查询")
    @GetMapping(value = "/listReviewQuestionByMainId")
    public Result<List<ReviewQuestion>> listReviewQuestionByMainId(@RequestParam(name = "classId") String classId, HttpServletRequest req) {
        List<ReviewQuestion> list = reviewQuestionService.getQuestionListByClassId(classId);
        return Result.OK(list);
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
        //Integer questionNum = reviewQuestionService.getMaxQuestionId(reviewQuestion.getClassId());
        Integer questionNum = 50;
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
     * 通过主表ID查询
     * @param questionId
     * @return
     */
    @ApiOperation(value="选项列表-通过主表ID查询", notes="选项列表-通过主表ID查询")
    @GetMapping(value = "/reviewAnswerList")
    public Result<List<QuestionVO>> reviewAnswerList(@RequestParam(name = "questionId") String questionId){
        if (!"".equals(StringUtils.trimToEmpty(questionId))){
            List<QuestionVO> list = reviewQuestionService.getAnswersByQuestionId(questionId);
            return Result.OK(list);
        }else {
            return null;
        }
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
            exportList = pageList.stream().filter(item -> selectionList.contains(item.getQuestionId())).collect(Collectors.toList());
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
                /*for (ReviewQuestion temp : list) {
                   temp.setClassId(mainId);
                }*/
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
}
