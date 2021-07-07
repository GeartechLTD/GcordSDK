package cn.com.geartech.gcordsdk;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import java.util.Calendar;

/**
 * Created by pangyuning on 15/6/2.
 */
public class PowerManager {

    static final String SET_SCREEN_ON = "cn.com.geartech.gtplatform.set_screen_on";

    static final String PARAM_ON = "cn.com.geartech.gtplatform.on";

    static final String ACTION_CHECK_SCREEN_ON_PERIOD = "cn.com.geartech.gcordSDK.action_check_screen_on_period";
    static final String ACTION_CHECK_SCREEN_ON_PERIOD_2 = "cn.com.geartech.gcordSDK.action_check_screen_on_period_2";

    static final String EVENT_POWER_DISCONNECTED = "cn.com.geartech.gtplatform.gcordSDK.on_power_disconnected";
    static final String EVENT_POWER_CONNECTED = "cn.com.geartech.gtplatform.gcordSDK.on_power_connected";
    static final String EVENT_POWER_LEVEL_CHANGED = "cn.com.geartech.gtplatform.gcordSDK.on_power_level_changed";

    static final String PARAM_POWER_LEVEL = "cn.com.geartech.gtplatform.power_level"; // 0~100

    static final String PARAM_PLUG_TYPE = "cn.com.geartech.gtplatform.plug_type";


    static final String PREF_POWER_CONTROL_LEVEL = "cn.com.geartech.gtplatform.pref_power_control_level";

    static final String PREF_MAX_BATTERY_STAND_BY_TIME = "cn.com.geartech.gtplatform.pref_battery_standby_time";


    static final String SYS_POWER_SCREEN_ON_ALL_DAY = "cn.com.geartech.gcordSDK.screen_on_all_day";


    public static final int POWER_LEVEL_FULL = 0; // brightness, shutdownScreen & shutdown
    public static final int POWER_LEVEL_SHUTDOWN_SCREEN = 1; // brightness, shutdownScreen
    public static final int POWER_LEVEL_BRIGHTNESS = 2; // brightness

    public static final String META_KEEP_SCREEN_ON = "gcord_meta_data_keep_screen_on";

    public static interface PowerLevelChangeCallback{
        public abstract void onPowerConnected();
        public abstract void onPowerDisconnected();
        public abstract void onPowerLevelChanged(int level);
        public abstract void onPowerPlugTypeChanged(boolean isUsingAlternative);
    }

    private PowerManager()
    {

    }

    static PowerManager instance;
    static protected PowerManager getInstance()
    {
        if(instance == null)
        {
            instance = new PowerManager();
        }
        return instance;
    }

    Context context;

