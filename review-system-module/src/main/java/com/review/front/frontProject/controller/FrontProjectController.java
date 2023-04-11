package com.review.front.frontProject.controller;

import com.review.front.frontProject.service.IFrontProjectService;
import com.review.manage.project.entity.ReviewProjectEntity;
import com.review.manage.project.vo.ReviewProjectVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Api(tags="测评项目管理")
@RestController
@RequestMapping("/reviewFront/project")
@Slf4j
public class FrontProjectController extends JeecgController<ReviewProjectEntity, IFrontProjectService> {

    @Autowired
    private IFrontProjectService frontProjectService;

    /**
     * 小程序-查询测评项目详情
     * @param reviewProject
     * @return
     */
    @AutoLog(value = "小程序-查询测评项目详情")
    @PostMapping(value = "getReviewProjectDetail")
    public Result<?> getReviewProjectDetail(@RequestBody ReviewProjectVO reviewProject) {

        if (reviewProject == null || reviewProject.getId() == null || reviewProject.getId() == 0) {
            return Result.error(300,"项目ID为空");
        }
        ReviewProjectEntity reviewProjectEntity = frontProjectService.getById(reviewProject.getId());
        return Result.OK(reviewProjectEntity);
    }
}
