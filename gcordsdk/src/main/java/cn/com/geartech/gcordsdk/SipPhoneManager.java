package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.com.geartech.app.SipPhoneApi;

import static cn.com.geartech.gcordsdk.PSTNInternal.EVENT_SIP_PHONE_RESTARTED;

/**
 * Created by cuizhy on 16/8/9.
 */
public class SipPhoneManager extends GcordHelper {

    public static final String SIP_PHONE_ALERT_ACTION = "cn.com.geartech.SIP_PHONE_ALERT_ACTION";
    public static final String SIP_PHONE_REGISTER_RESULT_ACTION = "cn.com.geartech.SIP_PHONE_REGISTER_RESULT_ACTION";
    public static final String SIP_PHONE_INIT_RESULT_ACTION = "cn.com.geartech.SIP_PHONE_INIT_RESULT_ACTION";
    public static final String META_DATA_NAME = "cn.com.geartech.sip_operation";
    public static final String META_DATA_VALUE_CUSTOM = "custom";
    public static final String META_DATA_OUT_GOING_CALL_INTERCEPTION = "cn.com.geartech.sip_out_going_call_interception";
    public static final String META_DATA_INCOMING_CALL_INTERCEPTION = "cn.com.geartech.sip_out_incoming_call_interception";
    private static final String SIP_PHONE_HANG_OFF_ACTION = "cn.com.geartech.SIP_PHONE_HANG_OFF_ACTION";
    private static final String SIP_PHONE_INCOMING_CALL_ACTION = "cn.com.geartech.SIP_PHONE_INCOMING_CALL_ACTION";
    private static final String SIP_PHONE_TALKING_ACTION = "cn.com.geartech.SIP_PHONE_TALKING_ACTION";
    private static final String SIP_PHONE_OUTGOING_CALL_ACTION = "cn.com.geartech.SIP_PHONE_OUTGOING_CALL_ACTION";
    private static final String SIP_PHONE_RECORDING_ACTION = "cn.com.geartech.SIP_PHONE_RECORDING_ACTION";
    private static final String SIP_PHONE_CHANGE_MUSIC_CHANNEL_ACTION = "cn.com.geartech.SIP_PHONE_CHANGE_MUSIC_CHANNEL_ACTION";
    private static final String SIP_PHONE_AIDL_DISCONNECTED = "cn.com.geartech.SIP_PHONE_AIDL_DISCONNECTED";
    private static final String RECONNECT_SIP_PHONE_SERVICE_ACTION = "cn.com.geartech.RECONNECT_SIP_PHONE_SERVICE";
    protected static SipPhoneManager instance = null;
    SipPhoneApi sipPhoneApi = null;
    private boolean isCmccVersion = false;
    private Context context = null;
    private List<SipPhoneEventListener> sipPhoneEventListeners = null;
    private Handler handler = null;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.e("Sip Phone", "sip phone broadcast received, action:" + action);
            int sessionId = intent.getIntExtra("sessionId", SipPhoneConstants.INVALIDID);


