package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.geartech.gcordsdk.dataType.CallStatusItem;
import cn.com.geartech.gcordsdk.dataType.CellSignalItem;
import cn.com.geartech.gtplatform.model.aidl.ICellPhoneAidlInterface;
import cn.com.geartech.gtplatform.model.aidl.ICommonCallback;

/**
 * Created by pangyuning on 15/12/25.
 */
public class CellPhoneManager {

    static CellPhoneManager instance = null;

    Context context;

    ICellPhoneAidlInterface cellPhoneAidlInterface;

    public static final int CALL_STATE_INVALID = -1;
    public static final int CALL_STATE_IDLE = 0;
    public static final int CALL_STATE_CONNECTING = 13;
    public static final int CALL_STATE_ACTIVE = 3;


    protected static final String Event_OnIncomingCall = "cn.com.geartech.cellphone.event_onincomingcall";
    protected static final String Event_OnCallStateChanged = "cn.com.geartech.cellphone.event_oncallstatechanged";
    protected static final String Event_OnCallDisconnect = "cn.com.geartech.cellphone.event_oncalldisconnect";

    public static final String Event_RecordingResult = "cn.com.geartech.gtplatform.cellphone.event_recordingresult";
    public static String PARAM_AUDIO_RECORD_PATH = "cn.com.geartech.gtplatform.cellphone.audio_record_path";
    public static String PARAM_AUDIO_RECORD_RESULT = "cn.com.geartech.gtplatform.cellphone.audio_record_result";
    public static String PARAM_AUDIO_RECORD_ERROR_CODE = "cn.com.geartech.gtplatform.cellphone.audio_record_error_code";
    public static String PARAM_AUDIO_RECORD_ERROR_MESSAGE = "cn.com.geartech.gtplatform.cellphone.audio_record_error_message";

    public final static int AUDIO_RECORD_ERROR_CODE_NOT_IN_CALL = -1;
    public final static int AUDIO_RECORD_ERROR_CODE_DISK_ERROR = -2;
    public final static int AUDIO_RECORD_ERROR_CODE_ALREADY_RECORDING =  -3;
    public final static int AUDIO_RECORD_ERROR_CODE_UNKNOWN = -99;

    static final String GT_MSG_SET_ALL_SUB_ID_DEFAULT_NETWORK_MODE = "msg_set_all_default_network_mode";

    static final String EVENT_GT_CELL_SIGNAL_CHANGED = "cn.com.geartech.cell_signal_changed";

    static final String EVENT_GT_CELL_MMI_EVENT_FINISHED = "cn.com.geartech.mmi_event_finished";
    static final String PARAM_INT_MMI_SUCCESSED = "cn.com.geartech.param_mmi_success";
    static final String PARAM_INT_MMI_MESSAGE = "cn.com.geartech.param_mmi_message";
    static final String PARAM_INT_MMI_DIALED_NUM = "cn.com.geartech.param_mmi_dialed_num";
    public static CellPhoneManager getInstance()
    {
        if(instance == null)
        {
            instance = new CellPhoneManager();
        }
        return instance;
    }


    protected CellPhoneManager()
    {

    }

    protected void init(Context c)
    {
        context = c;

        IntentFilter intentFilter = new IntentFilter(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED);
        intentFilter.addAction(Event_OnIncomingCall);
        intentFilter.addAction(Event_OnCallDisconnect);
        intentFilter.addAction(Event_OnCallStateChanged);
        intentFilter.addAction(Event_RecordingResult);
        intentFilter.addAction(EVENT_GT_CELL_SIGNAL_CHANGED);
        intentFilter.addAction(EVENT_GT_CELL_MMI_EVENT_FINISHED);

        context.registerReceiver(mReceiver, intentFilter);
    }


    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(!useCellPhoneService)
            {
                return;
            }

