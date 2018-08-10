package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AlarmManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Reminder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmReminderActivity extends AppCompatActivity {

    String[] day = {"Sun,", "Mon,", "Tue,", "Wed,", "Thu,", "Fri,", "Sat"};

    List<String> titles;

    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.rv_days)
    RecyclerView rvDays;

    Context context;
    @BindView(R.id.toolbar10)
    Toolbar toolbar;
    @BindView(R.id.btn_add_reminder)
    Switch btnAddReminder;

    Reminder reminder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reminder);
        ButterKnife.bind(this);
        context = this;

        new getUserReminder().execute();

        if (SharedPrefHelper.readBoolean(context, "reminder"))
            btnAddReminder.setChecked(true);
        else
            btnAddReminder.setChecked(false);

        int hour = SharedPrefHelper.readInteger(context, getString(R.string.hour));
        int min = SharedPrefHelper.readInteger(context, getString(R.string.minute));
        @SuppressLint("DefaultLocale") String timeString = String.format("%02d:%02d",hour, min);
        txtTime.setText(timeString);

        btnAddReminder.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                SharedPrefHelper.writeBoolean(context, "reminder", true);
            else
                SharedPrefHelper.writeBoolean(context, "reminder", false);
        });

        toolbar.setNavigationOnClickListener(view -> finish());

        LinearLayout fbNative = findViewById(R.id.fbNative);

        AdsManager.getInstance().showFacebookNativeAd(Application.getContext(), fbNative, null);

    }

    @OnClick({R.id.lr_repeat, R.id.txt_repeat, R.id.txt_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lr_repeat:
                setDays();
                break;
            case R.id.txt_repeat:
                setDays();
                break;
            case R.id.txt_time:
                selectTime();
                break;
        }
    }

    private void getDays() {
        titles = new ArrayList<>();
        if (reminder.isSunday())
            titles.add(day[0]);
        if (reminder.isMonday())
            titles.add(day[1]);
        if (reminder.isTuesday())
            titles.add(day[2]);
        if (reminder.isWednesday())
            titles.add(day[3]);
        if (reminder.isThursday())
            titles.add(day[4]);
        if (reminder.isFriday())
            titles.add(day[5]);
        if (reminder.isSatday())
            titles.add(day[6]);
    }

    private void setDaysData() {
        getDays();
        rvDays.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvDays.setAdapter(new DayAdapter(titles));
    }

    private void setDays() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Days");

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        boolean[] sel = {reminder.isSunday(), reminder.isMonday(), reminder.isTuesday(), reminder.isWednesday(), reminder.isThursday(), reminder.isFriday(), reminder.isSatday()};
        builder.setMultiChoiceItems(days, sel, (dialogInterface, i, b) -> {
            if (i == 0)
                reminder.setSunday(b);
            if (i == 1)
                reminder.setMonday(b);
            if (i == 2)
                reminder.setTuesday(b);
            if (i == 3)
                reminder.setWednesday(b);
            if (i == 4)
                reminder.setThursday(b);
            if (i == 5)
                reminder.setFriday(b);
            if (i == 6)
                reminder.setSatday(b);

        });

        AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Done", (dialogInterface, i) -> {
            new setUserReminder().execute();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void selectTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(context, (timePicker, hour, min) -> {
            @SuppressLint("DefaultLocale") String timeString = String.format("%02d:%02d",hour, min);
            txtTime.setText(timeString);
            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY,hour);
            calSet.set(Calendar.MINUTE, min);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            SharedPrefHelper.writeInteger(context, getString(R.string.hour), hour);
            SharedPrefHelper.writeInteger(context, getString(R.string.minute), min);
            AlarmManager.getInstance().setAlarm(this, calSet);
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        dialog.show();
    }

    class DayAdapter extends RecyclerView.Adapter<DayAdapter.MainMenuItemHolder> {

        private List<String> tilte;

        DayAdapter(List<String> tilte) {
            this.tilte = tilte;
        }

        @NonNull
        @Override
        public MainMenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false);
            return new MainMenuItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainMenuItemHolder holder, final int position) {
            holder.tvTitle.setText(tilte.get(position));
            holder.itemView.setOnClickListener(view -> setDays());
        }

        @Override
        public int getItemCount() {
            return tilte.size();
        }

        class MainMenuItemHolder extends RecyclerView.ViewHolder {

            TextView tvTitle;

            MainMenuItemHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class getUserReminder extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            reminder = AppDataBase.getInstance().reminderDao().findById(1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setDaysData();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class setUserReminder extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDataBase.getInstance().reminderDao().updateReminder(reminder);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setDaysData();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
