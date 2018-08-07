package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;


import java.util.List;


    public class ExerciseDetail {

    @Embedded
    private Exercise exercise;
    @Relation(parentColumn = "id", entityColumn = "eid", entity = Detail.class)
    private List<Detail> detail;

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }
}
