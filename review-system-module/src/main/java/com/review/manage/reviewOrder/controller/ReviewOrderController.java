package com.review.manage.reviewOrder.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.reviewOrder.entity.ReviewOrder;
import com.review.manage.reviewOrder.service.IReviewOrderService;
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
 * @Description: review_order
 * @Author: jeecg-boot
 * @Date:   2023-02-24
 * @Version: V1.0
 */
@Api(tags="review_order")
@RestController
@RequestMapping("/reviewOrder/reviewOrder")
@Slf4j
public class ReviewOrderController extends JeecgController<ReviewOrder, IReviewOrderService> {
	@Autowired
	private IReviewOrderService reviewOrderService;
	
	/**
	 * 分页列表查询
	 *
	 * @param reviewOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "review_order-分页列表查询")
	@ApiOperation(value="review_order-分页列表查询", notes="review_order-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ReviewOrder>> queryPageList(ReviewOrder reviewOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReviewOrder> queryWrapper = QueryGenerator.initQueryWrapper(reviewOrder, req.getParameterMap());
		Page<ReviewOrder> page = new Page<ReviewOrder>(pageNo, pageSize);
		IPage<ReviewOrder> pageList = reviewOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param reviewOrder
	 * @return
	 */
	@AutoLog(value = "review_order-添加")
	@ApiOperation(value="review_order-添加", notes="review_order-添加")
	//@RequiresPermissions("reviewOrder:review_order:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ReviewOrder reviewOrder) {
		reviewOrderService.save(reviewOrder);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param reviewOrder
	 * @return
	 */
	@AutoLog(value = "review_order-编辑")
	@ApiOperation(value="review_order-编辑", notes="review_order-编辑")
	//@RequiresPermissions("reviewOrder:review_order:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ReviewOrder reviewOrder) {
		reviewOrderService.updateById(reviewOrder);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "review_order-通过id删除")
	@ApiOperation(value="review_order-通过id删除", notes="review_order-通过id删除")
	//@RequiresPermissions("reviewOrder:review_order:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		reviewOrderService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "review_order-批量删除")
	@ApiOperation(value="review_order-批量删除", notes="review_order-批量删除")
	//@RequiresPermissions("reviewOrder:review_order:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.reviewOrderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "review_order-通过id查询")
	@ApiOperation(value="review_order-通过id查询", notes="review_order-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ReviewOrder> queryById(@RequestParam(name="id",required=true) String id) {
		ReviewOrder reviewOrder = reviewOrderService.getById(id);
		if(reviewOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(reviewOrder);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param reviewOrder
    */
    //@RequiresPermissions("reviewOrder:review_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReviewOrder reviewOrder) {
        return super.exportXls(request, reviewOrder, ReviewOrder.class, "review_order");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("reviewOrder:review_order:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ReviewOrder.class);
    }

}
