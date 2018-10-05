package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Content implements Parcelable{

    @SerializedName("text")
    private String text;

    @SerializedName("url")
    private String url;

    @SerializedName("image")
    private String image;

    public Content(String text, String url, String image) {
        this.text = text;
        this.url = url;
        this.image = image;
    }

    protected Content(Parcel in) {
        text = in.readString();
        url = in.readString();
        image = in.readString();
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(url);
        dest.writeString(image);
    }
}
