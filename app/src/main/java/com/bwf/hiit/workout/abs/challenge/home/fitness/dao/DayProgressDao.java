package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DayProgressModel;

import java.util.List;

@Dao
public interface DayProgressDao {

    @Query("SELECT COUNT(*) FROM dayprogressmodel")
    int getCount();

    @Query("SELECT COUNT(*) FROM dayprogressmodel WHERE complete = 1")
    int getComplete();

    @Insert
    void addPlan(DayProgressModel dayProgressModel);

    @Query("SELECT * FROM dayprogressmodel WHERE id = :id")
    DayProgressModel findById(int id);

    @Insert
    void insertAll(List<DayProgressModel> dayProgressModel);

    @Delete
    void delete(DayProgressModel dayProgressModel);

    @Update
    void updateProgressDay(DayProgressModel dayProgressModel);
}
