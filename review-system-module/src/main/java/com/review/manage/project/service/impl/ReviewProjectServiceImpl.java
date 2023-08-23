package com.review.manage.project.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.project.entity.ReviewProjectClassEntity;
import com.review.manage.project.entity.ReviewProjectEntity;
import com.review.manage.project.mapper.ReviewProjectMapper;
import com.review.manage.project.service.IReviewProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ReviewProjectServiceImpl extends ServiceImpl<ReviewProjectMapper, ReviewProjectEntity> implements IReviewProjectService {

    @Autowired
    private ReviewProjectMapper reviewProjectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        reviewProjectMapper.deleteById(id);
    }

    @Override
    public void insert(ReviewProjectEntity reviewProject) {
        //先删除该项目对应的测评量表再进行插入
        reviewProjectMapper.deleteProjectClassById(reviewProject.getId());
        List<ReviewProjectClassEntity> list = new ArrayList<>();
        Long projectId = reviewProject.getId();
        String classId =  reviewProject.getClassIds();
        String[] classIds = classId.split(",");
        for (int i = 0; i < classIds.length; i++) {
            ReviewProjectClassEntity reviewProjectClassEntity = new ReviewProjectClassEntity();
            reviewProjectClassEntity.setProjectId(projectId);
            reviewProjectClassEntity.setClassId(classIds[i]);
            list.add(reviewProjectClassEntity);
        }
        reviewProjectMapper.insertReviewProjectClass(list);
    }

    @Override
    public void getProjectClassInfo(IPage<ReviewProjectEntity> pageList) {
        List<ReviewProjectEntity> newPageList = pageList.getRecords();
        for (int i = 0; i < newPageList.size(); i++) {
            List<String> classIds = reviewProjectMapper.isExitProjectClass(newPageList.get(i).getId());
            String classIdsArray = "";
            for (int j = 0; j < classIds.size(); j++) {
                classIdsArray += classIds.get(j) + ",";
            }
            if (classIdsArray != ""){
                classIdsArray = classIdsArray.substring(0,classIdsArray.length()-1);
                newPageList.get(i).setClassIds(classIdsArray);
                pageList.setRecords(newPageList);
            }
            /*newPageList.get(i).setClassIdList(reviewProjectMapper.isExitProjectClass(newPageList.get(i).getId()));*/
        }
    }

    @Override
    public List<Map<String, String>> getAppInfo(Long tenantId) {
        return reviewProjectMapper.getAppInfo(tenantId);
    }
}
