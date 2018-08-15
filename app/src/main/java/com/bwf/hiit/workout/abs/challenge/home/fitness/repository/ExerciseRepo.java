package com.bwf.hiit.workout.abs.challenge.home.fitness.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ExerciseDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;

import java.util.List;

public class ExerciseRepo {

    private ExerciseDao exerciseDao;

    public ExerciseRepo() {
        AppDataBase db = AppDataBase.getInstance();
        exerciseDao = db.exerciseDao();
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return exerciseDao.getAllExercise();
    }

    public LiveData<Exercise> getExercise(int cid){
        return exerciseDao.findById(cid);
    }

    public void insert(Exercise exercise) {
        new insertAsyncTask(exerciseDao).execute(exercise);
    }

    private static class insertAsyncTask extends AsyncTask<Exercise, Void, Void> {

        private ExerciseDao mAsyncTaskDao;

        insertAsyncTask(ExerciseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Exercise... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}