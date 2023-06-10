package com.review.manage.expert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.manage.expert.entity.ExpertBeGoodAt;
import com.review.manage.expert.model.ExpertBeGoodAtTreeModel;

import java.util.List;

/**
 * @author javabage
 * @date 2023/5/17
 */
public interface IExpertBeGoodAtService extends IService<ExpertBeGoodAt> {
    List<ExpertBeGoodAtTreeModel> queryTreeListByPid(String parentId, String ids, String primaryKey);
}
