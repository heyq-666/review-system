package com.review.manage.subject.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.subject.entity.ReviewSubjectClassEntity;
import com.review.manage.subject.entity.ReviewSubjectEntity;
import com.review.manage.subject.mapper.ReviewSubjectMapper;
import com.review.manage.subject.service.IReviewSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javabage
 * @date 2023/2/16
 */
@Service
public class ReviewSubjectServiceImpl extends ServiceImpl<ReviewSubjectMapper,ReviewSubjectEntity> implements IReviewSubjectService {

    @Autowired
    private ReviewSubjectMapper reviewSubjectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        reviewSubjectMapper.deleteById(id);
    }

    @Override
    public void insert(ReviewSubjectEntity reviewSubject) {
        //先删除该专题对应的测评量表再进行插入
        reviewSubjectMapper.deleteSubjectClassById(reviewSubject.getId());
        List<ReviewSubjectClassEntity> list = new ArrayList<>();
        Long subjectId = reviewSubject.getId();
        String classId =  reviewSubject.getClassIds();
        String[] classIds = classId.split(",");
        for (int i = 0; i < classIds.length; i++) {
            ReviewSubjectClassEntity reviewSubjectClassEntity = new ReviewSubjectClassEntity();
            reviewSubjectClassEntity.setSubjectId(subjectId);
            reviewSubjectClassEntity.setClassId(classIds[i]);
            list.add(reviewSubjectClassEntity);
        }
        reviewSubjectMapper.insertReviewProjectClass(list);
    }

    @Override
    public void getSubjectClassInfo(IPage<ReviewSubjectEntity> pageList) {
        List<ReviewSubjectEntity> newPageList = pageList.getRecords();
        for (int i = 0; i < newPageList.size(); i++) {
            List<String> classIds = reviewSubjectMapper.isExitSubjectClass(newPageList.get(i).getId());
            String classIdsArray = "";
            for (int j = 0; j < classIds.size(); j++) {
                classIdsArray += classIds.get(j) + ",";
            }
            if (classIdsArray != ""){
                classIdsArray = classIdsArray.substring(0,classIdsArray.length()-1);
                newPageList.get(i).setClassIds(classIdsArray);
                pageList.setRecords(newPageList);
            }
        }
    }
}
