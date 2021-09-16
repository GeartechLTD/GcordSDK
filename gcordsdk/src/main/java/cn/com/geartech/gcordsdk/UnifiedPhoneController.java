package cn.com.geartech.gcordsdk;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.UiThread;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import cn.com.geartech.gcordsdk.areacode.AreaCodeItem;
import cn.com.geartech.gcordsdk.dataType.CallStatusItem;
import cn.com.geartech.gcordsdk.dataType.UnifiedPhoneCall;
import cn.com.geartech.gcordsdk.ui.InCallActivity;
import cn.com.geartech.gcordsdk.ui.InCallFloatingWindow;

/**
 * Created by pangyuning on 17/2/24.
 */
public class UnifiedPhoneController {


    public static final String Broadcast_Direct_Dial = "cn.com.geartech.direct.dial";
    public static final String Broadcast_Direct_Dial_By_Custom_Mode = "cn.com.geartech.action.Dial_By_Custom_Mode";
    public static final String Broadcast_Dial_Key_Number = "number";
    public static final String Broadcast_Dial_Key_Mode = "dial_mode";
    private static final String RECORDING_SETTINGS = "recording_settings";
    private static final String AUTO_RECORDING_ENABLED = "auto_recording_enabled";
    private static final String AUTO_RECORDING_SAMPLE_RATE = "auto_recording_sample_rate";
    private static final String AUTO_RECORDING_BIT_RATE = "auto_recording_bit_rate";
    private final static String SYS_PREF_RING_TONE_STREAM_TYPE = "cn.com.geartech.riing_tone_stream_type";
    private static final String EVENT_DIAL_CELL_CALL = "cn.com.geartech.gtplatform.event_dial_cell_call";
    private static final String PREF_USE_ONLINE_CHECKER = "use_online_checker";

    private static UnifiedPhoneController _instance = null;
    boolean autoDetectPreDial = false;
    boolean autoDetectPostDialFinish = false;
    boolean autoDetectIncomingCall = false;
    String lastDialNumber = "";
    LinkedBlockingDeque<UnifiedPhoneEventListener> listeners = new LinkedBlockingDeque<>();
    Handler handler = new Handler();
    int tickCount;
    volatile UnifiedPhoneCall currentPhoneCall;
    private Application mContext;
    private Class incomingCallActivityCls;
    private Class outGoingCallActivityCls;
    Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (activity != null) {
                if (activity.getClass().equals(incomingCallActivityCls)
                        || activity.getClass().equals(outGoingCallActivityCls)) {
                    setMode(false);
                }
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (activity != null) {
                if (activity.getClass().equals(incomingCallActivityCls)
                        || activity.getClass().equals(outGoingCallActivityCls)) {
                    setMode(true);
                }
            }
        }

