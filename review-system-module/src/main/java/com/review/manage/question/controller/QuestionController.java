package com.review.manage.question.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.review.common.Constants;
import com.review.front.frontReviewClass.vo.SelectVO;
import com.review.manage.question.entity.QuestionImportEntity;
import com.review.manage.question.entity.ReviewAnswerEntity;
import com.review.manage.question.entity.ReviewQuestion;
import com.review.manage.question.entity.ReviewQuestionClassEntity;
import com.review.manage.question.service.IReviewAnswerService;
import com.review.manage.question.service.IReviewQuestionClassService;
import com.review.manage.question.service.IReviewQuestionService;
import com.review.manage.question.vo.QuestionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
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

    @Autowired
    private IReviewQuestionClassService reviewQuestionClassService;

    @Autowired
    private IReviewAnswerService reviewAnswerService;

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
    @RequestMapping(value = "/importReviewQuestion")
    public Result<String> importReviewQuestion(HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String classId = request.getParameter("classId");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        String questionIds = "";
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<QuestionImportEntity> listQuestions;
                listQuestions = ExcelImportUtil.importExcel(file.getInputStream(),QuestionImportEntity.class,params);
                //先删除该分类下的所有题目
                reviewQuestionService.deleteQuestion(classId);
                //删除分类题目关联
                QueryWrapper<ReviewQuestionClassEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("class_id",classId);
                reviewQuestionClassService.remove(queryWrapper);
                ReviewQuestion questionEntity = null;
                ReviewQuestionClassEntity questionClass = null;
                List<ReviewAnswerEntity> selectList = null;
                for(QuestionImportEntity questionImport : listQuestions) {
                    questionEntity = reviewQuestionClassService.getQuestionByQnum(classId,questionImport.getQuestionNum());
                    if(questionEntity == null) {
                        questionEntity = new ReviewQuestion();
                        questionClass = new ReviewQuestionClassEntity();
                        questionEntity.setQuestionType(questionImport.getQuestionType());
                        questionEntity.setContent(questionImport.getQuestionContent());
                        questionEntity.setQuestionNum(questionImport.getQuestionNum());
                        questionEntity.setCreateTime(new Date());
                        questionEntity.setCreateBy(sysUser.getUsername());
                        reviewQuestionService.save(questionEntity);
                        questionClass.setClassId(classId);
                        questionClass.setQuestionId(questionEntity.getQuestionId());
                        reviewQuestionClassService.save(questionClass);
                        selectList = getSelectInfo(questionEntity.getQuestionId(), questionImport);
                        if(CollectionUtils.isNotEmpty(selectList)) {
                            for(ReviewAnswerEntity answerEntity : selectList) {
                                reviewAnswerService.save(answerEntity);
                            }
                        }
                    }else {
                        questionIds += questionImport.getQuestionNum() + ",";
                        continue;
                    }
                }
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
        if ("".equals(questionIds)){
            log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
            return Result.OK("导入成功！");
        }else {
            return Result.OK("题目编号 "+questionIds+"已存在，请修正再导入");
        }
    }

    @PostMapping(value = "/addQuestion")
    public Result<?> addQuestion(@RequestBody QuestionVO question, HttpServletRequest request, HttpServletResponse response) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String classId = question.getClassId();
        //获取该量表最大题目序号
        Integer maxQuestionNum = reviewQuestionService.getMaxQuestionId(classId);
        ReviewQuestion reviewQuestion = reviewQuestionService.getQuestionByQnum(classId, maxQuestionNum);
        if(reviewQuestion == null) {
            reviewQuestion = new ReviewQuestion();
            reviewQuestion.setContent(question.getContent());
            reviewQuestion.setCreateBy(sysUser.getUsername());
            reviewQuestion.setCreateTime(new Date());
            reviewQuestion.setIsImportant(0);
            reviewQuestion.setQuestionType(question.getQuestionType());
            reviewQuestion.setQuestionNum(maxQuestionNum == null ? 1 : maxQuestionNum + 1);
            reviewQuestionService.save(reviewQuestion);
            //添加题目分类
            ReviewQuestionClassEntity questionClass = new ReviewQuestionClassEntity();
            questionClass.setClassId(classId);
            questionClass.setQuestionId(reviewQuestion.getQuestionId());
            reviewQuestionClassService.save(questionClass);
            //添加题目选项内容
            List<SelectVO> selectList = question.getSelectList();
            SelectVO select = null;
            ReviewAnswerEntity answerEntity = null;
            for(int i=0; i<selectList.size();i++) {
                select = selectList.get(i);
                answerEntity = new ReviewAnswerEntity();
                answerEntity.setQuestionId(reviewQuestion.getQuestionId());
                answerEntity.setAnswerCode(select.getSelCode());
                answerEntity.setAnswerContent(select.getSelectContent());
                if(!"".equals(StringUtils.trimToEmpty(select.getSelectGrade()))) {
                    answerEntity.setGrade(Double.valueOf(select.getSelectGrade()));
                }
                reviewAnswerService.save(answerEntity);
            }
            return Result.OK("添加成功！");
        }else {
            return Result.OK("添加失败！");
        }
    }

    @PostMapping(value = "/editQuestion")
    public Result<?> editQuestion(@RequestBody QuestionVO question,HttpServletRequest request, HttpServletResponse response) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //更新题目
        ReviewQuestion reviewQuestion = reviewQuestionService.getById(question.getQuestionId());
        reviewQuestion.setContent(question.getContent());
        reviewQuestion.setCreateBy(sysUser.getUsername());
        reviewQuestion.setCreateTime(new Date());
        reviewQuestion.setQuestionType(question.getQuestionType());
        reviewQuestion.setRightAnswer(question.getRightAnswer());
        reviewQuestionService.saveOrUpdate(reviewQuestion);
        //更新选项
        List<SelectVO> selectList = question.getSelectList();
        SelectVO select = null;
        ReviewAnswerEntity answerEntity = null;
        //先清空再插入
        QueryWrapper<ReviewAnswerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_id",question.getQuestionId());
        reviewAnswerService.remove(queryWrapper);
        for(int i=0; i<selectList.size(); i++) {
            select = selectList.get(i);
            /*if(!"".equals(StringUtils.trimToEmpty(select.getSelectId()))) {
                answerEntity = reviewAnswerService.getById(select.getSelectId());
            } else {
                answerEntity = new ReviewAnswerEntity();
                answerEntity.setQuestionId(reviewQuestion.getQuestionId());
            }*/
            answerEntity = new ReviewAnswerEntity();
            answerEntity.setQuestionId(reviewQuestion.getQuestionId());
            answerEntity.setAnswerCode(select.getSelCode());
            answerEntity.setAnswerContent(select.getSelectContent());
            if(!"".equals(StringUtils.trimToEmpty(select.getSelectGrade()))) {
                answerEntity.setGrade(Double.valueOf(select.getSelectGrade()));
            }
            reviewAnswerService.saveOrUpdate(answerEntity);
        }
        return Result.OK("修改成功！");
    }

    /**
     * 封装每道题的选项内容
     * @param questionId
     * @param questionImport
     * @return
     */
    private List<ReviewAnswerEntity> getSelectInfo(Integer questionId, QuestionImportEntity questionImport) throws Exception{
        if (!Constants.QuestionType.SingleSelect.getValue().equals(questionImport.getQuestionType()) &&
                !Constants.QuestionType.MultiSelect.getValue().equals(questionImport.getQuestionType())) {
            return null;
        }
        Field[] fieldArr = QuestionImportEntity.class.getDeclaredFields();
        Field field = null;
        String filedName = "";
        String getMethodName = "";
        Method getMethod = null;
        List<ReviewAnswerEntity> selectList = new ArrayList<ReviewAnswerEntity>();
        Map<String, ReviewAnswerEntity> map = new HashMap<String, ReviewAnswerEntity>();
        for(int i = 0; i < fieldArr.length; i++) {
            field = fieldArr[i];
            filedName = field.getName();
            if(filedName.contains("select") || filedName.contains("grade")) {
                getMethodName = "get" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
                getMethod = QuestionImportEntity.class.getMethod(getMethodName);
                Object obj = getMethod.invoke(questionImport);
                if(obj == null || "".equals(StringUtils.trimToEmpty(obj.toString()))) {
                    continue;
                } else {
                    String selCode = filedName.substring(filedName.length()-1);
                    if(map.get(selCode) != null) {
                        if(field.getType().getName().equals("java.lang.String")) {
                            map.get(selCode).setAnswerContent(obj.toString());
                        } else if(field.getType().getName().equals("java.math.BigDecimal")) {
                            map.get(selCode).setGrade(Double.valueOf(obj.toString()));
                        }
                    } else {
                        map.put(selCode, new ReviewAnswerEntity());
                        map.get(selCode).setAnswerCode(selCode);
                        map.get(selCode).setQuestionId(questionId);
                        if(field.getType().getName().equals("java.lang.String")) {
                            map.get(selCode).setAnswerContent(obj.toString());
                        } else if(field.getType().getName().equals("java.math.BigDecimal")) {
                            map.get(selCode).setGrade(Double.valueOf(obj.toString()));
                        }
                    }
                }
            }
        }
        for(Map.Entry<String, ReviewAnswerEntity> entry : map.entrySet()) {
            if(entry.getValue() != null) {
                selectList.add(entry.getValue());
            }

        }
        return selectList;
    }
}
