package com.bwf.hiit.workout.abs.challenge.home.fitness.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.DetailDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ExerciseDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ExerciseDayDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.PlanDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Detail;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Plan;


@Database(entities = {ExerciseDay.class , Plan.class , Detail.class, Exercise.class}, version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase appDataBase;

    public abstract PlanDao planDao();

    public abstract ExerciseDao exerciseDao();

    public abstract ExerciseDayDao exerciseDayDao();

    public  abstract DetailDao detailDao();

    public static AppDataBase getInstance() {

        if (appDataBase == null) {
            Context context = Application.getContext();
            appDataBase = Room.databaseBuilder(context, AppDataBase.class, context.getString(R.string.database)).build();
        }
        return appDataBase;
    }

}
