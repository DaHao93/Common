package com.bighao.project.common.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2021/7/15
 */
final class ArrayTypeAdapter<E> extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Type type = typeToken.getType();
            if (!(type instanceof GenericArrayType || type instanceof Class && ((Class<?>) type).isArray())) {
                return null;
            }

            Type componentType = $Gson$Types.getArrayComponentType(type);
            TypeAdapter<?> componentTypeAdapter = gson.getAdapter(TypeToken.get(componentType));
            return new ArrayTypeAdapter(
                    gson, componentTypeAdapter, $Gson$Types.getRawType(componentType));
        }
    };

    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;

    public ArrayTypeAdapter(Gson context, TypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
        this.componentTypeAdapter =
                new TypeAdapterRuntimeTypeWrapper<E>(context, componentTypeAdapter, componentType);
        this.componentType = componentType;
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        List<E> list = new ArrayList<E>();
        try {
            in.beginArray();
            while (in.hasNext()) {
                E instance = componentTypeAdapter.read(in);
                list.add(instance);
            }
            in.endArray();
        } catch (Exception e) {
            if (e.getMessage().contains("Expected BEGIN_ARRAY but was STRING")) {
                String json = in.nextString();
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.setLenient(true);
                reader.beginArray();
                while (reader.hasNext()) {
                    E instance = componentTypeAdapter.read(reader);
                    list.add(instance);
                }
                reader.endArray();
            } else {
                throw new RuntimeException(e);
            }
        }

        int size = list.size();
        Object array = Array.newInstance(componentType, size);
        for (int i = 0; i < size; i++) {
            Array.set(array, i, list.get(i));
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(JsonWriter out, Object array) throws IOException {
        if (array == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        for (int i = 0, length = Array.getLength(array); i < length; i++) {
            E value = (E) Array.get(array, i);
            componentTypeAdapter.write(out, value);
        }
        out.endArray();
    }
}