        private void setMode(boolean isPause) {
            int type = GcordSDK.getInstance().getGcordPreference().getSystemDialMode();
            if (!isPause) {
                if (type == GcordPreferenceConstants.SYSTEM_DIAL_MODE_PSTN) {
                    if (!UnifiedPhoneCall.Phone_Type.PSTN.equals(currentPhoneCall.getCurrentCallType())) {
                        GcordSDK.getInstance().getHandleManager().setHandlePickUpType(HandleManager.HANDLE_PICK_UP_TYPE_VOIP);
                    } else {
                        GcordSDK.getInstance().getHandleManager().setHandlePickUpType(HandleManager.HANDLE_PICK_UP_TYPE_NONE);
                    }
                } else {
                    if (UnifiedPhoneCall.Phone_Type.PSTN.equals(currentPhoneCall.getCurrentCallType())) {
                        GcordSDK.getInstance().getHandleManager().setHandlePickUpType(HandleManager.HANDLE_PICK_UP_TYPE_NONE);
                    } else {
                        GcordSDK.getInstance().getHandleManager().setHandlePickUpType(HandleManager.HANDLE_PICK_UP_TYPE_VOIP);
                    }
                }
            } else {
                if (type == GcordPreferenceConstants.SYSTEM_DIAL_MODE_PSTN) {
                    GcordSDK.getInstance().getHandleManager().setHandlePickUpType(HandleManager.HANDLE_PICK_UP_TYPE_NONE);
                } else {
                    GcordSDK.getInstance().getHandleManager().setHandlePickUpType(HandleManager.HANDLE_PICK_UP_TYPE_VOIP);
                }
            }

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };
    private volatile boolean isTicking = false;
    Runnable tickingRunnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(this);
            if (currentPhoneCall != null && isTicking) {
                int oldTickCount = currentPhoneCall.getTickCount();

                for (UnifiedPhoneEventListener listener : listeners) {
                    listener.onTicking(oldTickCount);
                }
                handler.postDelayed(this, 1000);
            }
        }
    };
    private volatile boolean isRecording = false;
    private UnifiedPhoneRecordCallback sipRecordCallBack;
    private String sipRecordingPath;
    private volatile ExecutorService threadPool = null;
    private CopyOnWriteArrayList<AutoRecordingCallback> autoRecordingCallbacks;
    private static final String DelayTickingToken = "DelayTickingToken";
    PhoneAPI.PhoneEventListener phoneEventListener = new PhoneAPI.PhoneEventListener() {
        @Override
        public void onPickUp(PhoneAPI.PICKUP_STATE state) {
            GcordSDK.getInstance().getWallPaperManager().hideWallpaper();

            if (currentPhoneCall != null && currentPhoneCall.getCurrentCallType() != UnifiedPhoneCall.Phone_Type.PSTN) {
                return;
            }

            if (currentPhoneCall != null && currentPhoneCall.isCallIn()) {
                // Log.d("GcordSDK","prepare ticking 2");
                currentPhoneCall.setCallActiveTimeStamp(System.currentTimeMillis());
                currentPhoneCall.setAnswered(true);
                startTicking();
            } else if (currentPhoneCall == null) {
                currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.PSTN);
                currentPhoneCall.setCallIn(false);

                //   Log.d("GcordSDK","prepare ticking 3");
                //TODO 反极判断
                if (!GcordSDK.getInstance().getPhoneAPI().isReversalOfPolarityEnabled()) {
                    handler.postAtTime(() -> startTicking(),DelayTickingToken, SystemClock.uptimeMillis()+5000L);
                }
            }
        }

        @Override
        public void onInComingCall() {
            if (currentPhoneCall == null) {
                currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.PSTN);
                currentPhoneCall.setCallIn(true);
            }


            if (listeners != null && listeners.size() > 0) {
                for (UnifiedPhoneEventListener listener : listeners) {
                    listener.onIncomingCall();
                }
            }

            if (PhoneAPI.getInstance().isInCall()) {
                currentPhoneCall.setAnswered(true);
                startTicking();
            }

            GcordSDK.getInstance().getWallPaperManager().hideWallpaper();
        }

        @Override
        public void onRingEnd() {
            if (currentPhoneCall != null && currentPhoneCall.isCallIn() && !currentPhoneCall.isAnswered()) {
                // call end
                endCurrentCall();
            }
        }

        @Override
        public void onPhoneNumberReceived(String phoneNumber) {

//            if(currentPhoneCall != null && currentPhoneCall.getCurrentCallType() != UnifiedPhoneCall.Phone_Type.PSTN)
//            {
//                return;
//            }
//
//            if(currentPhoneCall == null)
//            {
//                return;
//            }
            if (currentPhoneCall != null && currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                currentPhoneCall.setOpponentPhoneNumber(phoneNumber);
            }


            if (listeners != null && listeners.size() > 0) {
                for (UnifiedPhoneEventListener listener : listeners) {
                    listener.onGetPhoneNumber(phoneNumber);
                }
            }
        }

        @Override
        public void onSwitchPhoneState(PhoneAPI.PICKUP_STATE state) {
            if (currentPhoneCall != null && currentPhoneCall.getCurrentCallType() != UnifiedPhoneCall.Phone_Type.PSTN) {
                return;
            }
        }

        @Override
        public void onHangOff() {

//            if(currentPhoneCall != null && currentPhoneCall.getCurrentCallType() != UnifiedPhoneCall.Phone_Type.PSTN)
//            {
//                return;
//            }

            endCurrentCall();
        }

        @Override
        public void onNumberSent(String number) {
            super.onNumberSent(number);

            if (currentPhoneCall != null && currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN
                    && !currentPhoneCall.isCallIn()) {
                String currentNumber = currentPhoneCall.getOpponentPhoneNumber();
                if (currentNumber == null) {
                    currentNumber = "" + number;
                } else {
                    currentNumber = currentNumber + number;
                }

                //Log.e("GcordSDK","on number sent:" + number);


                currentPhoneCall.setOpponentPhoneNumber(currentNumber);
            }
        }

        @Override
        public void onDialFinish() {
            super.onDialFinish();

            if (currentPhoneCall != null) {
                lastDialNumber = currentPhoneCall.getOpponentPhoneNumber();
                //TODO 反极判断
                if (!GcordSDK.getInstance().getPhoneAPI().isReversalOfPolarityEnabled()) {

                    if (currentPhoneCall.getTickCount() == 0) {
                        startTicking();
                    }
                }
            }

            if (listeners != null) {
                for (UnifiedPhoneEventListener listener : listeners) {
                    if (listener != null) {
                        listener.onDialFinish(lastDialNumber);
                    }
                }
            }


            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (autoDetectPostDialFinish && currentPhoneCall != null
                            && !InCallActivity.isActive()
                            && !InCallFloatingWindow.isActive()) {
                        Intent intent1 = new Intent(mContext, InCallActivity.class);
                        intent1.putExtra(InCallActivity.PARAM_CALL_NUMBER, currentPhoneCall.getOpponentPhoneNumber());
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra(InCallActivity.PARAM_ACTION, InCallActivity.ACTION_POST_DIAL_PSTN);
                        mContext.startActivity(intent1);
                    }
                }
            });
        }

        @Override
        public void onBusyTone() {
            super.onBusyTone();
            for (UnifiedPhoneEventListener listener : listeners) {
                try {
                    listener.onBusyTone();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        @Override
        public void onReversalOfPolarity() {
            startTicking();
        }
    };
    CellPhoneManager.CellPhoneEventListener cellPhoneEventListener = new CellPhoneManager.CellPhoneEventListener() {
        @Override
        public void onIncomingCall(String phoneNumber, String callId) {
            GcordSDK.getInstance().getWallPaperManager().hideWallpaper();
            if (currentPhoneCall == null) {
                currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.CELL);

                currentPhoneCall.setCallIn(true);

                currentPhoneCall.setOpponentPhoneNumber(phoneNumber);
            }


            if (listeners != null && listeners.size() > 0) {
                for (UnifiedPhoneEventListener listener : listeners) {
                    listener.onIncomingCall();
                    listener.onGetPhoneNumber(phoneNumber);
                }
            }
        }

        @Override
        public void onCallTerminated(String callId) {
            endCurrentCall();
        }

        @Override
        public void onCallStateChanged(int state, String callId) {
            if (currentPhoneCall != null && currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                if (state == CellPhoneManager.CALL_STATE_ACTIVE) {
                    // Log.d("GcordSDK","prepare ticking 4");
                    currentPhoneCall.setAnswered(true);
                    startTicking();
                    currentPhoneCall.setCallActiveTimeStamp(System.currentTimeMillis());
                }
            }
        }
    };
    SipPhoneManager.SipPhoneEventListener sipPhoneEventListener = new SipPhoneManager.SipPhoneEventListener() {
        @Override
        public void onSipPhoneSDKInitEnd() {

        }

        @Override
        public void onSipPhoneSDKInitFailed(int errorCode) {

        }

        @Override
        public void onIncomingCallReceived(int sessionId, boolean isVedioCall, String phoneNumber) {
            GcordSDK.getInstance().getWallPaperManager().hideWallpaper();
            if (currentPhoneCall == null) {
                currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.SIP);

                currentPhoneCall.setCallIn(true);
                currentPhoneCall.setSessionId(sessionId);

                currentPhoneCall.setOpponentPhoneNumber(phoneNumber);
            }


            if (listeners != null && listeners.size() > 0) {
                for (UnifiedPhoneEventListener listener : listeners) {
                    listener.onIncomingCall();
                    listener.onGetPhoneNumber(phoneNumber);
                }
            }

        }

        @Override
        public void onSipPhoneOutgoingCall(int sessionId, String phoneNumber) {


            // Log.e("GcordSDK", "OutGoing Call");

            if (currentPhoneCall == null) {
                currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.SIP);
            }

            currentPhoneCall.setCallIn(false);

            currentPhoneCall.setSessionId(sessionId);

            currentPhoneCall.setOpponentPhoneNumber(phoneNumber);

            currentPhoneCall.setNumberToDial(phoneNumber);

            if (listeners != null) {
                for (UnifiedPhoneEventListener listener : listeners) {
                    if (listener != null) {
                        listener.onDialFinish(phoneNumber);
                    }
                }
            }
        }

        @Override
        public void onSipPhoneAlerted(int sessionId) {

        }

        @Override
        public void onSipPhoneTalking(int sessionId) {
            if (currentPhoneCall != null) {
                // Log.d("GcordSDK","prepare ticking 6");
                currentPhoneCall.setCallActiveTimeStamp(System.currentTimeMillis());
                startTicking();
            }
        }

        @Override
        public void onSipPhoneCallHangOff(int sessionId) {
            endCurrentCall();
        }

        @Override
        public void onLoginFailed(int errorCode) {

        }

        @Override
        public void onLoginInProgress() {

        }

        @Override
        public void onLoginSuccessfully() {

        }

        @Override
        public void onLogout() {

        }

        @Override
        public void onLogoutInProgress() {

        }

        @Override
        public void onRegisterOperationError(int errorCode) {

        }

        @Override
        public void onForceOffline() {

        }
    };
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), EVENT_DIAL_CELL_CALL)) {
                String number = intent.getStringExtra("number");
                if (number != null) {
                    if (currentPhoneCall == null) {
                        currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.CELL);

                        currentPhoneCall.setCallIn(false);
                        currentPhoneCall.setOpponentPhoneNumber(number);
                        currentPhoneCall.setNumberToDial(number);
                    }

                    for (UnifiedPhoneEventListener listener : listeners) {
                        if (listener != null) {
                            listener.onDialFinish(number);
                        }
                    }
                }
            } else if (Objects.equals(intent.getAction(), GTPrivate.EVENT_DIAL_FINISH)) {

                if (currentPhoneCall != null) {
                    currentPhoneCall.setDialFinished();
                }
                //Log.e("GcordSDK","Dial finish:" + currentPhoneCall.getOpponentPhoneNumber());
//                if(currentPhoneCall != null)
//                {
//                    lastDialNumber = currentPhoneCall.getOpponentPhoneNumber();
//                }

//                if(listeners != null)
//                {
//                    for (UnifiedPhoneEventListener listener:listeners)
//                    {
//                        if(listener != null)
//                        {
//                            listener.onDialFinish(lastDialNumber);
//                        }
//                    }
//                }

            } else if (PSTNInternal.CTRL_DIAL.equals(intent.getAction())) {
                String number = intent.getStringExtra(PSTNInternal.PARAM_NUMBER);
                if (currentPhoneCall != null) {
                    currentPhoneCall.addNumberToDial(number);
                } else {
                    currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.PSTN);
                    currentPhoneCall.setCallIn(false);
                    currentPhoneCall.addNumberToDial(number);
                }
            } else if (PSTNInternal.CTRL_AUTO_PICK_UP.equals(intent.getAction())) {
                if (currentPhoneCall == null) {
                    currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.PSTN);

                    currentPhoneCall.setCallIn(false);

                    if (!GcordSDK.getInstance().getPhoneAPI().isReversalOfPolarityEnabled()) {
                        handler.postDelayed(() -> {
                            //Log.d("GcordSDK","prepare ticking 5");
                            startTicking();
                        }, 5000);
                    }
                }
            }
        }
    };

    {
        autoRecordingCallbacks = new CopyOnWriteArrayList<>();
    }


    protected UnifiedPhoneController() {

    }

    /**
     * 获取UnifiedPhoneController的实例
     *
     * @return UnifiedPhoneController的实例
     */
    public static UnifiedPhoneController getInstance() {
        return SingletonHolder.unifiedPhoneController;
    }

    private static boolean isCellPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }

        if (number.length() == 11 && number.startsWith("1")) {
            return true;
        } else if (number.length() == 12 && number.startsWith("01")) {
            return true;
        }

        return false;
    }
