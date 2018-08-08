package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.BreathRateReminder;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.NotificationManager;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReminderSetActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final int REMINDER_DAILY = 1;
    public static final int REMINDER_WEEKLY = 7;
    public static final int REMINDER_NONE = -1;

    int reminder = REMINDER_NONE;

    @BindView(R.id.reminderScreenTime)
    EditText edtReminderTime;
    @BindView(R.id.reminderScreenTime2)
    EditText edtReminderDaTe;

    Calendar reminderDateTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_set);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.setbuttonReminderScreen, R.id.reminderScreenTime, R.id.reminderScreenTime2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setbuttonReminderScreen:
                NotificationManager.getInstance().generateNotification(ReminderSetActivity.this, "Reminder", "Reminder");
                BreathRateReminder.schedule(ReminderSetActivity.this);
                finish();
                break;
            case R.id.reminderScreenTime2:
                selectDate();
                break;
            case R.id.reminderScreenTime:
                selectTime();
                break;
        }
    }

    private void selectDate() {

        Calendar now = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(ReminderSetActivity.this, this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void selectTime() {
        Calendar now = Calendar.getInstance();

        TimePickerDialog dialog = new TimePickerDialog(ReminderSetActivity.this, this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        reminderDateTime.set(Calendar.YEAR, year);
        reminderDateTime.set(Calendar.MONTH, month + 1);
        reminderDateTime.set(Calendar.DAY_OF_MONTH, day);
        selectTime();
        edtReminderDaTe.setText(String.valueOf(year + " - " + month + " - " + day));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        reminderDateTime.set(Calendar.HOUR_OF_DAY, hour);
        reminderDateTime.set(Calendar.MINUTE, min);
        reminderDateTime.set(Calendar.SECOND, 0);

        edtReminderTime.setText(String.valueOf(hour + " : " + min));

        SharedPrefHelper.setBreathReminder(ReminderSetActivity.this, reminder);
        SharedPrefHelper.setBreathReminderTime(ReminderSetActivity.this, reminderDateTime.getTimeInMillis());

    }
}
