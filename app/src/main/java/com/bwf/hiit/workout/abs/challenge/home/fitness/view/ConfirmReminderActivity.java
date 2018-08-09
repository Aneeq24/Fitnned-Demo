package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AlarmManager;

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
    @BindView(R.id.btn_on_reminder)
    ImageView btnOnReminder;
    @BindView(R.id.btn_delete_reminder)
    ImageView btnDeleteReminder;
    DayAdapter mAdapter;
    Calendar reminderDateTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reminder);
        ButterKnife.bind(this);
        context = this;

        setDaysData();

        if (SharedPrefHelper.readBoolean(context, "reminder"))
            btnOnReminder.setImageResource(R.drawable.reminder_screen_check_on_icon);
        else
            btnOnReminder.setImageResource(R.drawable.reminder_screen_check_off_icon);

        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @OnClick({R.id.btn_on_reminder, R.id.txt_repeat, R.id.btn_delete_reminder, R.id.txt_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_on_reminder:
                if (SharedPrefHelper.readBoolean(context, "reminder"))
                    SharedPrefHelper.writeBoolean(context, "reminder", false);
                else
                    SharedPrefHelper.writeBoolean(context, "reminder", true);

                if (SharedPrefHelper.readBoolean(context, "reminder"))
                    btnOnReminder.setImageResource(R.drawable.reminder_screen_check_on_icon);
                else
                    btnOnReminder.setImageResource(R.drawable.reminder_screen_check_off_icon);
                break;
            case R.id.txt_repeat:
                setDays();
                break;
            case R.id.btn_delete_reminder:
                break;
            case R.id.txt_time:
                selectTime();
                break;
        }
    }

    private void getDays() {
        titles = new ArrayList<>();
    }

    private void setDaysData() {
        getDays();
        mAdapter = new DayAdapter(titles);
        rvDays.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvDays.setAdapter(mAdapter);
    }

    private void setDays() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Days");

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        boolean[] sel = {false, false, false, false, false, false, false};
        builder.setMultiChoiceItems(days, sel, (dialogInterface, i, b) -> {
            if (!b)
                titles.remove(i);
            else titles.add(day[i]);
        });

        AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Done", (dialogInterface, i) -> {
            mAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void selectTime() {
        Calendar now = Calendar.getInstance();

        @SuppressLint("SetTextI18n") TimePickerDialog dialog = new TimePickerDialog(context, (timePicker, hour, min) -> {
            txtTime.setText(hour + " : " + min);
            reminderDateTime.set(Calendar.HOUR_OF_DAY, hour);
            reminderDateTime.set(Calendar.MINUTE, min);
            reminderDateTime.set(Calendar.SECOND, 0);
            AlarmManager.getInstance().setAlarm(this, reminderDateTime);
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
}
