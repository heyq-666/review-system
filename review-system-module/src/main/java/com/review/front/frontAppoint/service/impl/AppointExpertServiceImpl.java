package com.review.front.frontAppoint.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontAppoint.mapper.AppointExpertMapper;
import com.review.front.frontAppoint.service.IAppointExpertService;
import com.review.front.frontAppoint.vo.ConsultationVO;
import com.review.manage.expert.entity.ReviewExpert;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author javabage
 * @date 2023/4/4
 */
@Service
public class AppointExpertServiceImpl extends ServiceImpl<AppointExpertMapper, ReviewExpert>
        implements IAppointExpertService {

    @Autowired
    private AppointExpertMapper appointExpertMapper;

    @Override
    public List<ReviewExpertCalendarVo> getReviewExpertCalendars(ReviewExpertCalendarVo reviewExpertCalendar) {
        List<ReviewExpertCalendarVo> list = appointExpertMapper.getReviewExpertCalendars(reviewExpertCalendar.getExpertId());
        handleCalendarTime(list);
        return list;
    }

    @Override
    public List<ConsultationVO> getMyAppointList(String expertPhone) {
        return appointExpertMapper.getMyAppointList(expertPhone);
    }

    @Override
    public void handleTime(List<ConsultationVO> list) {
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
    }

    public void handleCalendarTime(List<ReviewExpertCalendarVo> reviewExpertCalendarList) {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < reviewExpertCalendarList.size(); i++) {
            if (reviewExpertCalendarList.get(i).getVisitDate().equals(formatter.format(new Date()))){
                int beginTimeHour = Integer.valueOf(reviewExpertCalendarList.get(i).getBeginTime().split(":")[0]);
                if(beginTimeHour < hour){
                    reviewExpertCalendarList.remove(i);
                    i--;
                    continue;
                }
            }
        }
    }
}
