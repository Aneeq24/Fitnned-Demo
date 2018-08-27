package com.bwf.hiit.workout.abs.challenge.home.fitness.database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ExerciseDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ExerciseDayDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.Recorddao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ReminderDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.Userdao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Reminder;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;


@Database(entities = {ExerciseDay.class , Exercise.class ,User.class, Record.class, Reminder.class}, version = 2 ,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase appDataBase;

    public abstract ExerciseDao exerciseDao();

    public abstract ExerciseDayDao exerciseDayDao();

    public  abstract Userdao userdao();

    public  abstract Recorddao recorddao();

    public abstract ReminderDao reminderDao();

    public static AppDataBase getInstance() {
        if (appDataBase == null) {
            Context context = Application.getContext();
            appDataBase = Room.databaseBuilder(context, AppDataBase.class, context.getString(R.string.database))
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDataBase;
    }
}
