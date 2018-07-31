package com.bwf.hiit.workout.abs.challenge.home.fitness.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.SharedPreferencesManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.AlarmManager;



public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharedPreferencesManager.getInstance().getBoolean(context.getString(R.string.alarm))) {
            int hour = Integer.parseInt(SharedPreferencesManager.getInstance().getString(context.getString(R.string.hour)));
            int minute = Integer.parseInt(SharedPreferencesManager.getInstance().getString(context.getString(R.string.minute)));
            AlarmManager.getInstance().setAlarm(context, hour, minute);
        }
    }
}
