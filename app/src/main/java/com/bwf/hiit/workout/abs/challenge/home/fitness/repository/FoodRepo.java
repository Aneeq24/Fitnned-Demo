package com.bwf.hiit.workout.abs.challenge.home.fitness.repository;

import android.arch.lifecycle.LiveData;

import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.FoodDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Food;

import java.util.List;

public class FoodRepo {

    private FoodDao foodDao;

    public FoodRepo() {
        AppDataBase db = AppDataBase.getInstance();
        foodDao = db.foodDao();
    }

    public LiveData<List<Food>> getAllFoods() {
        return foodDao.getAllFood();
    }

}