            if(intent.getAction().equals(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED))
            {
                if(useCellPhoneService)
                {
                    // reconnect
                    connectAIDL();
                }
            }
            else if(intent.getAction().equals(Event_OnIncomingCall))
            {
                String number = intent.getStringExtra("number");
                String callId = intent.getStringExtra("callId");

                handleIncomingCall(number, callId);
            }
            else if(intent.getAction().equals(Event_OnCallDisconnect))
            {
                String callId = intent.getStringExtra("callId");
                for (CellPhoneEventListener listener:listeners)
                {
                    listener.onCallTerminated(callId);
                }
                if(isRecording()){
                    stopRecording();
                }
            }
            else if(intent.getAction().equals(Event_OnCallStateChanged))
            {
                int callState = intent.getIntExtra("state",-1);
                String callId = intent.getStringExtra("callId");

                for (CellPhoneEventListener listener:listeners)
                {
                    listener.onCallStateChanged(callState, callId);
                }
            }else if(intent.getAction().equals(Event_RecordingResult)){

                boolean recordSuccess = intent.getBooleanExtra(PARAM_AUDIO_RECORD_RESULT, false);
                try {
                    AudioRecordListener listener = recordListener;
                    if(listener != null)
                    {
                        if(!recordSuccess)
                        {
                            int errorCode = intent.getIntExtra(PARAM_AUDIO_RECORD_ERROR_CODE, PhoneAPI.AUDIO_RECORD_ERROR_CODE_UNKNOWN);
                            String errorMessage = intent.getStringExtra(PARAM_AUDIO_RECORD_ERROR_MESSAGE);
                            listener.onRecordFail(errorCode, errorMessage);
                        }
                        else
                        {
                            String audioPath = intent.getStringExtra(PARAM_AUDIO_RECORD_PATH);
                            listener.onRecordSuccess(audioPath);
                        }

                        listener = null;
                    }
                    else
                    {
                        Log.d("GcordSDK", "listener is null");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }else if(intent.getAction().equals(EVENT_GT_CELL_SIGNAL_CHANGED))
            {
                try {
                    if(signalChangeListeners.size() > 0)
                    {
                        ArrayList<CellSignalItem> items = new ArrayList<>();
                        Map<Integer,Map<String, Object>> data = (Map<Integer, Map<String, Object>>) intent.getSerializableExtra("data");
                        for (Integer subId:data.keySet()) {
                            Map<String, Object> signalMap = data.get(subId);

                            if(signalMap != null){
                                CellSignalItem item = new CellSignalItem();

                                item.setDataStateString((String) signalMap.get("dataState"));
                                item.setNetworkTypeName((String)signalMap.get("networkType"));
                                item.setNetworkOperatorName((String)signalMap.get("operator"));

                                try {
                                    item.setLevel((Integer)signalMap.get("level"));
                                }catch (Exception e) {
                                }

                                try {
                                    item.setAsu((Integer) signalMap.get("asu"));
                                }catch (Exception e){
                                }

                                try {
                                    item.setDbm((Integer)signalMap.get("dbm"));
                                }catch (Exception e){
                                }

                                items.add(item);
                            }

                            for (CellSignalChangeListener listener:signalChangeListeners) {
                                if(listener != null) {
                                    listener.onSignalChange(items);
                                }
                            }
                        }

                        //Map<String,Object> data = intent.getParcelableExtra("data");

                    }


                }catch (Exception e)
                {
                    //e.printStackTrace();
                }

            }else if(intent.getAction().equals(EVENT_GT_CELL_MMI_EVENT_FINISHED)) {

                boolean success = intent.getIntExtra(PARAM_INT_MMI_SUCCESSED, -1) == 0;
                String message = intent.getStringExtra(PARAM_INT_MMI_MESSAGE);
                String dialedNum = intent.getStringExtra(PARAM_INT_MMI_DIALED_NUM);

                try {
                    if(cellMMIEventListener != null) {
                        cellMMIEventListener.onCellMMIEventFinished(success, message, dialedNum);
                    }
                }catch (Throwable e) {
                    e.printStackTrace();
                }

            }


        }
    };

    public List<CellSignalItem> getCurrentSignals()
    {
        try {
            ArrayList<CellSignalItem> items = new ArrayList<>();
            Map<Integer,Map<String, Object>> data = GTAidlHandler.getIgtAidlInterface().queryCellSignal();
            for (Integer subId:data.keySet()) {
                Map<String, Object> signalMap = data.get(subId);

                if (signalMap != null) {
                    CellSignalItem item = new CellSignalItem();

                    item.setDataStateString((String) signalMap.get("dataState"));
                    item.setNetworkTypeName((String) signalMap.get("networkType"));
                    item.setNetworkOperatorName((String) signalMap.get("operator"));

                    try {
                        item.setLevel((Integer) signalMap.get("level"));
                    } catch (Exception e) {
                    }

                    try {
                        item.setAsu((Integer) signalMap.get("asu"));
                    } catch (Exception e) {
                    }

                    try {
                        item.setDbm((Integer) signalMap.get("dbm"));
                    } catch (Exception e) {
                    }

                    items.add(item);
                }
            }

            return items;
        }catch (Exception e)
        {
            return null;
        }
    }

    void handleIncomingCall(String number, String callId)
    {
        for (CellPhoneEventListener listener:listeners)
        {
            listener.onIncomingCall(number, callId);
        }
    }

    /**
     * Cell Call事件回调
     */
    public interface CellPhoneEventListener{
        /**
         * cell call来电事件
         * @param phoneNumber 来电号码,可能为空
         * @param callId
         */
        void onIncomingCall(String phoneNumber, String callId);
        void onCallTerminated(String callId);
        void onCallStateChanged(int state, String callId);
    }

