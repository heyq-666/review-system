package com.review.manage.intake.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.intake.entity.IntakeContactRecordsEntity;
import com.review.manage.intake.mapper.IntakeContactRecordsMapper;
import com.review.manage.intake.service.IntakeContactRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author javabage
 * @date 2023/4/14
 */
@Service
public class IntakeContactRecordsServiceImpl extends ServiceImpl<IntakeContactRecordsMapper, IntakeContactRecordsEntity> implements IntakeContactRecordsService {
}
