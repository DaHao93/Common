package com.bighao.project.common.uitls;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Hao on 2022/3/14
 */
public class AppUtils {

    private static Application application;

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(final Context context) {
        if (context == null) {
            init(getApplicationByReflect());
            return;
        }
        init((Application) context.getApplicationContext());
    }

    public static void init(final Application app) {
        if (application == null) {
            if (app == null) {
                application = getApplicationByReflect();
            } else {
                application = app;
            }
        } else {
            application = app;
        }
    }

    public static Application getApp() {
        if (application != null) {
            return application;
        }
        Application app = getApplicationByReflect();
        init(app);
        return app;
    }

    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }
}
