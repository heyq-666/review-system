package com.review.manage.variate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.variate.entity.ReviewVariateGradeEntity;
import com.review.manage.variate.mapper.VariateGradeMapper;
import com.review.manage.variate.service.IVariateGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author javabage
 * @date 2023/3/23
 */
@Service
public class VariateGradeServiceImpl extends ServiceImpl<VariateGradeMapper, ReviewVariateGradeEntity> implements IVariateGradeService {

    @Autowired
    private VariateGradeMapper variateGradeMapper;
    @Override
    public void deleteVariateGrade(List<String> variateIds) {
        variateGradeMapper.deleteVariateGrade(variateIds);
    }
}
