package com.review.front.videoAnalysis.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.common.VideoAnalysisConfig;
import com.review.common.httpclient.HttpClientUtils;
import com.review.front.videoAnalysis.entity.ReviewVideoAnalysisEntity;
import com.review.front.videoAnalysis.mapper.ReviewVideoAnalysisMapper;
import com.review.front.videoAnalysis.service.IReviewVideoAnalysisService;
import com.review.front.videoAnalysis.vo.AnalysisResult;
import com.review.front.videoAnalysis.vo.AnalysisStatus;
import com.review.front.videoAnalysis.vo.AnalysisType;
import com.review.front.videoAnalysis.vo.OssTmpAuth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jeecg.common.util.CommonUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author javabage
 * @date 2023/4/10
 */
@Slf4j
@Service
public class ReviewVideoAnalysisServiceImpl extends ServiceImpl<ReviewVideoAnalysisMapper, ReviewVideoAnalysisEntity> implements IReviewVideoAnalysisService {
    @Override
    public ReviewVideoAnalysisEntity analysis(byte[] videoBytes, String classID, String userID, Long projectId) {
        //1.分析结果
        //1.获取oss临时授权
        OssTmpAuth ossTmpAuth = getOssAuth();
        //2.上传视频到OSS
        String ossObjName = uploadVideoToOss(ossTmpAuth, new ByteArrayInputStream(videoBytes));
        if (StrUtil.isBlank(ossObjName)) {
            throw new RuntimeException("Analysis failed: uploadVideoToOss failed");
        }
        //心理分析
        String healthResult = this.getAnalysisResult(ossObjName, ossTmpAuth.getToken(), AnalysisType.Health);
        JSONObject healthObj = JSONObject.parseObject(healthResult);
        AnalysisResult healthAnslysisResult = JSONObject.toJavaObject(healthObj, AnalysisResult.class);

        //保存结果
        ReviewVideoAnalysisEntity videoAnalysis = new ReviewVideoAnalysisEntity();
        videoAnalysis.setHealthAnalysisResult(healthResult);
        videoAnalysis.setCreateTime(new Date());
        videoAnalysis.setClassId(classID);
        videoAnalysis.setUserId(userID);
        videoAnalysis.setProjectId(projectId);
        videoAnalysis.setVideoPath(ossObjName);

        if (healthAnslysisResult.isRet()) {
            videoAnalysis.setHealthStatus(AnalysisStatus.Success.getValue());
        } else {
            videoAnalysis.setHealthStatus(AnalysisStatus.Failed.getValue());
            videoAnalysis.setHealthMsg(healthObj.getString("ErrorMessage"));
        }
        this.save(videoAnalysis);
        return videoAnalysis;
    }

    /**
     * 获取oss临时授权
     * @return
     */
    @Override
    public OssTmpAuth getOssAuth() {
        Map<String, String> param = getBaseParam();
        param.put("sign", CommonUtils.getSign(CommonUtils.getSignContent(param), VideoAnalysisConfig.SECURE_KEY));
        try {
            String result = HttpClientUtils.doPost(VideoAnalysisConfig.OSS_AUTH_URL, param);
            log.info("OssAuth result: {}", result);
            OssTmpAuth ossTmpAuth = JSONObject.toJavaObject(JSONObject.parseObject(result), OssTmpAuth.class);

            if (ossTmpAuth == null || !ossTmpAuth.getRet()) {
                log.warn("OssAuth get fail....");
                throw new RuntimeException("OssAuth get fail");
            }
            return ossTmpAuth;
        } catch (Exception e) {
            throw new RuntimeException("getOssAuth error, ", e);
        }
    }

    @Override
    public String uploadVideoToOss(OssTmpAuth ossTmpAuth, ByteArrayInputStream byteArrayInputStream) {
        OSS ossClient = null;
        try{
            OssTmpAuth.AuthResult authResult = ossTmpAuth.getResult();
            JSONObject ossCredentials = authResult.getOssCredentials();

            ossClient = new OSSClientBuilder().build(authResult.getOssEndPoint(), ossCredentials.getString("AccessKeyId"),
                    ossCredentials.getString("AccessKeySecret"), ossCredentials.getString("SecurityToken"));
            PutObjectResult putObjectResult = ossClient.putObject(authResult.getOssBucket(), authResult.getOssObjName(), byteArrayInputStream);
            log.info("PutObjectResult:{}", JSONObject.toJSONString(putObjectResult));
            return authResult.getOssObjName();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            IOUtils.closeQuietly(byteArrayInputStream);
        }
    }

    private Map<String, String> getBaseParam() {
        Map<String, String> param = new TreeMap<>();
        param.put("pid", VideoAnalysisConfig.PID);
        param.put("appid", VideoAnalysisConfig.APP_ID);
        param.put("timestamp", System.currentTimeMillis()+"");
        return param;
    }

    /**
     * 心理健康分析
     * @param ossObjName
     * @param token
     * @param analysisType
     * @return
     */
    public String getAnalysisResult(String ossObjName, String token, AnalysisType analysisType) {
        //3.调用视频生理分析接口
        String result = "";
        Map<String, String> params = getBaseParam();
        if (analysisType.getValue() == AnalysisType.Health.getValue()) { //心理健康分析
            params.put("obj_name", ossObjName);
            params.put("sign", CommonUtils.getSign(CommonUtils.getSignContent(params), VideoAnalysisConfig.SECURE_KEY));
            params.put("token", token);
            result = HttpClientUtils.doPost(VideoAnalysisConfig.HEALTH_ANALYSIS_URL, params);
        } else {
            params.put("obj_names", ossObjName);
            params.put("callback_uri", VideoAnalysisConfig.CALLBACK_URI);
            params.put("sign", CommonUtils.getSign(CommonUtils.getSignContent(params), VideoAnalysisConfig.SECURE_KEY));
            params.put("token", token);
            result = HttpClientUtils.doPost(VideoAnalysisConfig.EMO_ANALYSIS_URL, params);
        }
        log.info("type: {}, analysis result:{}", analysisType, result);
        return result;
    }
}
