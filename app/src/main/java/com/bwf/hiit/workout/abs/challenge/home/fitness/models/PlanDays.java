package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PlanDays {

    @SerializedName("plan_id")
    private int planId;

    @SerializedName("days")
    private List<Day> days;

    public PlanDays(int planId, List<Day> days) {
        this.planId = planId;
        this.days = days;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
