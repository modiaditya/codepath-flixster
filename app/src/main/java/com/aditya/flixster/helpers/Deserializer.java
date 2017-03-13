package com.aditya.flixster.helpers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by amodi on 3/9/17.
 */

public class Deserializer<T> {
    private final Class<T> mTypeParameterClass;

    public Deserializer(Class<T> typeParameterClass) {
        mTypeParameterClass = typeParameterClass;
    }

    public T parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        T model = gson.fromJson(response, mTypeParameterClass);
        return model;
    }
}
