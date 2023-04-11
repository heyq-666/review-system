package com.review.front.frontProject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontProject.mapper.FrontProjectMapper;
import com.review.front.frontProject.service.IFrontProjectService;
import com.review.manage.project.entity.ReviewProjectEntity;
import org.springframework.stereotype.Service;

/**
 * @author javabage
 * @date 2023/4/3
 */
@Service
public class FrontProjectServiceImpl extends ServiceImpl<FrontProjectMapper, ReviewProjectEntity> implements IFrontProjectService {
}
