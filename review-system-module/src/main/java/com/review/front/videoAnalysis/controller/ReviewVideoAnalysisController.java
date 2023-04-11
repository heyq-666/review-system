package com.review.front.videoAnalysis.controller;

import cn.hutool.core.io.FileUtil;
import com.review.front.videoAnalysis.entity.ReviewVideoAnalysisEntity;
import com.review.front.videoAnalysis.service.IReviewVideoAnalysisService;
import com.review.front.videoAnalysis.vo.OssTmpAuth;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

/**
 * @author javabage
 * @date 2023/4/10
 */
@Api(tags="视频分析")
@RestController
@RequestMapping("/reviewFront/videoAnalysis")
@Slf4j
public class ReviewVideoAnalysisController extends JeecgController<ReviewVideoAnalysisEntity, IReviewVideoAnalysisService> {

    @Autowired
    private IReviewVideoAnalysisService reviewVideoAnalysisService;

    /**
     * 分析视频路径
     * @param classId
     * @param projectId
     * @param multipartFile
     * @return
     */
    @PostMapping(value = "/videoAnalysis",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<ReviewVideoAnalysisEntity> videoAnalysis(@RequestParam String classId,
                                   @RequestParam Long projectId,
                                   @RequestParam("file") MultipartFile multipartFile){
        try {
            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            ReviewVideoAnalysisEntity videoAnalysis = reviewVideoAnalysisService.analysis(multipartFile.getBytes(), classId,
                    user.getId(), projectId);
            return Result.OK("视频分析成功",videoAnalysis);
        } catch(Exception e) {
            log.error("register error, ", e);
            return Result.error("视频分析失败");
        }
    }

    /**
     * 上传视频
     * @param multipartFile
     */
    @PostMapping(value = "/uploadVideo")
    public Result<String> uploadVideo(@RequestParam("file")MultipartFile multipartFile) {
        try {
            byte[] videoBytes = multipartFile.getBytes();
            //1.获取oss临时授权
            OssTmpAuth ossTmpAuth = reviewVideoAnalysisService.getOssAuth();
            //2.上传视频到OSS
            String ossObjName = reviewVideoAnalysisService.uploadVideoToOss(ossTmpAuth, new ByteArrayInputStream(videoBytes));
            FileUtil.mkdir(ossObjName.split("/")[0]);
            FileUtil.writeBytes(videoBytes, ossObjName);
            return Result.OK("视频上传成功",ossObjName);
        } catch (Exception e) {
            log.error("register error, ", e);
            return Result.error("视频上传失败");
        }
    }
}
