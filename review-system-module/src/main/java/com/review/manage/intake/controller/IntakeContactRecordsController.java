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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * intake联系记录
 * @author javabage
 * @date 2023/4/14
 */
@Api(tags="intake_contact_record")
@RestController
@RequestMapping("/reviewIntake/contactRecord")
@Slf4j
public class IntakeContactRecordsController extends JeecgController<IntakeContactRecordsEntity, IntakeContactRecordsService> {

    @Autowired
    private IntakeContactRecordsService intakeContact;

    @Autowired
    private IReviewIntakeService reviewIntakeService;

    /**
     * 添加联系记录
     * @param intakeContactRecords
     * @return
     */
    @AutoLog(value = "intake_contact_record-添加")
    @ApiOperation(value="intake_contact_record-添加", notes="intake_contact_record-添加")
    @PostMapping(value = "/saveContactRecords")
    public Result<String> saveContactRecords(@RequestBody IntakeContactRecordsEntity intakeContactRecords) {
        intakeContactRecords.setIntakeId(intakeContactRecords.getId());
        intakeContactRecords.setId(null);
        intakeContact.save(intakeContactRecords);
        return Result.OK("添加成功！");
    }

    /**
     * 修改联系记录
     * @param intakeContactRecords
     * @return
     */
    @AutoLog(value = "intake_contact_record-修改")
    @ApiOperation(value="intake_contact_record-修改", notes="intake_contact_record-修改")
    @PostMapping(value = "/editContactRecords")
    public Result<String> editContactRecords(@RequestBody IntakeContactRecordsEntity intakeContactRecords) {
        intakeContact.updateById(intakeContactRecords);
        return Result.OK("编辑成功！");
    }

    /**
     * 查看联系记录列表
     * @param intakeContactRecords
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "intake_contact_record-查看列表")
    @ApiOperation(value="intake_contact_record-查看列表", notes="intake_contact_record-查看列表")
    @GetMapping(value = "/getContactRecordsList")
    public Result<IPage<IntakeContactRecordsEntity>> getContactRecordsList(IntakeContactRecordsEntity intakeContactRecords,
                                                                           @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                           HttpServletRequest req) {
        QueryWrapper<IntakeContactRecordsEntity> queryWrapper = QueryGenerator.initQueryWrapper(intakeContactRecords, req.getParameterMap());
        Page<IntakeContactRecordsEntity> page = new Page<IntakeContactRecordsEntity>(pageNo, pageSize);
        IPage<IntakeContactRecordsEntity> pageList = intakeContact.page(page, queryWrapper);
        if (pageList.getRecords().size() > 0) {
            Long intakeId = pageList.getRecords().get(0).getIntakeId();
            ReviewIntake reviewIntake = reviewIntakeService.getById(intakeId);
            for (int i = 0; i < pageList.getRecords().size(); i++) {
                pageList.getRecords().get(i).setEmployeeName(reviewIntake.getEmployeeName());
            }
        }
        return Result.OK(pageList);
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "intake_contact_record-通过id删除")
    @ApiOperation(value="intake_contact_record-通过id删除", notes="intake_contact_record-通过id删除")
    @DeleteMapping(value = "/deleteOneContactRecord")
    public Result<String> deleteOneContactRecord(@RequestParam(name="id",required=true) String id) {
        intakeContact.removeById(id);
        return Result.OK("删除成功!");
    }
}
