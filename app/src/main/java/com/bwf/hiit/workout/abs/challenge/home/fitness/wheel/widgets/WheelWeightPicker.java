package com.bwf.hiit.workout.abs.challenge.home.fitness.wheel.widgets;

import android.content.Context;

import android.util.AttributeSet;

import com.bwf.hiit.workout.abs.challenge.home.fitness.wheel.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class WheelWeightPicker extends WheelPicker implements IWheelWeightPicker {
    private int mSelectedMonth;

    public WheelWeightPicker(Context context) {
        this(context, null);
    }

    public WheelWeightPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        List<Integer> data = new ArrayList<>();
        for (int i = 0; i <= 100; i++)
            data.add(i);
        super.setData(data);

        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        updateSelectedYear();
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(mSelectedMonth - 1);
    }


    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    @Override
    public int getSelectedWeight() {
        return mSelectedMonth;
    }

    @Override
    public void setSelectedWeight(int month) {
        mSelectedMonth = month;
        updateSelectedYear();
    }

    @Override
    public int getCurrentWeight() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition())));
    }
}
