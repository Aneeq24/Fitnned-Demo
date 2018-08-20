package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;

import java.util.List;

@Dao
public interface ExerciseDayDao {

    @Query("SELECT * FROM exerciseday WHERE pid = :pid LIMIT 1")
    LiveData<ExerciseDay> findByPid(int pid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ExerciseDay planDay);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExerciseDay> exerciseDays);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ExerciseDay exerciseday);

    @Delete
    void delete(ExerciseDay exerciseday);

    @Query("SELECT * FROM exerciseday WHERE planId = :planId and status = 0 GROUP BY dayId;")
    LiveData<List<ExerciseDay>> getIncompleteExerciseDays(int planId);

    @Query("SELECT * FROM exerciseday WHERE planId =:planId AND dayId = :dayId")
    LiveData<List<ExerciseDay>> getLiveExerciseDays(int planId, int dayId);

    @Query("SELECT * FROM exerciseday WHERE planId =:planId AND dayId = :dayId")
    List<ExerciseDay> getExerciseDays(int planId, int dayId);

}
