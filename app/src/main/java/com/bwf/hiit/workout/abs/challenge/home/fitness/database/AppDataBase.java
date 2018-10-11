package com.bwf.hiit.workout.abs.challenge.home.fitness.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ExerciseDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ExerciseDayDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.FoodDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.RecordDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ReminderDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.UserDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.WeightDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Food;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Reminder;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Weight;


@Database(entities = {ExerciseDay.class, Exercise.class, User.class, Record.class, Reminder.class, Weight.class, Food.class}, version = 6, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase appDataBase;

    public abstract ExerciseDao exerciseDao();

    public abstract ExerciseDayDao exerciseDayDao();

    public abstract UserDao userdao();

    public abstract RecordDao recorddao();

    public abstract ReminderDao reminderDao();

    public abstract WeightDao weightdao();

    public abstract FoodDao foodDao();

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
