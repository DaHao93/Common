package com.bighao.project.common.content;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by Hao on 2022/3/14
 */
public abstract class SharedPreference {
    private static final WeakHashMap<String, SharedPreference> preferencesMap = new WeakHashMap<>(2);

    private static Context sContext;

    public static void init(Application app) {
        sContext = app.getApplicationContext();
    }

    public static SharedPreference get() {
        return get(sContext.getPackageName() + ".default_preference");
    }

    public static SharedPreference get(String name) {
        return get(name, Context.MODE_PRIVATE);
    }

    public static SharedPreference get(String name, int mode) {
        String key = String.format(Locale.getDefault(), "%s-%d", name, mode);
        SharedPreference preference = preferencesMap.get(key);
        if (null == preference) {
            preference = new PreferenceIml(sContext, name, mode);
            preferencesMap.put(key, preference);
        }
        return preference;
    }

    public abstract Map<String, ?> getAll();

    public abstract String getString(String key, @Nullable String defValue);

    public abstract Set<String> getStringSet(String key, @Nullable Set<String> defValues);

    public abstract int getInt(String key, int defValue);

    public abstract long getLong(String key, long defValue);

    public abstract float getFloat(String key, float defValue);

    public abstract boolean getBoolean(String key, boolean defValue);

    public abstract boolean contains(String key);

    public abstract void putString(String key, @Nullable String value);

    public abstract void putStringSet(String key, @Nullable Set<String> values);

    public abstract void putInt(String key, int value);

    public abstract void putLong(String key, long value);

    public abstract void putFloat(String key, float value);

    public abstract void putBoolean(String key, boolean value);

    public abstract void remove(String key);

    public abstract void clear();

    public abstract void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener);

    public abstract void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener);
}
