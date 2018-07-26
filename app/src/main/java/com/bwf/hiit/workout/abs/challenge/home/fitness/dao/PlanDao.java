package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Plan;

import java.util.List;

@Dao
public interface PlanDao {

    @Query("SELECT COUNT(*) FROM plan")
    int getCount();

    @Insert
    void addPlan(Plan plan);

    @Query("SELECT * FROM plan WHERE id = :id")
    Plan findById(int id);

    @Insert
    void insertAll(List<Plan> plans);

    @Delete
    void delete(Plan plan);

    @Update
    void updatePlan(Plan plan);

}
