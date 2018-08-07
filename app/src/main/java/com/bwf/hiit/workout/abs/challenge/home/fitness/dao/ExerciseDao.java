package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDetail;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Query("SELECT COUNT(*) FROM exercise")
    int getCount();

    @Query("SELECT * FROM exercise WHERE lang = :language")
    List<Exercise> getAll(int language);

    @Query("SELECT * FROM exercise WHERE name LIKE :name LIMIT 1")
    Exercise findByName(String name);

    @Query("SELECT * FROM exercise WHERE id LIKE :id LIMIT 1")
    Exercise findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Exercise exercise);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Exercise> exercises);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Transaction
    @Query("SELECT * FROM exercise")
    List<ExerciseDetail> getAllExercisesWithDetails();

    @Transaction
    @Query("SELECT * FROM exercise WHERE id IN (:ids)")
    List<ExerciseDetail> getAllExercisesWithDetails(int[] ids);

}
