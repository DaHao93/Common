package com.bighao.project.common.content;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * Created by Hao on 2022/3/14
 */
class PreferenceIml extends SharedPreference {

    private final SharedPreferences mDelegate;

    PreferenceIml(Context context, String name, int mode) {
        this.mDelegate = context.getSharedPreferences(name, mode);
    }

    @Override
    public Map<String, ?> getAll() {
        return mDelegate.getAll();
    }

    @Override
    public String getString(String key, @Nullable String defValue) {
        return mDelegate.getString(key, defValue);
    }

    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return mDelegate.getStringSet(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        return mDelegate.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return mDelegate.getLong(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return mDelegate.getFloat(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return mDelegate.getBoolean(key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return mDelegate.contains(key);
    }

    @Override
    public void putString(String key, @Nullable String value) {
        mDelegate.edit().putString(key, value).apply();
    }

    @Override
    public void putStringSet(String key, @Nullable Set<String> values) {
        mDelegate.edit().putStringSet(key, values).apply();
    }

    @Override
    public void putInt(String key, int value) {
        mDelegate.edit().putInt(key, value).apply();
    }

    @Override
    public void putLong(String key, long value) {
        mDelegate.edit().putLong(key, value).apply();
    }

    @Override
    public void putFloat(String key, float value) {
        mDelegate.edit().putFloat(key, value).apply();
    }

    @Override
    public void putBoolean(String key, boolean value) {
        mDelegate.edit().putBoolean(key, value).apply();
    }

    @Override
    public void remove(String key) {
        mDelegate.edit().remove(key).apply();
    }

    @Override
    public void clear() {
        mDelegate.edit().clear().apply();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mDelegate.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mDelegate.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