//
//    public interface UnifiedSoundChannelEventListener{
//        /**
//         * 切换至手柄模式
//         */
//        void onSwitchToHandle();
//
//        /**
//         * 切换至免提模式
//         */
//        void onSwitchToHandsFree();
//    }

    public boolean isAutoDetectPostDialFinish() {
        return autoDetectPostDialFinish;
    }

    public void setAutoDetectPostDialFinish(boolean auto) {
        autoDetectPostDialFinish = auto;
    }

    public String getLastDialNumber() {
        return lastDialNumber;
    }

    protected void init(Application context) {
        mContext = context;
        context.registerActivityLifecycleCallbacks(lifecycleCallbacks);


        IntentFilter intentFilter = new IntentFilter(EVENT_DIAL_CELL_CALL);
        intentFilter.addAction(EVENT_DIAL_CELL_CALL);
        intentFilter.addAction(GTPrivate.EVENT_DIAL_FINISH);
        intentFilter.addAction(PSTNInternal.CTRL_DIAL);
        intentFilter.addAction(PSTNInternal.CTRL_AUTO_PICK_UP);
        mContext.registerReceiver(mReceiver, intentFilter);


        PhoneAPI.getInstance().addPhoneEventListener(phoneEventListener);
        CellPhoneManager.getInstance().addCellPhoneEventListener(cellPhoneEventListener);
        SipPhoneManager.getInstance().addSipPhoneEventListeners(sipPhoneEventListener);


    }

    void onSDKInited() {
        try {
            int pstnStatus = PSTNInternal.getInstance().getPSTNStatus();

            if (pstnStatus == 1 || pstnStatus == 2) {
                // is in pstn call

                if (currentPhoneCall == null) {
                    recoverCallIfNeeded();
                }
            }
        } catch (Exception e) {

        }

    }
    private void recoverCallIfNeeded(CallStatusItem callStatusItem) {
        if (callStatusItem != null) {
            if ("pstn".equals(callStatusItem.getCallType())) {
                currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.PSTN);
            } else if ("cell".equals(callStatusItem.getCallType())) {
                currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.CELL);
            } else {
                currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.SIP);
            }

            currentPhoneCall.setCallIn(callStatusItem.isIncomingCall());

