package cn.com.geartech.gcordsdk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;

import cn.com.geartech.gtplatform.model.aidl.IHomeAidlInterface;

/**
 * Created by pangyuning on 15/12/9.
 */
public class HomeKeyManager extends GcordHelper {

    static HomeKeyManager instance = null;

    static String CTRL_SET_HOME_KEY_ACTION_TYPE = "cn.com.geartech.gtplatform.set_home_key_action_type";
    static String PARAM_ACTION_TYPE = "action_type"; // String
    static String ACTION_TYPE_NONE = "none";
    static String ACTION_TYPE_CUSTOM = "custom";
    static String ACTION_TYPE_PICK_UP = "pickup";

    static String PARAM_PACKAGE_NAME = "packageName";

    static String CTRL_SET_HOME_KEY_CONTROL_IN_CALL = "cn.com.geartech.gtplatform.set_home_key_control_in_call";
    static String PARAM_ENABLE = "enable"; // boolean

    static String SYS_PREF_CUSTOM_HOME_KEY_EVENT_PRIORITY = "cn.com.geartech.sys_pref_home_key_custom_event_priority";

    static String EVENT_ON_HOME_CLICKED = "cn.com.geartech.gtplatform.on_home_key_clicked";

    static String EVENT_APP_RESUMED_BY_HOME_KEY = "cn.com.geartech.gtplatform.app_resumed_by_home_key";


    Context context;

    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 100;
    public static final int PRIORITY_LOW = -100;

//    static String CTRL_SET_HOME_KEY_ACTION_TYPE = "cn.com.geartech.gtplatform.set_home_key_action_type";
//    static String PARAM_ACTION_TYPE = "action_type"; // String

    ArrayList<HomeKeyListener> homeKeyListeners = new ArrayList<HomeKeyListener>();

    public interface HomeKeyListener{
        void onHomeClicked();
        void onResumedByHomeKey();
    }

    private HomeKeyManager()
    {

    }

    protected static HomeKeyManager getInstance()
    {

        if(instance == null)
        {
            instance = new HomeKeyManager();
        }

        return instance;
    }

    protected void init(Context c)
    {
        context = c;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED);
        intentFilter.addAction(EVENT_ON_HOME_CLICKED);
        intentFilter.addAction(EVENT_APP_RESUMED_BY_HOME_KEY);
        context.registerReceiver(mReceiver, intentFilter);

        connectToService();
    }

    /**
     * Home键的行为类型.仅当app在前台时生效
     */
    public enum HOME_KEY_ACTION_TYPE{

        /**
         * 设置为此项时,home键只执行一个动作:如果存在已经注册的默认跳转的app,则跳回该app.否则返回系统桌面.
         */
        NONE,

        /**
         * 设置为此项时,默认执行pstn的免提键功能.即待机时按一下home键,进行免提提机.在pstn手柄提机状态下按home键,将声道切换至免提.在pstn免提提机情况下按下home键,进行挂机.
         */
        PICK_UP,

        /**
         * 设置为此项时,会执行用户指定的HomeKeyListener回调.
         * @see HomeKeyListener
         */
        CUSTOM_KEY_EVENT
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED))
            {
                // re-register events
                //Log.e("tag", "platform restarted");
               // setHomeKeyActionType(homeKeyActionType);
                connectToService();
            }
            else if(intent.getAction().equals(EVENT_ON_HOME_CLICKED))
            {
                if(homeKeyListeners != null)
                {
                    String packageName = intent.getStringExtra(PARAM_PACKAGE_NAME);
                    if(packageName != null && packageName.equals(context.getPackageName()))
                    {
                        //Log.e("GcordSDK", "HomeKeyListener size:" + homeKeyListeners.size());
                        for (HomeKeyListener listener:homeKeyListeners)
                        {
                            listener.onHomeClicked();
                        }
                    }
                }
            }
            else if(intent.getAction().equals(EVENT_APP_RESUMED_BY_HOME_KEY))
            {
                //Log.e("GcordSDK", "resumed by home");
                for (HomeKeyListener listener:homeKeyListeners)
                {
                    listener.onResumedByHomeKey();
                }
            }
        }
    };

    /**
     * 注册HomeKeyEventListener
     * @param listener
     */
    public void addHomeKeyEventListener(HomeKeyListener listener)
    {
        //Log.e("GcordSDK", "add homekeylistener :" + listener.getClass());
        if(!homeKeyListeners.contains(listener))
        homeKeyListeners.add(listener);
        //Log.e("GcordSDK", "HomeKeyListener size:" + homeKeyListeners.size());

    }

    /**
     * 反注册HomeKeyEventListener
     * @param listener
     */
    public void removeHomeKeyEventListener(HomeKeyListener listener)
    {
        //Log.e("GcordSDK", "remove homekeylistener :" + listener.getClass());
        homeKeyListeners.remove(listener);
        //Log.e("GcordSDK", "HomeKeyListener size:" + homeKeyListeners.size());

    }

