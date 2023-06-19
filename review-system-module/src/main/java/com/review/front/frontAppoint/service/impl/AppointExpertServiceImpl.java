package com.review.front.frontAppoint.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.frontAppoint.mapper.AppointExpertMapper;
import com.review.front.frontAppoint.service.IAppointExpertService;
import com.review.front.frontAppoint.vo.ConsultationVO;
import com.review.front.frontAppoint.vo.VisitDate;
import com.review.manage.expert.entity.ReviewExpert;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        List<ReviewExpertCalendarVo> list1 = handleCalendarTime(list);
        return list1;
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

    public List<ReviewExpertCalendarVo> handleCalendarTime(List<ReviewExpertCalendarVo> reviewExpertCalendarList) {
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
            reviewExpertCalendarList.get(i).setVisitDateNew(reviewExpertCalendarList.get(i).getVisitDateMonth()
                    + "月" + reviewExpertCalendarList.get(i).getVisitDateDay()
                    + "日" + "(" + reviewExpertCalendarList.get(i).getWeekDayName() + ")");
        }
        List<ReviewExpertCalendarVo> list1 = reviewExpertCalendarList.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ReviewExpertCalendarVo::getVisitDate))), ArrayList::new));

        List<ReviewExpertCalendarVo> list2 = new ArrayList<>();
        List<ReviewExpertCalendarVo> list3 = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            String vv = list1.get(i).getVisitDate();
            ReviewExpertCalendarVo calenda = new ReviewExpertCalendarVo();
            calenda.setVisitDateNew(list1.get(i).getVisitDateNew());
            list2 = reviewExpertCalendarList.stream().filter(ReviewExpertCalendarVo -> vv.equals(ReviewExpertCalendarVo.getVisitDate())).collect(Collectors.toList());
            List<VisitDate> visitDateList = new ArrayList<>();
            for (int j = 0; j < list2.size(); j++) {
                VisitDate visitDate = new VisitDate();
                visitDate.setTime(list2.get(j).getBeginTime() + "-" + list2.get(j).getEndTime());
                visitDate.setIsChooseFlag(list2.get(j).getStatus() == 1 ? true : false);
                visitDateList.add(visitDate);
            }
            calenda.setVisitDateList(visitDateList);
            list3.add(calenda);
        }
        return list3;
    }
}
