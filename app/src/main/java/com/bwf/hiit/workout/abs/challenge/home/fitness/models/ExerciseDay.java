package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


@Entity
public class ExerciseDay implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int pid;

    private int planId;
    private int dayId;

    @SerializedName("id")
    private int id;

    @SerializedName("reps")
    private int reps;

    @SerializedName("rounds")
    private int rounds;

    @SerializedName("status")
    private boolean status;

    @SerializedName("round_completed")
    private int roundCompleted;

    @SerializedName("total_exercise")
    private int totalExercise;

    @SerializedName("exercise_complete")
    private int exerciseComplete;

    @SerializedName("delay")
    private  int delay;


    public ExerciseDay(int pid, int planId, int dayId, int id, int reps, boolean status, int rounds, int roundCompleted, int totalExercise, int exerciseComplete , int delay) {
        this.pid = pid;
        this.planId = planId;
        this.dayId = dayId;
        this.id = id;
        this.reps = reps;
        this.status = status;
        this.rounds = rounds;
        this.roundCompleted = roundCompleted;
        this.totalExercise = totalExercise;
        this.exerciseComplete = exerciseComplete;
        this.delay = delay;
    }

    private ExerciseDay(Parcel in) {
        pid = in.readInt();
        planId = in.readInt();
        dayId = in.readInt();
        id = in.readInt();
        reps = in.readInt();
        rounds = in.readInt();
        status = in.readByte() != 0;
        roundCompleted =in.readInt();
        totalExercise = in.readInt();
        exerciseComplete = in.readInt();
        delay = in.readInt();
    }

    public static final Creator<ExerciseDay> CREATOR = new Creator<ExerciseDay>() {
        @Override
        public ExerciseDay createFromParcel(Parcel in) {
            return new ExerciseDay(in);
        }

        @Override
        public ExerciseDay[] newArray(int size) {
            return new ExerciseDay[size];
        }
    };

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pid);
        dest.writeInt(planId);
        dest.writeInt(dayId);
        dest.writeInt(id);
        dest.writeInt(reps);
        dest.writeInt(totalExercise);
        dest.writeInt(exerciseComplete);
        dest.writeInt(roundCompleted);
        dest.writeByte((byte) (status ? 1 : 0));
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getExerciseComplete() {
        return exerciseComplete;
    }

    public void setExerciseComplete(int exerciseComplete) {
        this.exerciseComplete = exerciseComplete;
    }

    public int getTotalExercise() {
        return totalExercise;
    }

    public void setTotalExercise(int totalExercise) {
        this.totalExercise = totalExercise;
    }

    public int getRoundCompleted() {
        return roundCompleted;
    }

    public void setRoundCompleted(int roundCompleted) {
        this.roundCompleted = roundCompleted;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
