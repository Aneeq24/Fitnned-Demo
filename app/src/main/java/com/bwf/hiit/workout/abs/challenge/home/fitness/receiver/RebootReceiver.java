package com.bwf.hiit.workout.abs.challenge.home.fitness.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AlarmManager;


public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharedPrefHelper.readBoolean(context,context.getString(R.string.alarm))) {
            int hour = SharedPrefHelper.readInteger(context,context.getString(R.string.hour));
            int minute = SharedPrefHelper.readInteger(context,context.getString(R.string.minute));
            AlarmManager.getInstance().setAlarm(context, hour, minute);
        }
    }
}
