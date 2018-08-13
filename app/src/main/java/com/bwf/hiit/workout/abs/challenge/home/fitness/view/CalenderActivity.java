package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.RecordAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.exceptions.OutOfDateRangeException;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.CalendarView;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.DateUtils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.SelectedDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalenderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.rv_records)
    RecyclerView rvRecords;
    List<Record> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        ButterKnife.bind(this);
        recordList = new ArrayList<>();

        Calendar calNow = Calendar.getInstance();
        try {
            calendarView.setDate(calNow);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -2);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 2);

        calendarView.setMinimumDate(min);
        calendarView.setMaximumDate(max);

        toolbar.setNavigationOnClickListener(view1 -> finish());

        new getUserRecords().execute();
    }

    private void initApp() {
        setCalander();
        rvRecords.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecordAdapter mAdapter = new RecordAdapter(recordList);
        rvRecords.setAdapter(mAdapter);
    }

    private void setCalander() {
        List<SelectedDay> events = new ArrayList<>();

        for (int i = 0; i < recordList.size(); i++) {
            Calendar calSet = DateUtils.getCalendar();

            int day = calSet.get(Calendar.DAY_OF_MONTH);
            if (day > Integer.parseInt(recordList.get(i).getDay()))
                calSet.add(Calendar.DAY_OF_MONTH, (day - Integer.parseInt(recordList.get(i).getDay())));
            else
                calSet.add(Calendar.DAY_OF_MONTH, (day - Integer.parseInt(recordList.get(i).getDay())));
            events.add(new SelectedDay(calSet));
        }
        calendarView.setSelectedDates(events);
    }

    @SuppressLint("StaticFieldLeak")
    private class getUserRecords extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recordList = AppDataBase.getInstance().recorddao().getAllRecords();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            initApp();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
