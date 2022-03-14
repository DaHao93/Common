package com.bighao.project.common.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Hao on 2021/7/15
 */
final class CollectionTypeAdapter<E> extends TypeAdapter<Collection<E>> {
    public static TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        private ConstructorConstructor constructorConstructor;

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Type type = typeToken.getType();

            Class<? super T> rawType = typeToken.getRawType();
            if (!Collection.class.isAssignableFrom(rawType)) {
                return null;
            }

            if (null == constructorConstructor) {
                constructorConstructor = new ConstructorConstructor(Collections.EMPTY_MAP,true);
            }

            Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
            TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
            ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);

            @SuppressWarnings({"unchecked", "rawtypes"}) // create() doesn't define a type parameter
            TypeAdapter<T> result = new CollectionTypeAdapter(gson, elementType, elementTypeAdapter, constructor);
            return result;
        }
    };

    private final TypeAdapter<E> elementTypeAdapter;
    private final ObjectConstructor<? extends Collection<E>> constructor;

    public CollectionTypeAdapter(Gson context, Type elementType,
                                 TypeAdapter<E> elementTypeAdapter,
                                 ObjectConstructor<? extends Collection<E>> constructor) {
        this.elementTypeAdapter =
                new TypeAdapterRuntimeTypeWrapper<E>(context, elementTypeAdapter, elementType);
        this.constructor = constructor;
    }

    @Override
    public Collection<E> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        Collection<E> collection = constructor.construct();
        try {
            in.beginArray();
            while (in.hasNext()) {
                E instance = elementTypeAdapter.read(in);
                collection.add(instance);
            }
            in.endArray();
        } catch (Exception e) {
            if (e.getMessage().contains("Expected BEGIN_ARRAY but was STRING")) {
                String json = in.nextString();
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.setLenient(true);
                reader.beginArray();
                while (reader.hasNext()) {
                    E instance = elementTypeAdapter.read(reader);
                    collection.add(instance);
                }
                reader.endArray();
            } else {
                throw e;
            }
        }
        return collection;
    }

    @Override
    public void write(JsonWriter out, Collection<E> collection) throws IOException {
        if (collection == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        for (E element : collection) {
            elementTypeAdapter.write(out, element);
        }
        out.endArray();
    }
}