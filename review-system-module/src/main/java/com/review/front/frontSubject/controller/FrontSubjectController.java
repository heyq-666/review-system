package com.review.front.frontSubject.controller;

import com.review.front.frontSubject.service.IFrontSubjectService;
import com.review.manage.subject.entity.ReviewSubjectEntity;
import com.review.manage.subject.vo.ReviewSubjectVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Api(tags="测评专题管理")
@RestController
@RequestMapping("/reviewFront/subject")
@Slf4j
public class FrontSubjectController extends JeecgController<ReviewSubjectEntity, IFrontSubjectService> {

    @Autowired
    private IFrontSubjectService frontSubjectService;

    @AutoLog(value = "小程序-获取测评专题分类")
    @PostMapping(value = "getReviewSubjectClass")
    public Result<List<ReviewSubjectVO>> getReviewSubjectClass(@RequestBody ReviewSubjectVO reviewSubject,
                                           @RequestParam(name = "page", defaultValue="1") Integer page,
                                           @RequestParam(name = "rows", defaultValue="4") Integer rows) {
        List<ReviewSubjectVO> reviewSubjectList = frontSubjectService.getReviewSubjectClass(reviewSubject,page,rows);
        return Result.OK("查询成功",reviewSubjectList);
    }
}
