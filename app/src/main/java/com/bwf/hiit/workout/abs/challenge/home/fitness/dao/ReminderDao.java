package com.bwf.hiit.workout.abs.challenge.home.fitness.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Reminder;

@Dao
public interface ReminderDao {
    @Query("SELECT COUNT(*) FROM reminder")
    int getCount();

    @Insert
    void addReminder(Reminder reminder);

    @Query("SELECT * FROM reminder WHERE id = :id")
    Reminder findById(int id);

    @Insert
    void insertAll(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);
}
