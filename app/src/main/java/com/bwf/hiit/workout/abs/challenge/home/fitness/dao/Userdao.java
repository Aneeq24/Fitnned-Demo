package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;


@Dao
public interface Userdao {
    @Query("SELECT COUNT(*) FROM user")
    int getCount();

    @Insert
    void addUser(User user);

    @Query("SELECT * FROM user WHERE id = :id")
    User findById(int id);

    @Insert
    void insertAll(User users);

    @Delete
    void delete(User user);

    @Update
    void updateUser(User user);

}