//    public void setInCallControlEnabled(boolean enable)
//    {
//
//    }
//
//    boolean isInCallControlEnabled()
//    {
//        return true;
//    }

    HOME_KEY_ACTION_TYPE homeKeyActionType = HOME_KEY_ACTION_TYPE.PICK_UP;

    /**
     * 设置Home键按下的响应类型
     * @param actionType
     * @see HOME_KEY_ACTION_TYPE
     */
    public void setHomeKeyActionType(HOME_KEY_ACTION_TYPE actionType)
    {
        String type = ACTION_TYPE_PICK_UP;
        switch (actionType){
            case NONE:
                type =  ACTION_TYPE_NONE;
                break;
            case CUSTOM_KEY_EVENT:
                type = ACTION_TYPE_CUSTOM;
                break;
            case PICK_UP:
                type = ACTION_TYPE_PICK_UP;
                break;
        }

        try {
            if(iHomeAidlInterface != null)
            {
                iHomeAidlInterface.setHomeActionType(context.getPackageName(), type);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Log.e("tag","setHomeKeyActionType" + actionType);

        Intent intent = new Intent(CTRL_SET_HOME_KEY_ACTION_TYPE);
        intent.putExtra("packageName", context.getPackageName());
        intent.putExtra(PARAM_ACTION_TYPE, type);
        intent.setPackage("cn.com.geartech.gtplatform");

        context.sendBroadcast(intent);

        homeKeyActionType = actionType;
    }

    /**
     * 获取Home键按下的动作类型
     * @return
     */
    public HOME_KEY_ACTION_TYPE getHomeKeyActionType()
    {
        return homeKeyActionType;
    }

    /**
     * 模拟一次安卓的home key,用于返回系统桌面.效果等同于adb shell里面的input keyevent 3
     */
    public void performHomeEvent()
    {
        PSTNInternal.getInstance().performHomeEvent();
    }

    /**
     * 设置home键custom事件优先级
     * @param priority
     */
    public void setHomeKeyCustomEventPriority(int priority)
    {
        try {
            Settings.System.putInt(context.getContentResolver(), SYS_PREF_CUSTOM_HOME_KEY_EVENT_PRIORITY, priority);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getHomeKeyCustomEventPriority()
    {
        return Settings.System.getInt(context.getContentResolver(), SYS_PREF_CUSTOM_HOME_KEY_EVENT_PRIORITY, 0);
    }

    /**
     * 显示/隐藏底部导航栏
     * @param enable
     */
    public void setNavigationBarEnable(boolean enable)
    {
        try {
            if(iHomeAidlInterface != null)
            {
                iHomeAidlInterface.setNavigationBarEnable(enable);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置一直显示导航栏
     */


    IHomeAidlInterface iHomeAidlInterface;

    void connectToService()
    {
        Intent intent = new Intent("cn.com.geartech.gtplatform.HomeService");
        intent.setPackage("cn.com.geartech.gtplatform");
        boolean ret = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if(!ret)
        {
            if(internalInitCallback != null)
            {
                internalInitCallback.onInitFailed();
            }
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                Log.e("tag","home service connected");
                iHomeAidlInterface = IHomeAidlInterface.Stub.asInterface(service);
                setHomeKeyActionType(homeKeyActionType);

                if(internalInitCallback != null)
                {
                    internalInitCallback.onInitFinished();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iHomeAidlInterface = null;
        }
    };
}
