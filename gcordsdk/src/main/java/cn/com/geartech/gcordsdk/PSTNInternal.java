package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cn.com.geartech.gcordsdk.dataType.CallStatusItem;
import cn.com.geartech.gtplatform.model.aidl.IPSTNAidlCallback;
import cn.com.geartech.gtplatform.model.aidl.IPSTNAidlInterface;

/**
 * Created by dinner on 15/4/27.
 */
public class PSTNInternal extends GcordHelper {


    static PSTNInternal instance = null;

    static String CTRL_DIAL = "cn.com.geartech.gtplatform.api.dial";
    static String CTRL_SWITCH_PHONE_STATE = "cn.com.geartech.gtplatform.api.switch_phone_state";
    static String CTRL_HANG_OFF = "cn.com.geartech.gtplatform.api.hang_off";
    static String CTRL_REJECT = "cn.com.geartech.gtplatform.reject";


    static String PARAM_PICKUP_TYPE = "pick_up_type";
    static String PARAM_HANDLE = "handle";
    static String PARAM_HANDS_FREE = "hands_free";
    static String PARAM_NUMBER = "number";


    static String EVENT_ON_PICK_UP = "cn.com.geartech.gtplatform.on_pick_up";
    static String EVENT_ON_INCOMING_CALL = "cn.com.geartech.gtplatform.on_incoming_call";
    static String EVENT_ON_PHONE_NUMBER_RECEIVED = "cn.com.geartech.gtplatform.on_phone_number_received";
    static String EVENT_ON_RING_END = "cn.com.geartech.gtplatform.on_ring_end";
    static String EVENT_ON_SWITCH_STATUS = "cn.com.geartech.gtplatform.on_switch_status";
    static String EVENT_ON_HANG_OFF = "cn.com.geartech.gtplatform.on_hangoff";

    final static String EVENT_INTERCEPT_INCOMING_CALL = "cn.com.geartech.gtplatform.intercept_incoming_call";

    // following events are not prepared for customers

    static String REGISTER_PACKAGE_NAME = "cn.com.geartech.gtplatform.setpackagename";

    static String PARAM_PACKAGE_NAME = "packageName";
    static String PARAM_IS_FIRST_LAUNCH = "is_first_launch";

    protected static String PARAM_DEV_ID = "dev_id";
    protected static String PARAM_DEV_TOKEN = "dev_token";

    static String CTRL_CLICK_HOME = "cn.com.geartech.gtplatform.clickHome";

    static String CTRL_AUTO_PICK_UP = "cn.com.geartech.gtplatform.autoPickUp";

    static String CTRL_PICK_UP_WITH_TYPE = "cn.com.geartech.gtplatform.pick_up_with_type";

    static String CTRL_SET_FLASH = "cn.com.geartech.gtplatform.setFlash";
    static String PARAM_FLASH_TIME = "cn.com.geartech.gtplatform.flashTime";

    static String EVENT_ON_FLASH_COMPLETE = "cn.com.geartech.gtplatform.on_flash_complete";

    static String SYS_SETTING_FLASH_TIME = "cn.com.geartech.gtplatform.sys_setting_flash_time";


    protected static String EVENT_DIAL_FINISH = "cn.com.geartech.gtplatform.dial_finish";

    static final String EVENT_GT_PLATFORM_RESTARTED = "cn.com.geartech.gtplatform.gt_platform_restarted";
    static final String EVENT_SIP_PHONE_RESTARTED = "cn.com.geartech.sip_phone.sip_phone_restarted";

    public static String CTRL_SET_HOME_KEY_CUSTOMISE = "cn.com.geartech.gtplatform.set_home_key_customise";

    public static String EVENT_ON_HOME_CLICKED = "cn.com.geartech.gtplatform.on_home_key_clicked";

    static String PARAM_ENABLE_CUSTOMISE_HOME_KEY = "cn.com.geartech.gtplatform.enable_custom_home_key";

    static String CTRL_JUMP_TO_REG_APP = "cn.con.geartech.platform.jump_to_reg_app";


    static String CTRL_START_AUDIO_RECORD = "cn.com.geartech.gtplatform.start_audio_record";
    static String CTRL_STOP_AUDIO_RECORD = "cn.com.geartech.gtplatform.stop_audio_record";

    static String PARAM_AUDIO_RECORD_NO_COPY_TO_SYS_FOLDER = "no_copy_sys";

    static String EVENT_AUDIO_RECORD_RESULT = "cn.com.geartech.gtplatform.audio_record_result";
    static String PARAM_AUDIO_RECORD_PATH = "cn.com.geartech.gtplatform.audio_record_path";
    static String PARAM_AUDIO_RECORD_RESULT = "cn.com.geartech.gtplatform.audio_record_result";
    static String PARAM_AUDIO_RECORD_ERROR_CODE = "cn.com.geartech.gtplatform.audio_record_error_code";
    static String PARAM_AUDIO_RECORD_ERROR_MESSAGE = "cn.com.geartech.gtplatform.audio_record_error_message";


    static String CTRL_SET_USE_CLASSIC_RING = "cn.com.geartech.gtplatform.set_use_classic_ring";
    static String CTRL_SET_DO_NOT_USE_CLASSIC_RING = "cn.com.geartech.gtplatform.set_do_not_use_classic_ring";


    static String CTRL_PERFORM_HOME_CLICK = "cn.com.geartech.gtplatform.ctrl_perform_home_click";

    static String EVENT_HANDLE_UP = "com.skycom.HOOK_DOWN";
    static String EVENT_HANDLE_DOWN = "com.skycom.HOOK_UP";

    static String EVENT_DIAL_NUMBER = "cn.com.geartech.gtplatform.event_dial_number";

    static String EVENT_PICKUP_STATUS_CHANGE = "cn.com.geartech.gtplatform.pickup_status_change";

    static String PARAM_PHONE_STATE = "cn.com.geartech.gtplatform.phone_state";
    static String PHONE_STATE_HANDLE = "handle";
    static String PHONE_STATE_HANDS_FREE = "hands_free";
    static String PHONE_STATE_OFF = "off";

    static String EVENT_RING_START = "cn.com.geartech.gtplatform.ring_start";
    static String EVENT_RING_END = "cn.com.geartech.gtplatform.ring_end";

    static String PARAM_IS_FIRST_RING = "is_first_ring";

    final static String CTRL_CHECK_PLATFORM_RUNNING = "cn.com.geartech.gtplatform.check_platform_running";

    final static String RESP_PLATFORM_RUNNING = "cn.com.geartech.gtplatform.resp_platform_running";


    static String EVENT_EXT_LINE_BUSY = "cn.com.geartech.gtplatform.ext_line_busy";
    static String EVENT_EXT_LINE_FREE = "cn.com.geartech.gtplatform.ext_line_free";


    final static String EVENT_BOUNDING_IC_NOT_RESPONDING = "cn.com.geartech.event_bounding_ic_not_responding";

    final static String CTRL_CHECK_EXT_LINE_STATE = "cn.com.geartech.ctrl_check_ext_line_state";

