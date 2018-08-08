package com.bwf.hiit.workout.abs.challenge.home.fitness.helpers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.NotificationManager;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.Calendar;

public class BreathRateReminder extends Job {
    public static final String TAG = "breath_rate_reminder_job";

    public static void schedule(Context context) {

        JobManager.instance().cancelAllForTag(TAG);

        long scheduledTime = SharedPrefHelper.getBreathReminderTime(context);
        Calendar scheduledCalendar = Calendar.getInstance();
        scheduledCalendar.setTimeInMillis(scheduledTime);

        Calendar now = Calendar.getInstance();
        scheduledCalendar.set(Calendar.YEAR, now.get(Calendar.YEAR));
        scheduledCalendar.set(Calendar.MONTH, now.get(Calendar.MONTH));
        scheduledCalendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

        int daysAfter = SharedPrefHelper.getBreathReminder(context);

        scheduledCalendar.add(Calendar.DATE, daysAfter);

        long exactMillis = scheduledCalendar.getTimeInMillis() - now.getTimeInMillis();

        if (exactMillis > 0)
            new JobRequest.Builder(TAG)
                    .setPersisted(true)
                    .setExact(exactMillis)
                    .build()
                    .schedule();

    }

    public static void cancel(int jobId) {
        JobManager.instance().cancel(jobId);
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        try {
            NotificationManager.getInstance().generateNotification(getContext(), "Reminder", "Reminder");
            return Result.SUCCESS;
        } catch (Exception e) {
            return Result.SUCCESS;
        } finally {
            schedule(getContext());

        }
    }

}
