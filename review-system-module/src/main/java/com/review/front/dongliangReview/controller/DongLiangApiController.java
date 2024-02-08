package com.review.front.dongliangReview.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.review.common.httpclient.HttpClientUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private static final String reportUrl = "https://wlj.xinzhaitongxing.com/review/upload2";
    //栋梁测评码验证
    private static final String codeValidateUrlGet = "http://www.dlsycp.cn/web-cms/encryptiontestcode/selectEnCodeStatus";
    @Autowired
    private IDongLiangReviewService dongLiangReviewService;
    /**
     * 栋梁测评-测评码验证
     * @param evalCodeEntity
     * @return
     */
    @AutoLog(value = "栋梁测评-测评码验证")
    @PostMapping(value = "verifyEvalCode")
    public Result<?> verifyEvalCode(@RequestBody EvalCodeEntity evalCodeEntity) {
        QueryWrapper<EvalCodeEntity> queryWrapper = new QueryWrapper<EvalCodeEntity>();
        queryWrapper.eq("eval_code",evalCodeEntity.getEvalCode());
        List<EvalCodeEntity> list = dongLiangReviewService.list(queryWrapper);
        List<EvalCodeEntity> list1 = list.stream().filter(item -> item.getStatus() == 1 || item.getStatus() == 3).collect(Collectors.toList());
        Map map = new HashMap();
        map.put("code",evalCodeEntity.getEvalCode());
        String data = HttpClientUtils.doGet(codeValidateUrlGet,map);
        JSONObject json = JSONObject.parseObject(data);
        if (list1.size() > 0 && StringUtils.isNotEmpty(data) && json.get("code").equals(200) && json.get("data").equals("0")){
            return Result.OK("测评码有效");
        }
        log.info("测评码验证，code = {}，validateResult = {}", evalCodeEntity.getEvalCode(), data);
        return Result.error("测评码无效");
    }
    /**
     * 完成测试，提交调用栋梁接口
     * @param dongliangTestQuestionVO
     * @return
     */
    @AutoLog(value = "完成测试，提交调用栋梁接口")
    @PostMapping(value = "commitTest")
    public Result<?> commitTest(HttpServletResponse response, HttpServletRequest request, @RequestBody DongliangTestQuestionVO[] dongliangTestQuestionVO) throws Exception{
        Date date = new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime  = sdf.format(date);
        String dongLiangApiurl = "";
        if (dongliangTestQuestionVO[0].getVersion() == 1){
            dongLiangApiurl = dongLiangApiurlStu;
        }else if (dongliangTestQuestionVO[0].getVersion() == 2){
            dongLiangApiurl = dongLiangApiurlPro;
        }
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //接口入参处理
        dongLiangReviewService.handleData(dongliangTestQuestionVO);
        String param = JSON.toJSONString(dongliangTestQuestionVO);
        String paramSub = param.substring(1,param.length()-1);
        log.info("栋梁测评入参,param = {}", paramSub);
        //调用栋梁答题提交接口
        Integer flag = 1;
        String resultJson = HttpClientUtils.doPost(dongLiangApiurl,JSONObject.parseObject(paramSub),"utf-8");
        if (StringUtils.isNotEmpty(resultJson)) {
            JSONObject json = JSONObject.parseObject(resultJson);
            log.info("dongliangPostLog:", json.toString());
            if ((Integer) json.get("code") == 200){
                String pdfUrl = json.getString("data");
                String pdfUrlView = reportUrl + pdfUrl;
                dongliangTestQuestionVO[0].setReportUrl(pdfUrlView);
                //业务数据处理
                dongLiangReviewService.handleBusinessData(flag,currentTime,json.toString(),dongliangTestQuestionVO,new ReviewUser());
                log.info("提交成功,测试人,user = {}", dongliangTestQuestionVO[0].getUserInfo().getName());
                return Result.OK("提交成功",pdfUrlView);
            }else {
                flag = 2;
                dongLiangReviewService.handleBusinessData(flag,currentTime,json.toString(),dongliangTestQuestionVO,new ReviewUser());
                log.info("提交成功,测试人,user = {}，msg = {}", dongliangTestQuestionVO[0].getUserInfo().getName(),json.get("msg").toString());
                return Result.error(json.get("msg").toString());
            }
        }else {
            flag = 2;
            JSONObject json = new JSONObject();
            json.put("code", 500);
            json.put("msg", "提交失败");
            dongLiangReviewService.handleBusinessData(flag,currentTime,json.toString(),dongliangTestQuestionVO,new ReviewUser());
            log.info("提交成功,测试人,user = {}，msg = {}", dongliangTestQuestionVO[0].getUserInfo().getName(),json.get("msg").toString());
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
        queryWrapper.eq("status",1);
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
        boolean isSuccess = dongLiangReviewService.update(evalCode, new UpdateWrapper<EvalCodeEntity>().eq("eval_code",evalCodeEntity.getEvalCode()));
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
