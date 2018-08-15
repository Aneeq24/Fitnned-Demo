package com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Reminder;
import com.bwf.hiit.workout.abs.challenge.home.fitness.repository.ReminderRepo;

public class ReminderViewModel extends AndroidViewModel {

    private ReminderRepo mRepository;

    public ReminderViewModel(Application application) {
        super(application);
        mRepository = new ReminderRepo();
    }

    public LiveData<Reminder> getReminder() {
        return mRepository.getReminder(1);
    }

    public void insert(Reminder reminder) {
        mRepository.insert(reminder);
    }

    public void update(Reminder reminder) {
        mRepository.update(reminder);
    }

}