package com.bwf.hiit.workout.abs.challenge.home.fitness.utils;

import android.content.Context;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DateUtils {

    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        setMidnight(calendar);

        return calendar;
    }

    public static void setMidnight(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
    }

    public static boolean isMonthBefore(Calendar firstCalendar, Calendar secondCalendar) {
        if (firstCalendar == null) {
            return false;
        }

        Calendar firstDay = (Calendar) firstCalendar.clone();
        setMidnight(firstDay);
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        Calendar secondDay = (Calendar) secondCalendar.clone();
        setMidnight(secondDay);
        secondDay.set(Calendar.DAY_OF_MONTH, 1);

        return secondDay.before(firstDay);
    }


    public static boolean isMonthAfter(Calendar firstCalendar, Calendar secondCalendar) {
        if (firstCalendar == null) {
            return false;
        }

        Calendar firstDay = (Calendar) firstCalendar.clone();
        setMidnight(firstDay);
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        Calendar secondDay = (Calendar) secondCalendar.clone();
        setMidnight(secondDay);
        secondDay.set(Calendar.DAY_OF_MONTH, 1);

        return secondDay.after(firstDay);
    }


    public static String getMonthAndYearDate(Context context, Calendar calendar) {
        return String.format("%s  %s",
                context.getResources().getStringArray(R.array.material_calendar_months_array)[calendar.get(Calendar.MONTH)],
                calendar.get(Calendar.YEAR));
    }


    public static ArrayList<Calendar> getDatesRange(Calendar firstDay, Calendar lastDay) {
        if (lastDay.before(firstDay)) {
            return getCalendarsBetweenDates(lastDay.getTime(), firstDay.getTime());
        }

        return getCalendarsBetweenDates(firstDay.getTime(), lastDay.getTime());
    }

    private static ArrayList<Calendar> getCalendarsBetweenDates(Date dateFrom, Date dateTo) {
        ArrayList<Calendar> calendars = new ArrayList<>();

        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(dateFrom);

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(dateTo);

        long daysBetweenDates = TimeUnit.MILLISECONDS.toDays(
                calendarTo.getTimeInMillis() - calendarFrom.getTimeInMillis());

        for (int i = 1; i < daysBetweenDates; i++) {
            Calendar calendar = (Calendar) calendarFrom.clone();
            calendars.add(calendar);
            calendar.add(Calendar.DATE, i);
        }

        return calendars;
    }

    public static int getMonthsBetweenDates(Calendar startCalendar, Calendar endCalendar) {
        int years = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        return years * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }
}
