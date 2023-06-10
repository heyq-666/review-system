package com.review.manage.userManage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.manage.userManage.entity.ReviewResult;
import com.review.manage.userManage.entity.ReviewUser;
import com.review.manage.userManage.service.IReviewUserService;
import com.review.manage.userManage.vo.QuestionAnswerVo;
import com.review.manage.userManage.vo.ReviewResultVo;
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
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 测评用户
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
@Api(tags="测评用户")
@RestController
@RequestMapping("/reviewUser/reviewUser")
@Slf4j
public class ReviewUserController extends JeecgController<ReviewUser, IReviewUserService> {
	@Autowired
	private IReviewUserService reviewUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param reviewUser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="测评用户-分页列表查询", notes="测评用户-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ReviewUser>> queryPageList(ReviewUser reviewUser,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReviewUser> queryWrapper = QueryGenerator.initQueryWrapper(reviewUser, req.getParameterMap());
		Page<ReviewUser> page = new Page<ReviewUser>(pageNo, pageSize);
		IPage<ReviewUser> pageList = reviewUserService.page(page, queryWrapper);

		IPage<ReviewUser> pageList1 = new Page<>();

		//查询用户对应的用户组
		List<String> userIds = pageList.getRecords().stream().map(ReviewUser::getUserId).collect(Collectors.toList());
		/*if(userIds!=null && userIds.size()>0){
			Map<String,String> useDepNames = reviewUserService.getDepNamesByUserIds(userIds);
			pageList.getRecords().forEach(item->{
				item.setDepartName(useDepNames.get(item.getUserId()));
			});
		}*/
		if(userIds!=null && userIds.size()>0){
			for (int i = 0; i < pageList.getRecords().size(); i++) {
				String groupList = pageList.getRecords().get(i).getGroupId();
				String[] groupAr = groupList.split(",");
				List<String> groupListNew = new ArrayList<>();
				for (int k = 0; k < groupAr.length; k++) {
					groupListNew.add(groupAr[k]);
				}
				List<String> str = reviewUserService.getDepartNameList(groupListNew);
				String strNew = str.stream().collect(Collectors.joining(","));
				pageList.getRecords().get(i).setDepartName(strNew);
			}
		}
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param reviewUser
	 * @return
	 */
	@AutoLog(value = "测评用户-添加")
	@ApiOperation(value="测评用户-添加", notes="测评用户-添加")
	//@RequiresPermissions("reviewUser:review_user:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ReviewUser reviewUser) {
		Date date = new Date();
		/*SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
		System.out.println(dateFormat.format(date));*/
		reviewUser.setUpdateTime(date);
		reviewUserService.save(reviewUser);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param reviewUser
	 * @return
	 */
	@AutoLog(value = "测评用户-编辑")
	@ApiOperation(value="测评用户-编辑", notes="测评用户-编辑")
	//@RequiresPermissions("reviewUser:review_user:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ReviewUser reviewUser) {
		//reviewUserService.updateById(reviewUser);
		reviewUserService.updateByUserId(reviewUser);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param userId
	 * @return
	 */
	@AutoLog(value = "测评用户-通过id删除")
	@ApiOperation(value="测评用户-通过id删除", notes="测评用户-通过id删除")
	//@RequiresPermissions("reviewUser:review_user:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="userId",required=true) String userId) {
		reviewUserService.removeById(userId);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测评用户-批量删除")
	@ApiOperation(value="测评用户-批量删除", notes="测评用户-批量删除")
	//@RequiresPermissions("reviewUser:review_user:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.reviewUserService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "测评用户-通过id查询")
	@ApiOperation(value="测评用户-通过id查询", notes="测评用户-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ReviewUser> queryById(@RequestParam(name="id",required=true) String id) {
		ReviewUser reviewUser = reviewUserService.getById(id);
		if(reviewUser==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(reviewUser);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param reviewUser
    */
    //@RequiresPermissions("reviewUser:review_user:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReviewUser reviewUser) {
        return super.exportXls(request, reviewUser, ReviewUser.class, "测评用户");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("reviewUser:review_user:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ReviewUser.class);
    }

	@ApiOperation(value="测评用户-分页列表查询", notes="测评用户-分页列表查询")
	@GetMapping(value = "/getRecordList")
	public Result<IPage<ReviewResultVo>> getRecordList(ReviewResultVo reviewResultVo,
													   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													   HttpServletRequest req) {
		IPage<ReviewResultVo> pageList = new Page<>();
		List<ReviewResultVo> reviewResults = reviewUserService.getResultByUserId(reviewResultVo.getUserId());
		if (reviewResults.size() > 0 && reviewResults != null){
			pageList.setRecords(reviewResults);
		}
		return Result.OK(pageList);
	}
	@AutoLog(value = "测评记录-通过resultId删除")
	@ApiOperation(value="测评记录-通过resultId删除", notes="测评记录-通过resultId删除")
	@DeleteMapping(value = "/deleteRecord")
	public Result<String> deleteRecord(@RequestParam(name="resultId",required=true) String resultId) {
		reviewUserService.removeRecord(resultId);
		return Result.OK("删除成功!");
	}

	@AutoLog(value = "导出答题记录")
	@ApiOperation(value="导出答题记录", notes="导出答题记录")
	@PostMapping(value = "/exportQuestionAnswerByGroup")
	public Result<?> exportQuestionAnswerByGroup(@RequestBody QuestionAnswerVo questionAnswerVo) {
		System.out.println(questionAnswerVo.getEndTime());
		return null;
	}
}
