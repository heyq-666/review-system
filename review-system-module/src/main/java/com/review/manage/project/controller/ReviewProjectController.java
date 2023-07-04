package com.review.manage.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.util.WxAppletsUtils;
import com.review.manage.project.entity.ReviewProjectEntity;
import com.review.manage.project.service.IReviewProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * @author javabage
 * @date 2023/2/15
 */
@Api(tags="项目管理")
@RestController
@RequestMapping("/reviewProject/reviewProject")
public class ReviewProjectController extends JeecgController<ReviewProjectEntity, IReviewProjectService> {

    @Autowired
    private IReviewProjectService reviewProjectService;

    /**
     * 分页列表查询
     * @param reviewProject
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value="项目列表-分页列表查询", notes="项目列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<ReviewProjectEntity>> queryPageList(ReviewProjectEntity reviewProject,
                                                            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                            HttpServletRequest req) {
        QueryWrapper<ReviewProjectEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewProject, req.getParameterMap());
        Page<ReviewProjectEntity> page = new Page<ReviewProjectEntity>(pageNo, pageSize);
        IPage<ReviewProjectEntity> pageList = reviewProjectService.page(page, queryWrapper);
        //获取项目-量表信息
        reviewProjectService.getProjectClassInfo(pageList);
        return Result.OK(pageList);
    }

    /**
     * 项目管理-通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "项目管理-通过id删除")
    @ApiOperation(value="项目管理-通过id删除", notes="项目管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        reviewProjectService.delMain(id);
        return Result.OK("删除成功");
    }

    /**
     * 添加
     * @param reviewProject
     * @return
     */
    @AutoLog(value = "项目管理-添加")
    @ApiOperation(value="项目管理-添加", notes="项目管理-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody ReviewProjectEntity reviewProject) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        reviewProject.setCreator(sysUser.getUsername());
        reviewProjectService.save(reviewProject);
        reviewProjectService.insert(reviewProject);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     * @param reviewProject
     * @return
     */
    @AutoLog(value = "项目管理-编辑")
    @ApiOperation(value="项目管理-编辑", notes="项目管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody ReviewProjectEntity reviewProject) {
        //更新项目相关信息
        reviewProjectService.updateById(reviewProject);
        //更新项目关联的测评量表信息
        reviewProjectService.insert(reviewProject);
        return Result.OK("编辑成功!");
    }

    /**
     * 项目管理-生成二维码
     * @param reviewProject
     * @param req
     */
    @ApiOperation(value="项目管理-生成二维码", notes="项目管理-生成二维码")
    @GetMapping(value = "/generateAppletsQrCode")
    public Result<?> generateAppletsQrCode(ReviewProjectEntity reviewProject, HttpServletRequest req) {
        Result result = new Result();
        if (reviewProject.getId() == null){
            result.setSuccess(false);
            result.setCode(300);
            result.setMessage("测评项目ID为空，无法生成二维码");
            return result;
        }
        ReviewProjectEntity reviewProjectEntity = reviewProjectService.getById(reviewProject.getId());
        if (reviewProjectEntity == null) {
            result.setSuccess(false);
            result.setCode(301);
            result.setMessage("测评项目不存在，无法生成二维码");
            return result;
        }
        //生成二维码
        String qrCodePath = WxAppletsUtils.geneAppletsQrCode("pages/index/indexNew", "projectId=" + reviewProject.getId());
        reviewProject.setAppletsQrCodeLink(qrCodePath);
        reviewProject.setUpdateTime(new Date());
        reviewProjectService.saveOrUpdate(reviewProject);
        result.setCode(200);
        result.setMessage("生成成功");
        result.setResult(qrCodePath);
        return result;
    }

}
