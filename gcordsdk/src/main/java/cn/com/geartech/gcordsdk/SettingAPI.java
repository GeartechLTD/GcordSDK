package cn.com.geartech.gcordsdk;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import cn.com.geartech.gcordsdk.dao.GlobalSettingsTable;

/**
 * Created by kya on 15/4/24.
 */
public class SettingAPI {

    public static final String CTRL_SET_TIME = "cn.com.geartech.gtplatform.api.set_time";
    public static final String PARAM_TIME = "time";
    public static final int ETHER_STATE_DISCONNECTED = 0;
    public static final int ETHER_STATE_CONNECTING = 1;
    public static final int ETHER_STATE_CONNECTED = 2;
    public static final int LED_NUM_RED = 1;
    public static final int LED_NUM_BLUE = 2;
    public static final String BROADCAST_RESET_WALLPAPER_TO_DEFAULT = "cn.com.geartech.reset_wallpaper_to_default";
    public static final String BROADCAST_ADD_PICTURE_TO_WALLPAPER_POOL = "cn.com.geartech.add_picture_to_wallpaper_pool";
    public static final String PARAM_PICTURE = "param_picture";
    static final String EVENT_SET_TIME_COMPLETE = "cn.com.geartech.gtplatform.set_time_complete";
    static final String CTRL_SET_USE_NETWORK_TIME = "cn.com.geartech.gtplatform.set_use_network_time";
    static final String PARAM_USE_NETWORK_TIME = "use_network_time";
    static final String CTRL_SET_BOOT_RINGTONE = "cn.com.geartech.gtplatform.set_boot_ringtone";
    static final String PARAM_RING_TONE_PATH = "ring_tone_path";
    static final String EVENT_APP_HEART_BEAT = "cn.com.geartech.gtplatform.event_app_heart_beat";
    static final String GCORD_META_DATA_NEED_KEEP_ALIVE = "cn.com.geartech.gtplatform.metadata_need_keep_alive";
    static final String SYS_PREF_VOIP_AUDIO_COMPLIANCE_MODE_1 = "cn.com.geartech.sys_pref_voip_audio_compliance_mode_1";
    static final String SYS_PREF_ABOUT_VISIBLE = "cn.com.geartech.sys_pref_about_item_visible";
    static final String SYS_PREF_SHOW_ALL_WIDGET = "cn.com.geartech.sys_pref_show_all_widget";
    static final String SYS_PREF_HIDDEN_SETTING_ITEMS = "cn.com.geartech.sys_pref_hidden_setting_items";
    static final String SYS_PREF_LAUNCHER_DIAL_MODE = "cm.com.geartech.sys_pref_launcher_dial_mode";
    static final String SYS_PREF_ALWAYS_USE_COMMUNICATION_MODE = "cn.com.geartech.sys_pref_always_use_in_communication_mode";
    private static final String GCORD_INTENT_HANDLE_INCOMING_CALL = "gcord.intent_handle_incoming_call";
    private static final String GCORD_SCHEME_INCOMING_CALL = "gcord.scheme.incoming_call";
    static String SYS_SETTING_GT_MACHINE_ID = "cn.com.geartech.gtplatform.setting_machine_id";
    static String EVENT_ETHERNET_CONNECTION_STATE_CHANGED = "cn.com.geartech.ethernet_state_changed";
    static String PARAM_ETHERNET_STATE = "cn.com.geartech.ethernet_state";
    private static SettingAPI instance;
    Class lastActivityCls;
    Application context;
    Handler handler = new Handler();
    SetTimeListener setTimeListener;
    String machineId = "";
    EthernetStateChangeListener ethernetStateChangeListener;
    int lastEthernetConnectionState = ETHER_STATE_DISCONNECTED;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(EVENT_SET_TIME_COMPLETE)) {
                if (setTimeListener != null) {
                    setTimeListener.onSetTimeComplete();
                    setTimeListener = null;
                }
            } else if (intent.getAction().equals(EVENT_ETHERNET_CONNECTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(PARAM_ETHERNET_STATE, lastEthernetConnectionState);
                if (ethernetStateChangeListener != null) {
                    ethernetStateChangeListener.onEthernetStateChange(state);
                }
            }
        }
    };
    ArrayList<String> inComingCallActivities = new ArrayList<String>();
    Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            //activity.getIntent()
            try {
                if (inComingCallActivities != null && inComingCallActivities.contains(activity.getClass().getName())) {
                    Intent intent = activity.getIntent();
                    if (intent != null) {
                        if (intent.getAction().equals(PhoneAPI.GCORD_INTENT_HANDLE_INCOMING_CALL)) {
                            String token = intent.getStringExtra(PSTNInternal.PARAM_TOKEN);
                            if (token != null) {
                                if (PSTNInternal.getInstance().getLastIncomingCallToken() == null ||
                                        !PSTNInternal.getInstance().getLastIncomingCallToken().equals(token)) {
                                    PSTNInternal.getInstance().setLastIncomingCallToken(token);
                                    PSTNInternal.getInstance().notifyIncomingCallEvent("");
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            //Log.e("foreground","app is foreground:" + isAppInForeground());
            GTAidlHandler.getInstance().notifyForegroundState(isAppInForeground());

            if (activity != null) {
                lastActivityCls = activity.getClass();
            }

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            //  Log.e("foreground","app is foreground2:" + isAppInForeground());
            GTAidlHandler.getInstance().notifyForegroundState(isAppInForeground());
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };
    Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {

            Intent intent = new Intent(EVENT_APP_HEART_BEAT);
            intent.putExtra("packageName", context.getPackageName());
            intent.setPackage("cn.com.geartech.gtplatform");
            context.sendBroadcast(intent);

            handler.postDelayed(heartBeatRunnable, 5 * 1000);
        }
    };

    private SettingAPI() {


    }

    public static SettingAPI getInstance() {
        if (instance == null) {
            instance = new SettingAPI();
        }

        return instance;
    }

    static protected int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = 1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = -1;
                break;
            }
        }

        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? -1 : 1;
        }

        return res;
    }

    private static String getSystemProperty(Context context, String key, String def) throws IllegalArgumentException {
        String ret = def;
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
//参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = String.class;
            Method get = SystemProperties.getMethod("get", paramTypes);
//参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = new String(def);
            ret = (String) get.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = def;
//TODO
        }
        return ret;
    }

    protected void init(Application c) {
        context = c;
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
//        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
//        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
//        context.registerReceiver(mReceiver, intentFilter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EVENT_SET_TIME_COMPLETE);
        intentFilter.addAction(EVENT_ETHERNET_CONNECTION_STATE_CHANGED);
        context.registerReceiver(mReceiver, intentFilter);

        checkIncomingCallActivities();

        try {
            context.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        checkHeartBeat();
    }

    public boolean setCurrentTime(Context context, Date dateToSet) {

        if (dateToSet == null || context == null)
            return false;

        Intent intent = new Intent(CTRL_SET_TIME);
        intent.putExtra(PARAM_TIME, dateToSet);
        context.sendBroadcast(intent);

        return true;
    }

    /**
     * 设置时间
     *
     * @param dateToSet
     * @param listener
     * @return
     */
    public boolean setCurrentTime(Date dateToSet, SetTimeListener listener) {
        if (dateToSet == null || context == null)
            return false;

        Intent intent = new Intent(CTRL_SET_TIME);
        intent.putExtra(PARAM_TIME, dateToSet);
        context.sendBroadcast(intent);
        setTimeListener = listener;
        return true;
    }

    /**
     * 获取话机的序列号
     *
     * @return
     */
    public String getMachineId() {

        if (machineId == null || machineId.length() == 0) {
            try {
                machineId = Settings.System.getString(context.getContentResolver(), SYS_SETTING_GT_MACHINE_ID);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return machineId;
    }

    /**
     * 设置以太网状态变化的回调
     *
     * @param ethernetStateChangeListener
     */
    public void setEthernetStateChangeListener(EthernetStateChangeListener ethernetStateChangeListener) {
        this.ethernetStateChangeListener = ethernetStateChangeListener;
    }

    /**
     * 获取以太网连接状态
     *
     * @return ETHER_STATE_DISCONNECTED, ETHER_STATE_CONNECTING, ETHER_STATE_CONNECTED
     */
    public int getEthernetConnectionState() {
        if (GTAidlHandler.getIgtAidlInterface() != null) {
            try {
                lastEthernetConnectionState = GTAidlHandler.getIgtAidlInterface().getEthernetConnectionState();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return lastEthernetConnectionState;
    }

    public boolean isEthernetLineConnected() {
        if (GTAidlHandler.getIgtAidlInterface() != null) {
            try {
                return GTAidlHandler.getIgtAidlInterface().isEthernetLineConnected();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean isAppInForeground() {
        try {
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            if (appProcesses != null) {
                for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                    if (appProcess.processName.equals(context.getPackageName())) {
                        if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    private void checkIncomingCallActivities() {
        Intent intent = new Intent(GCORD_INTENT_HANDLE_INCOMING_CALL);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(GCORD_SCHEME_INCOMING_CALL + ":"));

        try {
            inComingCallActivities.clear();
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (infos != null && infos.size() > 0) {
                for (ResolveInfo info : infos) {
                    if (info.activityInfo.packageName.equals(context.getPackageName())) {
                        inComingCallActivities.add(info.activityInfo.name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setBootAnimation(String path) {
        try {
            GTAidlHandler.getIgtAidlInterface().setBootAnimation(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重启当前app
     */
    public void restartMe() {
        try {
            GTAidlHandler.getIgtAidlInterface().walkDog(context.getPackageName());
        } catch (Exception e) {
            //  e.printStackTrace();
        }
    }

    /**
     * 设置灯闪烁.
     *
     * @param lightNum 灯的号码, LED_NUM_RED 或者 LED_NUM_BLUE
     * @param flashing 是否闪烁
     */
    public void setLedFlashing(int lightNum, boolean flashing) {
        try {
            GTAidlHandler.getIgtAidlInterface().setLedFlashing(lightNum, flashing);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置灯亮/灭.
     *
     * @param lightNum 灯的号码, LED_NUM_RED 或者 LED_NUM_BLUE
     * @param on
     */
    public void setLedOn(int lightNum, boolean on) {
        try {

            GTAidlHandler.getIgtAidlInterface().setLedOn(lightNum, on);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置使用网络时间.
     *
     * @param useNetworkTime
     */
    public void setUseNetworkTime(boolean useNetworkTime) {
        Intent intent = new Intent(CTRL_SET_USE_NETWORK_TIME);
        if (useNetworkTime) {
            intent.putExtra(PARAM_USE_NETWORK_TIME, 1);
        } else {
            intent.putExtra(PARAM_USE_NETWORK_TIME, 0);
        }

        // Log.e("","set use network time:" + useNetworkTime);

        context.sendBroadcast(intent);
    }

    public void setBootRing(String path) {
//        Intent intent = new Intent(CTRL_SET_BOOT_RINGTONE);
//        intent.putExtra(PARAM_RING_TONE_PATH, path);
//        context.sendBroadcast(intent);
    }

    void checkHeartBeat() {
        try {

            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);


            Bundle metaData = info.metaData;

            Log.e("heartBeat", "checkHeartBeat");

//            for (String key:metaData.keySet())
//            {
//                Log.e("heartBeat", "key:" + key);
//            }

            if (metaData != null && metaData.containsKey(GCORD_META_DATA_NEED_KEEP_ALIVE)) {
                startSendHeartBeat();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void startSendHeartBeat() {
        Log.e("heartBeat", "startSendHeartBeat");

        handler.removeCallbacks(heartBeatRunnable);
        handler.post(heartBeatRunnable);
    }

    void stopSendHeartBeat() {
        handler.removeCallbacks(heartBeatRunnable);
    }

    public void reboot() {
        try {
            GTAidlHandler.getIgtAidlInterface().reboot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVOIPAudioComplianceMode1(boolean enable) {
        try {
            if (enable) {
                Settings.System.putInt(context.getContentResolver(), SYS_PREF_VOIP_AUDIO_COMPLIANCE_MODE_1, 1);
            } else {
                Settings.System.putInt(context.getContentResolver(), SYS_PREF_VOIP_AUDIO_COMPLIANCE_MODE_1, 0);
            }
        } catch (Exception e) {

        }
    }

    public boolean isInVOIPAudioComplianceMode1() {
        try {
            int mode = Settings.System.getInt(context.getContentResolver(), SYS_PREF_VOIP_AUDIO_COMPLIANCE_MODE_1, 0);
            if (mode == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public LauncherMode getLauncherMode() {

        String m = Settings.System.getString(context.getContentResolver(), "cn.com.geartech.app.launcher.mode");

        LauncherMode mode = null;

        if (TextUtils.isEmpty(m))
            m = "FULL";

        if (m.equals("EMPTY")) {
            mode = LauncherMode.EMPTY;
        } else if (m.equals("APP_LIST")) {
            mode = LauncherMode.APP_LIST;
        } else if (m.equals("FULL")) {
            mode = LauncherMode.FULL;
        } else {
            mode = LauncherMode.FULL;
        }

        return mode;
    }

    public void setLauncherMode(LauncherMode mode) {

        if (mode == null)
            return;


        String m = null;

        switch (mode) {
            case EMPTY:

                m = "EMPTY";
                break;
            case APP_LIST:

                m = "APP_LIST";
                break;
            case FULL:

                m = "FULL";
                break;

            default:

                m = "FULL";
                break;
        }

        Settings.System.putString(context.getContentResolver(), "cn.com.geartech.app.launcher.mode", m);
        DebugLog.logE("set launcher.mode as " + m);
    }

    /**
     * 获取系统字体大小. return -1 if failed to get font scale.
     */
    public float getFontScale() {

        if (GTAidlHandler.getIgtAidlInterface() != null) {
            try {
                return GTAidlHandler.getIgtAidlInterface().getFontScale();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return -1f;
    }

    /**
     * 设置系统字体大小.
     *
     * @param fontScale 系统默认值是“普通”, 普通是1.0f, 超大是2.0f. 小于等于0无效果.
     */
    public void setFontScale(Float fontScale) {

        if (GTAidlHandler.getIgtAidlInterface() != null) {
            try {
                GTAidlHandler.getIgtAidlInterface().setFontScale(fontScale);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置需要在launcher的程序列表中隐藏的程序.
     *
     * @param packageNames 把需要隐藏的包名用","分隔开. 比如隐藏应用宝+浏览器, 就传入"com.tencent.android.qqdownloader,com.android.browser".
     *                     传入null可取消隐藏. 调用后重启机器才会生效
     */
    public void setFilteredAppForLauncher(String packageNames) {

        if (packageNames == null)
            packageNames = "";

        Settings.System.putString(context.getContentResolver(), "cn.com.geartech.app.launcher.filteredappforlauncher", packageNames);

        DebugLog.logE("set filteredappforlauncher" + packageNames);
    }

    public void openWifiSetting(Context c) {
        PackageManager pm = c.getPackageManager();
        PackageInfo applicationInfo = null;
        try {
            applicationInfo = pm.getPackageInfo("cn.com.geartech.app", 0);
        } catch (Exception e) {
            //e.printStackTrace();
        }

//        int result =  compareVersionNames(applicationInfo.versionName, "2.2.27");
//
//        Log.e("compare","result:" + result);

        if (applicationInfo == null || compareVersionNames(applicationInfo.versionName, "2.2.27") > 0) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setAction("gcord.intent.activity.wifisetting");
            intent.putExtra("cn.com.geartech.extra_hide_home_key", true);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(intent);
        }

        //GcordSDK.getInstance().getSettingAPI().setLauncherMode(LauncherMode.EMPTY);
    }

    public boolean isAboutVisible() {
        int visibleVal = Settings.System.getInt(context.getContentResolver(), SYS_PREF_ABOUT_VISIBLE, 1);
        if (visibleVal == 1) {
            return true;
        } else {
            return false;
        }

    }

    public void setAboutVisible(boolean visible) {
        int val = 0;
        if (visible) {
            val = 1;
        }
        Settings.System.putInt(context.getContentResolver(), SYS_PREF_ABOUT_VISIBLE, val);
    }

    public void setGcordAccountVisible(boolean visible) {
        setSettingItemHidden("GcordAccount", !visible);
    }

    public void setWechatCommunicationVisible(boolean visible) {
        setSettingItemHidden("Wechat", !visible);
    }

    public void setEnterpriseContactVisible(boolean visible) {
        setSettingItemHidden("Enterprise", !visible);
    }

    void setSettingItemHidden(String itemName, boolean hidden) {
        try {
            HashSet<String> currentItems = getHiddenSettingItems();
            if (hidden) {
                currentItems.add(itemName);
            } else {
                currentItems.remove(itemName);
            }

            String ret = "";
            for (String s : currentItems) {
                if (ret.length() > 0) {
                    ret += ",";
                }
                ret += s;
            }

            Settings.System.putString(context.getContentResolver(), SYS_PREF_HIDDEN_SETTING_ITEMS,
                    ret);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public HashSet<String> getHiddenSettingItems() {
        HashSet<String> result = new HashSet<String>();

        try {
            String hiddenItems = Settings.System.getString(context.getContentResolver(), SYS_PREF_HIDDEN_SETTING_ITEMS);


            if (hiddenItems != null && hiddenItems.length() > 0) {
                String[] items = hiddenItems.split(",");
                if (items != null && items.length > 0) {
                    for (String s : items) {
                        result.add(s);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    public void setShowAllWidget(boolean showAllWidget) {

        try {
            if (showAllWidget) {
                Settings.System.putInt(context.getContentResolver(), SYS_PREF_SHOW_ALL_WIDGET, 1);
            } else {
                Settings.System.putInt(context.getContentResolver(), SYS_PREF_SHOW_ALL_WIDGET, 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean shouldShowAllWidget() {
        try {
            int shouldShowAllWidget = Settings.System.getInt(context.getContentResolver(), SYS_PREF_SHOW_ALL_WIDGET, 0);
            return (shouldShowAllWidget == 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void resetWallpaperToDefault() {
        context.sendBroadcast(new Intent(BROADCAST_RESET_WALLPAPER_TO_DEFAULT));
    }

    public void addPictureToWallpaperPool(String path) {
        if (TextUtils.isEmpty(path))
            return;

        Intent intent = new Intent(BROADCAST_ADD_PICTURE_TO_WALLPAPER_POOL);
        intent.putExtra(PARAM_PICTURE, path);

        context.sendBroadcast(intent);
    }

    protected void writeLauncherDialMode(String mode) {
        try {
            Settings.System.putString(context.getContentResolver(), SYS_PREF_LAUNCHER_DIAL_MODE, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSystemDialMode() {
        try {
            String mode = Settings.System.getString(context.getContentResolver(), SYS_PREF_LAUNCHER_DIAL_MODE);
            if (mode == null) {
                return "";
            } else {
                return mode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean isAlwaysUseInCommunicationMode() {
        int use = Settings.System.getInt(context.getContentResolver(), SYS_PREF_ALWAYS_USE_COMMUNICATION_MODE, 0);
        if (use == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setAlwaysUseInCommunicationMode(boolean use) {
        if (use) {
            Settings.System.putInt(context.getContentResolver(), SYS_PREF_ALWAYS_USE_COMMUNICATION_MODE, 1);
        } else {
            Settings.System.putInt(context.getContentResolver(), SYS_PREF_ALWAYS_USE_COMMUNICATION_MODE, 0);
        }
    }

    public void resumeApp() {
//        if(lastActivityCls != null)
//        {
//            Intent intent = new Intent(context, lastActivityCls);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |
//            Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            context.startActivity(intent);
//        }

        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());

            PackageManager pm = context.getPackageManager();
            Intent appStartIntent = pm.getLaunchIntentForPackage(context.getPackageName());

            if (appStartIntent != null) {
                List<ResolveInfo> activities = pm.queryIntentActivities(appStartIntent, 0);

                if (activities != null && activities.size() == 1) {

                    ResolveInfo resolveInfo = activities.get(0);
                    ActivityInfo activity = resolveInfo.activityInfo;
                    ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                            activity.name);
                    Intent i = new Intent(Intent.ACTION_MAIN);

                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    i.setComponent(name);

                    context.startActivity(i);
                } else if (intent != null) {
                    context.startActivity(intent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getGlobalSettingIntValue(String key, int defaultValue) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.com.geartech.app.globalsettingprovider/" + key);
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToNext()) {
                int keyColumn = cursor.getColumnIndex(GlobalSettingsTable.GLOBAL_SETTING_KEY);
                int intValColumn = cursor.getColumnIndex(GlobalSettingsTable.GLOBAL_SETTINGS_INT_VAL);
                if (intValColumn >= 0) {
                    return cursor.getInt(intValColumn);
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return defaultValue;
    }

    protected String getGlobalSettingStringValue(String key, String defaultValue) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.com.geartech.app.globalsettingprovider/" + key);
            ContentResolver contentResolver = context.getContentResolver();
            String where = GlobalSettingsTable.GLOBAL_SETTING_KEY + " = ? ";
            String[] args = new String[]{key};
            cursor = contentResolver.query(uri, null, where, args, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
//                    int keyColumn = cursor.getColumnIndex(GlobalSettingsTable.GLOBAL_SETTING_KEY);
                    int strValColumn = cursor.getColumnIndex(GlobalSettingsTable.GLOBAL_SETTINGS_STRING_VAL);
                    if (strValColumn >= 0) {
                        return cursor.getString(strValColumn);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return defaultValue;
    }

    protected boolean setGlobalSettingStringValue(String key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GlobalSettingsTable.GLOBAL_SETTING_KEY, key);
        contentValues.put(GlobalSettingsTable.GLOBAL_SETTINGS_STRING_VAL, value);
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.com.geartech.app.globalsettingprovider/");
            ContentResolver contentResolver = context.getContentResolver();
            String where = GlobalSettingsTable.GLOBAL_SETTING_KEY + " = ? ";
            String[] args = new String[]{key};
            cursor = contentResolver.query(uri, null, where, args, null);
            if (cursor != null && cursor.getCount() > 0) {
                contentResolver.update(uri, contentValues, where, args);
            } else {
                uri = contentResolver.insert(uri, contentValues);
                Log.e("SettingAPI", uri == null ? "false" : uri.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
        return true;

    }

    protected boolean getGlobalSettingBooleanValue(String key, boolean defaultValue) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.com.geartech.app.globalsettingprovider/" + key);
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToNext()) {
                int keyColumn = cursor.getColumnIndex(GlobalSettingsTable.GLOBAL_SETTING_KEY);
                int booleanColumn = cursor.getColumnIndex(GlobalSettingsTable.GLOBAL_SETTINGS_BOOLEAN_VAL);
                if (booleanColumn >= 0) {
                    int val = cursor.getInt(booleanColumn);
                    if (val == 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return defaultValue;
    }

    protected boolean setGlobalSettingBooleanValue(String key, boolean value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GlobalSettingsTable.GLOBAL_SETTING_KEY, key);
        contentValues.put(GlobalSettingsTable.GLOBAL_SETTINGS_BOOLEAN_VAL, value);

        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://cn.com.geartech.app.globalsettingprovider/" + key);
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(uri, null, null, null, null);
            uri = Uri.parse("content://cn.com.geartech.app.globalsettingprovider/");
            if (cursor != null && cursor.moveToFirst()) {
                String where = GlobalSettingsTable.GLOBAL_SETTING_KEY + " = ? ";
                String[] args = new String[]{key};
                contentResolver.update(uri, contentValues, where, args);
            } else {
                contentResolver.insert(uri, contentValues);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    protected void setGlobalValue(String key, int val) {
        try {
            Uri uri = Uri.parse("content://cn.com.geartech.app.globalsettingprovider/" + key);
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(GlobalSettingsTable.GLOBAL_SETTING_KEY, key);
            contentValues.put(GlobalSettingsTable.GLOBAL_SETTINGS_INT_VAL, val);
            contentResolver.update(uri, contentValues, GlobalSettingsTable.GLOBAL_SETTING_KEY + " = ?", new String[]{key});
        } catch (Exception e) {

        }
    }

    protected void setGlobalValue(String key, boolean val) {
        try {
            Uri uri = Uri.parse("content://cn.com.geartech.app.globalsettingprovider/" + key);
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(GlobalSettingsTable.GLOBAL_SETTING_KEY, key);
            contentValues.put(GlobalSettingsTable.GLOBAL_SETTINGS_BOOLEAN_VAL, val);
            contentResolver.update(uri, contentValues, GlobalSettingsTable.GLOBAL_SETTING_KEY + " = ?", new String[]{key});
        } catch (Exception e) {

        }
    }

    public int getDeviceType() {
        try {
            return GTAidlHandler.getIgtAidlInterface().getDeviceType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Deprecated
    /**
     * use setEnableWhiteList & addWhiteListApp instead
     */
    public void setEnableExcludeApp(boolean enable) {
        try {
            GTAidlHandler.getIgtAidlInterface().enableAppWhiteList(context.getPackageName(), enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    protected void setGlobalValue(String key, String val)
//    {
//
//    }

    public void setEnableWhiteList(boolean enable) {
        try {
            GTAidlHandler.getInstance().setEnableWhiteList(enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWhiteListApp(String packageName) {
        try {
            GTAidlHandler.getInstance().addWhiteListPackage(packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setForceDisableADB(boolean disableADB) {
        try {
            if (disableADB) {
                GTAidlHandler.getIgtAidlInterface().sendMessage("msg_force_disable_adb", 1, 0, context.getPackageName(), "");
            } else {
                GTAidlHandler.getIgtAidlInterface().sendMessage("msg_force_disable_adb", 0, 0, context.getPackageName(), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置是否安装更新后马上重启
     * @param enable true-立即重启，false-不立即重启
     */
    public void enableRebootAfterInstall(boolean enable) {
        try {
            GTAidlHandler.getIgtAidlInterface().sendMessage("msg_enable_reboot_after_install", enable ? 1 : 0, 0, context.getPackageName(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void suicide() {
        try {
            GTAidlHandler.getIgtAidlInterface().sendMessage("msg_app_suicide", 0, 0, context.getPackageName(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setParamCallBack(GTAidlHandler.ParamCallback callBack) {
        GTAidlHandler.getInstance().setParamCallback(callBack);
    }

    public String getRomVersion() {
        return getSystemProperty(context, "ro.product.version", "");
    }

    /**
     * 设置绿灯亮度. 只支持pro1c, rom版本需要>= 1.3.3_std
     *
     * @param brightness 亮度, 接受范围0<=brightness<=100.
     * @return true 成功; false 失败
     */
    public boolean setGreenLightBrightness(int brightness) {

        return GTAidlHandler.getInstance().setGreenLightBrightness(brightness);
    }

    public enum LauncherMode {

        EMPTY,
        APP_LIST,
        FULL
    }


    public interface EthernetStateChangeListener {
        void onEthernetStateChange(int newState);
    }

    public static abstract class SetTimeListener {
        abstract public void onSetTimeComplete();
    }
}
