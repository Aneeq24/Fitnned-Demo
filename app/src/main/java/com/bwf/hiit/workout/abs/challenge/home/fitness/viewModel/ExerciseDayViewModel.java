package com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.repository.ExerciseDayRepo;

import java.util.List;

public class ExerciseDayViewModel extends AndroidViewModel {

    private ExerciseDayRepo mRepository;

    public ExerciseDayViewModel(Application application) {
        super(application);
        mRepository = new ExerciseDayRepo();
    }

    public LiveData<List<ExerciseDay>> getExerciseDays(int planId, int dayId) {
        return mRepository.getLiveExerciseDays(planId, dayId);
    }

    public void insert(ExerciseDay exerciseDay) {
        mRepository.insert(exerciseDay);
    }

}
