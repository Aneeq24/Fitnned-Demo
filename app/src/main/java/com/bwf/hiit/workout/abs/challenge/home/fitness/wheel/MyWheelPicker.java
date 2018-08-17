package com.bwf.hiit.workout.abs.challenge.home.fitness.wheel;

import android.content.Context;

import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MyWheelPicker extends WheelPicker implements IMyWheelPicker {
    private int mSelectedMonth;

    public MyWheelPicker(Context context) {
        this(context, null);
    }

    public MyWheelPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<Integer> data = new ArrayList<>();
        for (int i = 0; i <= 10; i++)
            data.add(i);

        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        updateSelectedYear();
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(mSelectedMonth - 1);
    }

    @Override
    public void setData(List data) {
        super.setData(data);
    }

    @Override
    public void setValue(int month) {
        mSelectedMonth = month;
        updateSelectedYear();
    }

    @Override
    public int getValue() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}
