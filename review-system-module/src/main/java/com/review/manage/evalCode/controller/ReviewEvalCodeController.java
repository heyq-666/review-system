package com.review.manage.evalCode.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.evalCode.entity.ReviewEvalCode;
import com.review.manage.evalCode.service.IReviewEvalCodeService;
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
 * @Description: review_eval_code
 * @Author: jeecg-boot
 * @Date:   2023-07-28
 * @Version: V1.0
 */
@Api(tags="review_eval_code")
@RestController
@RequestMapping("/reviewEvalCode/reviewEvalCode")
@Slf4j
public class ReviewEvalCodeController extends JeecgController<ReviewEvalCode, IReviewEvalCodeService> {
	@Autowired
	private IReviewEvalCodeService reviewEvalCodeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param reviewEvalCode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="review_eval_code-分页列表查询", notes="review_eval_code-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ReviewEvalCode>> queryPageList(ReviewEvalCode reviewEvalCode,
													   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													   HttpServletRequest req) {
		QueryWrapper<ReviewEvalCode> queryWrapper = QueryGenerator.initQueryWrapper(reviewEvalCode, req.getParameterMap());
		Page<ReviewEvalCode> page = new Page<ReviewEvalCode>(pageNo, pageSize);
		IPage<ReviewEvalCode> pageList = reviewEvalCodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param reviewEvalCode
	 * @return
	 */
	@AutoLog(value = "review_eval_code-添加")
	@ApiOperation(value="review_eval_code-添加", notes="review_eval_code-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ReviewEvalCode reviewEvalCode) {
		reviewEvalCodeService.save(reviewEvalCode);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param reviewEvalCode
	 * @return
	 */
	@AutoLog(value = "review_eval_code-编辑")
	@ApiOperation(value="review_eval_code-编辑", notes="review_eval_code-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ReviewEvalCode reviewEvalCode) {
		reviewEvalCodeService.updateById(reviewEvalCode);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "review_eval_code-通过id删除")
	@ApiOperation(value="review_eval_code-通过id删除", notes="review_eval_code-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		reviewEvalCodeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "review_eval_code-批量删除")
	@ApiOperation(value="review_eval_code-批量删除", notes="review_eval_code-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.reviewEvalCodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="review_eval_code-通过id查询", notes="review_eval_code-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ReviewEvalCode> queryById(@RequestParam(name="id",required=true) String id) {
		ReviewEvalCode reviewEvalCode = reviewEvalCodeService.getById(id);
		if(reviewEvalCode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(reviewEvalCode);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param reviewEvalCode
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReviewEvalCode reviewEvalCode) {
        return super.exportXls(request, reviewEvalCode, ReviewEvalCode.class, "review_eval_code");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ReviewEvalCode.class);
    }

}
