package com.review.front.dongliangReview.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.review.front.dongliangReview.entity.EvalCodeEntity;
import com.review.front.dongliangReview.service.IDongLiangReviewService;
import com.review.front.dongliangReview.vo.DongliangTestQuestionVO;
import com.review.manage.userManage.entity.ReviewUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author javabage
 * @date 2023/4/4
 */
@Api(tags="栋梁测评管理")
@RestController
@RequestMapping("/reviewFront/dongLiang")
@Slf4j
public class DongLiangApiController extends JeecgController<EvalCodeEntity, IDongLiangReviewService> {

    //栋梁测评提交接口地址--formal
    private static final String dongLiangApiurlStu = "http://localhost:9999/api/commitTest";

    private static final String dongLiangApiurlPro = "http://localhost:9998/api/commitTest";
    //栋梁测评提交接口地址--local
    /*private static final String dongLiangApiurlStu = "http://www.xinzhaitongxing.com:9999/api/commitTest";

    private static final String dongLiangApiurlPro = "http://www.xinzhaitongxing.com:9998/api/commitTest";*/

    private static final String reportUrl = "https://www.xinzhaitongxing.com/review/upload2";

    @Autowired
    private IDongLiangReviewService dongLiangReviewService;

    /**
     * 栋梁测评-测评码验证
     * @param evalCodeEntity
     * @return
     */
    @AutoLog(value = "栋梁测评-测评码验证")
    @PostMapping(value = "verifyEvalCode")
    public Result<?> verifyEvalCode(EvalCodeEntity evalCodeEntity) {
        List<EvalCodeEntity> list = dongLiangReviewService.
                listByMap((Map<String, Object>) new HashMap<>().put("evalCode",evalCodeEntity.getEvalCode()));
        List<EvalCodeEntity> list1 = list.stream().filter(item -> item.getStatus() == 1 || item.getStatus() == 3).collect(Collectors.toList());
        return list1.size() > 0 ? Result.OK("测评码有效") : Result.OK("测评码无效或不存在");
    }

    /**
     * 完成测试，提交调用栋梁接口
     * @param dongliangTestQuestionVO
     * @return
     */
    @AutoLog(value = "完成测试，提交调用栋梁接口")
    @PostMapping(value = "commitTest")
    public Result<?> commitTest(@RequestBody DongliangTestQuestionVO[] dongliangTestQuestionVO) {

        Date date = new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime  = sdf.format(date);
        String dongLiangApiurl = "";
        if (dongliangTestQuestionVO[0].getVersion() == 1){
            dongLiangApiurl = dongLiangApiurlStu;
        }else if (dongliangTestQuestionVO[0].getVersion() == 2){
            dongLiangApiurl = dongLiangApiurlPro;
        }
        //接口入参处理
        dongLiangReviewService.handleData(dongliangTestQuestionVO);
        String param = JSON.toJSONString(dongliangTestQuestionVO);
        String paramSub = param.substring(1,param.length()-1);
        System.out.println("调用接口入参--测评人：" + dongliangTestQuestionVO[0].getUserInfo().getName());
        System.out.println(paramSub);

        //调用栋梁答题提交接口
        Integer flag = 1;
        RestTemplate restTemplate = new RestTemplate();
        String resultJson = restTemplate.postForObject(dongLiangApiurl,JSONObject.parseObject(paramSub),String.class);

        //post提交-需要测试
        /*HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DongliangTestQuestionVO> entityParam = new HttpEntity<DongliangTestQuestionVO>(dongliangTestQuestionVO[0],headers);
        String resultJsons = restTemplate.postForObject(dongLiangApiurl,entityParam,String.class);*/

        log.info("dongliangCommitMsg：",resultJson);
        if (StringUtils.isNotEmpty(resultJson)) {
            JSONObject json = JSONObject.parseObject(resultJson);
            if ((Integer) json.get("code") == 200){
                String pdfUrl = json.getString("data");
                String pdfUrlView = reportUrl + pdfUrl;
                dongliangTestQuestionVO[0].setReportUrl(pdfUrlView);
                //业务数据处理
                dongLiangReviewService.handleBusinessData(flag,currentTime,json.toString(),dongliangTestQuestionVO,new ReviewUser());
                return Result.OK("提交成功",pdfUrlView);
            }else {
                flag = 2;
                dongLiangReviewService.handleBusinessData(flag,currentTime,json.toString(),dongliangTestQuestionVO,new ReviewUser());
                return Result.error("提交失败",json);
            }
        }else {
            flag = 2;
            JSONObject json = new JSONObject();
            json.put("code", 500);
            json.put("msg", "提交失败");
            dongLiangReviewService.handleBusinessData(flag,currentTime,json.toString(),dongliangTestQuestionVO,new ReviewUser());
            return Result.error(500,"提交失败");
        }
    }

    /**
     * 获取测评码
     * @return
     */
    @PostMapping(value = "getEvalCode")
    public Result<String> getEvalCode() {
        QueryWrapper<EvalCodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("LIMIT 1");
        EvalCodeEntity evalCodeEntity = dongLiangReviewService.getOne(queryWrapper);
        if (evalCodeEntity != null) {
            String evalCode = evalCodeEntity.getEvalCode();
            evalCodeEntity.setStatus((byte) 3);
            dongLiangReviewService.updateById(evalCodeEntity);
            return Result.OK("获取测评码成功",evalCode);
        }else {
            return Result.error("库存不足");
        }
    }

    /**
     * 测评码订单未支付时，恢复测评码状态
     * @param evalCodeEntity
     * @return
     */
    @PostMapping(value = "updateEvalCodeStock")
    public Result<String> updateEvalCodeStock(@RequestBody EvalCodeEntity evalCodeEntity) {
        EvalCodeEntity evalCode = new EvalCodeEntity();
        evalCode.setStatus((byte) 1);
        boolean isSuccess = dongLiangReviewService.update(evalCode, new UpdateWrapper<EvalCodeEntity>().eq("evalCode",evalCodeEntity.getEvalCode()));
        return isSuccess ? Result.OK("该订单未成功支付") : Result.error(0,"支付异常");
    }

    /**
     * 获取测评码价格
     * @return
     */
    @PostMapping(value = "getEvalPrice")
    public Result<EvalCodeEntity> getEvalPrice() {
        QueryWrapper<EvalCodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("LIMIT 1");
        EvalCodeEntity evalCodeEntity = dongLiangReviewService.getOne(queryWrapper);
        return Result.OK("获取测评码价格成功",evalCodeEntity);
    }
}
