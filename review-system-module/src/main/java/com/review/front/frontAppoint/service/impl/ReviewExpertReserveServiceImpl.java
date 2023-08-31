package com.review.front.frontAppoint.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontAppoint.entity.ReviewExpertReserveEntity;
import com.review.front.frontAppoint.mapper.ReviewExpertReserveMapper;
import com.review.front.frontAppoint.service.IReviewExpertReserveService;
import com.review.front.frontAppoint.vo.BeGoodAt;
import com.review.front.frontAppoint.vo.ConsultationVO;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<ConsultationVO> list = reviewExpertReserveMapper.getMyConsultation(userId);
        int cancle = 0;//已取消
        int toBeginCount = 0;//待开始
        int completedCount = 0;//已完成
        int allStatus = 0;//全部
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus() == 1){
                toBeginCount += 1;
                list.get(i).setToBeginCount(toBeginCount);
            }else if (list.get(i).getStatus() == 2) {
                completedCount += 1;
                list.get(i).setCompletedCount(completedCount);
            }else if (list.get(i).getStatus() == 3) {
                cancle += 1;
                list.get(i).setCancle(cancle);
            }
        }
        return list;
    }

    @Override
    public List<ConsultationVO> getMyConsultationDetail(Long id,String userId) {
        Map map = new HashMap();
        map.put("id",id);
        map.put("userId",userId);
        return reviewExpertReserveMapper.getMyConsultationDetail(map);
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

    @Override
    public String getExpertFieldGroup(Integer expertId) {
        return reviewExpertReserveMapper.getExpertFieldGroup(expertId);
    }

    @Override
    public String getDictId(String expertFieldGroup) {
        return reviewExpertReserveMapper.getDictId(expertFieldGroup);
    }

    @Override
    public List<BeGoodAt> getDictText(String dictId,List<Integer> dictIdList) {
        return reviewExpertReserveMapper.getDictText(dictId,dictIdList);
    }

    @Override
    public List<BeGoodAt> getBeGoodAtList(Integer expertId) {
        return reviewExpertReserveMapper.getBeGoodAtList(expertId);
    }

    @Override
    public List<BeGoodAt> getBeGoodAtNameList(List<String> beGoodAtListNew) {
        return reviewExpertReserveMapper.getBeGoodAtNameList(beGoodAtListNew);
    }

    @Override
    public void isCancle(List<ConsultationVO> reviewExpertReserveList){
        if (reviewExpertReserveList != null && reviewExpertReserveList.size() > 0) {
            try {
                String visitDate = reviewExpertReserveList.get(0).getVisitDate();
                String beginTime = reviewExpertReserveList.get(0).getBeginTime();
                String visitTime = visitDate + " " + beginTime + ":00";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date visit = format.parse(visitTime);
                Date now = new Date();
                long nd = 1000 * 24 * 60 * 60;
                long nh = 1000 * 60 * 60;
                long threeHour = 1000 * 60 * 60 * 3;
                long diff = threeHour - visit.getTime() + now.getTime();
                long hour = diff % nd / nh;
                reviewExpertReserveList.get(0).setIsCancle(0);
                System.out.println(hour);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
