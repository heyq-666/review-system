package com.review.manage.expert.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.expert.entity.ReviewExpertCalendarEntity;
import com.review.manage.expert.mapper.ReviewExpertCalendarMapper;
import com.review.manage.expert.service.IReviewExpertCalendarService;
import com.review.manage.expert.vo.ReviewExpertCalendarVo;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author javabage
 * @date 2023/3/28
 */
@Service
public class ReviewExpertCalendarImpl extends ServiceImpl<ReviewExpertCalendarMapper, ReviewExpertCalendarEntity> implements IReviewExpertCalendarService {

    @Override
    public void handleCalendarData(ReviewExpertCalendarVo reviewExpertCalendarVo) {
        LoginUser user = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        //处理周期
        for (int i = 0; i < reviewExpertCalendarVo.getCycle(); i++) {
            ReviewExpertCalendarEntity reviewExpertCalendarEntity = new ReviewExpertCalendarEntity();
            reviewExpertCalendarEntity.setExpertId(reviewExpertCalendarVo.getId());
            reviewExpertCalendarEntity.setCycle(reviewExpertCalendarVo.getCycle());
            reviewExpertCalendarEntity.setVisitDate(this.weekHandle(reviewExpertCalendarVo.getVisitDate(),i));
            reviewExpertCalendarEntity.setWeekDay(this.getWeekOfDate(reviewExpertCalendarEntity.getVisitDate()));
            reviewExpertCalendarEntity.setCreator(user.getUsername());
            //处理时间段
            String timeSlot = reviewExpertCalendarVo.getTimeSlot();
            String[] timeSlotSplit = timeSlot.split(",");
            for (int j = 0; j < timeSlotSplit.length; j++) {
                reviewExpertCalendarEntity.setId(null);
                reviewExpertCalendarEntity.setBeginTime(Time.valueOf(timeSlotSplit[j].split("-")[0].trim() + ":00"));
                reviewExpertCalendarEntity.setEndTime(Time.valueOf(timeSlotSplit[j].split("-")[1].trim() + ":00"));
                this.save(reviewExpertCalendarEntity);
            }
        }
    }

    /**
     * 获取当前日期下几周的具体日期
     * @param date
     * @param cycle
     * @return
     */
    public Date weekHandle(String date,int cycle){
        LocalDate now = LocalDate.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nextWeek = now.plusWeeks(cycle);
        return Date.from(nextWeek.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时间对应的星期几
     * @param date
     * @return
     */
    public int getWeekOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0 ){
            week = 0;
        }
        return week;
    }
}
