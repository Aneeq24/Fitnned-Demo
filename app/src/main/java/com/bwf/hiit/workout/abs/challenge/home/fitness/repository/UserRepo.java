package com.bwf.hiit.workout.abs.challenge.home.fitness.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.Userdao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;

public class UserRepo {

    private Userdao userdao;

    public UserRepo() {
        AppDataBase db = AppDataBase.getInstance();
        userdao = db.userdao();
    }

    public LiveData<User> getUser() {
        return userdao.findById(1);
    }

    public void update (User user) {
        new updateAsyncTask(userdao).execute(user);
    }

    private static class updateAsyncTask extends AsyncTask<User, Void, Void> {

        private Userdao mAsyncTaskDao;

        updateAsyncTask(Userdao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.updateUser(params[0]);
            return null;
        }
    }
}