package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

import cn.com.geartech.gtplatform.model.aidl.IHeadsetAidlInterface;

/**
 * Created by pangyuning on 16/1/11.
 */
public class HandleManager extends GcordHelper {

    static HandleManager instance = null;

    static final String EVENT_SET_NO_PICK_UP = "cn.com.geartech.gtplatform.set_no_pick_up";
    static final String PARAM_NO_PICK_UP = "no_pick_up";

    static final String EVENT_GCORD_HANDLE_PICKED_UP = "cn.com.geartech.gtplatform.handle_picked_up";

    static final String EVENT_GCORD_HANDLE_PUT_DOWN = "cn.com.geartech.gtplatform.handle_put_down";

    static final String EVENT_WIRELESS_HEADSET_REG_STATE_CHANGE = "cn.com.geartech.gtplatform.event_wireless_headset_reg_state_change";

    static final String PARAM_REG_STATE = "reg_state";

    static final String EVENT_WIRELESS_HEADSET_CONNECTION_STATE_CHANGE = "cn.com.geartech.gtplatform.event_wireless_headset_connection_state_change";

    static final String PARAM_CONN_STATE = "connection_state";

    static final String EVENT_WIRELESS_HEADSET_KEY_PRESSED = "cn.com.geartech.gtplatform.event_wireless_headset_key_pressed";

    static final String PARAM_KEY_CODE = "keycode";

    static final String EVENT_WIRELESS_HEADSET_BATTERY_STATE_CHANGE = "cn.com.geartech.gtplatform.event_wireless_headset_battery_state_change";

    static final String PARAM_BATTERY_LEVEL = "battery_level";

    static final String EVENT_WIRELESS_HEADSET_CHARGE_STATE_CHANGE = "cn.com.geartech.gtplatform.event_wireless_headset_charge_state_change";

    static final String PARAM_CHARGE_STATE = "charge_state";

    public static int MUSIC_CHANNEL_HANDS_FREE = 1;
    public static int MUSIC_CHANNEL_HANDLE = 0;

    private HandleManager() {

    }

    /**
     * 默认的提手柄方式.在待机状态下提手柄会触发pstn的手柄提机.如果是pstn来电或者cell call来电,会自动进行接听.
     */
    public static final int HANDLE_PICK_UP_TYPE_NONE = 0;

    //public static final int HANDLE_PICK_UP_TYPE_PSTN = 1;

    /**
     * 针对cell call或者voip的提手柄方式.在待机状态下提手柄,只做声道切换.在此模式下,手柄不自动响应任何pstn的事件.
     */
    public static final int HANDLE_PICK_UP_TYPE_VOIP = 2;

    protected static HandleManager getInstance() {
        if (instance == null) {
            instance = new HandleManager();
        }

        return instance;
    }

    Context context;

    void init(Context c) {
        context = c;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED);
        intentFilter.addAction(EVENT_GCORD_HANDLE_PICKED_UP);
        intentFilter.addAction(EVENT_GCORD_HANDLE_PUT_DOWN);

        intentFilter.addAction(EVENT_WIRELESS_HEADSET_BATTERY_STATE_CHANGE);
        intentFilter.addAction(EVENT_WIRELESS_HEADSET_CONNECTION_STATE_CHANGE);
        intentFilter.addAction(EVENT_WIRELESS_HEADSET_KEY_PRESSED);
        intentFilter.addAction(EVENT_WIRELESS_HEADSET_REG_STATE_CHANGE);

        context.registerReceiver(mReceiver, intentFilter);

        Log.e("handle", "init handle manager");