    static final String SYS_SETTING_CLASSIC_RING_PATH = "cn.com.geartech.sys_setting_classic_ring_path";

    static final String PARAM_TOKEN = "token";

    static final String CTRL_SET_SHOULD_HOLD_INCOMING_CALL_UNTIL_NUMBER_ARRIVED = "cn.com.geartech.SET_SHOULD_HOLD_INCOMING_CALL_UNTIL_NUMBER_ARRIVED";

    final static String EVENT_BUSY_TONE = "cn.com.geartech.event_busy_tone";


    String lastIncomingCallToken = "";

    protected void setLastIncomingCallToken(String token) {
        lastIncomingCallToken = token;
    }

    protected String getLastIncomingCallToken() {
        return lastIncomingCallToken;
    }

    static final String SYS_SETTING_CURRENT_RING_PATH = "cn.com.geartech.sys_setting_current_ring_file_name";

    static final String SYS_SETTING_SHOULD_USE_GLOBAL_RING = "cn.com.geartech.sys_setting_should_use_global_ring";

//    static final String SYS_SETTING_3RD_SHOULD_USE_GLOBAL_RING = "cn.com.geartech.sys_setting_should_use_global_ring_by_3rd";

    private static final String ACTION_REVERSAL_OF_POLARITY = "cn.com.geartech.ACTION_REVERSAL_OF_POLARITY";

    protected static final String SETTING_AUTO_HANG_OFF_WHEN_BUSY_TONE = "cn.com.geartech.auto_hang_off_when_busy_tone";

    interface HangOffListener {
        void onHangOff();
    }

    HangOffListener mHangOffListener;

    enum PSTNStatus {
        IDLE,
        HANDLE,
        HANDS_FREE
    }

    PSTNStatus currentStatus = PSTNStatus.IDLE;

    Context context;

    String devId = "";
    String devToken = "";

    protected String getDevId() {
        return devId;
    }

    protected String getDevToken() {
        return devToken;
    }


    String lastDialNumber = "";
    String preDialNumber = "";

    String incomingNumber;

    boolean isRinging = false;
    boolean firstRingComing = false;

    boolean isHandleUp = false;

    boolean shouldInterceptIncomingCall = false;

    private PSTNInternal() {

    }

    protected String getLastDialNumber() {
        return lastDialNumber;
    }

    protected String getCurrentDialNumber() {
        if (currentTransaction != null) {
            return currentTransaction.getDialNum();
        } else {
            return "";
        }
    }

