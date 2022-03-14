package com.bighao.project.common.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Hao on 2021/7/15
 */
public class GsonUtil {

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .registerTypeAdapterFactory(ArrayTypeAdapter.FACTORY)
            .registerTypeAdapterFactory(ObjectTypeAdapter.FACTORY)
            .registerTypeAdapterFactory(CollectionTypeAdapter.FACTORY)
            .registerTypeAdapterFactory(GsonTypeAdapterFactory.getInstance())
            .create();

    public static <T> T fromJson(String json, Class<T> typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeOfT) {
        return gson.fromJson(json, typeOfT.getType());
    }

    public static String toJson(Object o) {
        return gson.toJson(o);
    }
}
