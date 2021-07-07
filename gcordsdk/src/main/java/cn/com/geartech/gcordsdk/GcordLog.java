package cn.com.geartech.gcordsdk;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * @author xujiahui
 * @since 2016/9/20
 */

public class GcordLog {
    private static final String TAG = "GcordLog";

    public static void d(String message) {
        d(TAG, message);
    }

    public static void d(String tag, String message) {
        GTAidlHandler.getInstance().logD(tag, message);
    }

    public static void e(String message) {
        e(TAG, message);
    }

    public static void e(String tag, String message) {
        GTAidlHandler.getInstance().logE(tag, message);
    }

    public static void fileLog(String message) {
        fileLog("", message);
    }

    public static void fileLog(String tag, String message) {
        fileLog(DateFormat.format("yyyyMMdd", new Date()) + ".log", tag, message);
    }

    public static void fileLog(String fileName, String tag, String message) {
        GTAidlHandler.getInstance().fileLog(fileName, tag, message);
    }

}
