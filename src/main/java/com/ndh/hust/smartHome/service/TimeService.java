package com.ndh.hust.smartHome.service;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class TimeService {

    public Date getTimeNow() {
        return new Date();
    }

    public Date getTimeNowInHour() {
        return  DateUtils.round(new Date(), Calendar.HOUR);
    }

    public String getTimeString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public Date getTimeInPreviousHour(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        Date dateRet = DateUtils.setHours(date, cal.get(Calendar.HOUR_OF_DAY) - 1 );
        return dateRet;
    }

    public Date getTimeYesterday(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public Date startOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime start = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(start);
    }

    public Date endOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime end = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(end);
    }
}
