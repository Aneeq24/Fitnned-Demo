package com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Weight;
import com.bwf.hiit.workout.abs.challenge.home.fitness.repository.WeightRepo;

import java.util.List;

public class WeightViewModel extends AndroidViewModel {

    private WeightRepo mRepository;
    private LiveData<List<Weight>> mAllWeights;

    public WeightViewModel(Application application) {
        super(application);
        mRepository = new WeightRepo();
        mAllWeights = mRepository.getAllWeights();
    }

    public LiveData<List<Weight>> getAllWeights() {
        return mAllWeights;
    }

    public void insert(Weight weight) {
        mRepository.insert(weight);
    }

}
