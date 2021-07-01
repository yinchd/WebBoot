package com.yinchd.web.utils;


import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author yinchd
 * @version 1.0
 * @description JavaLocalDate和LocalDateTime日期处理类
 * @date 2019/3/17 11:18
 */
public class DateUtils {

    /**
     * 默认时间格式
     */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_PATTERN_YMD = "yyyy-MM-dd";
    /**
     * 默认时间格式格式化工具
     */
    public static final DateTimeFormatter DEFAULT_FORMATER = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);

    /**
     * 获取时间格式化对象
     * @param timeFormat 自定义时间格式
     */
    public static DateTimeFormatter getFormator(String timeFormat) {
        return DateTimeFormatter.ofPattern(timeFormat);
    }

    /**
     * 使用默认时间格式格式化日期
     * @param localDateTime LocalDateTime对象
     */
    public static String format(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "localDateTime不能为空");
        return DEFAULT_FORMATER.format(localDateTime);
    }

    /**
     * 使用自定义时间格式格式化日期
     * @param localDateTime LocalDateTime对象
     * @param timeFormat 自定义时间格式
     */
    public static String format(LocalDateTime localDateTime, String timeFormat) {
        Assert.notNull(localDateTime, "localDateTime不能为空");
        if (timeFormat == null) {
            timeFormat = DEFAULT_PATTERN;
        }
        return getFormator(timeFormat).format(localDateTime);
    }

    /**
     * 格式化LocalDate对象为 yyyy-MM-dd格式
     * @param localDate LocalDate对象
     */
    public static String format(LocalDate localDate) {
        Assert.notNull(localDate, "localDate不能为空");
        return localDate.toString();
    }

    /**
     * 使用自定义日期格式格式化LocalDate对象
     * @param localDate LocalDate对象
     * @param timeFormat 自定义时间格式
     */
    public static String format(LocalDate localDate, String timeFormat) {
        Assert.notNull(localDate, "localDate不能为空");
        if (timeFormat == null) {
            timeFormat = DEFAULT_PATTERN_YMD;
        }
        return getFormator(timeFormat).format(localDate);
    }

    /**
     * 获取传入日期的当年的第一天
     * @param localDate LocalDate对象
     */
    public static LocalDate getFirstDayOfYear(LocalDate localDate) {
        Assert.notNull(localDate, "localDate不能为空");
        return localDate.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取传入日期的当月的第一天
     * @param localDate LocalDate对象
     */
    public static LocalDate getFirstDayOfMonth(LocalDate localDate) {
        Assert.notNull(localDate, "localDate不能为空");
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取传入日期的当年的最后一天
     * @param localDate LocalDate对象
     */
    public static LocalDate getLastDayOfYear(LocalDate localDate) {
        Assert.notNull(localDate, "localDate不能为空");
        return localDate.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 获取传入日期的当月的最后一天
     * @param localDate LocalDate对象
     */
    public static LocalDate getLastDayOfMonth(LocalDate localDate) {
        Assert.notNull(localDate, "localDate不能为空");
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 对LocalDate对象的年、月、日进行加减，
     * @param localDate LocalDate对象
     * @param year 年，可为负
     * @param month 月，可为负
     * @param day 天，可为负
     */
    public static LocalDate plus(LocalDate localDate, int year, int month, int day) {
        Assert.notNull(localDate, "localDate不能为空");
        return localDate.plusYears(year).plusMonths(month).plusDays(day);
    }

    /**
     * 对LocalDateTime对象的年、月、日进行加减，
     * @param localDateTime LocalDateTime对象
     * @param year 年，可为负
     * @param month 月，可为负
     * @param day 天，可为负
     */
    public static LocalDateTime plus(LocalDateTime localDateTime, int year, int month, int day) {
        Assert.notNull(localDateTime, "localDateTime不能为空");
        return localDateTime.plusYears(year).plusMonths(month).plusDays(day);
    }

    /**
     * 根据指定年、月、日值改变LocalDateTime对象
     * @param localDateTime LocalDateTime对象
     * @param year 年
     * @param month 月
     * @param day 日
     */
    public static LocalDateTime change(LocalDateTime localDateTime, int year, int month, int day) {
        Assert.notNull(localDateTime, "localDateTime不能为空");
        year = year > 0 ? year : localDateTime.getYear();
        month = month > 0 ? month : localDateTime.getMonthValue();
        day = day > 0 ? day : localDateTime.getDayOfMonth();
        return localDateTime.withYear(year).withMonth(month).withDayOfMonth(day);
    }

    /**
     * LocalDateTime转Date
     * @param localDateTime LocalDateTime对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "localDateTime不能为空");
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转LocalDateTime
     * @param date date对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        Assert.notNull(date, "date不能为空");
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

}