    public interface CellSignalChangeListener{
        void onSignalChange(List<CellSignalItem> signalItems);
    }

    public interface CellMMIEventListener {
        void onCellMMIEventFinished(boolean success, String message, String dialedNumber);
    }

    ArrayList<CellPhoneEventListener> listeners = new ArrayList<CellPhoneEventListener>();

    ArrayList<CellSignalChangeListener> signalChangeListeners = new ArrayList<>();


    /**
     * 注册Cell Call事件回调, 跟removeCellPhoneEventListener相对应
     * @param listener
     */
    public void addCellPhoneEventListener(CellPhoneEventListener listener)
    {
        if(!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }

    /**
     * 反注册Cell Call事件回调
     * @param listener
     */
    public void removeCellPhoneEventListener(CellPhoneEventListener listener)
    {
        listeners.remove(listener);
    }


    public void addCellSignalChangeListener(CellSignalChangeListener listener)
    {
        if(!signalChangeListeners.contains(listener))
        {
            signalChangeListeners.add(listener);
        }
    }

    public void removeCellSignalChangeListener(CellSignalChangeListener listener)
    {
        signalChangeListeners.remove(listener);
    }

    CellMMIEventListener cellMMIEventListener = null;
    /**
     * 设置Cell Call MMI事件回调, 不需要了可以setCellMMIEventListener(null)
     * @param listener
     */
    public void setCellMMIEventListener(CellMMIEventListener listener)
    {
        cellMMIEventListener = listener;
    }

    boolean useCellPhoneService = true;

    public void disableCellPhoneService()
    {
        useCellPhoneService = false;
    }


    public void enableCellPhoneService()
    {
        useCellPhoneService = true;

        connectAIDL();
    }

    void connectAIDL()
    {
        try {
            Intent intent = new Intent("cn.com.geartech.gtplatform.cell_phone_service");
            intent.setPackage("cn.com.geartech.gtplatform");
            intent.putExtra("packageName", context.getPackageName());
            boolean ret = context.bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
            if(!ret)
            {
                DebugLog.logE("bind cellPhoneService Failed!" );
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                cellPhoneAidlInterface = ICellPhoneAidlInterface.Stub.asInterface(service);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            cellPhoneAidlInterface = null;
        }
    };

    /**
     * 拨号
     * @param phoneNumber 呼出的号码
     */
    public void makeCall(String phoneNumber)
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                cellPhoneAidlInterface.makeCall(phoneNumber);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 通话中发送dtmf号码
     * @param dtmfNum 发送的号码,只允许0~9*#
     */
    public void dialDTMF(String dtmfNum)
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                cellPhoneAidlInterface.dialDTMF(dtmfNum);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前通话的call id
     * @return callId
     */
    public String getCurrentCallId()
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                return cellPhoneAidlInterface.getCurrentCallId();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断是否处于正在通话的状态,即call是否处于active状态.
     * @return
     */
    public boolean isInCall()
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                return cellPhoneAidlInterface.isInCall();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 接听当前来电
     */
    public void acceptCall()
    {
        acceptCall(getCurrentCallId());
    }

    protected void acceptCall(String callId)
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                cellPhoneAidlInterface.acceptCall(callId);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void endCall(String callId)
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                cellPhoneAidlInterface.endCall(callId);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 挂断当前通话
     */
    public void endCurrentCall()
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                cellPhoneAidlInterface.endCurrentCall();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 拒绝当前来电
     */
    public void rejectCurrentCall()
    {
        rejectCall(getCurrentCallId());
    }

    protected void rejectCall(String callId)
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                cellPhoneAidlInterface.rejectCall(callId);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前通话状态
     * @return 当前通话状态
     */
    public int getCallStatus()
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                return cellPhoneAidlInterface.getCallStatus();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return -1;
    }

    /**
     * 设置通话麦克风静音
     * @param mute
     */
    public void setMute(boolean mute)
    {
        try {
            if(cellPhoneAidlInterface != null)
            {
                cellPhoneAidlInterface.setMute(mute);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取麦克风静音状态
     * @return
     */
    public boolean isMute()
    {
        try {
            return cellPhoneAidlInterface.isMute();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 获取当前通话的对方号码
     * @return
     */
    public String getCurrentCallNumber(){
        try {

            return cellPhoneAidlInterface.getCurrentCallNumber();

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    AudioRecordListener recordListener;

    //WeakReference<AudioRecordListener> recordListenerWeakReference;

    /**
     * 开始通话录音.通话结束会自动停止录音
     * @param listener 录音回调
     */
    public void startRecording(final AudioRecordListener listener)
    {
        try {

            recordListener = listener;
            //this.recordListenerWeakReference = new WeakReference<AudioRecordListener>(listener);
            cellPhoneAidlInterface.startRecording();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 开始录音, 格式为wav
     * @param listener 录音回调
     */
    public void startRecordingWav(final AudioRecordListener listener)
    {
        try {

            recordListener = listener;
            //this.recordListenerWeakReference = new WeakReference<AudioRecordListener>(listener);
            cellPhoneAidlInterface.startRecordingWav();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startRecordingMP3(final AudioRecordListener listener)
    {
        try {

            recordListener = listener;
            //this.recordListenerWeakReference = new WeakReference<AudioRecordListener>(listener);
            cellPhoneAidlInterface.startRecordingMp3();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通话录音，录音格式是mp3
     * @param listener 录音回调
     * @param sampleRate mp3文件的采样率，例如44100，8000等
     * @param bitRate mp3文件的比特率，例如32（表示32k的比特率）
     */
    public void startRecordingMP3(final AudioRecordListener listener, int sampleRate, int bitRate)
    {
        try {

            recordListener = listener;
            //this.recordListenerWeakReference = new WeakReference<AudioRecordListener>(listener);
            cellPhoneAidlInterface.startRecordingMp3WithParams(sampleRate,bitRate);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 结束通话录音
     */
    public void stopRecording()
    {
        try {

            cellPhoneAidlInterface.stopRecording();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 判断是否正在录音
     * @return
     */
    public boolean isRecording(){
        try {

            return cellPhoneAidlInterface.isRecording();

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 通话录音回调
     */
    public interface AudioRecordListener{
        /**
         * 录音成功
         * @param audioPath 录音文件的路径
         */
        void onRecordSuccess(String audioPath);

        /**
         * 录音失败
         * @param errorCode 错误码
         * @param errorMessage 错误信息
         */
        void onRecordFail(int errorCode, String errorMessage);
    }

    /**
     * 获取当前cell call通话的音量
     * @return
     */
    public int getCurrentInCallVolume(){
        try {

            return cellPhoneAidlInterface.getCurrentInCallVolume();

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取cell call通话的最大音量
     * @return
     */
    public int getMaxInCallVolume(){
        try {

            return cellPhoneAidlInterface.getMaxInCallVolume();

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 设置通话音量
     * @param volume 0-最大通话音量
     */
    public void setInCallVolume(int volume){
        try {

            cellPhoneAidlInterface.setInCallVolume(volume);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 强制切换至手柄通话
     */
    public void setHandleOn()
    {
        try {
            cellPhoneAidlInterface.setHandleOn(new ICommonCallback.Stub() {
                @Override
                public void onComplete(boolean ret, int num, int num2, String s, String s1) throws RemoteException {

                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 强制切换至免提通话
     */
    public void setHandsFreeOn()
    {
        try {
            cellPhoneAidlInterface.setHandsFreeOn(new ICommonCallback.Stub() {
                @Override
                public void onComplete(boolean ret, int num, int num2, String s, String s1) throws RemoteException {

                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 判断通话是否处于免提状态.这个接口只有在call的状态变成active之后才能正确获取得到
     * @return
     */
    public boolean isHandsFreeOn()
    {
        try {
            return cellPhoneAidlInterface.isHandsFreeOn();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断sim卡是否已经被识别
     * @return
     */
    public boolean isSimCardAvailable()
    {
        try {
            return cellPhoneAidlInterface.isSIMCardAvailable();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


    public void setBTHeadsetOn()
    {
        try {
            cellPhoneAidlInterface.setBTOn(new ICommonCallback.Stub() {
                @Override
                public void onComplete(boolean ret, int num, int num2, String s, String s1) throws RemoteException {

                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean isBTHeadsetOn()
    {
        try {
            return cellPhoneAidlInterface.isBTOn();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBTHeadsetConnected()
    {
        try {
            return cellPhoneAidlInterface.isBTHeadsetConnected();
        }catch (Exception e)
        {

        }
        return false;
    }

    /**
     *
     * @param mode: 1 for 2g, 0 for 3g , 9 for 4g
     */
    public void setDefaultNetworkModeforAllSubId(int mode)
    {
        try {
            GTAidlHandler.getIgtAidlInterface().sendMessage(GT_MSG_SET_ALL_SUB_ID_DEFAULT_NETWORK_MODE, mode, 0,
                    context.getPackageName(),"");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected boolean resumeCallStatus(CallStatusItem callStatusItem)
    {
        if(callStatusItem == null)
        {
            return false;
        }

        if(callStatusItem.isIncomingCall() && callStatusItem.getCallActiveTimeStamp() == 0 && !isInCall()  )
        {
            for (CellPhoneEventListener cellPhoneEventListener:listeners)
            {
                if(cellPhoneEventListener != null)
                {
                    cellPhoneEventListener.onIncomingCall(callStatusItem.getOpponentNumber(), callStatusItem.getCallId());
                }
            }

            return false;
        }

        return true;
    }
}
