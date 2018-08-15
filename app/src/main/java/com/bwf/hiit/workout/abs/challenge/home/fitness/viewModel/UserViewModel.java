package com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.repository.UserRepo;


public class UserViewModel extends AndroidViewModel {

    private UserRepo mRepository;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepo();
    }

    public LiveData<User> getUser() {
        return mRepository.getUser(1);
    }

    public void insert(User user) {
        mRepository.insert(user);
    }

    public void update(User user) {
        mRepository.update(user);
    }

}