    protected String getIncomingNumber() {
        try {
            if (mPSTNInterface != null) {
                return mPSTNInterface.getCurrentIncomingPhoneNumber();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return incomingNumber;
        //return incomingNumber;
    }


    DialTransaction currentTransaction;

    ArrayList<PhoneAPI.PhoneEventListener> listeners = new ArrayList<>();

    PhoneAPI.FlashListener flashListener;

    protected void setFlashListener(PhoneAPI.FlashListener listener) {
        flashListener = listener;
    }


    protected static PSTNInternal getInstance() {
        if (instance == null) {
            instance = new PSTNInternal();
        }

        return instance;
    }

    protected void init(final Context c, String devId, String devToken) {
        Context oldContext = context;
        context = c;

        try {
            if (oldContext != null) {
                oldContext.unregisterReceiver(mReceiver);
            }
        } catch (Exception e) {

        }

        this.devId = devId;
        this.devToken = devToken;

        initAIDL();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EVENT_ON_PICK_UP);
        intentFilter.addAction(EVENT_ON_INCOMING_CALL);
        intentFilter.addAction(EVENT_INTERCEPT_INCOMING_CALL);
        intentFilter.addAction(EVENT_ON_PHONE_NUMBER_RECEIVED);
        intentFilter.addAction(EVENT_ON_RING_END);
        intentFilter.addAction(EVENT_ON_HANG_OFF);
        intentFilter.addAction(EVENT_ON_SWITCH_STATUS);
        intentFilter.addAction(EVENT_GT_PLATFORM_RESTARTED);
        intentFilter.addAction(EVENT_ON_HOME_CLICKED);

        intentFilter.addAction(EVENT_AUDIO_RECORD_RESULT);
        intentFilter.addAction(EVENT_HANDLE_DOWN);
        intentFilter.addAction(EVENT_HANDLE_UP);

        intentFilter.addAction(EVENT_DIAL_NUMBER);
        intentFilter.addAction(EVENT_PICKUP_STATUS_CHANGE);

        intentFilter.addAction(EVENT_RING_START);
        intentFilter.addAction(EVENT_RING_END);

        intentFilter.addAction(EVENT_EXT_LINE_BUSY);
        intentFilter.addAction(EVENT_EXT_LINE_FREE);

        intentFilter.addAction(EVENT_ON_FLASH_COMPLETE);

        intentFilter.addAction(EVENT_BOUNDING_IC_NOT_RESPONDING);

        intentFilter.addAction(EVENT_BUSY_TONE);

        intentFilter.addAction(RESP_PLATFORM_RUNNING);

        intentFilter.addAction(ACTION_REVERSAL_OF_POLARITY);

        context.registerReceiver(mReceiver, intentFilter);


        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter2.addDataScheme("package");
        context.registerReceiver(packageInstallReceiver, intentFilter2);

        startGTPlatform();

        checkGTPlatformInstallation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                registerPackageAtStartUp();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!gtPlatformResponsed) {
                            DebugLog.logE("22");
                            startGTPlatform();
                        }
                    }
                }, 1000);
            }
        }, 500);

        File file = new File(ringPath);
        if (!file.exists())
            file.mkdirs();
    }

    boolean gtPlatformResponsed = false;


    void startGTPlatform() {
//        DebugLog.logE("should start GTPlatform!");
//
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("cn.com.geartech.gtplatform", "cn.com.geartech.gtplatform.MainActivity"));
//        intent.putExtra("AUTO_STOP", "1");
//        context.startActivity(intent);

        try {
            Intent i = new Intent();
            i.setComponent(new ComponentName("cn.com.geartech.gtplatform", "cn.com.geartech.gtplatform.model.service.GTPhoneManService"));
            ComponentName cc = context.startService(i);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void registerPackageAtStartUp() {
        if (!gtPlatformResponsed) {
            String packageName = context.getApplicationInfo().packageName;
            Intent intent = new Intent(REGISTER_PACKAGE_NAME);
            intent.putExtra(PARAM_PACKAGE_NAME, packageName);
            intent.putExtra(PARAM_IS_FIRST_LAUNCH, true);
            if (devId == null || devId.length() == 0 || devToken == null || devToken.length() == 0) {
                return;
            }
            intent.putExtra(PARAM_DEV_ID, devId);
            intent.putExtra(PARAM_DEV_TOKEN, devToken);
            context.sendBroadcast(intent);
        }
    }


    void checkGTPlatformInstallation() {
        String version = getGTPlatformVersion();

        boolean shouldInstall = false;

        if (version == null || version.length() == 0) {
            shouldInstall = true;
        } else {
            DebugLog.logE(version);

            String[] versionStrings = version.split("\\.");

            for (String s : versionStrings) {
                DebugLog.logE(s);
            }

            try {
                int v1 = Integer.parseInt(versionStrings[0]);
                int v2 = Integer.parseInt(versionStrings[1]);
                int v3 = Integer.parseInt(versionStrings[2]);
                if (v1 <= 1 && v2 == 0 && v3 < 11) {
                    shouldInstall = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (shouldInstall) {
            DebugLog.logE("should install!");

            AssetManager assetManager = context.getAssets();
            try {
                InputStream inputStream = assetManager.open("gtplatform.apk");
                if (inputStream != null) {
                    String path = Environment.getExternalStorageDirectory().getPath();
                    if (!path.endsWith("\\") && !path.endsWith("/")) {
                        path += "/";
                    }
                    path += "gtplatform.apk";

                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream outputStream = new FileOutputStream(file);

                    byte[] bytes = new byte[1024];
                    int c;
                    while ((c = inputStream.read(bytes)) > 0) {
                        outputStream.write(bytes, 0, c);
                    }

                    outputStream.close();
                    inputStream.close();

                    Intent intent = new Intent("android.intent.action.SILENT_INSTALL_PACKAGE");
                    intent.putExtra("apkPath", path);

                    context.startService(intent);
                }
            } catch (Exception e) {

            }
        } else {

        }
    }

    String getGTPlatformVersion() {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        if (packageInfos != null) {
            for (PackageInfo info : packageInfos) {
                if (info.packageName != null && info.packageName.equals("cn.com.geartech.gtplatform")) {
                    return info.versionName;
                }
            }
        }

        return null;
    }

    void checkGTPlatformRunning() {

    }

    PhoneAPI.PhoneEventListener phoneEventListenerToSet = null;

    protected void setPhoneEventListener(PhoneAPI.PhoneEventListener listener) {
        if (phoneEventListenerToSet != null) {
            removePhoneEventListener(phoneEventListenerToSet);
        }

        phoneEventListenerToSet = listener;

        if (listener != null) {
            addPhoneEventListener(phoneEventListenerToSet);
        }
    }

    protected void addPhoneEventListener(PhoneAPI.PhoneEventListener listener) {
        if (listener == null) {
            Log.e("gcordSDK", "PhoneEventListener cannot be null");
            NullPointerException exception = new NullPointerException();
            throw exception;
            //throw new NullPointerException();
        }

        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    protected void removePhoneEventListener(PhoneAPI.PhoneEventListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }


    protected void dial(final String number) {
        DebugLog.logE("dial:" + number);

//        if(number.equals("*#31#") || number.equals("*#32#"))
//        {
//            DebugLog.logE(number);
//
//            Intent intent = new Intent(CTRL_DIAL);
//            intent.putExtra(PARAM_NUMBER, number);
//
//            //  Log.e("111", "dial! " + number );
//
//            context.sendBroadcast(intent);
//            return;
//        }
//        else

        if (number.equals("*#*#9001#*")) {
            DebugLog.setEnableLog(true);
            if (isInCall()) {
                hangOff();
            }
        } else if (number.equals("*#*#9000#*")) {
            DebugLog.setEnableLog(false);
            if (isInCall()) {
                hangOff();
            }
        } else {
            Intent intent = new Intent(CTRL_DIAL);
            intent.putExtra(PARAM_NUMBER, number);


            addCallStackAndPackageNameToIntent(intent);

            context.sendBroadcast(intent);
        }

//        if(!isInCall())
//        {
//            if(!isHandleUp)
//            {
//                pickUpWithHandsFree();
//            }
//            else
//            {
//                pickUpWithHandle();
//            }
//
//            pickupActionListener = new PhoneAPI.PickupActionListener() {
//                @Override
//                public void onPickUp() {
//                    Intent intent = new Intent(CTRL_DIAL);
//                    intent.putExtra(PARAM_NUMBER, number);
//
//                    //  Log.e("111", "dial! " + number );
//
//                    context.sendBroadcast(intent);
//                }
//            };
//        }
//        else
//        {
//
//        }


        //  Log.e("111", "dial! " + number );


        if (isInCall()) {
            currentTransaction.inputNumber(number);
        } else {
            preDialNumber = number;
        }
    }

    protected void SwitchPhoneState(PhoneAPI.PICKUP_STATE state) {
        DebugLog.logE("switch phone state");

        Intent intent = new Intent(CTRL_SWITCH_PHONE_STATE);
        if (state == PhoneAPI.PICKUP_STATE.HANDLE) {
            intent.putExtra(PARAM_PICKUP_TYPE, PARAM_HANDLE);
        } else if (state == PhoneAPI.PICKUP_STATE.HANDS_FREE) {
            intent.putExtra(PARAM_PICKUP_TYPE, PARAM_HANDS_FREE);
        }

        addCallStackAndPackageNameToIntent(intent);

        context.sendBroadcast(intent);
    }

    protected void hangOff() {
        DebugLog.logE("hang off");
        Intent intent = new Intent(CTRL_HANG_OFF);

        addCallStackAndPackageNameToIntent(intent);

        context.sendBroadcast(intent);
    }

    private void addCallStackAndPackageNameToIntent(Intent intent) {
        try {
            intent.putExtra("packageName", context.getPackageName());
            Exception exception = new Exception();
            StackTraceElement[] traces = exception.getStackTrace();
            String s = "";
            if (traces != null) {
                for (StackTraceElement element : traces) {
                    s += element.toString();
                }
            }

            if (s.length() > 1000) {
                s = s.substring(s.length() - 1000);
            }

            if (s.length() < 2000) {
                intent.putExtra("callStack", s);
            }

        } catch (Exception e) {

        }

    }

    protected void reject() {
        DebugLog.logE("reject");

        Intent intent = new Intent(CTRL_REJECT);

        addCallStackAndPackageNameToIntent(intent);

        context.sendBroadcast(intent);
    }

    protected void clickHome() {
        Intent intent = new Intent(CTRL_CLICK_HOME);
        intent.putExtra("packageName", context.getPackageName());

        addCallStackAndPackageNameToIntent(intent);

        context.sendBroadcast(intent);
    }

    protected boolean isInCall() {
        return currentStatus != PSTNStatus.IDLE;
//        try {
//            if(mPSTNInterface != null)
//            {
//                return mPSTNInterface.getPSTNStatus() != 0;
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return false;
    }

    protected boolean isHandsFreeOn() {
        return currentStatus == PSTNStatus.HANDS_FREE;
//        try {
//            if(mPSTNInterface != null)
//            {
//                int status = mPSTNInterface.getPSTNStatus();
//                //Log.e("tag","hands free:" + status);
//                return status == 1;
//                //return mPSTNInterface.getPSTNStatus() == 1;
//            }
//            else
//            {
//                //Log.e("tag","mPSTNInterface is null");
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//
//       // Log.e("333","444");
//
//        return true;
    }

    protected boolean isHandleOn() {
        return currentStatus == PSTNStatus.HANDLE;
    }


    protected boolean isLineBusy = false;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(EVENT_ON_PICK_UP)) {
                firstRingComing = false;
                PhoneAPI.PICKUP_STATE state = PhoneAPI.PICKUP_STATE.HANDS_FREE;
                String type = intent.getStringExtra(PARAM_PICKUP_TYPE);
                DebugLog.logE("on pickup event " + type);

                if (type != null && type.equals(PARAM_HANDLE)) {
                    state = PhoneAPI.PICKUP_STATE.HANDLE;

                    currentStatus = PSTNStatus.HANDLE;
                } else {
                    currentStatus = PSTNStatus.HANDS_FREE;
                }

                currentTransaction = new DialTransaction(context);
                currentTransaction.start();

                if (preDialNumber != null && preDialNumber.length() > 0) {
                    currentTransaction.inputNumber(preDialNumber);
                }

                if (pickupActionListener != null) {
                    pickupActionListener.onPickUp();
                    pickupActionListener = null;
                } else {
                    DebugLog.logE("listener on pickup");
                    for (PhoneAPI.PhoneEventListener listener : listeners) {
                        listener.onPickUp(state);
                    }
                }
                setIsPickingUp(false);
                isRinging = false;
            } else if (action.equals(EVENT_ON_HANG_OFF)) {
                DebugLog.logE("on hangoff event");

                if (currentTransaction != null) {
                    currentTransaction.end();
                    String num = currentTransaction.getDialNum();
                    if (num != null && num.length() > 0) {
                        lastDialNumber = num;
                    }
                }

                onHangOff();


            } else if (action.equals(EVENT_ON_INCOMING_CALL)) {
                DebugLog.logE("on incoming call");


                isRinging = true;

                String phoneNumber = intent.getStringExtra("phoneNumber");
                if (phoneNumber != null && phoneNumber.trim().length() > 0) {
                    incomingNumber = phoneNumber;
                    Log.e("phoneNumber", phoneNumber);
                }
                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    listener.onInComingCall();
                }

            } else if (action.equals(EVENT_INTERCEPT_INCOMING_CALL)) {
                DebugLog.logE("intercept incoming call");

                String token = intent.getStringExtra(PARAM_TOKEN);
                if (token != null) {
                    if (lastIncomingCallToken == null || !lastIncomingCallToken.equals(token)) {
                        lastIncomingCallToken = token;
                        String phoneNumber =
                                intent.getStringExtra("phoneNumber");
                        Log.e("phoneNumber", phoneNumber);
                        notifyIncomingCallEvent(phoneNumber);
                    }
                }
            } else if (action.equals(EVENT_ON_PHONE_NUMBER_RECEIVED)) {
                DebugLog.logE("on phone number received");


                String phoneNumber = intent.getStringExtra(PARAM_NUMBER);
                incomingNumber = phoneNumber;
                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    listener.onPhoneNumberReceived(phoneNumber);
                }
            } else if (action.equals(EVENT_ON_RING_END)) {
                DebugLog.logE("on ring end");

                isRinging = false;

                incomingNumber = null;
                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    listener.onRingEnd();
                }
            } else if (action.equals(EVENT_ON_SWITCH_STATUS)) {
                DebugLog.logE("on switch status");

                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    PhoneAPI.PICKUP_STATE state = PhoneAPI.PICKUP_STATE.HANDS_FREE;
                    String type = intent.getStringExtra(PARAM_PICKUP_TYPE);
                    if (type != null && type.equals(PARAM_HANDLE)) {
                        state = PhoneAPI.PICKUP_STATE.HANDLE;
                        currentStatus = PSTNStatus.HANDLE;
                    } else if (type != null && type.equals(PARAM_HANDS_FREE)) {
                        state = PhoneAPI.PICKUP_STATE.HANDS_FREE;
                        currentStatus = PSTNStatus.HANDS_FREE;
                    }


                    listener.onSwitchPhoneState(state);
                }
            } else if (action.equals(EVENT_RING_START)) {
                boolean isFirstRing = intent.getBooleanExtra(PARAM_IS_FIRST_RING, false);
                if (isFirstRing) {
                    DebugLog.logE("first ring!");
                    firstRingComing = true;
                }

                isRinging = true;
            } else if (action.equals(EVENT_RING_END)) {
                firstRingComing = false;
                isRinging = true;
            } else if (action.equals(EVENT_PICKUP_STATUS_CHANGE)) {

            } else if (action.equals(EVENT_EXT_LINE_BUSY)) {
                isLineBusy = true;
                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    listener.onLineBusy();
                }
            } else if (action.equals(EVENT_EXT_LINE_FREE)) {
                isLineBusy = false;
                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    listener.onLineResumed();
                }
            } else if (action.equals(EVENT_ON_FLASH_COMPLETE)) {
                DebugLog.logE("on flash complete!");
                if (flashListener != null) {
                    flashListener.onFlashComplete();
                    flashListener = null;
                }
            } else if (action.equals(EVENT_GT_PLATFORM_RESTARTED)) {
                GTAidlHandler.getInstance().initAIDL();
                initAIDL();
                String packageName = context.getApplicationInfo().packageName;
                Intent intent1 = new Intent(REGISTER_PACKAGE_NAME);
                intent1.putExtra(PARAM_PACKAGE_NAME, packageName);

                if (devId == null || devId.length() == 0 || devToken == null || devToken.length() == 0) {
                    return;
                }
                intent1.putExtra(PARAM_DEV_ID, devId);
                intent1.putExtra(PARAM_DEV_TOKEN, devToken);

                context.sendBroadcast(intent1);


                setHomeKeyCustomEventEnabled(isCustomHomeKeyEnabled);

//                setUseClassicRing(shouldUseClassicRing());

            } else if (action.equals(EVENT_ON_HOME_CLICKED)) {
                String packageName = intent.getStringExtra(PARAM_PACKAGE_NAME);
                if (packageName != null && packageName.equals(context.getPackageName())) {
//                    for(PhoneAPI.HomeKeyListener listener:homeKeyListeners)
//                    {
//                        listener.onHomeClicked();
//                    }
                }
            } else if (action.equals(EVENT_DIAL_NUMBER)) {
                String number = intent.getStringExtra(PARAM_NUMBER);

                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    listener.onNumberSent(number);
                }

                DebugLog.logE("on dial:" + number);

                try {
                    currentTransaction.checkDialFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (action.equals(EVENT_BOUNDING_IC_NOT_RESPONDING)) {
                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    listener.onCommunicationError();
                }
                DebugLog.logE("on platform not responding");
            } else if (action.equals(EVENT_HANDLE_UP)) {
                isHandleUp = true;
            } else if (action.equals(EVENT_HANDLE_DOWN)) {
                isHandleUp = false;
            } else if (action.equals(EVENT_AUDIO_RECORD_RESULT)) {
                Log.e("123", "on audio record result");
                boolean recordSuccess = intent.getBooleanExtra(PARAM_AUDIO_RECORD_RESULT, false);
                String uuid = intent.getStringExtra("uuid");
                PhoneAPI.AudioRecordListener listener = null;
                Object token = null;
                try {

                    if (uuid == null) {
                        for (String s : audioRecordItems.keySet()) {
                            uuid = s;
                            break;
                        }
                    }


                    if (uuid != null) {
                        AudioRecordListenerItem item = audioRecordItems.get(uuid);
                        if (item != null) {
                            listener = item.listener;
                            token = item.customToken;
                        }
                    }


                    if (listener != null) {
                        if (!recordSuccess) {
                            int errorCode = intent.getIntExtra(PARAM_AUDIO_RECORD_ERROR_CODE, PhoneAPI.AUDIO_RECORD_ERROR_CODE_UNKNOWN);
                            String errorMessage = intent.getStringExtra(PARAM_AUDIO_RECORD_ERROR_MESSAGE);
                            listener.onRecordFail(errorCode, errorMessage, token);
                        } else {
                            final String audioPath = intent.getStringExtra(PARAM_AUDIO_RECORD_PATH);
                            listener.onRecordSuccess(audioPath, token);

                            if (audioPath != null && audioPath.contains(context.getPackageName())) {
                                if (shouldDeleteAudioRecordAfterCallback) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                new File(audioPath).delete();
                                            } catch (Exception e) {

                                            }
                                        }
                                    }, 5000);
                                }
                            }
                        }

                        if (uuid != null) {
                            audioRecordItems.remove(uuid);
                        }
                    }
                } catch (Exception e) {

                }
            } else if (action.equals(RESP_PLATFORM_RUNNING)) {
                DebugLog.logE("gtplatform responsed!");
                gtPlatformResponsed = true;
            } else if (action.equals(EVENT_BUSY_TONE)) {
                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    listener.onBusyTone();
                }
            } else if (ACTION_REVERSAL_OF_POLARITY.equals(action)) {
                for (PhoneAPI.PhoneEventListener listener : listeners) {
                    listener.onReversalOfPolarity();
                }
            }
        }
    };

    Handler handler = new Handler();

    protected void pickUpWithHandsFree() {
        Intent intent = new Intent(CTRL_PICK_UP_WITH_TYPE);
        intent.putExtra(PARAM_PICKUP_TYPE, PARAM_HANDS_FREE);

        addCallStackAndPackageNameToIntent(intent);

        context.sendBroadcast(intent);
    }

    protected void pickUpWithHandle() {
        Intent intent = new Intent(CTRL_PICK_UP_WITH_TYPE);
        intent.putExtra(PARAM_PICKUP_TYPE, PARAM_HANDLE);

        addCallStackAndPackageNameToIntent(intent);

        context.sendBroadcast(intent);
    }

    PhoneAPI.PickupActionListener pickupActionListener;

    protected void autoPickUpForAction(PhoneAPI.PickupActionListener l) {
        if (currentStatus != PSTNStatus.IDLE) {
            if (l != null) {
                l.onPickUp();
            }
        } else {
            setIsPickingUp(true);
            autoPickUp();
            pickupActionListener = l;
        }
    }

    protected void autoPickUp() {
        Intent intent = new Intent(CTRL_AUTO_PICK_UP);

        addCallStackAndPackageNameToIntent(intent);

        context.sendBroadcast(intent);
    }

    protected boolean isRinging() {
        return isRinging;
    }


    protected void setFlashTime(byte interval) {
        try {
            Settings.System.putInt(context.getContentResolver(), SYS_SETTING_FLASH_TIME, (int) interval);
            flashTime = interval;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    byte flashTime = 0;

    protected byte getFlashTime() {
        if (flashTime == 0) {
            flashTime = (byte) Settings.System.getInt(context.getContentResolver(), SYS_SETTING_FLASH_TIME, 0x32);
        }
        return flashTime;
    }

    protected void setFlash(byte interval) {
        if (interval == (byte) 0x31 || interval == (byte) 0x32 || interval == (byte) 0x33 || interval == (byte) 0x34) {

        } else {
            interval = (byte) 0x34;
        }

        Intent intent = new Intent(CTRL_SET_FLASH);
        intent.putExtra(PARAM_FLASH_TIME, interval);

        addCallStackAndPackageNameToIntent(intent);

        context.sendBroadcast(intent);
    }

    // ArrayList<PhoneAPI.HomeKeyListener> homeKeyListeners = new ArrayList<PhoneAPI.HomeKeyListener>();

    public void setHomeKeyCustomEventEnabled(boolean enable) {
        Intent intent = new Intent(CTRL_SET_HOME_KEY_CUSTOMISE);
        intent.putExtra(PARAM_ENABLE_CUSTOMISE_HOME_KEY, enable);
        intent.putExtra(PARAM_PACKAGE_NAME, context.getPackageName());
        context.sendBroadcast(intent);

        isCustomHomeKeyEnabled = enable;
    }

    boolean isCustomHomeKeyEnabled = false;

//    public void addHomeKeyEventListener(PhoneAPI.HomeKeyListener l)
//    {
//        homeKeyListeners.add(l);
//    }
//
//    public void removeHomeKeyEventListener(PhoneAPI.HomeKeyListener l)
//    {
//        homeKeyListeners.remove(l);
//    }


    public void jumpToRegApp() {
        Intent intent = new Intent(CTRL_JUMP_TO_REG_APP);
        context.sendBroadcast(intent);
    }

    public void finish() {
        try {

            DebugLog.logE("finish......");

            if (context != null) {
                context.unregisterReceiver(mReceiver);
                context.unregisterReceiver(packageInstallReceiver);
            }
        } catch (Exception e) {

        }

    }

    PhoneAPI.AudioRecordListener audioRecordListener;
    //LinkedBlockingQueue<PhoneAPI.AudioRecordListener> audioRecordListeners = new LinkedBlockingQueue<PhoneAPI.AudioRecordListener>();


    public void hangOffAndDial(final String number) {

        DebugLog.logE("hangOffAndDial");

        if (currentStatus == PSTNStatus.IDLE) {
            return;
        }

        final PSTNStatus lastPickupStatus = currentStatus;
        if (isInCall()) {
            hangOff();
        } else reject();
        mHangOffListener = new HangOffListener() {
            @Override
            public void onHangOff() {

                DebugLog.logE("hangOffAndDial on hangOff");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (lastPickupStatus == PSTNStatus.HANDLE) {
                            pickUpWithHandle();
                            DebugLog.logE("hangOffAndDial redial with handle");
                        } else {
                            pickUpWithHandsFree();
                            DebugLog.logE("hangOffAndDial redial with hands free");
                        }

                        pickupActionListener = new PhoneAPI.PickupActionListener() {
                            @Override
                            public void onPickUp() {
                                DebugLog.logE("hangOffAndDial on pick up");
                                dial(number);
                            }
                        };
                    }
                }, 700);

            }
        };
    }

    boolean shouldNotCopyExtraAudioRecordToSysFolder = false;

    /**
     * set to true if you don't want extra copy in sys folder which is used in CallLogManager
     *
     * @param noCopy if not set, default to false
     */
    public void setShouldNotCopyExtraAudioRecordToSysFolder(boolean noCopy) {
        shouldNotCopyExtraAudioRecordToSysFolder = noCopy;
    }

    public void startRecording(final PhoneAPI.AudioRecordListener listener) {
        audioRecordListener = listener;
        Intent intent = new Intent(CTRL_START_AUDIO_RECORD);
        if (shouldNotCopyExtraAudioRecordToSysFolder) {
            intent.putExtra(PARAM_AUDIO_RECORD_NO_COPY_TO_SYS_FOLDER, true);
        }

        intent.putExtra(PARAM_PACKAGE_NAME, context.getPackageName());

        context.sendBroadcast(intent);

    }

    static class AudioRecordListenerItem {
        PhoneAPI.AudioRecordListener listener;
        Object customToken;
    }

    HashMap<String, AudioRecordListenerItem> audioRecordItems = new HashMap<String, AudioRecordListenerItem>();

    static final String PREF_AUDIO_RECORD_SORT_BY_DATE = "cn.com.geartech.gtplatform.pref_audio_record_sort_by_date";

    public void setAudioRecordSortByDateDirectory(boolean use) {
        try {
            if (use) {
                Settings.System.putInt(context.getContentResolver(), PREF_AUDIO_RECORD_SORT_BY_DATE, 1);
            } else {
                Settings.System.putInt(context.getContentResolver(), PREF_AUDIO_RECORD_SORT_BY_DATE, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAudioRecordSortByDateDirectory() {
        try {
            int isAudioRecordSortByDate = Settings.System.getInt(context.getContentResolver(), PREF_AUDIO_RECORD_SORT_BY_DATE);
            if (isAudioRecordSortByDate == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    boolean alreadyRecording = false;

    public boolean startRecording(final PhoneAPI.AudioRecordListener listener, Object customToken) {
        return startRecording(listener, customToken, "");
    }

    boolean startRecordingWav(final PhoneAPI.AudioRecordListener listener, Object customToken) {
        return startRecording(listener, customToken, "wav");
    }

    boolean startRecordingMp3(final PhoneAPI.AudioRecordListener listener, Object customToken) {
        return startRecordingMp3(listener, customToken, 8000, 32);
    }

    boolean startRecordingMp3(final PhoneAPI.AudioRecordListener listener, Object customToken, int sampleRate, int bitRate) {
        if (alreadyRecording) {
            return false;
        }
        String uuid = UUID.randomUUID().toString();
        AudioRecordListenerItem item = new AudioRecordListenerItem();
        item.customToken = customToken;
        item.listener = listener;
        audioRecordItems.put(uuid, item);

        Intent intent = new Intent(CTRL_START_AUDIO_RECORD);
        intent.putExtra("uuid", uuid);
        intent.putExtra("packageName", context.getPackageName());
        intent.putExtra("format", "mp3");
        intent.putExtra("SampleRate", sampleRate);
        intent.putExtra("BitRate", bitRate);

        if (shouldNotCopyExtraAudioRecordToSysFolder) {
            intent.putExtra(PARAM_AUDIO_RECORD_NO_COPY_TO_SYS_FOLDER, true);
        }

        context.sendBroadcast(intent);
        return true;

    }

    private boolean startRecording(final PhoneAPI.AudioRecordListener listener, Object customToken, String format) {

        if (alreadyRecording) {
            return false;
        }
        String uuid = UUID.randomUUID().toString();
        AudioRecordListenerItem item = new AudioRecordListenerItem();
        item.customToken = customToken;
        item.listener = listener;
        audioRecordItems.put(uuid, item);

        Intent intent = new Intent(CTRL_START_AUDIO_RECORD);
        intent.putExtra("uuid", uuid);
        intent.putExtra("packageName", context.getPackageName());
        if (format != null && format.trim().length() > 0) {
            intent.putExtra("format", format);
        }

        if (shouldNotCopyExtraAudioRecordToSysFolder) {
            intent.putExtra(PARAM_AUDIO_RECORD_NO_COPY_TO_SYS_FOLDER, true);
        }

        context.sendBroadcast(intent);
        return true;
    }


    public void stopRecording() {
        Intent intent = new Intent(CTRL_STOP_AUDIO_RECORD);
        context.sendBroadcast(intent);
    }


    public void setUseClassicRing(boolean use) {
        int enable = use ? 1 : 0;
        Settings.System.putInt(context.getContentResolver(), SHOULD_USE_CLASSIC_RING_LAUNCHER, enable);
//        Settings.System.putInt(context.getContentResolver(), SHOULD_USE_CLASSIC_RING, enable);
    }

    /**
     * @param use
     * @see #setUseClassicRing
     */
    @Deprecated
    public void setUseClassicRingByLauncher(boolean use) {
        setUseClassicRing(use);
    }

    //    final static String SHOULD_USE_CLASSIC_RING = "cn.com.geartech.gtplatform.shouldUseClassicRing";
    final static String SHOULD_USE_CLASSIC_RING_LAUNCHER = "cn.com.geartech.gtplatform.shouldUseClassicRing_by_launcher";


    public boolean shouldUseClassicRing() {
        int shouldUse = Settings.System.getInt(context.getContentResolver(), SHOULD_USE_CLASSIC_RING_LAUNCHER, 0);
        return (shouldUse == 1);
    }

    protected void setClassicRingPath(String path) {
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                String fileName = file.getName();
                String targetPath = classicalRingtonePath + "/" + fileName;
                File target = new File(targetPath);
                if (target.exists() && target.isFile()) {
                    String sourceSha1 = calBigFileSHA1(path);
                    String targetSha1 = calBigFileSHA1(targetPath);
                    if (sourceSha1 != null && sourceSha1.equals(targetSha1)) {
                        Settings.System.putString(context.getContentResolver(), SYS_SETTING_CLASSIC_RING_PATH, path);
                        return;
                    }
                }
                renameOrignalFile(fileName, targetPath);
                if (copyFileUsingStream(file, new File(targetPath))) {
                    Settings.System.putString(context.getContentResolver(), SYS_SETTING_CLASSIC_RING_PATH, targetPath);
                    DebugLog.logE("setClassicRingPath" + path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected String getClassicRingPath() {
        try {
            return Settings.System.getString(context.getContentResolver(), SYS_SETTING_CLASSIC_RING_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    BroadcastReceiver packageInstallReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                String pkgName = intent.getDataString();
                if (pkgName != null && pkgName.equals("cn.com.geartech.gtplatform")) {
                    // gtplatform installed
                    if (!gtPlatformResponsed) {
                        checkGTPlatformRunning();
                    }
                }
            }
        }
    };

    private boolean isPickingUp = false;

    public boolean isPickingUp() {
        return isPickingUp;
    }

    public void setIsPickingUp(boolean isPickingUp) {
        this.isPickingUp = isPickingUp;
    }

    final String PREF_INST_INTERVAL = "cn.com.geartech.PREF_INST_INTERVAL";


    protected void setDialInterval(int interval) {
        try {
            Settings.System.putInt(context.getContentResolver(), PREF_INST_INTERVAL, interval);
            GTAidlHandler.getInstance().updateInstInterval(interval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getDialInterval() {
        try {
            return Settings.System.getInt(context.getContentResolver(), PREF_INST_INTERVAL, 250);
        } catch (Exception e) {
            e.printStackTrace();
            return 250;
        }
    }


    boolean enablePSTN = true;
    boolean pstnWasSetByUser = false;

    protected boolean isEnablePSTN() {
        return enablePSTN;
    }

    protected void setEnablePSTN(boolean enablePSTN) {
        this.enablePSTN = enablePSTN;

        pstnWasSetByUser = true;

        try {
            if (mPSTNInterface != null) {
                mPSTNInterface.enablePSTN(enablePSTN, context.getPackageName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void setVOIPTunnel(int tunnel) {
        try {
            if (mPSTNInterface != null) {
                mPSTNInterface.setMusicTunnel(tunnel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getVOIPTunnel() {
        try {
            if (mPSTNInterface != null) {
                return mPSTNInterface.getMusicTunnel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    void initAIDL() {
        Intent intent = new Intent("cn.com.geartech.gtplatform.model.aidl.IGTPhoneManInterface");
        intent.setPackage("cn.com.geartech.gtplatform");
        intent.putExtra("packageName", context.getPackageName());

        Log.e("aidl", "init pstn aidl");
        boolean ret = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if (!ret) {
            if (internalInitCallback != null) {
                internalInitCallback.onInitFailed();
            }
        }
    }

    IPSTNAidlInterface mPSTNInterface = null;

    protected boolean isPhysicalHandleUp() {
        try {
            return mPSTNInterface.isHandlePickedUp();
        } catch (Throwable e) {
            return false;
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mPSTNInterface = IPSTNAidlInterface.Stub.asInterface(iBinder);

            try {
                if (pstnWasSetByUser) {
                    mPSTNInterface.enablePSTN(enablePSTN, context.getPackageName());
                }

                if (shouldInterceptIncomingCall) {
                    mPSTNInterface.startInterceptIncomingCall(context.getPackageName());
                } else {
                    mPSTNInterface.endInterceptIncomingCall(context.getPackageName());
                }

                if (internalInitCallback != null) {
                    internalInitCallback.onInitFinished();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mPSTNInterface = null;
        }
    };

    protected void notifyIncomingCallEvent(String phoneNumber) {
        try {
            Intent intent = new Intent(EVENT_ON_INCOMING_CALL);
            intent.setPackage(context.getPackageName());
            if (phoneNumber != null && phoneNumber.trim().length() > 0) {
                intent.putExtra("phoneNumber", phoneNumber);
            }
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getExtLineState() {
        if (mPSTNInterface != null) {
            try {
                return mPSTNInterface.getExtLineStatus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public void checkExtLineState() {
        context.sendBroadcast(new Intent(CTRL_CHECK_EXT_LINE_STATE));
    }

    public void performHomeEvent() {
        Intent intent = new Intent(CTRL_PERFORM_HOME_CLICK);
        context.sendBroadcast(intent);
    }

    protected void startInterceptIncomingCall() {
        shouldInterceptIncomingCall = true;
        try {
            if (mPSTNInterface != null) {
                mPSTNInterface.startInterceptIncomingCall(context.getPackageName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void endInterceptIncomingCall() {
        shouldInterceptIncomingCall = false;
        try {
            if (mPSTNInterface != null) {
                mPSTNInterface.endInterceptIncomingCall(context.getPackageName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setIncomingCallShouldWaitForPhoneNumber(boolean shouldWait) {
        Intent intent = new Intent(CTRL_SET_SHOULD_HOLD_INCOMING_CALL_UNTIL_NUMBER_ARRIVED);
        if (shouldWait) {
            intent.putExtra("shouldDelay", 1);
        } else {
            intent.putExtra("shouldDelay", 0);
        }

        context.sendBroadcast(intent);
    }

//    protected boolean shouldIncomingCallWaitForPhoneNumber()
//    {
//
//    }

    protected void volUp() {
        try {
            mPSTNInterface.volUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void volDown() {
        try {
            mPSTNInterface.volDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean setMicrophoneMute(boolean mute) {
        try {
            return mPSTNInterface.setMicrophoneMute(mute);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

//    protected boolean setCurrentCallMute(boolean mute)
//    {
//        try {
//            if(mPSTNInterface != null)
//            {
//                return mPSTNInterface.setMicrophoneMute(mute);
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

    protected boolean isCurrentCallMute() {
        try {
            if (mPSTNInterface != null) {
                return mPSTNInterface.isMicrophoneMute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    protected int getCallDirection() {
        try {
            if (mPSTNInterface != null) {
                return mPSTNInterface.getCurrentCallDirection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PhoneAPI.CALL_DIRECTION_NONE;
    }

    public boolean isAutoAnswerOpen() {
        try {
            if (mPSTNInterface != null) {
                return mPSTNInterface.isAutoAnswerOpen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void enableAutoAnswer(boolean enable) {
        try {
            if (mPSTNInterface != null) {
                mPSTNInterface.enableAutoAnswer(enable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean copyFileUsingStream(File source, File dest) {

        try {
            FileInputStream fileInputStream = new FileInputStream(source);
            FileOutputStream fileOutputStream = new FileOutputStream(
                    dest);

            int bufferSize;
            byte[] buffer = new byte[512];
            while ((bufferSize = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferSize);
            }
            fileInputStream.close();
            fileOutputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String calBigFileSHA1(String filePath) {

        File file = new File(filePath);
        if (!file.exists())
            return null;

        MessageDigest messagedigest;

        FileInputStream in = null;

        byte[] hash = null;

        try {

            in = new FileInputStream(file);
            messagedigest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024 * 1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, len);
            }

            in.close();

            hash = messagedigest.digest();
        } catch (Exception ignored) {
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        if (hash != null) {
            StringBuilder buf = new StringBuilder();
            for (byte b : hash) {
                int halfbyte = (b >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                    halfbyte = b & 0x0F;
                } while (two_halfs++ < 1);
            }
            return buf.toString();
        } else {
            return null;
        }
    }

    public boolean setCurrentRingTone(String fullPath) {

        if (TextUtils.isEmpty(fullPath))
            return false;

        File file = new File(fullPath);
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            String targetPath = ringPath + "/" + fileName;
            File target = new File(targetPath);
            if (target.exists() && target.isFile()) {
                String sourceSha1 = calBigFileSHA1(fullPath);
                String targetSha1 = calBigFileSHA1(targetPath);
                if (sourceSha1 != null && sourceSha1.equals(targetSha1)) {
                    Settings.System.putString(context.getContentResolver(), SYS_SETTING_CURRENT_RING_PATH, fileName);
                    return true;
                }
            }

            renameOrignalFile(fileName, targetPath);

            if (copyFileUsingStream(file, new File(targetPath))) {
                String path = target.getAbsolutePath();
                Settings.System.putString(context.getContentResolver(), SYS_SETTING_CURRENT_RING_PATH, fileName);

                Log.e("SYS_SETTING", "setCurrentRingTone" + path);
                DebugLog.logE("setCurrentRingTone" + path);
                return true;
            }
        }

        return false;
    }

    private void renameOrignalFile(String fileName, String targetPath) {
        try {
            File target = new File(targetPath);
            if (target.exists() && target.isFile()) {
                String newPath = targetPath;
                int index = fileName.lastIndexOf('.');
                if (index < 0) {
                    String postFix = fileName.substring(index);
                    String name = fileName.substring(0, fileName.lastIndexOf('.'));
                    newPath = ringPath + "/" + name + "2" + postFix;
                } else {
                    newPath += 2;
                }
                target.renameTo(new File(newPath));

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static String classicalRingtonePath = Environment.getExternalStorageDirectory() + "/ClassicalRingtones";
    private static String ringPath = Environment.getExternalStorageDirectory() + "/" + "Ringtones";

    public String getCurrentRingTone() {

        String path = Settings.System.getString(context.getContentResolver(), SYS_SETTING_CURRENT_RING_PATH);

        if (path == null) {
            path = "";
        }
        if (new File(path).exists()) return path;
        return ringPath + "/" + path;
    }

    public String getClassicalRingtoneFolderPath() {
        return classicalRingtonePath;
    }

    public String getRingToneFolderPath() {
        return ringPath;
    }

    public void setUseGlobalRingTone(boolean b) {
        int use = b ? 1 : 0;
        Settings.System.putInt(context.getContentResolver(), SYS_SETTING_SHOULD_USE_GLOBAL_RING, use);
//        Settings.System.putInt(context.getContentResolver(), SYS_SETTING_3RD_SHOULD_USE_GLOBAL_RING, use);
    }

    public boolean isUsingGlobalRingTone() {

        int use = Settings.System.getInt(context.getContentResolver(), SYS_SETTING_SHOULD_USE_GLOBAL_RING, 0);

        return use == 1;
    }


    /**
     * use setUseGlobalRingTone instead
     *
     * @param enable enable or disable global ringtone
     * @see #setUseGlobalRingTone(boolean)
     */
    @Deprecated
    public void setUseGlobalRingTone3RD(boolean enable) {
//        setUseGlobalRingTone(enable);
    }

    /**
     * use isUsingGlobalRingTone instead
     *
     * @see #isUsingGlobalRingTone
     */
    @Deprecated
    public boolean isUsingGlobalRingTone3RD() {

        return isUsingGlobalRingTone();
    }

    int getPSTNStatus() {
        try {
            return mPSTNInterface.getPSTNStatus();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return -1;
    }

    boolean shouldDeleteAudioRecordAfterCallback = false;

    public void setShouldDeleteAudioRecordAfterCallback(boolean shouldDeleteAudioRecordAfterCallback) {
        this.shouldDeleteAudioRecordAfterCallback = shouldDeleteAudioRecordAfterCallback;
    }

    public void cancelAutoAnswer() {
        try {
            mPSTNInterface.cancelAutoAnswer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onHangOff() {
        currentStatus = PSTNStatus.IDLE;
        incomingNumber = null;

        preDialNumber = "";


        isRinging = false;

        if (mHangOffListener != null) {
            mHangOffListener.onHangOff();
            mHangOffListener = null;
        } else {
            for (PhoneAPI.PhoneEventListener listener : listeners) {
                listener.onHangOff();
            }
        }
        if (PhoneAPI.getInstance().isRecording()) {
            PhoneAPI.getInstance().stopRecording();
        }
    }

    /**
     * @param volume         音量
     * @param channel        0 for 手柄, 1 for免提
     * @param volumeCallback
     */
    public void setVolume(int volume, final int channel, final PhoneAPI.VolumeCallback volumeCallback) {
        try {
            mPSTNInterface.setVolume(channel, volume, new IPSTNAidlCallback.Stub() {
                @Override
                public void onEvent(String eventType, String param1, String param2, String param3, String param4) throws RemoteException {

                    //Log.e("GcordSDK", "onGetVolume:" + param1 + " " + param2);

                    int volume = -1;
                    try {
                        volume = Integer.parseInt(param2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (volumeCallback != null) {
                        volumeCallback.onVolume(channel, volume);
                    }

                }

                @Override
                public void onMusicTunnelChange(int tunnel) throws RemoteException {

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param channel        0 for 手柄 1 for 免提
     * @param volumeCallback
     */
    public void getVolume(final int channel, final PhoneAPI.VolumeCallback volumeCallback) {

        //if(mPSTNInterface != null)
        {
            try {

                mPSTNInterface.getVolume(channel, new IPSTNAidlCallback.Stub() {
                    @Override
                    public void onEvent(String eventType, String param1, String param2, String param3, String param4) throws RemoteException {

                        //Log.e("GcordSDK", "onGetVolume:" + param1 + " " + param2);

                        int volume = -1;
                        try {
                            volume = Integer.parseInt(param2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (volumeCallback != null) {
                            volumeCallback.onVolume(channel, volume);
                        }

                    }

                    @Override
                    public void onMusicTunnelChange(int tunnel) throws RemoteException {

                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取最大音量
     *
     * @return
     */
    public int getMaxVolume() {
        try {
            return mPSTNInterface.getMaxVolume();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param callStatusItem
     * @return if the event needs to be handled outside
     */
    protected boolean resumeCurrentCallState(CallStatusItem callStatusItem) {
        if (callStatusItem == null) {
            return false;
        }

        if (currentTransaction == null) {
            currentTransaction = new DialTransaction(context);
        }

        try {
            int currentPSTNStatus = mPSTNInterface.getPSTNStatus();
            if (2 == currentPSTNStatus) {
                currentStatus = PSTNStatus.HANDLE;
            } else if (1 == currentPSTNStatus) {
                currentStatus = PSTNStatus.HANDS_FREE;
            } else if (0 == currentPSTNStatus) {
                currentStatus = PSTNStatus.IDLE;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isRinging() && callStatusItem.isIncomingCall() && callStatusItem.getCallActiveTimeStamp() == 0) {

            for (PhoneAPI.PhoneEventListener phoneEventListener : listeners) {
                if (phoneEventListener != null) {
                    phoneEventListener.onInComingCall();
                    if (!TextUtils.isEmpty(callStatusItem.getOpponentNumber())) {
                        phoneEventListener.onPhoneNumberReceived(callStatusItem.getOpponentNumber());
                    }
                }
            }

            return false;
        }

        return true;
    }

    public boolean isReversalOfPolarityEnabled() {
        try {
            return mPSTNInterface.isReversalOfPolarityEnabled();
        } catch (Exception ex) {
            return false;
        }
    }

    protected boolean isAutoHangOffWhenBusyTone() {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTING_AUTO_HANG_OFF_WHEN_BUSY_TONE, true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    protected void setAutoHangOffWhenBusyTone(boolean should) {
        try {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SETTING_AUTO_HANG_OFF_WHEN_BUSY_TONE, should).apply();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