//            long delta = System.currentTimeMillis() - callStatusItem.getCallActiveTimeStamp();
//            currentPhoneCall.setTickCount((int) (delta / 1000L) + 2);
            currentPhoneCall.setOpponentPhoneNumber(callStatusItem.getOpponentNumber());
            currentPhoneCall.setCallActiveTimeStamp(callStatusItem.getCallActiveTimeStamp());
            currentPhoneCall.setCallBeginTimestamp(callStatusItem.getCallBeginTimeStamp());
            if (callStatusItem.isIncomingCall()) {
                currentPhoneCall.setAnswered(true);
            }


            handler.postDelayed(() -> {
                Log.d("GcordSDK", "prepare ticking 1");
                startTicking();
            }, 2000);
        }
    }
    private void recoverCallIfNeeded() {
        CallStatusItem callStatusItem = queryCurrentCallStatus();
        recoverCallIfNeeded(callStatusItem);
    }

    private void registerIncomingCallActivity(Class cls) {
        incomingCallActivityCls = cls;
    }

    private void registerOutgoingCallActivity(Class cls) {
        outGoingCallActivityCls = cls;
    }

    private void registerInCallActivity(Class cls) {
        incomingCallActivityCls = cls;
        outGoingCallActivityCls = cls;
    }

    void startTicking() {
        // Log.d("GcordSDK","start ticking");
        if (!isTicking) {
            isTicking = true;
            if (currentPhoneCall != null && currentPhoneCall.getCallActiveTimeStamp() == 0){
                currentPhoneCall.setCallActiveTimeStamp(System.currentTimeMillis());
            }
            handler.post(tickingRunnable);
            if (!isRecording) {
                startAutoRecordingIfNeeded();
            }
        }
    }

    void endTicking() {
        //Log.d("GcordSDK","end ticking");
        if (isTicking) {
            isTicking = false;
            handler.removeCallbacks(tickingRunnable);
        }
    }

    /**
     * 添加通话事件回调
     *
     * @param listener
     */
    public void addPhoneEventListener(UnifiedPhoneEventListener listener) {
        if (listener == null) {
            return;
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        //check current call state
        new Thread(){
            @Override
            public void run() {
                CallStatusItem callStatusItem = UnifiedPhoneController.getInstance().queryCurrentCallStatus();
                Log.e("queryCurrentCallStatus","data:"+callStatusItem);
                if(callStatusItem != null)
                {
                    //Log.e("Demo","resume call status");
                    // resume call state
                    boolean needToHandle = false;

                    if("pstn".equals(callStatusItem.getCallType()))
                    {
                        needToHandle = PSTNInternal.getInstance().resumeCurrentCallState(callStatusItem);
                    }
                    else if("cell".equals(callStatusItem.getCallType()))
                    {
                        needToHandle = CellPhoneManager.getInstance().resumeCallStatus(callStatusItem);
                    }

                    if(needToHandle)
                    {
                        if (currentPhoneCall == null){
                            recoverCallIfNeeded(callStatusItem);
                        }
                        handler.post(()->{
                            Log.e("queryCurrentCallStatus","onResumeInCallStatus, data:"+callStatusItem);
                            try {
                                listener.onResumeInCallStatus(callStatusItem);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        });
                    }
                }
            }
        }.start();
    }

    /**
     * 移除通话事件回调
     *
     * @param listener
     */
    public void removePhoneEventListener(UnifiedPhoneEventListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /**
     * 获取当前通话
     *
     * @return
     */
    public UnifiedPhoneCall getCurrentPhoneCall() {
        return currentPhoneCall;
    }

    void endCurrentCall() {
        endTicking();
        handler.removeCallbacksAndMessages(DelayTickingToken);
        if (listeners != null && listeners.size() > 0) {
            for (UnifiedPhoneEventListener listener : listeners) {
                listener.onCallTerminated();
            }
        }
        stopRecording();
        currentPhoneCall = null;
    }

    /**
     * 接听当前来电
     */
    public void answer() {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                if (GcordSDK.getInstance().getHandleManager().isHandlePickedUp()) {
                    GcordSDK.getInstance().getPhoneAPI().pickUpWithHandle();
                } else {
                    GcordSDK.getInstance().getPhoneAPI().pickUpWithHandsFree();
                }
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                CellPhoneManager.getInstance().acceptCall();
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                try {
                    SipPhoneManager.getInstance().answerSipPhone(currentPhoneCall.getSessionId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 挂断当前通话
     *
     * @see {@link #endCall()}
     * use {@link #endCall()} instead
     */
    @Deprecated
    public void hangOff() {
        // Log.e("GcordSDK", "HangOff");
        endCall();
    }

    /**
     * 拒绝当前来电
     *
     * @see {@link #endCall()}
     * use {@link #endCall()} instead
     */
    @Deprecated
    public void reject() {
        endCall();
    }

    /**
     * 挂断当前通话，包括来电和去电
     */
    public void endCall() {
        endTicking();
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                if (GcordSDK.getInstance().getPhoneAPI().isInCall()) {
                    GcordSDK.getInstance().getPhoneAPI().hangOff();
                } else {
                    GcordSDK.getInstance().getPhoneAPI().reject();
                }
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                if (currentPhoneCall.isCallIn() && !currentPhoneCall.isAnswered()) {
                    CellPhoneManager.getInstance().rejectCurrentCall();
                } else {
                    CellPhoneManager.getInstance().endCurrentCall();
                }
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                try {
                    SipPhoneManager.getInstance().hangOff(currentPhoneCall.getSessionId(),
                            SipPhoneConstants.HANG_OFF_REASON_DECLINE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 开始录音
     *
     * @param callback 录音回调
     */
    public synchronized void startRecording(final UnifiedPhoneRecordCallback callback) {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                GcordSDK.getInstance().getPhoneAPI().startRecording(new PhoneAPI.AudioRecordListener() {
                    @Override
                    public void onRecordSuccess(String audioPath, Object customToken) {
                        if (callback != null) {
                            callback.onRecordSuccess(audioPath);
                        }
                    }

                    @Override
                    public void onRecordFail(int errorCode, String errorMessage, Object customToken) {
                        if (callback != null) {
                            callback.onRecordFailed(errorCode, errorMessage);
                        }
                    }
                }, "");
                isRecording = true;
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                CellPhoneManager.getInstance().startRecording(new CellPhoneManager.AudioRecordListener() {
                    @Override
                    public void onRecordSuccess(String audioPath) {
                        if (callback != null) {
                            callback.onRecordSuccess(audioPath);
                        }
                    }

                    @Override
                    public void onRecordFail(int errorCode, String errorMessage) {
                        if (callback != null) {
                            callback.onRecordFailed(errorCode, errorMessage);
                        }
                    }
                });
                isRecording = true;
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                startSipRecording(RecordingType.RECORDING_AMR, callback, null, null);
            }
        }
    }

    private void startSipRecording(int format, UnifiedPhoneRecordCallback callback, Integer sampleRate, Integer bitRate) {
        try {
            String filePath = getRecordingPathForSip(format);
            switch (format) {
                case RecordingType.RECORDING_MP3:
                    SipPhoneManager.getInstance().startRecordingMp3(filePath, sampleRate, bitRate);
                    break;
                case RecordingType.RECORDING_AMR:
                    SipPhoneManager.getInstance().startRecording(filePath);
                    break;
                default:
                    SipPhoneManager.getInstance().startRecordingWav(filePath);
            }

            isRecording = true;
            sipRecordCallBack = callback;
            sipRecordingPath = filePath;
        } catch (Exception e) {
            e.printStackTrace();
            isRecording = false;
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    private String getRecordingPathForSip(int format) {
        String postfix = ".wav";
        if (!"com.zengyan.shphone".equals(GcordSDK.getInstance().getContext().getPackageName())) {
            if (format == RecordingType.RECORDING_AMR) {
                postfix = ".amr";
            } else if (format == RecordingType.RECORDING_MP3) {
                postfix = ".mp3";
            }
        } else {
            postfix = ".mp3";
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        if (!path.endsWith("/") && !(path.endsWith("\\"))) {
            path += "/";
        }
        Calendar calendar = Calendar.getInstance();
        String dateStr = String.format(Locale.getDefault(), "%1$d%2$02d%3$02d",
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        String catchPath = path + "GTSysData/CallRecord/" + dateStr + "/";


        File dir = new File(catchPath);
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                dir.delete();
                dir.mkdirs();
            }
        } else {
            dir.mkdirs();
        }
        dateStr = dateStr + String.format(Locale.getDefault(), "%1$02d%2$02d%3$02d%4$d",
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND));

        String name = dateStr + postfix;
        return String.format(Locale.getDefault(), "%1$s%2$s",
                catchPath, name);
    }

    @SuppressWarnings("unused")
    public synchronized void startRecordingWav(final UnifiedPhoneRecordCallback callback) {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                GcordSDK.getInstance().getPhoneAPI().startRecordingWav(new PhoneAPI.AudioRecordListener() {
                    @Override
                    public void onRecordSuccess(String audioPath, Object customToken) {
                        if (callback != null) {
                            callback.onRecordSuccess(audioPath);
                        }
                    }

                    @Override
                    public void onRecordFail(int errorCode, String errorMessage, Object customToken) {
                        if (callback != null) {
                            callback.onRecordFailed(errorCode, errorMessage);
                        }
                    }
                }, "");
                isRecording = true;
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                CellPhoneManager.getInstance().startRecordingWav(new CellPhoneManager.AudioRecordListener() {
                    @Override
                    public void onRecordSuccess(String audioPath) {
                        if (callback != null) {
                            callback.onRecordSuccess(audioPath);
                        }
                        isRecording = false;
                    }

                    @Override
                    public void onRecordFail(int errorCode, String errorMessage) {
                        if (callback != null) {
                            callback.onRecordFailed(errorCode, errorMessage);
                        }
                        isRecording = false;
                    }
                });
                isRecording = true;
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                startSipRecording(RecordingType.RECORDING_WAV, callback, null, null);
            }
        }
    }

    @SuppressWarnings("unused")
    public synchronized void startRecordingMp3(final UnifiedPhoneRecordCallback callback) {
        startRecordingMp3(callback, 8000, 32);
    }

    /**
     * 通话录音，音频格式为mp3
     *
     * @param callback   通话录音回调
     * @param sampleRate mp3采样率
     * @param bitRate    mp3比特率
     */
    public synchronized void startRecordingMp3(final UnifiedPhoneRecordCallback callback, int sampleRate, int bitRate) {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                GcordSDK.getInstance().getPhoneAPI().startRecordingMp3(new PhoneAPI.AudioRecordListener() {
                    @Override
                    public void onRecordSuccess(String audioPath, Object customToken) {
                        if (callback != null) {
                            callback.onRecordSuccess(audioPath);
                        }
                    }

                    @Override
                    public void onRecordFail(int errorCode, String errorMessage, Object customToken) {
                        if (callback != null) {
                            callback.onRecordFailed(errorCode, errorMessage);
                        }
                    }
                }, "", sampleRate, bitRate);

                isRecording = true;
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                CellPhoneManager.getInstance().startRecordingMP3(new CellPhoneManager.AudioRecordListener() {
                    @Override
                    public void onRecordSuccess(String audioPath) {
                        if (callback != null) {
                            callback.onRecordSuccess(audioPath);
                        }
                    }

                    @Override
                    public void onRecordFail(int errorCode, String errorMessage) {
                        if (callback != null) {
                            callback.onRecordFailed(errorCode, errorMessage);
                        }
                    }
                }, sampleRate, bitRate);
                isRecording = true;
            } else {
                startSipRecording(RecordingType.RECORDING_MP3, callback, sampleRate, bitRate);
            }
        }
    }

    /**
     * 停止录音
     */
    public synchronized void stopRecording() {
        if (isRecording && currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                GcordSDK.getInstance().getPhoneAPI().stopRecording();
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                CellPhoneManager.getInstance().stopRecording();
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                try {
                    SipPhoneManager.getInstance().stopRecording();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (sipRecordCallBack != null && sipRecordingPath != null && sipRecordingPath.trim().length() > 0) {
                                File file = new File(sipRecordingPath);
                                if (file.exists()) {
                                    sipRecordCallBack.onRecordSuccess(sipRecordingPath);
                                } else
                                    sipRecordCallBack.onRecordFailed(-1, GcordSDK.getInstance().getContext().getString(R.string.recording_failed));
                            }
                        }
                    }, 1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        isRecording = false;
    }

    /**
     * 判断是否处于免提声道
     *
     * @return
     */
    public boolean isHandsFreeOn() {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                if (GcordSDK.getInstance().getPhoneAPI().isHandsFreeOn()) {
                    return true;
                } else if (GcordSDK.getInstance().getPhoneAPI().isHandleOn()) {
                    return false;
                }
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                if (GcordSDK.getInstance().getCellPhoneManager().isInCall()) {
                    return GcordSDK.getInstance().getCellPhoneManager().isHandsFreeOn();
                }
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                //Log.e("GcordSDK", "Music:" + GcordSDK.getInstance().getHandleManager().getCurrentMusicChannel4VOIP());

                return GcordSDK.getInstance().getHandleManager().getCurrentMusicChannel4VOIP() ==
                        HandleManager.MUSIC_CHANNEL_HANDS_FREE;
            }
        }

        return !GcordSDK.getInstance().getHandleManager().isHandlePickedUp();
    }

    /**
     * 通话中切换至免提声道
     */
    public void switchToHandsFree() {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                GcordSDK.getInstance().getPhoneAPI().switchPhoneState(PhoneAPI.PICKUP_STATE.HANDS_FREE);
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                if (GcordSDK.getInstance().getCellPhoneManager().isInCall()) {
                    GcordSDK.getInstance().getCellPhoneManager().setHandsFreeOn();
                } else {
                    GcordSDK.getInstance().getHandleManager().switchToHandsFree4VOIP();
                }
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                GcordSDK.getInstance().getHandleManager().switchToHandsFree4VOIP();
            }
        } else {
            GcordSDK.getInstance().getHandleManager().switchToHandsFree4VOIP();
        }
    }

    /**
     * 通话中切换至手柄声道
     */
    public void switchToHandle() {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                GcordSDK.getInstance().getPhoneAPI().switchPhoneState(PhoneAPI.PICKUP_STATE.HANDLE);
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                if (GcordSDK.getInstance().getCellPhoneManager().isInCall()) {
                    GcordSDK.getInstance().getCellPhoneManager().setHandleOn();
                } else {
                    GcordSDK.getInstance().getHandleManager().switchToHandle4VOIP();
                }
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                GcordSDK.getInstance().getHandleManager().switchToHandle4VOIP();
            }
        } else {
            GcordSDK.getInstance().getHandleManager().switchToHandle4VOIP();
        }
    }

    /**
     * 根据设置的拨号模式进行拨号
     *
     * @param number    the number to dial
     * @param dial_mode the mode for dialing
     */
    public void makeCall(String number, DIAL_MODE dial_mode) {
        if (dial_mode == DIAL_MODE.PSTN) {
            GcordSDK.getInstance().getPhoneAPI().dial(number);
        } else if (dial_mode == DIAL_MODE.CELL) {
            GcordSDK.getInstance().getCellPhoneManager().makeCall(number);
            lastDialNumber = number;
        } else if (dial_mode == DIAL_MODE.SIP) {
            try {
                if (currentPhoneCall == null) {
                    currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.SIP);
                    currentPhoneCall.setCallIn(false);
                    currentPhoneCall.setOpponentPhoneNumber(number);
                }
                GcordSDK.getInstance().getSipPhoneManager().dialByPhoneNumber(number);
            } catch (Exception e) {
                e.printStackTrace();
            }

            lastDialNumber = number;
        }
    }

    /**
     * 判断当前是否存在通话
     *
     * @return
     */
    public boolean hasCall() {
        return currentPhoneCall != null;
    }

    public void autoDialAndShowSystemInCallActivity(String phoneNumber) {
        Intent intent = new Intent(Broadcast_Direct_Dial);
        intent.setPackage("cn.com.geartech.app");
        intent.putExtra(Broadcast_Dial_Key_Number, phoneNumber);
        mContext.sendBroadcast(intent);
    }

    public void dialByCustomModeAndShowSystemInCallActivity(String phoneNumber, DIAL_MODE mode) {
        Intent intent = new Intent(Broadcast_Direct_Dial_By_Custom_Mode);
        intent.setPackage("cn.com.geartech.app");
        intent.putExtra(Broadcast_Dial_Key_Number, phoneNumber);
        intent.putExtra(Broadcast_Dial_Key_Mode, mode);
        mContext.sendBroadcast(intent);
    }

    /**
     * 根据当前通话环境呼出一个号码
     *
     * @param number 拨号号码
     */
    public void autoDial(final String number) {
        autoDial(number, true);
    }

    /**
     * 根据当前通话环境呼出一个号码
     *
     * @param number     拨号号码
     * @param dialAnyway 如果设置为true，则如果在launcher设定的拨号方式不可用时，依然使用该方式拨号，
     *                   反之，则自动切换到下一个可以使用的拨号方式来拨号，查询的顺序是固话，sim卡，SIP
     */

    public void autoDial(final String number, boolean dialAnyway) {
        GcordSDK.getInstance().getWallPaperManager().hideWallpaper();
        DIAL_MODE dial_mode = checkDialMode(dialAnyway);

        if (dial_mode == DIAL_MODE.PSTN) {
            dialByPSTN(number);
        } else if (dial_mode == DIAL_MODE.CELL) {
            GcordSDK.getInstance().getCellPhoneManager().makeCall(number);
            lastDialNumber = number;
        } else if (dial_mode == DIAL_MODE.SIP) {
            dialBySip(number);
        }
    }

    private void dialBySip(String number) {
        if (currentPhoneCall == null || currentPhoneCall.getCurrentCallType() != UnifiedPhoneCall.Phone_Type.SIP) {
            currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.SIP);
            currentPhoneCall.setCallIn(false);
            currentPhoneCall.setOpponentPhoneNumber(number);
        }
        try {
            GcordPreference gcordPreference = GcordSDK.getInstance().getGcordPreference();
            if (!isAppInChinese() || !gcordPreference.isSmartDialOn()) {
                try {
                    GcordSDK.getInstance().getSipPhoneManager().dialByPhoneNumber(number);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                smartDial(number, new OnSmartDialListener() {
                    @Override
                    public void dial(String phoneNumber) {
                        try {
                            GcordSDK.getInstance().getSipPhoneManager().dialByPhoneNumber(phoneNumber);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        lastDialNumber = number;
    }

    private void startAutoRecordingIfNeeded() {
        final int recordingType = getAutoRecordingType();
        if (recordingType == -1) return;
        startRecording(new UnifiedPhoneRecordCallback() {
            @Override
            public void onRecordSuccess(String audioPath) {
                for (AutoRecordingCallback callback :
                        autoRecordingCallbacks) {
                    try {
                        callback.onRecordSuccess(audioPath);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onRecordFailed(int errorCode, String errorMessage) {
                for (AutoRecordingCallback callback :
                        autoRecordingCallbacks) {
                    try {
                        callback.onRecordFailed(errorCode, errorMessage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }, recordingType);

        for (AutoRecordingCallback callback :
                autoRecordingCallbacks) {
            try {
                callback.onRecordingStarted();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * start recording the specified audio type
     *
     * @param callback      the recording call back
     * @param recordingType the audio type to record
     * @see UnifiedPhoneRecordCallback
     * @see RecordingType
     */
    @SuppressWarnings("unused")
    public synchronized void startRecording(UnifiedPhoneRecordCallback callback, @RecordingType int recordingType) {
        if (RecordingType.RECORDING_AMR == recordingType) {
            startRecording(callback);
        } else if (RecordingType.RECORDING_WAV == recordingType) {
            startRecordingWav(callback);
        } else if (RecordingType.RECORDING_MP3 == recordingType) {
            Context context = mContext;
            if (context == null) context = GcordSDK.getInstance().getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(RECORDING_SETTINGS, Context.MODE_PRIVATE);
            int sampleRate = sharedPreferences.getInt(AUTO_RECORDING_SAMPLE_RATE, 8000);
            int bitRate = sharedPreferences.getInt(AUTO_RECORDING_BIT_RATE, 32);
            startRecordingMp3(callback, sampleRate, bitRate);
        }
    }

    private void dialByPSTN(final String number) {
        final GcordPreference gcordPreference = GcordSDK.getInstance().getGcordPreference();
        if (isAppInChinese() && gcordPreference.isSmartDialOn()) {
            smartDial(number, this::pickupAndDial);
        } else {
//            String dialNumber = checkDialPrefix(number);
            pickupAndDial(number);
        }
    }

    private boolean isAppInChinese() {

        String locale = Locale.getDefault().getLanguage();
        if (locale.contains("cn") || locale.equals("zh"))
            return true;

        return false;
    }

    private void smartDial(final String number, OnSmartDialListener listener) {
        /*
         * 只有智能拨号打开的时候才需要加前缀
         */
        if (isCellPhoneNumber(number)) {
            GcordPreference gcordPreference = GcordSDK.getInstance().getGcordPreference();
            /*
             * 本机设置的区号
             */
            final String currentAreaCode = gcordPreference.getCurrentAreaCode();
            if (currentAreaCode != null && currentAreaCode.trim().length() > 0) {
                checkSmartDialSettings(number, currentAreaCode.trim(), listener);
                return;
            }
        }
        if (listener != null) {
            String dialNumber = checkDialPrefix(number);
            listener.dial(dialNumber);
        }
    }

    private String checkDialPrefix(final String number) {
        GcordPreference gcordPreference = GcordSDK.getInstance().getGcordPreference();
        String result = number;
        if (!TextUtils.isEmpty(gcordPreference.getDialPrefix())) {
            if (number.length() > gcordPreference.getInnerNumberMaxLength()) {
                //内线号码不需要加前缀
                result = gcordPreference.getDialPrefix().concat(number);
            }
        }
        return result;
    }

    private void checkSmartDialSettings(final String number, final String currentAreaCode, final OnSmartDialListener listener) {

        if(isPreferUsingOnlineChecker()){
            PhoneNumberUtil.checkNumberInfo(number, new AreaCodeManager.GetAreaCodeCallback() {
                @Override
                public void onGetAreaCode(AreaCodeItem areaCodeItem) {
                    String result = number;

                    if (areaCodeItem != null
                            && !currentAreaCode.equals(areaCodeItem.getAreaCode())) {
                        if (number.length() == 11
                                && number.startsWith("1")) {
                            //异地手机号加0
                            result = "0".concat(number);
                        }
                    }
                    if (listener != null) {
                        String dialNumber = checkDialPrefix(result);
                        listener.dial(dialNumber);
                    }
                }
            });

        }else{
            GcordSDK.getInstance().getAreaCodeManager().getAreaCode(number, false,
                    new AreaCodeManager.GetAreaCodeCallback() {
                        @Override
                        public void onGetAreaCode(AreaCodeItem areaCodeItem) {
                            String result = number;

                            if (areaCodeItem != null
                                    && !currentAreaCode.equals(areaCodeItem.getAreaCode())) {
                                if (number.length() == 11
                                        && number.startsWith("1")) {
                                    //异地手机号加0
                                    result = "0".concat(number);
                                }
                            }
                            if (listener != null) {
                                String dialNumber = checkDialPrefix(result);
                                listener.dial(dialNumber);
                            }
                        }
                    });

        }
    }

    private void pickupAndDial(final String number) {
        currentPhoneCall = new UnifiedPhoneCall(UnifiedPhoneCall.Phone_Type.PSTN);
        currentPhoneCall.setCallIn(false);
        currentPhoneCall.addNumberToDial(number);
        PSTNInternal.getInstance().autoPickUpForAction(new PhoneAPI.PickupActionListener() {
            @Override
            public void onPickUp() {
                GcordSDK.getInstance().getPhoneAPI().dial(number);
            }
        });
    }

    public DIAL_MODE checkDialMode() {
        return checkDialMode(true);
    }

    public DIAL_MODE checkDialMode(boolean shouldDialAnyway) {
        boolean isPSTNReady = (PhoneAPI.getInstance().getExtLineState() == PhoneAPI.EXT_LINE_STATE_AVAILABLE);
        boolean isSimCardReady = CellPhoneManager.getInstance().isSimCardAvailable();
        boolean isSipReady = false;
        try {
            isSipReady = SipPhoneManager.getInstance().isLogined();
        } catch (Exception ignored) {

        }

//        Log.e("GcordSDK", "" + PhoneAPI.getInstance().getExtLineState() + " " +
//                isPSTNReady + " " + isSimCardReady + " " + isSipReady);

        int dialMode = GcordSDK.getInstance().getGcordPreference().getSystemDialMode();
        if (dialMode == GcordPreferenceConstants.SYSTEM_DIAL_MODE_PSTN) {
            if (isPSTNReady || shouldDialAnyway) {
                return DIAL_MODE.PSTN;
            }
        }
        if (GcordPreferenceConstants.SYSTEM_DIAL_MODE_CELL == dialMode) {
            if (isSimCardReady || shouldDialAnyway) {
                return DIAL_MODE.CELL;
            }
        }
        if (GcordPreferenceConstants.SYSTEM_DIAL_MODE_SIP == dialMode) {
            if (isSipReady || shouldDialAnyway) {
                return DIAL_MODE.SIP;
            }
        }

        if (isPSTNReady) {
            return DIAL_MODE.PSTN;
        } else if (isSimCardReady) {
            return DIAL_MODE.CELL;
        } else if (isSipReady) {
            return DIAL_MODE.SIP;
        } else {
            return DIAL_MODE.PSTN;
        }
    }

    /**
     * 通话中拨dtmf号码
     *
     * @param number dtmf号码
     */
    public void dialDTMF(String number) {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                if (PSTNInternal.getInstance().isInCall()) {
                    PhoneAPI.getInstance().dial(number);
                }
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                CellPhoneManager.getInstance().dialDTMF(number);
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                try {
                    SipPhoneManager.getInstance().sendDTMF(number);
                } catch (Exception ignored) {

                }
            }
        }
    }

    public void pickUpPSTNWithHandsFreeIfInPSTNMode() {
        if (checkDialMode() == DIAL_MODE.PSTN) {
            PhoneAPI.getInstance().pickUpWithHandsFree();
        }
    }

    public void setAutoPlayRingTone(boolean autoPlay) {
        PhoneAPI.getInstance().setUseGlobalRingTone(autoPlay);
    }

    public void setUseClassicRingTone() {
        PhoneAPI.getInstance().turnOnClassicRing(true);
    }

    public void setUseMusicRingTone() {
        PhoneAPI.getInstance().turnOnClassicRing(false);
    }

    public void setMusicRingTonePath(String path) {
        PhoneAPI.getInstance().setGlobalRingTonePath(path);
    }

    public void setClassicRingTonePath(String path) {
        PhoneAPI.getInstance().setClassicRingPath(path);
    }

    /**
     * 设置底层自动播放铃声时候的stream type
     *
     * @param streamType only support AudioManager.STREAM_RING or AudioManager.STREAM_MUSIC
     */
    public void setRingToneMusicStream(int streamType) {
        if (streamType == AudioManager.STREAM_MUSIC || streamType == AudioManager.STREAM_RING) {
            Settings.System.putInt(mContext.getContentResolver(), SYS_PREF_RING_TONE_STREAM_TYPE,
                    streamType);
        }
    }

    public CallStatusItem queryCurrentCallStatus() {
        Map<String,String> map = GTAidlHandler.getInstance().queryCurrentCallStatus();

//        if(map != null)
//        for (Object key:map.keySet())
//        {
//            Log.e("CallStatus",""+key + " : " + map.get(key));
//        }
//        else
//        {
//            Log.e("CallStatus", "null");
//        }
        if (map != null) {
            StringBuilder sb = new StringBuilder();
            for (String key : map.keySet()) {
                sb.append(key + "=" + map.get(key));
            }
            Log.e("queryCurrentCallStatus", "data: " + sb.toString());
        }else{
            Log.e("queryCurrentCallStatus", "map is null");
        }
        return CallStatusItem.fromHashMap(map);

    }

    protected void onResumeInCallStatus(CallStatusItem item) {
        Log.e("queryCurrentCallStatus","onResumeInCallStatus, data:"+item);
        for (UnifiedPhoneEventListener listener : listeners) {
            if (listener != null) {
                listener.onResumeInCallStatus(item);
            }
        }
    }

    /***
     * 通话中，静音或去除静音
     * @param shouldMute true-静音，false-去除静音
     */
    public synchronized void muteCall(boolean shouldMute) {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.PSTN) {
                GcordSDK.getInstance().getPhoneAPI().setCurrentCallMute(shouldMute);
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.CELL) {
                GcordSDK.getInstance().getCellPhoneManager().setMute(shouldMute);
            } else if (currentPhoneCall.getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP) {
                try {
                    GcordSDK.getInstance().getSipPhoneManager().setMicrophoneMute(currentPhoneCall.getSessionId(), shouldMute);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    HashMap<DIAL_MODE, Integer> dialModePriorities = new HashMap<>();
//
//    public void setDialModePriority(DIAL_MODE mode, int priority)
//    {
//        dialModePriorities.put(mode, priority);
//    }p

    public synchronized boolean isRecording() {
        return isRecording;
    }

    public void setVolume(final int volume, int channel,
                          final PhoneAPI.VolumeCallback callback) {
        if (currentPhoneCall == null) return;
        UnifiedPhoneCall.Phone_Type type = currentPhoneCall.getCurrentCallType();
        if (UnifiedPhoneCall.Phone_Type.PSTN.equals(type)) {
            GcordSDK.getInstance().getPhoneAPI().setVolume(volume, channel, callback);
        } else if (UnifiedPhoneCall.Phone_Type.CELL.equals(type)) {
            GcordSDK.getInstance().getCellPhoneManager().setInCallVolume(volume);
            if (callback != null) {
                callback.onVolume(channel, GcordSDK.getInstance().getCellPhoneManager().getCurrentInCallVolume());
            }
        } else if (UnifiedPhoneCall.Phone_Type.SIP.equals(type)) {
            try {
                GcordSDK.getInstance().getSipPhoneManager().setSpeakerVolume(currentPhoneCall.getSessionId(), volume);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                int vol = 0;
                try {
                    vol = GcordSDK.getInstance().getSipPhoneManager().getSpeakerVolume(currentPhoneCall.getSessionId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (callback != null) {
                    callback.onVolume(channel, vol);
                }
            }
        }
    }

    public void setVolume(final int volume, final PhoneAPI.VolumeCallback callback) {
        int channel;
        if (isHandsFreeOn()) {
            channel = 1;
        } else {
            channel = 0;
        }
        setVolume(volume, channel, callback);
    }

    public void getVolume(final int channel, final PhoneAPI.VolumeCallback callback) {
        ExecutorService pool = getThreadPool();
        pool.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                while (currentPhoneCall == null) {
                    Thread.sleep(10L);
                }
                try {
                    UnifiedPhoneCall.Phone_Type type = currentPhoneCall.getCurrentCallType();
                    if (UnifiedPhoneCall.Phone_Type.PSTN.equals(type)) {
                        GcordSDK.getInstance().getPhoneAPI().getVolume(channel, callback);
                    } else if (UnifiedPhoneCall.Phone_Type.CELL.equals(type)) {
                        int vol = GcordSDK.getInstance().getCellPhoneManager().getCurrentInCallVolume();
                        if (callback != null) {
                            callback.onVolume(channel, vol);
                        }
                    } else if (UnifiedPhoneCall.Phone_Type.SIP.equals(type)) {
                        int vol = 0;
                        try {
                            vol = GcordSDK.getInstance().getSipPhoneManager().getSpeakerVolume(currentPhoneCall.getSessionId());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (callback != null) {
                            callback.onVolume(channel, vol);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        });
    }

    public void getVolume(final PhoneAPI.VolumeCallback callback) {
        int channel;
        if (isHandsFreeOn()) {
            channel = 1;
        } else {
            channel = 0;
        }
        getVolume(channel, callback);
    }

    @UiThread
    public int getMaxVolume() {
        ExecutorService pool = getThreadPool();
        Future<Integer> future = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() {
                try {
                    UnifiedPhoneCall.Phone_Type type = currentPhoneCall.getCurrentCallType();
                    if (UnifiedPhoneCall.Phone_Type.PSTN.equals(type)) {
                        return GcordSDK.getInstance().getPhoneAPI().getMaxVolume();
                    } else if (UnifiedPhoneCall.Phone_Type.CELL.equals(type)) {
                        return GcordSDK.getInstance().getCellPhoneManager().getMaxInCallVolume();
                    } else if (UnifiedPhoneCall.Phone_Type.SIP.equals(type)) {
                        return 20;
                    }
                    return 4;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return 4;
            }
        });
        try {
            return future.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            return 4;
        }
    }

    @SuppressWarnings("unused")
    public void getMaxVolume(final PhoneAPI.VolumeCallback callback) {
        ExecutorService pool = getThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                int maxVol = 4;
                try {
                    UnifiedPhoneCall.Phone_Type type = currentPhoneCall.getCurrentCallType();
                    if (UnifiedPhoneCall.Phone_Type.PSTN.equals(type)) {
                        maxVol = GcordSDK.getInstance().getPhoneAPI().getMaxVolume();
                    } else if (UnifiedPhoneCall.Phone_Type.CELL.equals(type)) {
                        maxVol = GcordSDK.getInstance().getCellPhoneManager().getMaxInCallVolume();
                    } else if (UnifiedPhoneCall.Phone_Type.SIP.equals(type)) {
                        maxVol = 20;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (callback != null) {
                    callback.onVolume(0, maxVol);
                }
            }
        });
    }

    @UiThread
    private ExecutorService getThreadPool() {
        if (threadPool == null) {
            synchronized (this) {
                if (threadPool == null) {
                    threadPool = Executors.newCachedThreadPool();
                }
            }
        }
        return threadPool;
    }

    /**
     * enable auto recording
     *
     * @param recordingType one of the constants in RecordingType
     * @see RecordingType
     */
    @SuppressWarnings("unused")
    public void enableAutoRecording(@RecordingType int recordingType) {
        enableAutoRecording(recordingType, 8000, 32);
    }

    /**
     * 自动录音设置
     *
     * @param recordingType 自动录音类型
     * @param sampleRate    mp3文件采样率，例如44100，8000等，只有在录音格式为mp3的时候生效
     * @param bitRate       mp3文件比特率，例如32（表示32k的比特率）等，只有在录音格式为mp3的时候生效
     * @see RecordingType
     */
    public void enableAutoRecording(@RecordingType int recordingType, int sampleRate, int bitRate) {
        Context context = mContext;
        if (context == null) context = GcordSDK.getInstance().getContext();
        if (context == null) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECORDING_SETTINGS, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putInt(AUTO_RECORDING_ENABLED, recordingType).putInt(AUTO_RECORDING_SAMPLE_RATE, sampleRate)
                    .putInt(AUTO_RECORDING_BIT_RATE, bitRate).apply();
        }
    }

    /**
     * disable auto recording
     */
    @SuppressWarnings("unused")
    public void disableAutoRecording() {
        Context context = mContext;
        if (context == null) context = GcordSDK.getInstance().getContext();
        if (context == null) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECORDING_SETTINGS, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            sharedPreferences.edit().remove(AUTO_RECORDING_ENABLED).apply();
        }
    }

    /**
     * get the auto recording audio type
     *
     * @return the auto recording audio type, and return -1 if not specified
     * @see RecordingType
     */

    public @RecordingType
    int getAutoRecordingType() {
        Context context = mContext;
        if (context == null) context = GcordSDK.getInstance().getContext();
        if (context == null) return -1;
        SharedPreferences sharedPreferences = context.getSharedPreferences(RECORDING_SETTINGS, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(AUTO_RECORDING_ENABLED, -1);
        }
        return -1;
    }

    /***
     * adding auto recording call backs
     * @param autoRecordingCallback auto recording call back to add;
     * @see AutoRecordingCallback
     */
    @SuppressWarnings("unused")
    public void addAutoRecordingCallback(AutoRecordingCallback autoRecordingCallback) {
        try {
            if (!autoRecordingCallbacks.contains(autoRecordingCallback)) {
                autoRecordingCallbacks.add(autoRecordingCallback);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /***
     * remove auto recording call back
     * @param autoRecordingCallback auto recording call back to remove
     */
    @SuppressWarnings("unused")
    public void removeAutoRecordingCallback(AutoRecordingCallback autoRecordingCallback) {
        try {
            autoRecordingCallbacks.remove(autoRecordingCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public enum DIAL_MODE {
        PSTN,
        SIP,
        CELL
    }

    public interface UnifiedPhoneRecordCallback {
        void onRecordSuccess(String audioPath);

        void onRecordFailed(int errorCode, String errorMessage);
    }

    public interface UnifiedPhoneEventListener {

        /**
         * 来电
         */
        void onIncomingCall();

        /**
         * 获取到来电号码
         *
         * @param number
         */
        void onGetPhoneNumber(String number);

        /**
         * 通话计时, 从0开始,单位为秒，每1秒钟回调一次。
         *
         * @param seconds
         */
        void onTicking(int seconds);

        /**
         * 通话结束
         */
        void onCallTerminated();

        /**
         * on dial finish
         *
         * @param number
         */
        void onDialFinish(String number);

        /**
         * 收到忙音信号
         */
        void onBusyTone();

        /**
         * app重启的时候如果当前正在通话中，将收到此回调。
         * 如果是正在来电尚未接听的情况，会直接收到onIncomingCall而不是此回调
         */
        void onResumeInCallStatus(CallStatusItem item);
    }

    private interface OnSmartDialListener {
        void dial(String phoneNumber);
    }

    /**
     * 录音类型
     * 1-amr，2-wav，3-mp3
     */
    @SuppressWarnings("unused")
    public @interface RecordingType {
        int RECORDING_AMR = 1;
        int RECORDING_WAV = 2;
        int RECORDING_MP3 = 3;
    }

    public interface AutoRecordingCallback extends UnifiedPhoneRecordCallback {
        void onRecordingStarted();
    }

    private static class SingletonHolder {
        private static UnifiedPhoneController unifiedPhoneController;

        static {
            unifiedPhoneController = new UnifiedPhoneController();
        }
    }

    public String getCurrentCallLogId() {
        if (currentPhoneCall != null) {
            if (currentPhoneCall.getCurrentCallType() != UnifiedPhoneCall.Phone_Type.SIP) {
                return CallLogManager.getInstance().getCurrentCallLogId();
            } else return SipPhoneManager.getInstance().getCurrentCallLogId();
        }
        return null;
    }

    /**
     * 设置是否优先使用在线的号码数据库
     * 默认为false，即优先使用离线数据库
     * @param use true = 优先使用在线； false = 优先使用离线
     */
    public void setPreferUsingOnlineChecker(boolean use){
        try {
            Context context = mContext;
            if (context == null) context = GcordSDK.getInstance().getContext();
            if (context == null) return;
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_USE_ONLINE_CHECKER, Context.MODE_PRIVATE);
            if (sharedPreferences != null) {
                sharedPreferences.edit().putInt(PREF_USE_ONLINE_CHECKER, use ? 1 : 0).apply();
            }
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取是否优先使用在线的号码数据库
     * 默认为false，即优先使用离线数据库
     * @return true = 优先使用在线； false = 优先使用离线
     */
    public boolean isPreferUsingOnlineChecker(){
        try {
            Context context = mContext;
            if (context == null) context = GcordSDK.getInstance().getContext();
            if (context == null) return false;
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_USE_ONLINE_CHECKER, Context.MODE_PRIVATE);
            if (sharedPreferences != null) {
                return sharedPreferences.getInt(PREF_USE_ONLINE_CHECKER, 0) == 1;
            }
        }catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }
}
