package com.bwf.hiit.workout.abs.challenge.home.fitness.helpers;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class MyJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case BreathRateReminder.TAG:
                return new BreathRateReminder();
            default:
                return null;

        }
    }
}
