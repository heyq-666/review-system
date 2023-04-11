package com.review.front.frontAppoint.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.common.Constants;
import com.review.front.frontAppoint.entity.ReviewExpertReserveEntity;
import com.review.front.frontAppoint.mapper.ReviewExpertReserveMapper;
import com.review.front.frontAppoint.service.IReviewExpertReserveService;
import com.review.front.frontAppoint.vo.ConsultationVO;
import com.review.front.frontReviewClass.service.IFrontReviewClassService;
import com.review.manage.expert.entity.ReviewExpert;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author javabage
 * @date 2023/4/4
 */
@Service
public class ReviewExpertReserveServiceImpl extends ServiceImpl<ReviewExpertReserveMapper, ReviewExpertReserveEntity>
        implements IReviewExpertReserveService {

    private static final Integer dValue = 300000;

    @Autowired
    private ReviewExpertReserveMapper reviewExpertReserveMapper;

    @Override
    public List<ConsultationVO> getMyConsultation(String userId) {
        return reviewExpertReserveMapper.getMyConsultation(userId);
    }

    @Override
    public List<ConsultationVO> getMyConsultationDetail(Integer id) {
        return reviewExpertReserveMapper.getMyConsultationDetail(id);
    }

    @Override
    public List<ConsultationVO> beginAndEndTimehandle(List<ConsultationVO> reviewExpertReserveList) {
        String consultTime = reviewExpertReserveList.get(0).getVisitDate()+ " " + reviewExpertReserveList.get(0).getBeginTime();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {
            long consultaTimeL = Long.valueOf(DateUtils.dateToStamp(consultTime));
            long currentTimeL = Long.valueOf(DateUtils.dateToStamp(currentTime));
            if (consultaTimeL < currentTimeL){//判断该预约是否支付（过期不能支付）
                reviewExpertReserveList.get(0).setBuy(true);
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return this.beginAndEndTimeUtils(reviewExpertReserveList);
    }

    @Override
    public void videoConsultCondition(List<ConsultationVO> reviewExpertReserveList) {
        //判断当前时间是否可以发起视频咨询
        String completeTime = reviewExpertReserveList.get(0).getVisitDate() + " " + reviewExpertReserveList.get(0).getBeginTime()+":00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());
        try {
            long complete = Long.valueOf(DateUtils.dateToStamp(completeTime));
            long current = Long.valueOf(DateUtils.dateToStamp(currentTime));
            if (Math.abs(complete-current) <= dValue){
                reviewExpertReserveList.get(0).setVideoConsultCondition("Y");
            }else {
                reviewExpertReserveList.get(0).setVideoConsultCondition("N");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void isConfirmByExpert(ConsultationVO consultationVO,List<ConsultationVO> reviewExpertReserveList) {
        ReviewExpertReserveEntity reserveEntity = new ReviewExpertReserveEntity();
        reserveEntity.setId(Long.valueOf(consultationVO.getId()));
        reserveEntity.setConfirmFlag(1);
        QueryWrapper<ReviewExpertReserveEntity> queryWrapper = QueryGenerator.initQueryWrapper(reserveEntity, null);
        List<ReviewExpertReserveEntity> list = this.list(queryWrapper);
        if (list.size() > 0 && list != null) {
            reviewExpertReserveList.get(0).setIsConfirmByExpert("Y");
        }else {
            reviewExpertReserveList.get(0).setIsConfirmByExpert("N");
        }
    }

    /**
     * 时间处理
     * @param list
     */
    private List<ConsultationVO> beginAndEndTimeUtils(List<ConsultationVO> list) {
        String beginTime = "";
        String endTime = "";
        for (int i = 0; i < list.size(); i++) {
            //处理就诊开始时间和结束时间
            beginTime = list.get(i).getBeginTime();
            endTime = list.get(i).getEndTime();
            beginTime = beginTime.split(":")[0]+":"+beginTime.split(":")[1];
            endTime = endTime.split(":")[0]+":"+endTime.split(":")[1];
            list.get(i).setBeginTime(beginTime);
            list.get(i).setEndTime(endTime);
        }
        return list;
    }
}
