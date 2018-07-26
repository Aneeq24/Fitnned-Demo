package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import com.google.gson.annotations.SerializedName;


import java.util.List;

public class Day {

    @SerializedName("day_id")
    private int dayId;

    @SerializedName("exercises")
    private List<ExerciseDay> exercisesOfDay;

    @SerializedName("round_completed")
    private int roundCompleted;

    @SerializedName("total_exercise")
    private int totalExercise;

    @SerializedName("exercise_complete")
    private int exerciseComplete;

    public Day(int dayId, List<ExerciseDay> exercisesOfDay) {
        this.dayId = dayId;
        this.exercisesOfDay = exercisesOfDay;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public List<ExerciseDay> getExercisesOfDay() {
        return exercisesOfDay;
    }

    public void setExercisesOfDay(List<ExerciseDay> exercisesOfDay) {
        this.exercisesOfDay = exercisesOfDay;
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
}
