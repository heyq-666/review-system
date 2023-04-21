package com.review.front.frontNotice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.review.common.Constants;
import com.review.front.frontNotice.service.IFrontNoticeService;
import com.review.manage.notice.entity.ReviewNoticeEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Api(tags="通告管理")
@RestController
@RequestMapping("/reviewFront/notice")
@Slf4j
public class FrontNoticeController extends JeecgController<ReviewNoticeEntity, IFrontNoticeService> {

    @Autowired
    private IFrontNoticeService frontNoticeService;

    /**
     * 小程序-公告列表查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "小程序-公告列表查询")
    @PostMapping(value = "list")
    public Result<IPage<ReviewNoticeEntity>> list(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                               HttpServletRequest req){
        ReviewNoticeEntity reviewNotice = new ReviewNoticeEntity().setStatus(Constants.StatusPublish);
        QueryWrapper<ReviewNoticeEntity> queryWrapper = QueryGenerator.initQueryWrapper(reviewNotice, req.getParameterMap());
        Page<ReviewNoticeEntity> page = new Page<ReviewNoticeEntity>(pageNo, pageSize);
        IPage<ReviewNoticeEntity> pageList = frontNoticeService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 小程序-公告详情
     * @param reviewNotice
     * @return
     */
    @AutoLog(value = "小程序-公告详情")
    @PostMapping(value = "detail")
    public Result<ReviewNoticeEntity> detail(@RequestBody ReviewNoticeEntity reviewNotice){
        if (reviewNotice.getId() != null && reviewNotice.getId() > 0) {
            ReviewNoticeEntity reviewNotice1 = frontNoticeService.getById(reviewNotice.getId());
            return Result.OK("查询成功",reviewNotice1);
        }else {
            return Result.error(300,"公告id为空");
        }
    }
}
