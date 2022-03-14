package com.bighao.project.common.uitls;

import android.util.Log;

/**
 * Created by Hao on 2022/3/14
 * 日志输出帮助类
 */
public class SuperLog {

    /**
     * 修改关闭或打开输出
     */
    private static boolean DEBUG = true;
    private static String TAG = "SuperLog";

    public static void init(boolean debug, String tag) {
        DEBUG = debug;
        TAG = tag;
    }

    /**
     * 输出日志
     *
     * @param logInfo
     */
    public static void e(Object logInfo) {
        if (DEBUG) {
            String info = String.valueOf(logInfo);
            String[] ifs = info.split("\\n");
            Log.e(TAG, getLineInfo(0) + ifs[0] + "\n");
            for (int i = 1; i < ifs.length; i++) {
                Log.e(TAG, "\t" + ifs[i] + "\n");
            }
        }
    }

    /**
     * 输出调试信息 并附上当前方法调用者的前几个调用者
     *
     * @param logInfo  调试信息
     * @param upMethod 前几个调用者
     */
    public static void e(Object logInfo, int upMethod) {
        if (DEBUG) {
            Log.e(TAG, getLineInfo(upMethod) + logInfo + "\n");
        }
    }

    /**
     * 获取日志输出调用的文件和函数
     *
     * @return 返回文件日志输出的函数和位置 例:(xxx.java:12):
     */
    private static String getLineInfo(int line) {
        StackTraceElement[] traces = new Throwable().getStackTrace();
        int maxLine = Math.min(2 + line, traces.length - 1);
        StackTraceElement ste = traces[maxLine];
        return "(" + ste.getFileName() + ":" + ste.getLineNumber() + "):";
    }
}
