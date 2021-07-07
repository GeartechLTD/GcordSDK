package cn.com.geartech.gcordsdk;

import android.util.Log;

/**
 * Created by dinner on 15/5/29.
 */
public class DebugLog {

    static boolean enableLog = false;

    public static void logE(String log)
    {
        if(enableLog)
        {
              Log.e("gcordSDK", log);
        }
    }

    public static void setEnableLog(boolean enable)
    {
        enableLog = enable;
    }
}
