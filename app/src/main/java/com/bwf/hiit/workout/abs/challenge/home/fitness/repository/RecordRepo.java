package com.bwf.hiit.workout.abs.challenge.home.fitness.repository;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.Recorddao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;

import java.util.List;

public class RecordRepo {

    private Recorddao recorddao;

    public RecordRepo() {
        AppDataBase db = AppDataBase.getInstance();
        recorddao = db.recorddao();
    }

    public LiveData<List<Record>> getAllRecords() {
        return recorddao.getAllRecords();
    }

    public LiveData<Record> getRecord(int day) {
        return recorddao.findById(day);
    }

    public void insert(Record record) {
        new insertAsyncTask(recorddao).execute(record);
    }

    private static class insertAsyncTask extends AsyncTask<Record, Void, Void> {

        private Recorddao mAsyncTaskDao;

        insertAsyncTask(Recorddao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Record... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }


}