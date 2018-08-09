package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;


@Dao
public interface Recorddao {

    @Query("SELECT COUNT(*) FROM record")
    int getCount();

    @Insert
    void addPlan(Record record);

    @Query("SELECT * FROM record WHERE id = :id")
    Record findById(int id);

    @Insert
    void insertAll(Record record);

    @Delete
    void delete(Record record);

    @Update
    void updatePlan(Record record);
}
