package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Detail;


import java.util.List;

@Dao
public interface DetailDao
{

    @Query("SELECT COUNT(*) FROM detail")
    int getCount();




    @Insert
    void addDetail(Detail detail);

    @Query("SELECT * FROM detail WHERE eid = :id LIMIT 1")
    Detail findById(int id);

    @Insert
    void insertAll(List<Detail> details);

    @Delete
    void delete(Detail detail);

    @Update
    void updateDetail(Detail detail);


}
