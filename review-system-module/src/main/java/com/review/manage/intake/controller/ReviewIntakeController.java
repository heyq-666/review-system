package com.review.manage.intake.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.intake.entity.IntakeContactRecordsEntity;
import com.review.manage.intake.entity.ReviewIntake;
import com.review.manage.intake.service.IReviewIntakeService;
import com.review.manage.intake.service.IntakeContactRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: review_intake
 * @Author: jeecg-boot
 * @Date:   2023-04-11
 * @Version: V1.0
 */
@Api(tags="review_intake")
@RestController
@RequestMapping("/reviewIntake/reviewIntake")
@Slf4j
public class ReviewIntakeController extends JeecgController<ReviewIntake, IReviewIntakeService> {

	@Autowired
	private IReviewIntakeService reviewIntakeService;

	/**
	 * 分页列表查询
	 *
	 * @param reviewIntake
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "review_intake-分页列表查询")
	@ApiOperation(value="review_intake-分页列表查询", notes="review_intake-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ReviewIntake>> queryPageList(ReviewIntake reviewIntake,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReviewIntake> queryWrapper = QueryGenerator.initQueryWrapper(reviewIntake, req.getParameterMap());
		Page<ReviewIntake> page = new Page<ReviewIntake>(pageNo, pageSize);
		IPage<ReviewIntake> pageList = reviewIntakeService.page(page, queryWrapper);
		for (int i = 0; i < pageList.getRecords().size(); i++) {
			String companyId = pageList.getRecords().get(i).getCompanyId();
			if(StringUtils.isNotBlank(companyId)){
				String[] coompanyAr = companyId.split(",");
				List<String> companyIdNew = new ArrayList<>();
				for (int j = 0; j < coompanyAr.length; j++) {
					companyIdNew.add(coompanyAr[j]);
				}
				List<String> str = reviewIntakeService.getDepartNameList(companyIdNew);
				String strNew = str.stream().collect(Collectors.joining(","));
				pageList.getRecords().get(i).setCompanyName(strNew);
			}
		}
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param reviewIntake
	 * @return
	 */
	@AutoLog(value = "review_intake-添加")
	@ApiOperation(value="review_intake-添加", notes="review_intake-添加")
	//@RequiresPermissions("reviewIntake:review_intake:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ReviewIntake reviewIntake) {
		reviewIntakeService.save(reviewIntake);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param reviewIntake
	 * @return
	 */
	@AutoLog(value = "review_intake-编辑")
	@ApiOperation(value="review_intake-编辑", notes="review_intake-编辑")
	//@RequiresPermissions("reviewIntake:review_intake:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ReviewIntake reviewIntake) {
		reviewIntakeService.updateById(reviewIntake);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "review_intake-通过id删除")
	@ApiOperation(value="review_intake-通过id删除", notes="review_intake-通过id删除")
	//@RequiresPermissions("reviewIntake:review_intake:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		reviewIntakeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "review_intake-批量删除")
	@ApiOperation(value="review_intake-批量删除", notes="review_intake-批量删除")
	//@RequiresPermissions("reviewIntake:review_intake:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.reviewIntakeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "review_intake-通过id查询")
	@ApiOperation(value="review_intake-通过id查询", notes="review_intake-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ReviewIntake> queryById(@RequestParam(name="id",required=true) String id) {
		ReviewIntake reviewIntake = reviewIntakeService.getById(id);
		if(reviewIntake==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(reviewIntake);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param reviewIntake
    */
    //@RequiresPermissions("reviewIntake:review_intake:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReviewIntake reviewIntake) {
        return super.exportXls(request, reviewIntake, ReviewIntake.class, "review_intake");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("reviewIntake:review_intake:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ReviewIntake.class);
    }
}
