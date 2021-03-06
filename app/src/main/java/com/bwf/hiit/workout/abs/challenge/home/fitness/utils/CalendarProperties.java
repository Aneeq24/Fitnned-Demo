package com.bwf.hiit.workout.abs.challenge.home.fitness.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.annimon.stream.Stream;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.interfaces.OnCalendarPageChangeListener;
import com.bwf.hiit.workout.abs.challenge.home.fitness.interfaces.OnDayClickListener;
import com.bwf.hiit.workout.abs.challenge.home.fitness.interfaces.OnSelectionAbilityListener;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarProperties {
    private int mCalendarType, mHeaderColor, mHeaderLabelColor, mSelectionColor, mTodayLabelColor
            , mItemLayoutResource, mDisabledDaysLabelsColor, mPagesColor, mAbbreviationsBarColor,
            mAbbreviationsLabelsColor, mDaysLabelsColor, mSelectionLabelColor,
            mAnotherMonthsDaysLabelsColor;

    private Drawable mPreviousButtonSrc, mForwardButtonSrc;

    private Calendar mCurrentDate = DateUtils.getCalendar();
    private Calendar mSelectedDate = DateUtils.getCalendar();
    private Calendar mCalendar, mMinimumDate, mMaximumDate;

    private OnDayClickListener mOnDayClickListener;
    private OnSelectionAbilityListener mOnSelectionAbilityListener;
    private OnCalendarPageChangeListener mOnPreviousPageChangeListener;
    private OnCalendarPageChangeListener mOnForwardPageChangeListener;

    private List<EventDay> mEventDays = new ArrayList<>();
    private List<Calendar> mDisabledDays = new ArrayList<>();

    private Context mContext;

    public CalendarProperties(Context context) {
        mContext = context;
    }

    public int getCalendarType() {
        return mCalendarType;
    }

    public void setCalendarType(int calendarType) {
        mCalendarType = calendarType;
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    public void setCalendar(Calendar calendar) {
        mCalendar = calendar;
    }

    public int getHeaderColor() {
        if (mHeaderColor <= 0) {
            return mHeaderColor;
        }

        return ContextCompat.getColor(mContext, mHeaderColor);
    }

    public void setHeaderColor(int headerColor) {
        mHeaderColor = headerColor;
    }

    public int getHeaderLabelColor() {
        if (mHeaderLabelColor <= 0) {
            return mHeaderLabelColor;
        }

        return ContextCompat.getColor(mContext, mHeaderLabelColor);
    }

    public void setHeaderLabelColor(int headerLabelColor) {
        mHeaderLabelColor = headerLabelColor;
    }

    public Drawable getPreviousButtonSrc() {
        return mPreviousButtonSrc;
    }

    public void setPreviousButtonSrc(Drawable previousButtonSrc) {
        mPreviousButtonSrc = previousButtonSrc;
    }

    public Drawable getForwardButtonSrc() {
        return mForwardButtonSrc;
    }

    public void setForwardButtonSrc(Drawable forwardButtonSrc) {
        mForwardButtonSrc = forwardButtonSrc;
    }

    public int getSelectionColor() {
        if (mSelectionColor == 0) {
            return ContextCompat.getColor(mContext, R.color.defaultColor);
        }

        return mSelectionColor;
    }

    public void setSelectionColor(int selectionColor) {
        mSelectionColor = selectionColor;
    }

    public int getTodayLabelColor() {
        if (mTodayLabelColor == 0) {
            return ContextCompat.getColor(mContext, R.color.defaultColor);
        }

        return mTodayLabelColor;
    }

    public void setTodayLabelColor(int todayLabelColor) {
        mTodayLabelColor = todayLabelColor;
    }

    public Calendar getMinimumDate() {
        return mMinimumDate;
    }

    public void setMinimumDate(Calendar minimumDate) {
        mMinimumDate = minimumDate;
    }

    public Calendar getMaximumDate() {
        return mMaximumDate;
    }

    public void setMaximumDate(Calendar maximumDate) {
        mMaximumDate = maximumDate;
    }

    public OnSelectionAbilityListener getOnSelectionAbilityListener() {
        return mOnSelectionAbilityListener;
    }

    public int getItemLayoutResource() {
        return mItemLayoutResource;
    }

    public void setItemLayoutResource(int itemLayoutResource) {
        mItemLayoutResource = itemLayoutResource;
    }

    public OnCalendarPageChangeListener getOnPreviousPageChangeListener() {
        return mOnPreviousPageChangeListener;
    }

    public void setOnPreviousPageChangeListener(OnCalendarPageChangeListener onPreviousButtonClickListener) {
        mOnPreviousPageChangeListener = onPreviousButtonClickListener;
    }

    public OnCalendarPageChangeListener getOnForwardPageChangeListener() {
        return mOnForwardPageChangeListener;
    }

    public void setOnForwardPageChangeListener(OnCalendarPageChangeListener onForwardButtonClickListener) {
        mOnForwardPageChangeListener = onForwardButtonClickListener;
    }

    public Calendar getCurrentDate() {
        return mCurrentDate;
    }

    public Calendar getSelectedDate() {
        return mSelectedDate;
    }

    public OnDayClickListener getOnDayClickListener() {
        return mOnDayClickListener;
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        mOnDayClickListener = onDayClickListener;
    }

    public List<EventDay> getEventDays() {
        return mEventDays;
    }

    public void setEventDays(List<EventDay> eventDays) {
        mEventDays = eventDays;
    }

    public List<Calendar> getDisabledDays() {
        return mDisabledDays;
    }

    public void setDisabledDays(List<Calendar> disabledDays) {
        mDisabledDays = Stream.of(disabledDays).map(calendar -> {
            DateUtils.setMidnight(calendar);
            return calendar;
        }).toList();
    }

    public int getDisabledDaysLabelsColor() {
        if (mDisabledDaysLabelsColor == 0) {
            return ContextCompat.getColor(mContext, R.color.nextMonthDayColor);
        }

        return mDisabledDaysLabelsColor;
    }

    public void setDisabledDaysLabelsColor(int disabledDaysLabelsColor) {
        mDisabledDaysLabelsColor = disabledDaysLabelsColor;
    }

    public int getPagesColor() {
        return mPagesColor;
    }

    public void setPagesColor(int pagesColor) {
        mPagesColor = pagesColor;
    }

    public int getAbbreviationsBarColor() {
        return mAbbreviationsBarColor;
    }

    public void setAbbreviationsBarColor(int abbreviationsBarColor) {
        mAbbreviationsBarColor = abbreviationsBarColor;
    }

    public int getAbbreviationsLabelsColor() {
        return mAbbreviationsLabelsColor;
    }

    public void setAbbreviationsLabelsColor(int abbreviationsLabelsColor) {
        mAbbreviationsLabelsColor = abbreviationsLabelsColor;
    }

    public int getDaysLabelsColor() {
        if (mDaysLabelsColor == 0) {
            return ContextCompat.getColor(mContext, R.color.currentMonthDayColor);
        }

        return mDaysLabelsColor;
    }

    public void setDaysLabelsColor(int daysLabelsColor) {
        mDaysLabelsColor = daysLabelsColor;
    }

    public int getSelectionLabelColor() {
        if (mSelectionLabelColor == 0) {
            return ContextCompat.getColor(mContext, android.R.color.white);
        }

        return mSelectionLabelColor;
    }

    public void setSelectionLabelColor(int selectionLabelColor) {
        mSelectionLabelColor = selectionLabelColor;
    }

    public int getAnotherMonthsDaysLabelsColor() {
        if (mAnotherMonthsDaysLabelsColor == 0) {
            return ContextCompat.getColor(mContext, R.color.nextMonthDayColor);
        }

        return mAnotherMonthsDaysLabelsColor;
    }

    public void setAnotherMonthsDaysLabelsColor(int anotherMonthsDaysLabelsColor) {
        mAnotherMonthsDaysLabelsColor = anotherMonthsDaysLabelsColor;
    }

    public void setmOnSelectionAbilityListener(OnSelectionAbilityListener mOnSelectionAbilityListener) {
        this.mOnSelectionAbilityListener = mOnSelectionAbilityListener;
    }
}
