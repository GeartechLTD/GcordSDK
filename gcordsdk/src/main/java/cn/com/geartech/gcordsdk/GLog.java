package cn.com.geartech.gcordsdk;

/**
 * @author xujiahui
 * @since 2017/1/6
 */

public class GLog {

    private static final String TAG = "GLog";

    public static void raw(String msg) {
        GTAidlHandler.getInstance().raw(TAG, msg);
    }

    public static void raw(String tag, String msg) {
        GTAidlHandler.getInstance().raw(tag, msg);
    }

    public static void logic(String msg) {
        GTAidlHandler.getInstance().logic(TAG, msg);
    }

    public static void logic(String tag, String msg) {
        GTAidlHandler.getInstance().logic(tag, msg);
    }

    public static void biz(String msg) {
        GTAidlHandler.getInstance().biz(TAG, msg);
    }

    public static void biz(String tag, String msg) {
        GTAidlHandler.getInstance().biz(tag, msg);
    }

    public static void statistics(String msg) {
        GTAidlHandler.getInstance().statistics(TAG, msg);
    }

    public static void statistics(String tag, String msg) {
        GTAidlHandler.getInstance().statistics(tag, msg);
    }
}
