package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Food;

import java.util.List;

@Dao
public interface FoodDao {

    @Query("SELECT * FROM food")
    LiveData<List<Food>> getAllFood();

    @Query("SELECT * FROM food WHERE id LIKE :id LIMIT 1")
    LiveData<Food> findById(int id);

    @Query("SELECT * FROM food WHERE id LIKE :id LIMIT 1")
    Food findByIdbg(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Food food);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Food> foods);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Food food);

    @Delete
    void delete(Food food);

}
