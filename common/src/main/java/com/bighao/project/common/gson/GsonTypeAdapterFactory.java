package com.bighao.project.common.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hao on 2021/7/15
 */
@SuppressWarnings("all")
public class GsonTypeAdapterFactory implements TypeAdapterFactory {

    private static Map<Type, TypeAdapter> sTypeAdapters = new HashMap<>();

    public static GsonTypeAdapterFactory getInstance() {
        return Holder.sInstance;
    }

    public static <T> void registerTypeAdapter(Class<T> cls, TypeAdapter<T> adapter) {
        sTypeAdapters.remove(cls);
        sTypeAdapters.put(cls, adapter);
    }

    public static <T> void registerTypeAdapter(TypeToken<T> token, TypeAdapter<T> adapter) {
        sTypeAdapters.remove(token.getType());
        sTypeAdapters.put(token.getType(), adapter);
    }

    public static <T> void unregisterTypeAdapter(Class<T> cls) {
        sTypeAdapters.remove(cls);
    }

    public static <T> void unregisterTypeAdapter(TypeToken<T> token) {
        sTypeAdapters.remove(token.getType());
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Type t = type.getType();
        TypeAdapter<T> adapter = null;
        try {
            gson.getAdapter(type);
            if (sTypeAdapters.containsKey(t)) {
                adapter = sTypeAdapters.get(t);
            }
        } catch (Exception e) {
            //TODO nothing
        }
        return adapter;
    }

    private static class Holder {
        private static GsonTypeAdapterFactory sInstance = new GsonTypeAdapterFactory();
    }
}
