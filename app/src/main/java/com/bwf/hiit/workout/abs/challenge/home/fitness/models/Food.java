package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Food implements Parcelable {

    @PrimaryKey
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    @TypeConverters(ConvertersUrl.class)
    private List<Url> foodDetail;

    public Food(int id, String name, List<Url> foodDetail) {
        this.id = id;
        this.name = name;
        this.foodDetail = foodDetail;
    }

    protected Food(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Url> getFoodDetail() {
        return foodDetail;
    }

    public void setFoodDetail(List<Url> foodDetail) {
        this.foodDetail = foodDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeList(foodDetail);
    }
}