        connectAIDL();
    }

    private void connectAIDL() {
        Intent intent = new Intent("cn.com.geartech.gtplatform.model.aidl.IHeadsetAidlInterface");
        intent.setPackage("cn.com.geartech.gtplatform");
        intent.putExtra("packageName", context.getPackageName());
        boolean ret = context.bindService(intent, mHeadsetSC, Context.BIND_AUTO_CREATE);
        if (!ret) {
            if (internalInitCallback != null) {
                internalInitCallback.onInitFailed();
            }
        }
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED)) {
                connectAIDL();
            } else if (action.equals(EVENT_GCORD_HANDLE_PICKED_UP)) {
                //Log.e("handle", "EVENT_GCORD_HANDLE_PICKED_UP");

                for (HandleEventListener listener : listeners) {
                    listener.onHandlePickedUp();
                }
            } else if (action.equals(EVENT_GCORD_HANDLE_PUT_DOWN)) {
                //Log.e("handle", "EVENT_GCORD_HANDLE_PUT_DOWN");

                for (HandleEventListener listener : listeners) {
                    listener.onHandlePutDown();
                }
            } else if (action.equals(EVENT_WIRELESS_HEADSET_REG_STATE_CHANGE)) {
                int regState = intent.getIntExtra(PARAM_REG_STATE, -1);

                for (OnHeadsetOperationListener headsetOperationListener : mOnHeadsetOperationListeners) {
                    if (headsetOperationListener != null) {
                        headsetOperationListener.onPairingStateChange(regState);
                    }
                }
            } else if (action.equals(EVENT_WIRELESS_HEADSET_CONNECTION_STATE_CHANGE)) {
                int connState = intent.getIntExtra(PARAM_CONN_STATE, -1);

                for (OnHeadsetOperationListener headsetOperationListener : mOnHeadsetOperationListeners) {
                    if (headsetOperationListener != null) {
                        headsetOperationListener.onConnectionStateChange(connState);
                    }
                }
            } else if (action.equals(EVENT_WIRELESS_HEADSET_BATTERY_STATE_CHANGE)) {
                int batteryPercent = intent.getIntExtra(PARAM_BATTERY_LEVEL, 0);

                for (OnHeadsetOperationListener headsetOperationListener : mOnHeadsetOperationListeners) {
                    if (headsetOperationListener != null) {
                        headsetOperationListener.onBatteryLevelChange(batteryPercent);
                    }
                }
            } else if (action.equals(EVENT_WIRELESS_HEADSET_CHARGE_STATE_CHANGE)) {
                int chargeState = intent.getIntExtra(PARAM_CHARGE_STATE, 0);
                for (OnHeadsetOperationListener headsetOperationListener : mOnHeadsetOperationListeners) {
                    if (headsetOperationListener != null) {
                        headsetOperationListener.onChargeStateChange(chargeState);
                    }
                }
            } else if (action.equals(EVENT_WIRELESS_HEADSET_KEY_PRESSED)) {
                String keyCode = intent.getStringExtra(PARAM_KEY_CODE);
                if (keyCode != null && keyCode.equals("key1")) {
                    for (OnHeadsetOperationListener headsetOperationListener : mOnHeadsetOperationListeners) {
                        if (headsetOperationListener != null) {
                            headsetOperationListener.onBtn1Pressed();
                        }
                    }
                }
            }
        }
    };

    public void setNoPickUp(boolean noPickUp) {
        Intent i = new Intent(EVENT_SET_NO_PICK_UP);
        i.putExtra(PARAM_NO_PICK_UP, noPickUp);
        context.sendBroadcast(i);
    }

    /**
     * 手柄事件监听器
     */
    public static interface HandleEventListener {

        /**
         * 手柄提起事件
         */
        abstract void onHandlePickedUp();

        /**
         * 手柄挂下事件
         */
        abstract void onHandlePutDown();
    }

    HashSet<HandleEventListener> listeners = new HashSet<HandleEventListener>();

    /**
     * 注册手柄事件的监听器.
     *
     * @param listener
     */
    public void addHandleEventListener(HandleEventListener listener) {
        //Log.e("handle", "addHandleEventListener");
        if (listener != null) {
          //  Log.e("handle", "listener type:" + listener.getClass().getName());
            listeners.add(listener);
        }
    }

    /**
     * 移除手柄事件的监听器
     *
     * @param listener
     */
    public void removeHandleEventListener(HandleEventListener listener) {
       // Log.e("handle", "removeHandleEventListener");
        if (listener != null) {
         //   Log.e("handle", "remove :" + listener.getClass().getName());

            listeners.remove(listener);
        }
    }

    /**
     * 判断当前手柄是否处于提起状态.
     *
     * @return
     */
    public boolean isHandlePickedUp() {
        return PSTNInternal.getInstance().isPhysicalHandleUp();
        // return false;
    }

    /**
     * 设置提起手柄的响应方式. HANDLE_PICK_UP_TYPE_NONE 或者 HANDLE_PICK_UP_TYPE_VOIP
     *
     * @param type
     */
    public void setHandlePickUpType(int type) {
        Log.d("GcordSDK", "enter set handle pickup type:" + type);
        GTAidlHandler.getInstance().setHandlePickUpType(type);
    }

    /**
     * 获取手柄的默认响应方式
     *
     * @return
     */
    public static int getHandlePickUpType() {
        return GTAidlHandler.getInstance().getHandlePickUpType();
    }

    private ArrayList<OnHeadsetOperationListener> mOnHeadsetOperationListeners = new ArrayList<OnHeadsetOperationListener>();

    public void addOnHeadsetOperationListener(OnHeadsetOperationListener listener) {
        if (mOnHeadsetOperationListeners == null) {
            mOnHeadsetOperationListeners = new ArrayList<OnHeadsetOperationListener>();
        }
        if (!mOnHeadsetOperationListeners.contains(listener)) {
            mOnHeadsetOperationListeners.add(listener);
        }
    }

    public void removeOnHeadsetOperationListener(OnHeadsetOperationListener listener) {
        if (mOnHeadsetOperationListeners != null && mOnHeadsetOperationListeners.contains(listener)) {
            mOnHeadsetOperationListeners.remove(listener);
        }
    }

    IHeadsetAidlInterface mHeadsetAidlInterface;

    ServiceConnection mHeadsetSC = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mHeadsetAidlInterface = IHeadsetAidlInterface.Stub.asInterface(service);
                //mHeadsetAidlInterface.registerCallback(mHeadsetCallback);

                if (internalInitCallback != null) {
                    internalInitCallback.onInitFinished();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mHeadsetAidlInterface = null;
        }
    };

