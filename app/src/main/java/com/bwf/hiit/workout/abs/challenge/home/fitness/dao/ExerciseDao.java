package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Query("SELECT * FROM exercise")
    LiveData<List<Exercise>> getAllExercise();

    @Query("SELECT * FROM exercise WHERE id LIKE :id LIMIT 1")
    LiveData<Exercise> findById(int id);

    @Query("SELECT * FROM exercise WHERE id LIKE :id LIMIT 1")
    Exercise findByIdbg(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Exercise exercise);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Exercise> exercises);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

}
