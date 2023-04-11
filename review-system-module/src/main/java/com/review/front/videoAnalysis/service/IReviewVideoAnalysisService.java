package com.review.front.videoAnalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.review.front.videoAnalysis.entity.ReviewVideoAnalysisEntity;
import com.review.front.videoAnalysis.vo.OssTmpAuth;

import java.io.ByteArrayInputStream;

/**
 * @author javabage
 * @date 2023/4/10
 */
public interface IReviewVideoAnalysisService extends IService<ReviewVideoAnalysisEntity> {

    /**
     * 分析视频路径
     * @param bytes
     * @param classId
     * @param id
     * @param projectId
     * @return
     */
    ReviewVideoAnalysisEntity analysis(byte[] bytes, String classId, String id, Long projectId);

    /**
     * 获取oss临时授权
     * @return
     */
    OssTmpAuth getOssAuth();

    /**
     * 上传视频到OSS
     * @param ossTmpAuth
     * @param byteArrayInputStream
     * @return
     */
    String uploadVideoToOss(OssTmpAuth ossTmpAuth, ByteArrayInputStream byteArrayInputStream);
}