//    IHeadsetCallback.Stub mHeadsetCallback = new IHeadsetCallback.Stub() {
//        @Override
//        public void onConnectionStateChange(int status) throws RemoteException {
//            if (mOnHeadsetOperationListeners != null) {
//                for (OnHeadsetOperationListener listener : mOnHeadsetOperationListeners) {
//                    listener.onConnectionStateChange(status);
//                }
//            }
//        }
//
//        @Override
//        public void onPairingStateChange(int status) throws RemoteException {
//            if (mOnHeadsetOperationListeners != null) {
//                for (OnHeadsetOperationListener listener : mOnHeadsetOperationListeners) {
//                    listener.onPairingStateChange(status);
//                }
//            }
//        }
//
//        @Override
//        public void onBatteryLevelChange(int percent) throws RemoteException {
//            if (mOnHeadsetOperationListeners != null) {
//                for (OnHeadsetOperationListener listener : mOnHeadsetOperationListeners) {
//                    listener.onBatteryLevelChange(percent);
//                }
//            }
//        }
//
//        @Override
//        public void onBtn1Pressed() throws RemoteException {
//            if (mOnHeadsetOperationListeners != null) {
//                for (OnHeadsetOperationListener listener : mOnHeadsetOperationListeners) {
//                    listener.onBtn1Pressed();
//                }
//            }
//        }
//    };

    public void startPairing() {
        try {
            if (mHeadsetAidlInterface != null) {
                mHeadsetAidlInterface.startPairing();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void stopPairing() {
        try {
            if (mHeadsetAidlInterface != null) {
                mHeadsetAidlInterface.stopPairing();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            if (mHeadsetAidlInterface != null) {
                return mHeadsetAidlInterface.isWirelessHeadsetConnected();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPaired() {
        try {
            if (mHeadsetAidlInterface != null) {
                return mHeadsetAidlInterface.isPaired();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public void unPair() {
        try {
            if (mHeadsetAidlInterface != null) {
                mHeadsetAidlInterface.unPair();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public boolean isCharging() {
        try {
            if (mHeadsetAidlInterface != null) {
                return mHeadsetAidlInterface.isCharging();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAllowCharging() {
        try {
            if (mHeadsetAidlInterface != null) {
                return mHeadsetAidlInterface.isAllowCharging();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setAllowCharging(boolean allowCharging) {
        try {
            if (mHeadsetAidlInterface != null) {
                mHeadsetAidlInterface.setAllowCharging(allowCharging);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void checkConnectionState() {
        try {
            if (mHeadsetAidlInterface != null) {
                mHeadsetAidlInterface.checkConnectionState();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void checkPairingState() {
        try {
            if (mHeadsetAidlInterface != null) {
                mHeadsetAidlInterface.checkPairingState();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void checkBatteryLevel() {
        try {
            if (mHeadsetAidlInterface != null) {
                mHeadsetAidlInterface.checkBatteryLevel();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换至免提, pstn通话中调用此接口无效
     */
    public void switchToHandsFree4VOIP() {
        PSTNInternal.getInstance().setVOIPTunnel(MUSIC_CHANNEL_HANDS_FREE);
    }

    /**
     * 切换至手柄, pstn通话中调用此接口无效
     */
    public void switchToHandle4VOIP() {
        PSTNInternal.getInstance().setVOIPTunnel(MUSIC_CHANNEL_HANDLE);
    }

    /**
     * 获取当前声道
     *
     * @return MUSIC_CHANNEL_HANDS_FREE, MUSIC_CHANNEL_HANDLE
     */
    public int getCurrentMusicChannel4VOIP() {
        return PSTNInternal.getInstance().getVOIPTunnel();
    }

    public static abstract class OnHeadsetOperationListener {
        public void onConnectionStateChange(int status) {

        }

        public void onPairingStateChange(int status) {

        }

        public void onBatteryLevelChange(int percent) {

        }

        public void onChargeStateChange(int status) {

        }

        public void onBtn1Pressed() {

        }
    }
}
