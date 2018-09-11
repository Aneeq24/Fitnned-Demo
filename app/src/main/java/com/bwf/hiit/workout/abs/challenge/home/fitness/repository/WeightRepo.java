package com.bwf.hiit.workout.abs.challenge.home.fitness.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.WeightDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Weight;

import java.util.List;

public class WeightRepo {

    private WeightDao weightdao;

    public WeightRepo() {
        AppDataBase db = AppDataBase.getInstance();
        weightdao = db.weightdao();
    }

    public LiveData<List<Weight>> getAllWeights() {
        return weightdao.getAllWeight();
    }

    public void insert(Weight weight) {
        new insertAsyncTask(weightdao).execute(weight);
    }

    private static class insertAsyncTask extends AsyncTask<Weight, Void, Void> {

        private WeightDao mAsyncTaskDao;

        insertAsyncTask(WeightDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Weight... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

}