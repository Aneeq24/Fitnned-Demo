package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.RecordAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.exceptions.OutOfDateRangeException;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.CalendarView;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.DateUtils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.SelectedDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.RecordViewModel;

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
    RecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        ButterKnife.bind(this);
        RecordViewModel mViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);

        mAdapter = new RecordAdapter();
        rvRecords.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvRecords.setAdapter(mAdapter);

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

        mViewModel.getAllRecords().observe(this, records -> {
            if (records != null) {
                mAdapter.setRecordList(records);
                setCalander(records);
            }
        });
    }

    private void setCalander(List<Record> records) {

        List<SelectedDay> events = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            Calendar calSet = DateUtils.getCalendar();
            int day = calSet.get(Calendar.DAY_OF_MONTH);
            if (day < Integer.parseInt(records.get(i).getDay()))
                calSet.add(Calendar.DAY_OF_MONTH, (day - Integer.parseInt(records.get(i).getDay())));
            else {
                int sel = (-Integer.parseInt(records.get(i).getDay()) + day) * -1;
                calSet.add(Calendar.DAY_OF_MONTH, sel);
            }
            events.add(new SelectedDay(calSet));
        }

        calendarView.setSelectedDates(events);
    }

}
