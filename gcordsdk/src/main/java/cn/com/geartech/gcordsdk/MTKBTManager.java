package cn.com.geartech.gcordsdk;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import cn.com.geartech.gtplatform.model.aidl.IMTKBTAidlInterface;
import cn.com.geartech.gtplatform.model.aidl.IMTKBTCallback;

public class MTKBTManager extends GcordHelper {

    protected static final String SETTING_ENABLE_HF_MODE = "cn.com.geartech.enable_bt_hf_mode";


    private static MTKBTManager mtkbtManager;
    private Context context;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                Log.d(TAG, "onServiceConnected: " + name.toString());
                imtkbtAidlInterface = IMTKBTAidlInterface.Stub.asInterface(service);
                if (imtkbtAidlInterface != null) {
                    Log.d(TAG, "onServiceConnected: " + name.toString());
                    service.linkToDeath(deathRecipient, 0);
                    imtkbtAidlInterface.addCallback(imtkbtCallback);
                    Log.d(TAG, "onServiceConnected: call back added");
                    if (internalInitCallback != null) {
                        internalInitCallback.onInitFinished();
                    } else {
                        if (internalInitCallback != null) {
                            internalInitCallback.onInitFailed();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (internalInitCallback != null) {
                    internalInitCallback.onInitFailed();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: " + name.toString());
            imtkbtAidlInterface = null;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    connectAidl(context);
                }
            }, 2000);

        }
    };
    private final static String PLATFORM_PACKAGE_NAME = "cn.com.geartech.gtplatform";

    private MTKBTManager() {

    }

    protected static synchronized MTKBTManager getInstance() {
        if (mtkbtManager == null) {
            mtkbtManager = new MTKBTManager();
        }
        return mtkbtManager;
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private IMTKBTAidlInterface imtkbtAidlInterface = null;
    private SharedPreferences pref;
    public static final String PREF_ENABLE_MTK_BT = "PREF_ENABLE_MTK_BT";
    public static final String PREF_MTK_BT_IN_GCORD_SDK = "PREF_MTK_BT_IN_GCORD_SDK";

    protected void init(Application application) {
        context = application;
        pref = application.getSharedPreferences(PREF_MTK_BT_IN_GCORD_SDK, Context.MODE_PRIVATE);
        enable = pref.getBoolean(PREF_ENABLE_MTK_BT, false);
        if (shouldConnectAidl() && enable) {
            boolean result = connectAidl(application);
            if (!result) {
                if (internalInitCallback != null) {
                    internalInitCallback.onInitFailed();
                }
            }
        } else {
            if (internalInitCallback != null) {
                internalInitCallback.onInitFinished();
            }
        }
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.w(TAG, "MTKBTManager aidl is dead");
            imtkbtAidlInterface = null;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (enable) {
                        connectAidl(context);
                    }
                }
            }, 2000);
        }
    };

    private final static String TAG = "MTKBTManager";

    private boolean connectAidl(Context context) {
        try {
            Log.d(TAG, "begin connectting aidl");
            if (context == null) {
                return false;
            }
            Intent intent = getServiceIntent();
            boolean ret = this.context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.d(TAG, "connecting result:" + ret);
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (internalInitCallback != null) {
                internalInitCallback.onInitFailed();
            }
        }
        return false;
    }

    @NonNull
    private Intent getServiceIntent() {
        Intent intent = new Intent("cn.com.geartech.gtplatform.ACTION_GT_MTK_BT");
        intent.setPackage(PLATFORM_PACKAGE_NAME);
        intent.putExtra("packageName", context.getPackageName());
        return intent;
    }

    private boolean shouldConnectAidl() {
        try {
            PackageManager packageManager = context.getPackageManager();
            ResolveInfo resolveInfo = packageManager.resolveService(getServiceIntent(),
                    PackageManager.MATCH_ALL);
            Log.d(TAG, "resolveInfo is null? " + (resolveInfo == null));
            return resolveInfo != null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Set<MtkBtListener> mtkBtListeners = new HashSet<>();

    /**
     * ????????????????????????
     *
     * @param mtkBtListener
     */
    public synchronized void addMtkBtListener(MtkBtListener mtkBtListener) {
        if (mtkBtListener != null) {
            mtkBtListeners.add(mtkBtListener);
        }
    }

    /**
     * ????????????????????????
     *
     * @param mtkBtListener
     */
    public synchronized void removeMtkBtListener(MtkBtListener mtkBtListener) {
        if (mtkBtListener != null && mtkBtListeners.contains(mtkBtListener)) {
            mtkBtListeners.remove(mtkBtListener);
        }
    }

    private boolean enable = false;

    public void enableMTKBTService(boolean enable){
        if(enable){
            Settings.Global.putInt(context.getContentResolver(),SETTING_ENABLE_HF_MODE, 1);
        }
        else {
            Settings.Global.putInt(context.getContentResolver(),SETTING_ENABLE_HF_MODE, 0);
        }
    }

    public boolean isMTKBTServiceEnabled(){
        return  Settings.Global.getInt(context.getContentResolver(),SETTING_ENABLE_HF_MODE, 0) == 1;
    }

    public void enableMtkBt(boolean enable) {
        Log.d(TAG, "enableMtkBt,before:" + this.enable + ", after: " + enable);
        if (!this.enable && enable) {
            connectAidl(context);
        } else if (this.enable && !enable) {
            try {
                if (imtkbtAidlInterface != null) {
                    imtkbtAidlInterface.removeCallback(imtkbtCallback);
                    context.unbindService(serviceConnection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.enable = enable;
        pref.edit().putBoolean(PREF_ENABLE_MTK_BT, enable).apply();
    }

    /**
     * ?????????????????????
     * ???????????????onInitSucceed()
     */
    public void restBluetooth() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.restBluetooth();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????????????????
     * ???????????????onCurrentDeviceName()
     */
    public void getLocalName() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.getLocalName();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????????????????
     * ???????????????onCurrentDeviceName()
     *
     * @param name ????????????????????????
     */
    public void setLocalName(String name) {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.setLocalName(name);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????????????????
     * ???????????????onLocalAddress()
     */
    public void getLocalAddress() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.getLocalAddress();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ??????????????????(????????????)
     * ???????????????onInPairMode()
     */
    public void setPairMode() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.setPairMode();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ??????????????????(???????????????)
     * ???????????????onExitPairMode()
     */
    public void cancelPairMode() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.cancelPairMode();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ??????????????????????????????
     * ???????????????onHfpConnected() onA2dpConnected() onCurrentAndPairList()
     */

    public void connectLast() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.connectLast();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * ???????????????????????? ??????????????????????????????????????????
     * ???????????????onHfpConnected() onA2dpConnected() onCurrentAndPairList()
     *
     * @param addr ???????????????????????????
     */
    public void connectDevice(String addr) {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.connectDevice(addr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ???????????????????????????????????????
     * --->onSppDisconnect() onHfpDisconnected() onA2dpDisconnected() onHidDisconnected() etc;
     */
    public void disconnect() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ?????????????????????????????????
     * ????????????????????????callback??????
     *
     * @param addr ??????????????????????????????????????????
     */
    public void deletePair(String addr) {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.deletePair(addr);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ??????????????????????????????
     * ???????????????onDiscovery() NOTED:???????????????????????????????????????????????????????????????????????????
     */
    public void startDiscovery() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.startDiscovery();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????????????????
     * ???????????????onCurrentAndPairList() NOTED:???????????????????????????????????????????????????????????????????????????????????????
     */
    public void getPairList() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.getPairList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * //??????????????????
     * ???????????????onDiscoveryDone()
     */
    public void stopDiscovery() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.stopDiscovery();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????
     * ???????????????onTalking()
     */
    public void answer() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.phoneAnswer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????
     * ???????????????onHangUp()
     */
    public void hangup() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.phoneHangUp();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????
     * ???????????????onCallSucceed()
     *
     * @param phonenum ????????????????????????
     */
    public void dial(String phonenum) {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.phoneDail(phonenum);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ??????Dtmf
     *
     * @param dtmf
     */
    public void sendDTMF(String dtmf) {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.phoneTransmitDTMFCode(dtmf.charAt(0));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????????????????
     * ???????????????onHfpRemote() onVoiceDisconnected()
     */
    public void phoneTransfer() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.phoneTransfer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ??????????????????????????????
     * ???????????????onHfpLocal() onVoiceConnected()
     */
    public void phoneTransferBack() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.phoneTransferBack();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ???????????????
     * ???????????????onPhoneBook() onPhoneBookDone()
     */
    public void phoneBookStartUpdate() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.phoneBookStartUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????
     */
    public static final int GETTING_OUT_GOING_CALL_LOG = 0x04;
    /**
     * ????????????
     */
    public static final int GETTING_INCOMING_CALL_LOG = 0x05;
    /**
     * ??????????????????
     */
    public static final int GETTING_MISSED_CALL_LOG = 0x06;

    /**
     * ??????????????????
     * ???????????????onCalllog() onCalllogDone() NOTED:onCalllog???????????????????????????????????????????????????
     *
     * @param type ????????????GETTING_OUT_GOING_CALL_LOG???GETTING_INCOMING_CALL_LOG???GETTING_MISSED_CALL_LOG
     */
    public void callLogstartUpdate(int type) {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.callLogstartUpdate(type);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ?????????????????????
     * ???????????????onMusicPlaying() onMusicStopped() onMusicInfo()
     */
    public void musicPlayOrPause() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.musicPlayOrPause();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????
     * ???????????????onMusicStopped()
     */
    public void musicStop() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.musicStop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ?????????
     */
    public void musicPrevious() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.musicPrevious();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ?????????
     */
    public void musicNext() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.musicNext();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????
     *
     * @param isMute true-?????????false-????????????
     */
    public void musicMute(boolean isMute) {
        if (imtkbtAidlInterface != null) {
            try {
                if (isMute) {
                    imtkbtAidlInterface.musicMute();
                } else {
                    imtkbtAidlInterface.musicUnmute();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????
     */
    public void musicBackground() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.musicBackground();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ??????????????????
     */
    public void musicNormal() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.musicNormal();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????????????????
     * ???????????????onMusicInfo()
     */
    public void getMusicInfo() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.getMusicInfo();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ????????????hfp??????
     * ???????????????onHfpStatus()
     */
    public void inquiryHfpStatus() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.inquiryHfpStatus();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ?????????????????????????????????
     */
    public void getCurrentDeviceAddr() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.getCurrentDeviceAddr();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ?????????????????????????????????
     */
    public void getCurrentDeviceName() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.getCurrentDeviceName();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ?????????????????????
     */
    public void pauseDownLoadContact() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.pauseDownLoadContact();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * ??????????????????????????????
     * ???????????????onAuthorizationResult
     */
    public void inquiryAuthorizationStatus() {
        if (imtkbtAidlInterface != null) {
            try {
                imtkbtAidlInterface.inquiryAuthorizationStatus();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private IMTKBTCallback.Stub imtkbtCallback = new IMTKBTCallback.Stub() {
        @Override
        public void onInitSucceed() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onInitSucceed();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

        @Override
        public void onCurrentDeviceName(final String name) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onCurrentDeviceName(name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onLocalAddress(final String addr) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onLocalAddress(addr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onInPairMode() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
//                            listener.onInPairMode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onExitPairMode() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
//                            listener.onExitPairMode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onHfpConnected() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onHfpConnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onA2dpConnected() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onA2dpConnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        /**
         * NOTED:???????????????????????????????????????????????????????????????????????????????????????
         * @param index ??????
         * @param name ??????????????????????????????
         * @param addr ??????????????????????????????
         * @throws RemoteException
         */
        @Override
        public void onCurrentAndPairList(final int index,
                                         final String name,
                                         final String addr) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onCurrentAndPairList(index, name, addr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onHfpDisconnected() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onHfpDisconnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onA2dpDisconnected() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onA2dpDisconnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onSppDisconnect(final int index) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onSppDisconnect(index);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        /**
         * NOTED:???????????????????????????????????????????????????????????????????????????
         * @param name ?????????????????????????????????
         * @param addr ?????????????????????????????????
         * @param rssi ??????????????????????????????rssi
         * @throws RemoteException
         */
        @Override
        public void onDiscovery(final String name,
                                final String addr,
                                final int rssi) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onDiscovery(name, addr, rssi);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onDiscoveryDone() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onDiscoveryDone();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onTalking(final String number) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onTalking(number);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onHangUp() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onHangUp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onCallSucceed(final String number) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onCallSucceed(number);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onIncoming(final String number) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onIncoming(number);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onHfpRemote() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onHfpRemote();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onVoiceDisconnected() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onVoiceDisconnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onHfpLocal() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onHfpLocal();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onVoiceConnected() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onVoiceConnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onPhoneBook(final String name, final String number) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onPhoneBook(name, number);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onPhoneBookDone() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onPhoneBookDone();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onCalllogDone() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onCalllogDone();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        /**
         * NOTED:????????????????????????????????????????????????????????????????????????????????????
         * @param type ????????????GETTING_OUT_GOING_CALL_LOG???GETTING_INCOMING_CALL_LOG???GETTING_MISSED_CALL_LOG
         * @param name ??????????????????????????????
         * @param number ???????????????????????????
         * @throws RemoteException
         */
        @Override
        public void onCalllog(final int type, final String name, final String number) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onCalllog(type, name, number);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onHfpStatus(final int status) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onHfpStatus(status);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onMusicPlaying() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onMusicPlaying();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onMusicStopped() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onMusicStopped();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        /**
         * NOTED?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         * @param music_name ????????????
         * @param artist_nameString ???????????????
         * @param duration ????????????
         * @param pos ????????? ?????????
         * @param total
         * @param album ?????? ??????????????????????????????
         * @throws RemoteException
         */
        @Override
        public void onMusicInfo(final String music_name,
                                final String artist_nameString,
                                final int duration,
                                final int pos,
                                final int total,
                                final String album) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onMusicInfo(music_name, artist_nameString, duration, pos, total, album);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onCurrentAddr(final String addr) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onCurrentAddr(addr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onCurrentName(final String name) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onCurrentName(name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onGetPairedListEnd() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onGetPairedListEnd();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onConnecting() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onConnecting();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onOutGoingOrTalkingNumber(final String number) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onOutGoingOrTalkingNumber(number);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @Override
        public void onAuthorizationResult(final int result) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for (MtkBtListener listener :
                            mtkBtListeners) {
                        try {
                            listener.onAuthorizationResult(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };

    public interface MtkBtListener {
        void onInitSucceed();

        void onCurrentDeviceName(String name);

        void onLocalAddress(String addr);

//        void onInPairMode();

//        void onExitPairMode();

        void onHfpConnected();

        void onA2dpConnected();

        void onCurrentAndPairList(int index, String name, String addr);

        void onHfpDisconnected();

        void onA2dpDisconnected();

        void onSppDisconnect(int index);

        void onDiscovery(String name, String addr, int rssi);

        void onDiscoveryDone();

        void onTalking(String number);

        void onHangUp();

        void onCallSucceed(String number);

        void onIncoming(String number);

        void onHfpRemote();

        void onVoiceDisconnected();

        void onHfpLocal();

        void onVoiceConnected();

        void onPhoneBook(String name, String number);

        void onPhoneBookDone();

        void onCalllogDone();

        void onCalllog(int type, String name, String number);

        void onHfpStatus(int status);

        void onMusicPlaying();

        void onMusicStopped();

        void onMusicInfo(String music_name, String artist_nameString, int duration, int pos, int total, String album);

        void onCurrentAddr(String addr);

        void onCurrentName(String name);

        void onGetPairedListEnd();

        void onConnecting();

        void onOutGoingOrTalkingNumber(String number);

        void onAuthorizationResult(int result);
    }

    public static abstract class MtkBtListenerImp implements MtkBtListener {

        @Override
        public void onInitSucceed() {

        }

        @Override
        public void onCurrentDeviceName(String name) {

        }

        @Override
        public void onLocalAddress(String addr) {

        }

//        @Override
//        public void onInPairMode() {
//
//        }
//
//        @Override
//        public void onExitPairMode() {
//
//        }

        @Override
        public void onHfpConnected() {

        }

        @Override
        public void onA2dpConnected() {

        }

        @Override
        public void onCurrentAndPairList(int index, String name, String addr) {

        }

        @Override
        public void onHfpDisconnected() {

        }

        @Override
        public void onA2dpDisconnected() {

        }

        @Override
        public void onSppDisconnect(int index) {

        }

        @Override
        public void onDiscovery(String name, String addr, int rssi) {

        }

        @Override
        public void onDiscoveryDone() {

        }

        @Override
        public void onTalking(String number) {

        }

        @Override
        public void onHangUp() {

        }

        @Override
        public void onCallSucceed(String number) {

        }

        @Override
        public void onIncoming(String number) {

        }

        @Override
        public void onHfpRemote() {

        }

        @Override
        public void onVoiceDisconnected() {

        }

        @Override
        public void onHfpLocal() {

        }

        @Override
        public void onVoiceConnected() {

        }

        @Override
        public void onPhoneBook(String name, String number) {

        }

        @Override
        public void onPhoneBookDone() {

        }

        @Override
        public void onCalllogDone() {

        }

        @Override
        public void onCalllog(int type, String name, String number) {

        }

        @Override
        public void onHfpStatus(int status) {

        }

        @Override
        public void onMusicPlaying() {

        }

        @Override
        public void onMusicStopped() {

        }

        @Override
        public void onMusicInfo(String music_name, String artist_nameString, int duration, int pos, int total, String album) {

        }

        @Override
        public void onCurrentAddr(String addr) {

        }

        @Override
        public void onCurrentName(String name) {

        }

        @Override
        public void onGetPairedListEnd() {

        }

        @Override
        public void onConnecting() {

        }

        @Override
        public void onOutGoingOrTalkingNumber(String number) {

        }

        @Override
        public void onAuthorizationResult(int result) {

        }
    }

}
