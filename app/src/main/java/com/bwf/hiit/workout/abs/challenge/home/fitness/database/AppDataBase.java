package com.bwf.hiit.workout.abs.challenge.home.fitness.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.DayProgressDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.DetailDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ExerciseDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.ExerciseDayDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.PlanDao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.Recorddao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.dao.Userdao;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.DayProgressModel;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Detail;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Exercise;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.ExerciseDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Plan;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;


@Database(entities = {ExerciseDay.class , Plan.class , Detail.class, Exercise.class , DayProgressModel.class, User.class, Record.class}, version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase appDataBase;

    public abstract PlanDao planDao();

    public abstract ExerciseDao exerciseDao();

    public abstract ExerciseDayDao exerciseDayDao();

    public  abstract DetailDao detailDao();

    public  abstract DayProgressDao dayProgressDao();

    public  abstract Userdao userdao();

    public  abstract Recorddao recorddao();

    public static AppDataBase getInstance() {

        if (appDataBase == null) {
            Context context = Application.getContext();
            appDataBase = Room.databaseBuilder(context, AppDataBase.class, context.getString(R.string.database)).build();
        }
        return appDataBase;
    }

}
