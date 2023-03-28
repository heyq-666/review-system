package com.review.manage.expert.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.expert.entity.ReviewExpert;
import com.review.manage.expert.entity.ReviewExpertCalendarEntity;
import com.review.manage.expert.service.IReviewExpertCalendarService;
import com.review.manage.expert.service.IReviewExpertService;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;
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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: review_expert
 * @Author: jeecg-boot
 * @Date:   2023-03-10
 * @Version: V1.0
 */
@Api(tags="review_expert")
@RestController
@RequestMapping("/reviewExpert/reviewExpert")
@Slf4j
public class ReviewExpertController extends JeecgController<ReviewExpert, IReviewExpertService> {
	@Autowired
	private IReviewExpertService reviewExpertService;

	@Autowired
	private IReviewExpertCalendarService reviewExpertCalendarService;
	
	/**
	 * 分页列表查询
	 *
	 * @param reviewExpert
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "review_expert-分页列表查询")
	@ApiOperation(value="review_expert-分页列表查询", notes="review_expert-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ReviewExpert>> queryPageList(ReviewExpert reviewExpert,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReviewExpert> queryWrapper = QueryGenerator.initQueryWrapper(reviewExpert, req.getParameterMap());
		Page<ReviewExpert> page = new Page<ReviewExpert>(pageNo, pageSize);
		IPage<ReviewExpert> pageList = reviewExpertService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param reviewExpert
	 * @return
	 */
	@AutoLog(value = "review_expert-添加")
	@ApiOperation(value="review_expert-添加", notes="review_expert-添加")
	//@RequiresPermissions("reviewExpert:review_expert:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ReviewExpert reviewExpert) {
		reviewExpertService.save(reviewExpert);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param reviewExpert
	 * @return
	 */
	@AutoLog(value = "review_expert-编辑")
	@ApiOperation(value="review_expert-编辑", notes="review_expert-编辑")
	//@RequiresPermissions("reviewExpert:review_expert:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ReviewExpert reviewExpert) {
		reviewExpertService.updateById(reviewExpert);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "review_expert-通过id删除")
	@ApiOperation(value="review_expert-通过id删除", notes="review_expert-通过id删除")
	//@RequiresPermissions("reviewExpert:review_expert:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		reviewExpertService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "review_expert-批量删除")
	@ApiOperation(value="review_expert-批量删除", notes="review_expert-批量删除")
	//@RequiresPermissions("reviewExpert:review_expert:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.reviewExpertService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "review_expert-通过id查询")
	@ApiOperation(value="review_expert-通过id查询", notes="review_expert-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ReviewExpert> queryById(@RequestParam(name="id",required=true) String id) {
		ReviewExpert reviewExpert = reviewExpertService.getById(id);
		if(reviewExpert==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(reviewExpert);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param reviewExpert
    */
    //@RequiresPermissions("reviewExpert:review_expert:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReviewExpert reviewExpert) {
        return super.exportXls(request, reviewExpert, ReviewExpert.class, "review_expert");
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
        return super.importExcel(request, response, ReviewExpert.class);
    }

	 @ApiOperation(value="review_expert_calendar-分页列表查询", notes="review_expert_calendar-分页列表查询")
	 @GetMapping(value = "/expertCalendarDetailList")
	 public Result<?> expertCalendarDetailList(ReviewExpertCalendarEntity expertCalendarEntity,
																			   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																			   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																			   HttpServletRequest req) {
		 List<ReviewExpertCalendarVo> list = reviewExpertService.getCalendarDetailList(expertCalendarEntity);
		 Result result = new Result();
		 result.setCode(200);
		 result.setResult(list);
		 return result;
		 /*Result<IPage<ReviewExpertCalendarEntity>> result = new Result<>();
		 QueryWrapper<ReviewExpertCalendarEntity> queryWrapper = QueryGenerator.initQueryWrapper(expertCalendarEntity, req.getParameterMap());
		 Page<ReviewExpertCalendarEntity> page = new Page<ReviewExpertCalendarEntity>(pageNo, pageSize);
		 IPage<ReviewExpertCalendarEntity> pageList = reviewExpertCalendarService.page(page, queryWrapper);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;*/
	 }
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "review_expert_calendar-通过id删除")
	@ApiOperation(value="review_expert_calendar-通过id删除", notes="review_expert_calendar-通过id删除")
	@DeleteMapping(value = "/deleteOneCalendar")
	public Result<String> deleteOneCalendar(@RequestParam(name="id",required=true) String id) {
		reviewExpertCalendarService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 添加
	 * @param reviewExpertCalendarVo
	 * @return
	 */
	@AutoLog(value = "review_expert_calendar-添加")
	@ApiOperation(value="review_expert_calendar-添加", notes="review_expert_calendar-添加")
	@PostMapping(value = "/saveCalendarInfo")
	public Result<String> saveCalendarInfo(@RequestBody ReviewExpertCalendarVo reviewExpertCalendarVo) {
		//处理日历入参并保存
		reviewExpertCalendarService.handleCalendarData(reviewExpertCalendarVo);
		return Result.OK("添加成功！");
	}
}
