package com.bwf.hiit.workout.abs.challenge.home.fitness.models;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Converters {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Tts> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Tts>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Tts> someObjects) {
        return gson.toJson(someObjects);
    }
}
