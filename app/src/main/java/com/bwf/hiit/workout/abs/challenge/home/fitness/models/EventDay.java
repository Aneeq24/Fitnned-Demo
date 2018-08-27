package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.support.annotation.RestrictTo;
import java.util.Calendar;

public class EventDay {
    private Calendar mDay;
    private boolean mIsDisabled;

    public EventDay(Calendar day) {
        mDay = day;
    }

    public Calendar getCalendar() {
        return mDay;
    }

    public boolean isEnabled() {
        return !mIsDisabled;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void setEnabled(boolean enabled) {
        mIsDisabled = enabled;
    }
}
