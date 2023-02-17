package com.review.manage.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.project.entity.ReviewProjectEntity;

public interface IReviewProjectService extends IService<ReviewProjectEntity> {

    /**
     * 根据项目表主键id删除项目
     * @param id
     */
    void delMain (String id);

    /**
     * 更新项目关联的测评量表信息
     * @param reviewProject
     */
    void insert(ReviewProjectEntity reviewProject);

    void getProjectClassInfo(IPage<ReviewProjectEntity> pageList);
}
