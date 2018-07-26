package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity(indices = @Index(name = "exercise_id", value = "eid", unique = true))
public class Detail {

    @PrimaryKey(autoGenerate = true)
    private int pid;

    @SerializedName("id")
    private int eid;

    @SerializedName("lang")
    private int lang;

    @SerializedName("url")
    private String url;

    @SerializedName("detail")
    private String detail;

    public Detail(int eid, int lang, String url, String detail) {
        this.eid = eid;
        this.lang = lang;
        this.url = url;
        this.detail = detail;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
