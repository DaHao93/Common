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
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * Created by Hao on 2021/7/15
 */
final class ObjectTypeAdapter<E> extends TypeAdapter<Object> {
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
            return new ObjectTypeAdapter(
                    gson, componentTypeAdapter, $Gson$Types.getRawType(componentType));
        }
    };

    private final TypeAdapter<E> componentTypeAdapter;

    public ObjectTypeAdapter(Gson context, TypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(context, componentTypeAdapter, componentType);
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        E instance = null;

        try {
            in.beginObject();
            instance = componentTypeAdapter.read(in);
            in.endObject();
        } catch (Exception e) {
            if (e.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                String json = in.nextString();
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.setLenient(true);
                reader.beginObject();
                instance = componentTypeAdapter.read(reader);
                reader.endObject();
            } else {
                throw e;
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(JsonWriter out, Object obj) throws IOException {
        if (obj == null) {
            out.nullValue();
            return;
        }
        E value = (E) obj;
        out.beginObject();
        componentTypeAdapter.write(out, value);
        out.endObject();
    }
}