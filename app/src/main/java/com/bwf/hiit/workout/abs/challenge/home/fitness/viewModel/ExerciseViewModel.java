package com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.repository.ExerciseDayRepo;
import com.bwf.hiit.workout.abs.challenge.home.fitness.repository.ExerciseRepo;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {

    private ExerciseRepo mRepository;

    public ExerciseViewModel(Application application) {
        super(application);
        mRepository = new ExerciseRepo();
    }

    public LiveData<Exercise> getExerciseDays(int cid) {
        return mRepository.getExercise(cid);
    }

    public void insert(Exercise exercise) {
        mRepository.insert(exercise);
    }

}
