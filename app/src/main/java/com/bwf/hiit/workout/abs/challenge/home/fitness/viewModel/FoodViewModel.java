package com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Food;
import com.bwf.hiit.workout.abs.challenge.home.fitness.repository.FoodRepo;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {

    private LiveData<List<Food>> mAllRecords;

    public FoodViewModel(Application application) {
        super(application);
        mAllRecords = new FoodRepo().getAllFoods();
    }

    public LiveData<List<Food>> getAllRecords() {
        return mAllRecords;
    }


}