    void init(Context c)
    {
        try {
            context = c;

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
            intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            intentFilter.addAction(ACTION_CHECK_SCREEN_ON_PERIOD);
            intentFilter.addAction(ACTION_CHECK_SCREEN_ON_PERIOD_2);
            //intentFilter.addAction();
            context.registerReceiver(mReceiver, intentFilter);

            IntentFilter platformEventFilter = new IntentFilter();
            platformEventFilter.addAction(EVENT_POWER_CONNECTED);
            platformEventFilter.addAction(EVENT_POWER_DISCONNECTED);
            platformEventFilter.addAction(EVENT_POWER_LEVEL_CHANGED);
            context.registerReceiver(platformEventReceiver, platformEventFilter);

            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);
            if(batteryStatus != null)
            {
                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                powerConnected = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;

            }


            Intent intent = new Intent(ACTION_CHECK_SCREEN_ON_PERIOD);

            mPi = PendingIntent.getBroadcast(context, 0, intent, 0);

            mPi2 = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_CHECK_SCREEN_ON_PERIOD_2), 0);


            loadSysSettings();
        }catch (Exception e)
        {
            e.printStackTrace();
        }


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                setScreenOn(!isScreenOn);
//
//                new Handler().postDelayed(this,5000);
//            }
//        },1000);
    }

    boolean powerConnected = true;
    boolean isScreenOn = true;

    boolean userForceScreenOn = true;

    Activity currentActivity;

    Application regApplication;


    public void register(Application application)
    {
        DebugLog.logE("reg power manager!");

        regApplication = application;
        try {
            regApplication.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        regApplication.registerActivityLifecycleCallbacks(lifecycleCallbacks);

        //checkScreenOnByPeriod();
        scheduleAlarmTask();
    }

    boolean enableGTScreenControlStrategy = true;

    public void enableGTScreenStrategy(boolean enable)
    {
        enableGTScreenControlStrategy = enable;

        checkScreenOnByPeriod();
    }

    public void unRegister()
    {
        DebugLog.logE("unreg power manager!");

        try {
            regApplication.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public void setScreenOn(boolean on)
    {
        Intent intent = new Intent(SET_SCREEN_ON);
        intent.putExtra(PARAM_ON, on);
        context.sendBroadcast(intent);
    }


    public boolean isScreenOn()
    {
        return isScreenOn;
    }

    public void setScreenOffTime(int screenOffTime)
    {
        DebugLog.logE("set screen off time:" + screenOffTime);
        if(screenOffTime > 0)
        {
            if(screenOffTime > 30*60*1000)
            {
                screenOffTime = 30*60*1000;
            }

            try{
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, screenOffTime);
            }catch (Exception localException){
                localException.printStackTrace();
            }

            setUserForceScreenOn(false);
        }
        else
        {
            setUserForceScreenOn(true);
        }
    }

    static final String SYS_POWER_ENABLE_SCREEN_ON_BY_PERIOD = "cn.com.geartech.gcordSDK.enable_screen_on_by_period";
    static final String SYS_POWER_SCREEN_ON_PERIOD_START = "cn.com.geartech.gcordSDK.screen_on_period_start";
    static final String SYS_POWER_SCREEN_ON_PERIOD_END = "cn.com.geartech.gcordSDK.screen_on_period_end";
    static final String SYS_POWER_SCREEN_OFF_AT_WEEKEND = "cn.com.geartech.gcordSDK.screen_off_at_weekend";
    static final String SYS_POWER_SCREEN_OFF_ALL_DAY = "cn.com.geartech.gcordSDK.screen_off_all_day";

    void loadSysSettings()
    {
        try {
            int isEnableScreenOffByPeriod = Settings.System.getInt(context.getContentResolver(), SYS_POWER_ENABLE_SCREEN_ON_BY_PERIOD,0);
            if(isEnableScreenOffByPeriod == 1)
            {
                screenOnOffByPeriod = true;
            }
            else
            {
                screenOnOffByPeriod = false;
            }

            int totalStartMinute = Settings.System.getInt(context.getContentResolver(), SYS_POWER_SCREEN_ON_PERIOD_START,0);
            if(totalStartMinute == 0)
            {
                startHour = 8;
                startMin = 0;
            }
            else
            {
                startHour = totalStartMinute / 60;
                startMin = totalStartMinute % 60;
            }

            int totalEndMinute = Settings.System.getInt(context.getContentResolver(), SYS_POWER_SCREEN_ON_PERIOD_END,0);
            if(totalEndMinute == 0)
            {
                endHour = 23;
                endMin = 0;
            }
            else
            {
                endHour = totalEndMinute / 60;
                endMin = totalEndMinute % 60;
            }

            int closeAtWeekend = Settings.System.getInt(context.getContentResolver(), SYS_POWER_SCREEN_OFF_AT_WEEKEND, 0);
            if(closeAtWeekend == 1)
            {
                screenOffAtWeekEnd = true;
            }
            else
            {
                screenOffAtWeekEnd = false;
            }

            int closeAllDay = Settings.System.getInt(context.getContentResolver(), SYS_POWER_SCREEN_OFF_ALL_DAY, 1);
            if(closeAllDay == 1)
            {
                screenOffAllDay = true;
            }
            else
            {
                screenOffAllDay = false;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void setScreenOnAllDay(boolean enable)
    {
        if(enable)
        {
            Settings.System.putInt(context.getContentResolver(), SYS_POWER_SCREEN_ON_ALL_DAY, 1);
        }
        else
        {
            Settings.System.putInt(context.getContentResolver(), SYS_POWER_SCREEN_ON_ALL_DAY, 0);
        }
    }


    public boolean isScreenOnAllDay()
    {
        int on = Settings.System.getInt(context.getContentResolver(), SYS_POWER_SCREEN_ON_ALL_DAY, 0);
        return on == 1;
    }


    public void enableScreenOffByPeriod(boolean enable)
    {
        DebugLog.logE("enableScreenOnOffByPeriod" + enable);
        screenOnOffByPeriod = enable;

        int result = 0;
        if(enable)
        {
            result = 1;
        }

        Settings.System.putInt(context.getContentResolver(), SYS_POWER_ENABLE_SCREEN_ON_BY_PERIOD, result);

        checkScreenOnByPeriod();
        scheduleAlarmTask();
    }

    public boolean isScreenOffByPeriodEnabled()
    {
        return screenOnOffByPeriod;
    }

    boolean screenOnOffByPeriod;
    int startHour;
    int startMin;
    int endHour;
    int endMin;

    boolean screenOffAllDay = false;
    boolean screenOffAtWeekEnd;

    public boolean isScreenOffAtWeekEnd() {
        return screenOffAtWeekEnd;
    }

    public boolean isScreenOffAllDay()
    {
        return screenOffAllDay;
    }

    public void setScreenOffAllDay(boolean allDay)
    {
        screenOffAllDay = allDay;

        try {
            int ret;
            if(screenOffAllDay)
            {
                ret = 1;
            }
            else
            {
                ret = 0;
            }

            Settings.System.putInt(context.getContentResolver(), SYS_POWER_SCREEN_OFF_ALL_DAY, ret);

            checkScreenOnByPeriod();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void setScreenOffAtWeekEnd(boolean screenOffAtWeekEnd) {
        this.screenOffAtWeekEnd = screenOffAtWeekEnd;

        try {
            int ret = 0;
            if(screenOffAtWeekEnd)
            {
                ret = 1;
            }
            else
            {
                ret = 0;
            }
            Settings.System.putInt(context.getContentResolver(), SYS_POWER_SCREEN_OFF_AT_WEEKEND, ret);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        checkScreenOnByPeriod();
    }

    public void setScreenOnPeriod(int startHour, int startMin, int stopHour, int stopMin)
    {
        DebugLog.logE("setScreenOnPeriod :"  + startHour +":" +startMin + " " + stopHour + ":" + stopMin);
        this.startHour = startHour;
        this.startMin = startMin;

        endHour = stopHour;
        endMin = stopMin;

        int totalStartMin = startHour * 60 + startMin;
        int totalEndMin = endHour * 60 + endMin;

        Settings.System.putInt(context.getContentResolver(), SYS_POWER_SCREEN_ON_PERIOD_START, totalStartMin);
        Settings.System.putInt(context.getContentResolver(), SYS_POWER_SCREEN_ON_PERIOD_END, totalEndMin);


        checkScreenOnByPeriod();
        scheduleAlarmTask();
    }

    PendingIntent mPi;
    PendingIntent mPi2;

    void scheduleAlarmTask()
    {
        DebugLog.logE("schedule time task!");

        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, startHour);
        c.set(Calendar.MINUTE, startMin);
        c.set(Calendar.SECOND, 0);


        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(mPi);

        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 24 * 3600 * 1000, mPi);

//        c.add(Calendar.MINUTE, 1);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 24 * 3600 * 1000, mPi);


        am.cancel(mPi2);
        c.set(Calendar.HOUR_OF_DAY, endHour);
        c.set(Calendar.MINUTE, endMin);

        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 24 * 3600 * 1000, mPi2);


//        c.add(Calendar.MINUTE, 1);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 24 * 3600 * 1000, mPi2);

    }

    public int getScreenOnPeriodStartHour()
    {
        return startHour;
    }

    public int getScreenOnPeriodStartMin()
    {
        return startMin;
    }

    public int getScreenOnPeriodEndHour()
    {
        return endHour;
    }

    public int getScreenOnPeriodEndMin()
    {
        return endMin;
    }

    public boolean isInScreenOnPeriod()
    {

//        if(!screenOnOffByPeriod)
//        {
//            DebugLog.logE("!screenOnOffByPeriod return true");
//            return true;
//        }
//        else
        {
            Calendar c = Calendar.getInstance();

            if(screenOffAllDay)
            {
                DebugLog.logE("screenOffAllDay return true");
                return false;
            }

            if(screenOffAtWeekEnd)
            {
                if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                        c.get(Calendar.DAY_OF_WEEK )== Calendar.SUNDAY)
                {
                    DebugLog.logE("screenOffAtWeekEnd return true");
                    return false;
                }
            }
            int currentHour = c.get(Calendar.HOUR_OF_DAY);
            int currentMin = c.get(Calendar.MINUTE);

            int totalMin = currentHour * 60 + currentMin;

            int totalStartMin = startHour * 60 + startMin;
            int totalEndMin = endHour * 60 + endMin;

            boolean shouldOn;

            if(totalStartMin <= totalEndMin)
            {
                shouldOn = (totalMin >= totalStartMin && totalMin < totalEndMin);
            }
            else
            {
                shouldOn = totalMin >= totalStartMin || totalMin < totalEndMin;
            }
            DebugLog.logE("totalMin=" + totalMin);
            DebugLog.logE("totalStartMin="+totalStartMin);
            DebugLog.logE("totalEndMin="+totalEndMin);

            DebugLog.logE("shouldOn " + shouldOn);
            return shouldOn;
        }
    }

    void checkScreenOnByPeriod()
    {
        DebugLog.logE("check screen on by period!");

        if(!powerConnected || !enableGTScreenControlStrategy)
        {
            //
            return;
        }

        int strategy = queryScreenStrategy();

        DebugLog.logE("strategy:" + strategy);

        if(strategy == 0)
        {
            setUserForceScreenOn(false);
        }
        else if(strategy == 1)
        {
            setUserForceScreenOn(true);
        }
//        if(isInScreenOnPeriod())
//        {
//            DebugLog.logE("is in on perriod");
//
//            if(!isScreenOn())
//                setScreenOn(true);
//
//            setUserForceScreenOn(true);
//        }
//        else
//        {
//            DebugLog.logE("is in off perriod");
//            setUserForceScreenOn(false);
//            //setScreenOffTime(10*60*1000);
//        }
    }

    int queryScreenStrategy()
    {
        try {
            return GTAidlHandler.getIgtAidlInterface().queryScreenStrategy();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    boolean isCurrentActivityKeepScreenOn() {
        boolean currentActivityKeepScreenOn = false;
        if(currentActivity != null) {
            int flags = currentActivity.getWindow().getAttributes().flags;
            currentActivityKeepScreenOn = ((flags & WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)!=0);
        }
        return currentActivityKeepScreenOn;
    }

    void resumeCurrentActivityFlag()
    {
        if(currentActivity != null)
        {
            if(isCurrentActivityKeepScreenOn() || userForceScreenOn)
            {
                currentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            else
            {
                currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
    }

    void setCurrentActivity(Activity activity)
    {
        if(currentActivity != null)
        {
            //resumeCurrentActivityFlag();
        }

        currentActivity = activity;

        if(shouldUpdateFlags)
            updateFlags();
    }

    private boolean shouldUpdateFlags = true;

    public void setShouldUpdateFlags(boolean should){
        this.shouldUpdateFlags = should;

        if(should)
            updateFlags();
    }

    private void updateFlags() {


        DebugLog.logE("current activity keeps screen on: " + isCurrentActivityKeepScreenOn() + "userforceScreenOn:" + userForceScreenOn);


        boolean shouldKeepScreenOn = false;
        if(currentActivity != null) {
            Intent intent = currentActivity.getIntent();
            if (intent != null) {
                shouldKeepScreenOn = intent.getBooleanExtra(META_KEEP_SCREEN_ON, false);
            }

            if(userForceScreenOn)
            {
                currentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {

                if(!shouldKeepScreenOn)
                {
                    currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }

            if(!powerConnected)
            {
                DebugLog.logE("power not connected");

                if(getPowerControlLevel() <= POWER_LEVEL_SHUTDOWN_SCREEN && !shouldKeepScreenOn)
                {
                    currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        }
    }

    public void setUserForceScreenOn(boolean forceScreenOn)
    {
        userForceScreenOn = forceScreenOn;

        Log.e("DebugUtil", "setUserForceScreenOn = " + forceScreenOn);

        if(currentActivity != null) {

            if(userForceScreenOn) {
                currentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }else{
                currentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }

    }

    Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

            currentActivity = activity;

            setCurrentActivity(activity);

            if(!powerConnected)
            {
                setCurrentActivityDark();
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

            resumeCurrentActivityFlag();

            if(!powerConnected)
            {
                //  resumeCurrentActivityDarkness();
                delayResumeCurrentActivityDarkness();
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            DebugLog.logE(intent.getAction());

            if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED))
            {
                powerConnected = true;
                leavePowerSaveMode();
            }
            else if(intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED))
            {
                powerConnected = false;
                enterPowerSaveMode();
            }
            else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
            {
                isScreenOn = true;
            }
            else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
            {
                isScreenOn = false;
            }
//            else if(intent.getAction().equals(Intent.ACTION_TIME_CHANGED))
//            {
////                checkScreenOnByPeriod();
////                scheduleAlarmTask();
//            }
//            else if(intent.getAction().equals(ACTION_CHECK_SCREEN_ON_PERIOD) ||
//                    intent.getAction().equals(ACTION_CHECK_SCREEN_ON_PERIOD_2))
//            {
//                checkScreenOnByPeriod();
//            }

        }
    };


    float lastScreenBrightness = -1;

    void setCurrentActivityDark()
    {
//        if(currentActivity != null)
//        {
//            try {
//                WindowManager.LayoutParams lp = currentActivity.getWindow().getAttributes();
//                int brightness = 20;
//
//
//                DebugLog.logE("lp brightness:" + lp.screenBrightness);
//
//                if(lastScreenBrightness < 0 && lp.screenBrightness > 0)
//                {
//                    lastScreenBrightness = lp.screenBrightness;
//                }
//
//                if(lp.screenBrightness > 0)
//                {
//                    lp.screenBrightness = (float)(brightness/255.0);
//                }
//
//                currentActivity.getWindow().setAttributes(lp);
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
    }

    void delayResumeCurrentActivityDarkness()
    {
//        if(currentActivity != null && lastScreenBrightness >= 0)
//        {
//            final Activity activity = currentActivity;
//            final float brightNess = lastScreenBrightness;
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//
//                        if (lp.screenBrightness >= 0) {
//                            lp.screenBrightness = brightNess;
//                        }
//                        activity.getWindow().setAttributes(lp);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }, 1000);
//        }
    }

    void resumeCurrentActivityDarkness()
    {
//        try {
//            if(currentActivity != null && lastScreenBrightness >= 0)
//            {
//                DebugLog.logE("resume!!");
//
//                WindowManager.LayoutParams lp = currentActivity.getWindow().getAttributes();
//                lp.screenBrightness = lastScreenBrightness;
//                currentActivity.getWindow().setAttributes(lp);
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }

    int lastBrightness = -1;


    void enterPowerSaveMode()
    {
//        int currentBrightness =  Settings.System.getInt(context.getContentResolver(),
//                Settings.System.SCREEN_BRIGHTNESS, 0);
//
//        lastBrightness = currentBrightness;

        setCurrentActivityDark();

//        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 20);

        if(currentActivity != null)
        {
            setCurrentActivity(currentActivity);
        }
    }


    public void setSysScreenBrightness(int brightness) {
        try {
            ContentResolver resolver = context.getContentResolver();
            Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
            Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
            resolver.notifyChange(uri, null); // 实时通知改变
        } catch (Exception e) {

        }
    }

    void leavePowerSaveMode()
    {

//        int currentBrightness =  Settings.System.getInt(context.getContentResolver(),
//                Settings.System.SCREEN_BRIGHTNESS, 0);
//
//        int target = Math.max(currentBrightness, lastBrightness);
//
//        Toast.makeText(context, "target "+ target, Toast.LENGTH_SHORT).show();
//
//        if(target <= 0)
//            target = 20;
//
//        if(target > 255)
//            target = 255;
//
////        if(target >= 0)
//        {
//            DebugLog.logE("target brightness:" + target);
//
//            setSysScreenBrightness(target);
//        }

        resumeCurrentActivityDarkness();

        resumeCurrentActivityFlag();

        checkScreenOnByPeriod();

        lastScreenBrightness = -1;
    }


    PowerLevelChangeCallback powerLevelChangeCallback;

    public void setPowerLevelChangeCallback(PowerLevelChangeCallback callback)
    {
        powerLevelChangeCallback = callback;
    }

    int currentPowerLevel = -1;

    public int getPowerLevel()
    {
        if(GTAidlHandler.getIgtAidlInterface() != null)
        {
            try {
                currentPowerLevel = GTAidlHandler.getIgtAidlInterface().getCurrentPowerLevel();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

//        if(currentPowerLevel < 0)
//        {
//            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//            Intent batteryStatus = context.registerReceiver(null, ifilter);
//            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
//            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
//            if(scale > 0)
//            {
//                currentPowerLevel = level*100/scale;
//            }
//            else
//            {
//                currentPowerLevel = 100;
//            }
//        }

        return currentPowerLevel;
    }


    BroadcastReceiver platformEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if(intent.getAction().equals(EVENT_POWER_CONNECTED))
            {
                if(powerLevelChangeCallback != null)
                {
                    powerLevelChangeCallback.onPowerConnected();
                }
            }
            else if(intent.getAction().equals(EVENT_POWER_DISCONNECTED))
            {
                if(powerLevelChangeCallback != null)
                {
                    powerLevelChangeCallback.onPowerDisconnected();
                }
            }
            else if(intent.getAction().equals(EVENT_POWER_LEVEL_CHANGED))
            {
                Bundle bundle = intent.getExtras();
                if(bundle.keySet().contains(PARAM_POWER_LEVEL))
                {
                    currentPowerLevel = bundle.getInt(PARAM_POWER_LEVEL);
                    int plugType = bundle.getInt(PARAM_PLUG_TYPE, -1);
                    if(powerLevelChangeCallback != null)
                    {
                        powerLevelChangeCallback.onPowerLevelChanged(currentPowerLevel);

                    }

                    if(plugType >= 0)
                    {
                        currentPlugType = plugType;
                    }

                    powerPlugCBHandler.removeCallbacks(powerPlugCBRunnable);
                    powerPlugCBHandler.postDelayed(powerPlugCBRunnable, 2000);
                }
            }
        }
    };

    Handler powerPlugCBHandler = new Handler();
    Runnable powerPlugCBRunnable = new Runnable() {
        @Override
        public void run() {
            if(powerLevelChangeCallback != null)
            {
                powerLevelChangeCallback.onPowerPlugTypeChanged(currentPlugType == 1);
            }
        }
    };

    int currentPlugType = 0;

    public boolean isChargingWithAlternative()
    {
        if(GTAidlHandler.getIgtAidlInterface() != null)
        {
            try {
                int type = GTAidlHandler.getIgtAidlInterface().getCurrentPluggedType();
                return type == 1;
            }catch (Exception e)
            {
                return false;
            }
        }
        return false;
    }

    public boolean isPowerConnected()
    {
        if(GTAidlHandler.getIgtAidlInterface() != null)
        {
            try {
                return GTAidlHandler.getIgtAidlInterface().isPowerConnected();
            }catch (Exception e)
            {
                return powerConnected;
            }
        }

        return powerConnected;
    }

    public void setPowerControlLevel(int level)
    {
        try {
            Settings.System.putInt(context.getContentResolver(), PREF_POWER_CONTROL_LEVEL, level);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getPowerControlLevel()
    {
        try {
            int level = Settings.System.getInt(context.getContentResolver(), PREF_POWER_CONTROL_LEVEL, 0);
            return level;
        }catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public void setMaxBatteryStandbyTime(long standbyTime)
    {
        try {
            Settings.System.putLong(context.getContentResolver(), PREF_MAX_BATTERY_STAND_BY_TIME, standbyTime);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public long getMaxBatteryStandByTime()
    {
        try {
            return Settings.System.getLong(context.getContentResolver(), PREF_MAX_BATTERY_STAND_BY_TIME);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return -1;
    }
}