            if (EVENT_SIP_PHONE_RESTARTED.equals(action) || RECONNECT_SIP_PHONE_SERVICE_ACTION.equals(action)) {
                Log.e("Sip Phone", "reconnect aidl");
                connectAIDL();
            } else if (SIP_PHONE_HANG_OFF_ACTION.equals(action)) {
                for (SipPhoneEventListener l : sipPhoneEventListeners) {
                    if (l != null) {
                        Log.e("Sip Phone", "onSipPhoneCallHangOff");
                        l.onSipPhoneCallHangOff(sessionId);
                    }
                }
            } else if (SIP_PHONE_INCOMING_CALL_ACTION.equals(action)) {
                String phoneNumber = intent.getStringExtra("peer number");
                boolean isVedioCall = intent.getBooleanExtra("Is Video Call", false);
                for (SipPhoneEventListener l : sipPhoneEventListeners) {
                    if (l != null) {
                        Log.e("Sip Phone", "onIncomingCallReceived");
                        l.onIncomingCallReceived(sessionId, isVedioCall, phoneNumber);
                    }
                }
            } else if (SIP_PHONE_TALKING_ACTION.equals(action)) {
                for (SipPhoneEventListener l : sipPhoneEventListeners) {
                    if (l != null) {
                        Log.e("Sip Phone", "onSipPhoneTalking");
                        l.onSipPhoneTalking(sessionId);
                    }
                }
            } else if (SIP_PHONE_OUTGOING_CALL_ACTION.equals(action)) {
                String phoneNumber = intent.getStringExtra("peer number");
                for (SipPhoneEventListener l : sipPhoneEventListeners) {
                    if (l != null) {
                        Log.e("Sip Phone", "onSipPhoneOutgoingCall");
                        l.onSipPhoneOutgoingCall(sessionId, phoneNumber);
                    }
                }
            } else if (SIP_PHONE_ALERT_ACTION.equals(action)) {
                for (SipPhoneEventListener l : sipPhoneEventListeners) {
                    if (l != null) {
                        Log.e("Sip Phone", "onSipPhoneAlerted");
                        l.onSipPhoneAlerted(sessionId);
                    }
                }
            } else if (SIP_PHONE_REGISTER_RESULT_ACTION.equals(action)) {
                int state = intent.getIntExtra("state", -1);
                int stateCode = intent.getIntExtra("stateCode", -1);
                try {

                    switch (state) {
                        case SipPhoneConstants.LOGIN_STATE_REGED: {
                            for (SipPhoneEventListener listener : sipPhoneEventListeners) {
                                Log.e("Sip Phone", "onLoginSuccessfully");
                                listener.onLoginSuccessfully();
                            }
                            break;
                        }
                        case SipPhoneConstants.LOGIN_STATE_REGING:
                            for (SipPhoneEventListener listener : sipPhoneEventListeners) {
                                Log.e("Sip Phone", "onLoginInProgress");
                                listener.onLoginInProgress();
                            }
                            break;
                        case SipPhoneConstants.LOGIN_STATE_UNREGING:
                            for (SipPhoneEventListener listener : sipPhoneEventListeners) {
                                Log.e("Sip Phone", "onLogoutInProgress");
                                listener.onLogoutInProgress();
                            }

                            break;
                        case SipPhoneConstants.LOGIN_STATE_IDLE: {
                            if (stateCode == SipPhoneConstants.LOGIN_ERR_DEACTED) {
                                for (SipPhoneEventListener listener : sipPhoneEventListeners) {
                                    Log.e("Sip Phone", "onForceOffline");
                                    listener.onForceOffline();
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_SEND_MSG) {
                                Log.e("Sip Phone", "登陆失败，信令发送有误");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_AUTH_FAILED) {
                                Log.e("Sip Phone", "登陆失败，注册鉴权有误，无效用户名或密码");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_INVALID_USER) {
                                Log.e("Sip Phone", "登陆失败，无效用户名");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_TIMEOUT) {
                                Log.e("Sip Phone", "登陆失败，登陆超时");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_SERV_BUSY) {
                                Log.e("Sip Phone", "登陆失败，服务器忙");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_SERV_NOT_REACH) {
                                Log.e("Sip Phone", "登陆失败，服务器不可达");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_SRV_FORBIDDEN) {
                                Log.e("Sip Phone", "登陆失败，无效账号或密码错误");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_SRV_UNAVAIL) {
                                Log.e("Sip Phone", "登陆失败，服务器不可用");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_DNS_QRY) {
                                Log.e("Sip Phone", "登陆失败，域名解析失败");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_NETWORK) {
                                Log.e("Sip Phone", "登陆失败，网络问题");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_DEACTED) {
                                Log.e("Sip Phone", "登陆失败，注册状态失效");
                                for (SipPhoneEventListener listener : sipPhoneEventListeners) {
                                    Log.e("Sip Phone", "onForceOffline");
                                    listener.onForceOffline();
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_INTERNAL) {
                                Log.e("Sip Phone", "登陆失败，服务器内部错误");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_REJECTED) {
                                Log.e("Sip Phone", "强制退出，账号已在其他地方登录");
                                for (SipPhoneEventListener listener : sipPhoneEventListeners) {
                                    Log.e("Sip Phone", "onForceOffline");
                                    listener.onForceOffline();
                                }
                            } else if (stateCode == SipPhoneConstants.LOGIN_ERR_OTHER) {
                                Log.e("Sip Phone", "登陆失败，其他错误");
                                for (SipPhoneEventListener listener :
                                        sipPhoneEventListeners) {
                                    listener.onRegisterOperationError(stateCode);
                                }
                            } else {
                                for (SipPhoneEventListener listener : sipPhoneEventListeners) {
                                    Log.e("Sip Phone", "onLogout");
                                    listener.onLogout();
                                }
                            }
                        }
                        break;
                        case -1: {
                            if (-1 != stateCode) {
                                switch (stateCode) {
                                    case SipPhoneConstants.LOGIN_ERR_AUTH_FAILED:
                                    case SipPhoneConstants.LOGIN_ERR_INVALID_USER:
                                    case SipPhoneConstants.LOGIN_ERR_SRV_FORBIDDEN:
                                        for (SipPhoneEventListener listener :
                                                sipPhoneEventListeners) {
                                            Log.e("Sip Phone", "onLoginFailed");
                                            listener.onLoginFailed(stateCode);
                                        }
                                        break;
                                    case SipPhoneConstants.LOGIN_ERR_DEACTED:
                                        for (SipPhoneEventListener listener :
                                                sipPhoneEventListeners) {
                                            Log.e("Sip Phone", "onForceOffline");
                                            listener.onForceOffline();
                                        }
                                        break;
                                    case SipPhoneConstants.LOGIN_ERR_TIMEOUT:
                                    case SipPhoneConstants.LOGIN_ERR_SRV_UNAVAIL:
                                    case SipPhoneConstants.LOGIN_ERR_NETWORK:
                                    default:
                                        for (SipPhoneEventListener listener :
                                                sipPhoneEventListeners) {
                                            Log.e("Sip Phone", "onRegisterOperationError");
                                            listener.onRegisterOperationError(stateCode);
                                        }
                                        break;
                                }
                            }
                        }
                        break;
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else if (SIP_PHONE_INIT_RESULT_ACTION.equals(action)) {
                boolean result = intent.getBooleanExtra("init result", false);
                if (result) {
                    for (SipPhoneEventListener listener :
                            sipPhoneEventListeners) {
                        listener.onSipPhoneSDKInitEnd();
                    }
                } else {
                    int errorCode = intent.getIntExtra("result code", -100);
                    for (SipPhoneEventListener listener :
                            sipPhoneEventListeners) {
                        listener.onSipPhoneSDKInitFailed(errorCode);
                    }
                }
            }
        }
    };
    private Runnable checkSipPhoneApiRunnable = new Runnable() {
        @Override
        public void run() {
            if (sipPhoneApi == null) {
                connectAIDL();
            }
        }
    };
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.w("Sip Phone", "Sip phone manager aidl is dead");
            sipPhoneApi = null;
            restartAidl(1500L);
        }
    };
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Tiger Log", "sip phone service connected");
            sipPhoneApi = SipPhoneApi.Stub.asInterface(service);
            if (sipPhoneApi != null) {
                try {
                    sipPhoneApi.initSipPhone();
                    registerReceiver();
                    handler.removeCallbacks(checkSipPhoneApiRunnable);
                    service.linkToDeath(deathRecipient, 0);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sipPhoneApi = null;
            Log.e("Tiger Log", "onServiceDisconnected");
            try {
                context.unregisterReceiver(mReceiver);
            } catch (Exception ex) {

            }
            Intent intent = new Intent(SIP_PHONE_AIDL_DISCONNECTED);
            if (!isCmccVersion) {
                intent.setPackage("cn.com.geartech.sip");
            } else {
                intent.setPackage("cn.com.geartech.sip.cmcc");
            }
            context.sendBroadcast(intent);
            handler.postDelayed(checkSipPhoneApiRunnable, 1000);
//            connectAIDL();
        }
    };
    private boolean recording = false;
    private Object customToken = null;

    protected SipPhoneManager() {

//        new Exception().printStackTrace();
//
//        Log.e("GcordSDK", "SipPhoneManager inited!");

        handler = new Handler();
        if (this.sipPhoneEventListeners == null) {
            this.sipPhoneEventListeners = new ArrayList<>();
        }
    }

    protected static SipPhoneManager getInstance() {
        if (instance == null) {
            instance = new SipPhoneManager();
        }
        return instance;
    }

    protected boolean connectAIDL() {
        Intent intent = new Intent("cn.com.geartech.sip.cmcc.service.SipPhoneService");
        intent.setPackage("cn.com.geartech.sip");
        intent.putExtra("packageName", context.getPackageName());
        boolean ret = this.context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
//        handler.postDelayed(checkSipPhoneApiRunnable, 2*1000);
        return ret;
    }

    private boolean shouldConnectAIDL() {
        if ("cn.com.geartech.app".equals(context.getPackageName())) {
            return true;
        }
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        if (applicationInfoList != null) {
            for (ApplicationInfo applicationInfo : applicationInfoList) {
                if (context.getPackageName().equals(applicationInfo.packageName)) {
                    try {
                        Bundle metaData = applicationInfo.metaData;
                        if (metaData != null && metaData.containsKey(META_DATA_NAME)) {
                            String value = metaData.getString(META_DATA_NAME);
                            if (value != null && value.equals(META_DATA_VALUE_CUSTOM)) {
                                return true;
                            }
                        }
                        if (metaData != null && metaData.containsKey(META_DATA_INCOMING_CALL_INTERCEPTION)) {
                            String value = metaData.getString(META_DATA_INCOMING_CALL_INTERCEPTION);
                            if (value != null && value.equals(META_DATA_VALUE_CUSTOM)) {
                                return true;
                            }
                        }
                        if (metaData != null && metaData.containsKey(META_DATA_OUT_GOING_CALL_INTERCEPTION)) {
                            String value = metaData.getString(META_DATA_OUT_GOING_CALL_INTERCEPTION);
                            if (value != null && value.equals(META_DATA_VALUE_CUSTOM)) {
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    private void restartAidl(long i) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                connectAIDL();
            }
        }, i);
    }

    public void addSipPhoneEventListeners(SipPhoneEventListener sipPhoneEventListener) {
        if (sipPhoneEventListener != null && !sipPhoneEventListeners.contains(sipPhoneEventListener)) {
            this.sipPhoneEventListeners.add(sipPhoneEventListener);
        }
    }

    public void removeSipPhoneEventListener(SipPhoneEventListener sipPhoneEventListener) {
        if (this.sipPhoneEventListeners != null && this.sipPhoneEventListeners.contains(sipPhoneEventListener)) {
            this.sipPhoneEventListeners.remove(sipPhoneEventListener);
        }
    }

    protected void init(Context context) {
        this.context = context;

        if (connectAIDL()) {
            if (internalInitCallback != null) {
                internalInitCallback.onInitFinished();
            }
        } else {
            if (internalInitCallback != null) {
                internalInitCallback.onInitFailed();
            }
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(EVENT_SIP_PHONE_RESTARTED);
        intentFilter.addAction(SIP_PHONE_HANG_OFF_ACTION);
        intentFilter.addAction(SIP_PHONE_INCOMING_CALL_ACTION);
        intentFilter.addAction(SIP_PHONE_TALKING_ACTION);
        intentFilter.addAction(SIP_PHONE_OUTGOING_CALL_ACTION);
        intentFilter.addAction(SIP_PHONE_ALERT_ACTION);
        intentFilter.addAction(SIP_PHONE_REGISTER_RESULT_ACTION);
        intentFilter.addAction(SIP_PHONE_INIT_RESULT_ACTION);
        intentFilter.addAction(RECONNECT_SIP_PHONE_SERVICE_ACTION);
        context.registerReceiver(mReceiver, intentFilter);
    }

    /**
     * init sip phone sdk
     */
    public void initSipPhoneSDK() throws RemoteException {
        if (sipPhoneApi != null) {
            sipPhoneApi.initSipPhone();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * 拨打sip电话
     *
     * @param uri 拨打电话的uri(示例:sip:800@172.16.6.7)
     * @return session ID
     * @throws RemoteException
     */
    public int dial(String uri) throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.dial(uri);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * 结束通话接口
     *
     * @param sessionId 当次通话的session ID
     * @param reason    结束通话的原因
     * @throws RemoteException
     */
    public void hangOff(int sessionId, int reason) throws RemoteException {
        if (sipPhoneApi != null) {
            sipPhoneApi.hangoff(sessionId, reason);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
        if (isRecording()) stopRecording();
    }

    /**
     * @param sessionId 当次会ID
     * @throws RemoteException
     */
    public void answerSipPhone(int sessionId) throws RemoteException {
        if (sipPhoneApi != null) {
            sipPhoneApi.answerPhoneCall(sessionId);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * @param account         账户名
     * @param password        密码
     * @param callId
     * @param port            端口
     * @param registerAddress 注册地址
     * @param realm           域
     * @param proxy           代理
     * @param transportMode   传输模式:tcp,udp
     * @throws RemoteException
     */
    public void loginSipPhone(final String account,
                              final String password,
                              final String callId,
                              final int port,
                              final String registerAddress,
                              final String realm,
                              final String proxy,
                              final int transportMode) throws RemoteException {
        if (sipPhoneApi != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sipPhoneApi.login(account, password, callId, port, registerAddress, realm, proxy, transportMode);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * 注销接口
     *
     * @throws RemoteException
     */
    public void logout() throws RemoteException {
        if (sipPhoneApi != null) {
            sipPhoneApi.logout();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * 发送dtmf
     *
     * @param dtmf 取值范围0-9,*,#
     * @throws RemoteException
     */
    public void sendDTMF(String dtmf) throws RemoteException {
        if (sipPhoneApi != null) {
            sipPhoneApi.sendDTMF(dtmf);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public boolean isRecording() {
        return recording;
    }

    /**
     * 开始录音, 录音格式是amr
     *
     * @param path 录音文件存放路径
     */
    public void startRecording(String path) {
        sendRecordingBroadcast(true, path, UnifiedPhoneController.RecordingType.RECORDING_AMR, null, null);
    }

    /**
     * 开始录音
     *
     * @param path 录音文件存放路径,因录音格式为wav,存放路径必须以".wav"为后缀,否则自动添加".wav"
     */
    public void startRecordingMp3(String path, int sampleRate, int bitRate) {
        sendRecordingBroadcast(true, path, UnifiedPhoneController.RecordingType.RECORDING_MP3, sampleRate, bitRate);
    }

    public void startRecordingMp3(String path) {
        startRecordingMp3(path, 8000, 32);
    }

    /**
     * 开始录音
     *
     * @param path 录音文件存放路径,因录音格式为wav,存放路径必须以".wav"为后缀,否则自动添加".wav"
     */
    public void startRecordingWav(String path) {
        sendRecordingBroadcast(true, path, UnifiedPhoneController.RecordingType.RECORDING_WAV, null, null);
    }

    /**
     * stop recording
     */
    public void stopRecording() {
        sendRecordingBroadcast(false, null, -1, null, null);
    }

    private void sendRecordingBroadcast(boolean start, String path, int format, Integer sampleRate, Integer bitRate) {
        Intent intent = new Intent(SIP_PHONE_RECORDING_ACTION);

        intent.putExtra("start", start);
        if (start) {
            if (TextUtils.isEmpty(path) || TextUtils.isEmpty(path.trim())) {
                recording = false;
                return;
            }
            intent.putExtra("path", path);
            intent.putExtra("format", format);
            if (sampleRate != null) intent.putExtra("SampleRate", sampleRate);
            if (bitRate != null) intent.putExtra("BitRate", bitRate);
            intent.setPackage("cn.com.geartech.sip");
        }
        recording = start;
        GcordSDK.getInstance().getContext().sendBroadcast(intent);
    }

    /**
     * get the volume of the call session
     *
     * @param sessionId session id of the sip phone call
     * @return
     * @throws RemoteException
     */
    public int getSpeakerVolume(int sessionId) throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getSpeakerVolume(sessionId);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * set the volume of the call session
     *
     * @param sessionId session id of the sip phone call
     * @param volume    the volme to set, between 0 to 20
     * @throws RemoteException
     */
    public void setSpeakerVolume(int sessionId, int volume) throws RemoteException {
        if (sipPhoneApi != null) {
            sipPhoneApi.setSpeakerVolume(sessionId, volume);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * @return
     * @throws RemoteException
     */
    public String getUserName() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getUserName();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * @return
     * @throws RemoteException
     */
    public String getAuthName() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getAuthName();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * @return
     * @throws RemoteException
     */
    public String getRealm() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getRealm();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * @return
     * @throws RemoteException
     */
    public String getRegIp() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getRegIp();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public String getPassword() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getPassword();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public int getPort() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getPort();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public String getProxy() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getProxy();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * set the music channel
     *
     * @param channel: 手柄:HandleManager.MUSIC_CHANNEL_HANDLE, 公放:HandleManager.MUSIC_CHANNEL_HANDS_FREE
     */
    public void setMusicChannel(int channel, final boolean shouldAdjustMusicChannel) {

        if (Build.VERSION.SDK_INT < 21) {
            if (HandleManager.MUSIC_CHANNEL_HANDLE == channel) {
                GcordSDK.getInstance().getHandleManager().switchToHandle4VOIP();
            } else if (HandleManager.MUSIC_CHANNEL_HANDS_FREE == channel) {
                GcordSDK.getInstance().getHandleManager().switchToHandsFree4VOIP();
            }
        } else {
            Intent intent = new Intent(SIP_PHONE_CHANGE_MUSIC_CHANNEL_ACTION);
            if (!isCmccVersion) {
                intent.setPackage("cn.com.geartech.sip");
            } else {
                intent.setPackage("cn.com.geartech.sip.cmcc");
            }
            intent.putExtra("channel", channel);
            intent.putExtra("shouldAdjustMusicChannel", shouldAdjustMusicChannel);
            context.sendBroadcast(intent);
            return;
        }

    }

    public boolean isMicrophoneMute(int sessionId) throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.isMicrophoneMute(sessionId);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public void setMicrophoneMute(int sessionId, boolean isMute) throws RemoteException {
        if (sipPhoneApi != null) {
            sipPhoneApi.setMicrophoneMute(sessionId, isMute);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public String getPhoneNumber(int sessionId) throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getPhoneNumber(sessionId);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public int getRegExpireTime() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getRegExpireTime();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public void setRegExpireTime(int time) throws RemoteException {
        if (sipPhoneApi != null) {
            sipPhoneApi.setRegExpireTime(time);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public boolean isSDKinitialized() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.isSDKInitialized();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public int getSipRegTpt() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.getSipRegTpt();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public boolean isLogined() throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.isLogined();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public int dialByPhoneNumber(String phoneNumber) throws RemoteException {
        if (sipPhoneApi != null) {
            return sipPhoneApi.dialByPhoneNumber(phoneNumber);
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    /**
     * 开始录音
     *
     * @param listener    录音回调
     * @param customToken 自定义的token.会在回调里面把这个token传回去.
     *
     */

    /**
     * @hide
     */
    public void startRecording(final PhoneAPI.AudioRecordListener listener, Object customToken) {
        this.customToken = customToken;
        String fileName = generateFileName();
//        if()
    }

    private String generateFileName() {
        String result = getAudioRecordDir();
        String uuid = UUID.randomUUID().toString();
        DateFormat df = new SimpleDateFormat("yyyyMMddkkmmss");
        Date date = new Date();
        String str = df.format(date);

        result += (str + uuid + ".mp3");

        return result;
    }

    private String getAudioRecordDir() {
        String result = "";

        String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        result += sdCardPath;

        if (!(result.endsWith("\\") || result.endsWith("/"))) {
            result += "/";
        }

        result += "GcordAudioRecord/";
        //TODO fix it later
        String packageName = "com.smartque";

        result += packageName + "/";

        File dir = new File(result);
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                dir.delete();
                dir.mkdirs();
            }
        } else {
            dir.mkdirs();
        }

        return result;
    }

    /**
     * @return 当前通话的call log id（如果有通话的话）
     */
    public String getCurrentCallLogId() {
        try {
            return sipPhoneApi.getCurrentCallLogId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface SipPhoneEventListener {

        /**
         * sip phone sdk init successfully
         */
        void onSipPhoneSDKInitEnd();

        /**
         * sip phone sdk init failed
         *
         * @param errorCode
         */
        void onSipPhoneSDKInitFailed(int errorCode);

        /**
         * 来电事件通知
         *
         * @param sessionId   当前通话id
         * @param isVedioCall 是否视频通话,true-视频通话,false-语音通话
         * @param phoneNumber 来电号码
         */
        void onIncomingCallReceived(int sessionId, boolean isVedioCall, String phoneNumber);
//        void onSipPhoneInCall(int sessionId);

        /**
         * 去电时间
         *
         * @param sessionId   去电会话id
         * @param phoneNumber 去电号码
         */
        void onSipPhoneOutgoingCall(int sessionId, String phoneNumber);

        /**
         * 响铃时间,表示已接通,电话正在响铃
         *
         * @param sessionId
         */
        void onSipPhoneAlerted(int sessionId);

        /**
         * 通话事件,双方正在通话中
         *
         * @param sessionId
         */
        void onSipPhoneTalking(int sessionId);

        /**
         * 挂断,拒绝通话事件,无论主动还是被动挂断都会收到该事件通知
         *
         * @param sessionId
         */
        void onSipPhoneCallHangOff(int sessionId);


        /**
         * 登录失败回调
         *
         * @param errorCode 错误码 参见SipPhoneConstants
         */
        void onLoginFailed(int errorCode);

        /**
         * 发送登录请求中回调
         */
        void onLoginInProgress();

        /**
         * 登录成功回调
         */
        void onLoginSuccessfully();

        /**
         * 注销成功回调
         */
        void onLogout();

        /**
         * 注销请求发送中
         */
        void onLogoutInProgress();

        /**
         * 登录或注销过程中发生错误回调
         *
         * @param errorCode 错误码,参见SipPhoneConstants
         */
        void onRegisterOperationError(int errorCode);

        /**
         * 被强制下线回调
         */
        void onForceOffline();
    }

    public static abstract class SipPhoneEventListenerAdapter implements SipPhoneEventListener {

        /**
         * sip phone sdk init successfully
         */
        @Override
        public void onSipPhoneSDKInitEnd() {

        }

        /**
         * sip phone sdk init failed
         *
         * @param errorCode
         */
        @Override
        public void onSipPhoneSDKInitFailed(int errorCode) {

        }

        @Override
        public void onIncomingCallReceived(int sessionId, boolean isVedioCall, String phoneNumber) {

        }

        @Override
        public void onSipPhoneOutgoingCall(int sessionId, String phoneNumber) {

        }

        @Override
        public void onSipPhoneAlerted(int sessionId) {

        }

        @Override
        public void onSipPhoneTalking(int sessionId) {

        }

        @Override
        public void onSipPhoneCallHangOff(int sessionId) {

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
    }
}
