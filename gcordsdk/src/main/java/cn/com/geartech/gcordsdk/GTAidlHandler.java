package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.geartech.gcordsdk.dataType.CallStatusItem;
import cn.com.geartech.gtplatform.model.aidl.ICommonCallback;
import cn.com.geartech.gtplatform.model.aidl.IGTAidlCallback;
import cn.com.geartech.gtplatform.model.aidl.IGTAidlInterface;

/**
 * Created by pangyuning on 15/7/16.
 */
public class GTAidlHandler extends GcordHelper {


    /**
     * param int0 = 1 : enable
     * param str0: packageName
     */
    static final String GT_MSG_SET_USE_UNIFY_CALL = "msg_set_use_unify_call";

    static final String SDK_PARAM_INT_UNIFY_CALL_CURRENT_DIAL_TYPE = "sdk_param_int_unify_call_current_dial_type";

    static final String SDK_PARAM_INT_SUPPORT_CALL_SCENE_RECOVERY = "sdk_param_support_call_scene_recovery";

    static final String GT_KEY_BEGIN_ANDROID_TO_PSTN_AUDIO_OUTPUT = "gt_key_begin_android_to_pstn_audio_output";

    static final String GT_KEY_END_ANDROID_TO_PSTN_AUDIO_OUTPUT = "gt_key_end_android_to_pstn_audio_output";

    static IGTAidlInterface igtAidlInterface;

    static final String GT_MSG_SET_ENABLE_WHITE_APP_LIST = "msg_enable_white_list";

    static final String GT_MSG_ADD_WHITE_LIST_APP = "msg_add_white_list_app";

    static final String GT_MSG_SET_GREEN_LIGHT_BRIGHTNESS = "gt_msg_set_green_light_brightness";

    private final static String ACTION_PSTN_VERSION_UPDATED = "cn.com.geartech.action.PSTN_VERSION_UPDATED";

    Context context;

    Handler handler = new Handler();

    protected void setContext(Context c)
    {
        context = c;
        initAIDL();
    }

    private GTAidlHandler()
    {}


    int handlePickUpType = 0;

    static GTAidlHandler instance;


    public interface ParamCallback{
        int onQueryInt(String key);
        String onQueryStr(String key);
        int onQueryInt1(String key, String param);

        default void onEvent(String eventType, Bundle data) {

        }
    }

    ParamCallback paramCallback;

    protected void setParamCallback(ParamCallback callback)
    {
        paramCallback = callback;
    }

    protected static GTAidlHandler getInstance()
    {
        if(instance == null)
        {
            instance = new GTAidlHandler();
        }
        return instance;
    }

    protected void initAIDL()
    {
        DebugLog.logE("begin to bind!");

        Intent intent = new Intent("cn.com.geartech.gtplatform.model.aidl.IGTAidlInterface");
        intent.setPackage("cn.com.geartech.gtplatform");
        intent.putExtra("packageName", context.getPackageName());

        boolean ret = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if(!ret)
        {
            if(internalInitCallback != null)
            {
                internalInitCallback.onInitFailed();
            }
        }
        IntentFilter intentFilter = new IntentFilter(ACTION_PSTN_VERSION_UPDATED);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(ACTION_PSTN_VERSION_UPDATED.equals(intent.getAction())){
                    setupDeviceCapabilities();
                }
            }
        }, intentFilter);
    }

    protected void setupDeviceCapabilities(){
        try {
            GcordSDK.getInstance().getCapability().setCapabilityMap(igtAidlInterface.getDeviceCapabilities());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DebugLog.logE("AIDL connected");

            IGTAidlInterface gtInterface = IGTAidlInterface.Stub.asInterface(iBinder);
            GTAidlHandler.setInterface(gtInterface);

            try {
                GTAidlHandler.getIgtAidlInterface().registerAidlCallback(context.getPackageName(), 0, aidlCallback);

                notifyForegroundState(SettingAPI.getInstance().isAppInForeground());

                setupDeviceCapabilities();
//                gtInterface.sendMessage(GT_MSG_SET_USE_UNIFY_CALL, 0 , 0, context.getPackageName(), "");
                //check current call state
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
                        UnifiedPhoneController.getInstance().onResumeInCallStatus(callStatusItem);
                    }
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            if(internalInitCallback != null)
            {
                internalInitCallback.onInitFinished();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            DebugLog.logE("AIDL disconnected");


            if(downloadPackageCallback != null)
            {
                downloadPackageCallback.onDownloadFailed("LOCAL_SERVICE_DISCONNECTED");
                downloadPackageCallback = null;
            }

            GTAidlHandler.setInterface(null);
        }
    };

    IGTAidlCallback.Stub aidlCallback = new IGTAidlCallback.Stub() {
        @Override
        public void onPushMessage(String message) throws RemoteException {
            if(pushEvent != null)
            {
                pushEvent.onPushMessage(message);
            }
        }

        @Override
        public void onEvent(String eventType, Bundle bundle) throws RemoteException {
            if(paramCallback != null)
            {
                paramCallback.onEvent(eventType, bundle);
            }
        }

        @Override
        public void onCheckUpgradeResult(boolean success, boolean needUpdate, String newVersionName, String upgradeInfo, String packageSHA1,
                                         String errorMessage, int flags, List<String> v1, List<String> v2) throws RemoteException {
            if(checkUpgradeCallback != null)
            {
                checkUpgradeCallback.onCheckUpgradeResult(success, needUpdate, newVersionName, upgradeInfo,
                        packageSHA1, errorMessage, flags, null);
            }
        }

        @Override
        public void onDowndloadComplete(boolean success, String errorMessage) throws RemoteException {
          //  Log.e("DebugUtil", "download success:" + success + " " + errorMessage);
            if(downloadPackageCallback != null)
            {
                if(success)
                    downloadPackageCallback.onDownloadSuccess();
                else
                    downloadPackageCallback.onDownloadFailed(errorMessage);

                downloadPackageCallback = null;
            }
        }

        @Override
        public void onDownloadProgressChange(int progress) throws RemoteException {
           // Log.e("DebugUtil", "progress change:" + progress);
            if(downloadPackageCallback != null)
            {
                downloadPackageCallback.onDownloadProgressChange(progress);
            }
        }

        @Override
        public void onInstallComplete(boolean success, String errorMessage) throws RemoteException {
            if(installCallback != null)
            {
                if(success)
                {
                    installCallback.onInstallComplete();
                }
                else
                {
                    installCallback.onInstallFailed(errorMessage);
                }
                installCallback = null;
            }
        }

        @Override
        public int fetchDogBell(String packageName) throws RemoteException {

            if(packageName != null && packageName.equals(context.getPackageName()))
            {
                return android.os.Process.myPid();
            }

            return 0;
        }

        @Override
        public int getHandlePickUpType() throws RemoteException {

            Log.d("GcordSDK","get handle pickup type:" + handlePickUpType);

            return handlePickUpType;
        }

        @Override
        public String queryContactData(String number) throws RemoteException {
            ContactManager.Check3rdContactDataListener listener = GcordSDK.getInstance().getContactManager().getCheck3rdContactDataListener();
            if(listener != null)
            {
                return listener.onQueryContactData(number);
            }
            else
            {
                return null;
            }
        }

        @Override
        public void onScreenStrategyChange(int strategy, int param1, int param2) throws RemoteException {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    PowerManager.getInstance().checkScreenOnByPeriod();
                }
            });
        }

        @Override
        public int queryInt(String key) throws RemoteException {

            if(key == null || key.length() == 0)
            {
                return -1;
            }

            if(key.equals(SDK_PARAM_INT_UNIFY_CALL_CURRENT_DIAL_TYPE))
            {
                UnifiedPhoneController.DIAL_MODE dial_mode = UnifiedPhoneController.getInstance().checkDialMode();
                if(dial_mode == UnifiedPhoneController.DIAL_MODE.PSTN)
                {
                    return 1;
                }
                else if(dial_mode == UnifiedPhoneController.DIAL_MODE.CELL)
                {
                    return 2;
                }
                else if(dial_mode == UnifiedPhoneController.DIAL_MODE.SIP)
                {
                    return 3;
                }
            } else if (GcordLoginHandler.GCORD_KEY_INT_GCORD_LOGIN_STATUS.equals(key)) {
                return GcordLoginHandler.getInstance().queryGcordLoginStatus();
            }
            else if(SDK_PARAM_INT_SUPPORT_CALL_SCENE_RECOVERY.equals(key))
            {
                return 1;
            }else if(PSTNInternal.SETTING_AUTO_HANG_OFF_WHEN_BUSY_TONE.equals(key)) {

                return PSTNInternal.getInstance().isAutoHangOffWhenBusyTone() ? 0 : -1;
            }

            if(paramCallback != null)
            {
                return paramCallback.onQueryInt(key);
            }


            return 0;
        }

        @Override
        public String queryString(String key) throws RemoteException {
            if (GcordLoginHandler.GCORD_KEY_STR_GCORD_LOGIN_MASTER_APP.equals(key)) {
                return GcordLoginHandler.getInstance().queryMasterApp();
            }

            if(paramCallback != null)
            {
                return paramCallback.onQueryStr(key);
            }

            return "";
        }

        @Override
        public int quertInt1(String key, String param) throws RemoteException {
            try {
                if(paramCallback != null)
                {
                    return paramCallback.onQueryInt1(key,param);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            return 0;
        }
    };

    UpgradeManager.InstallCallback installCallback;

    protected void setHandlePickUpType(int type)
    {
        handlePickUpType = type;
    }

    public int getHandlePickUpType() {
        return handlePickUpType;
    }

    protected void setInstallCallback(UpgradeManager.InstallCallback cb)
    {
        installCallback = cb;
    }

    protected static void setInterface(IGTAidlInterface i)
    {
        igtAidlInterface = i;
        GcordLoginHandler.getInstance().setIgtAidlInterface(i);
    }

    protected static IGTAidlInterface getIgtAidlInterface()
    {
        return igtAidlInterface;
    }

    GTPushEvent pushEvent;

    protected void registerPushListener(GTPushEvent event)
    {
        pushEvent = event;
    }


    public static interface CheckUpgradeCallback{
        public abstract void onCheckUpgradeResult(boolean success, boolean needUpdate, String newVersionName, String upgradeInfo, String packageSHA1,
                                                  String errorMessage, int flag, HashMap<String, String> v1);
    }

    CheckUpgradeCallback checkUpgradeCallback;

    protected void setCheckUpgradeCallback(CheckUpgradeCallback callback)
    {
        checkUpgradeCallback = callback;
    }

    protected boolean hasCheckUpgradeCallback()
    {
        return checkUpgradeCallback != null;
    }

    protected void checkUpgrade()
    {
        String packageName = context.getPackageName();
        try {
            getIgtAidlInterface().checkUpgrade(packageName);
           // getIgtAidlInterface().checkUpgrade("com.smartque");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static interface DownloadPackageCallback{
        abstract void onDownloadSuccess();
        abstract void onDownloadFailed(String errorMessage);
        abstract void onDownloadProgressChange(int percent);
    }

    protected void installPackage(String sha1)
    {
        String packageName = context.getPackageName();
        try {
            getIgtAidlInterface().downloadAndInstall(packageName, sha1);
            // getIgtAidlInterface().checkUpgrade("com.smartque");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    DownloadPackageCallback downloadPackageCallback;

    protected void setDownloadPackageCallback(DownloadPackageCallback callback)
    {
        downloadPackageCallback = callback;
    }

    protected void updateInstInterval(int interval)
    {
        try {
            getIgtAidlInterface().updateInstInterval(interval);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void startFMBroadcasting(int interval)
    {
        try {
            getIgtAidlInterface().startFMBroadcasting(interval);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void stopFMBroadcasting()
    {
        try {
            getIgtAidlInterface().stopFMBroadcasting();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected boolean setEnableWhiteList(boolean enableWhiteAppList)
    {
        try {
            if(enableWhiteAppList)
            {
                return getIgtAidlInterface().sendMessage(GT_MSG_SET_ENABLE_WHITE_APP_LIST, 1,0,context.getPackageName(),"") == 1;
            }
            else
            {
                return getIgtAidlInterface().sendMessage(GT_MSG_SET_ENABLE_WHITE_APP_LIST, 0,0,context.getPackageName(),"") == 1;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean addWhiteListPackage(String packageName)
    {
        try {
            return getIgtAidlInterface().sendMessage(GT_MSG_ADD_WHITE_LIST_APP, 0, 0, context.getPackageName(),packageName) == 1;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


    protected void fm()
    {
        try {
            getIgtAidlInterface().fm();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void notifyForegroundState(Boolean isForeGround)
    {
        try {
            getIgtAidlInterface().notifyAppForegroundState(context.getPackageName(), isForeGround, android.os.Process.myPid());
        }catch (Exception e)
        {
           // e.printStackTrace();
        }
    }

    protected void beginPSTNAudioOutput()
    {
        try {
            getIgtAidlInterface().sendMessage(GT_KEY_BEGIN_ANDROID_TO_PSTN_AUDIO_OUTPUT, 0, 0, "", "");
        }catch (Exception e)
        {

        }

    }

    protected void stopPSTNAudioOutput()
    {
        try {
            getIgtAidlInterface().sendMessage(GT_KEY_END_ANDROID_TO_PSTN_AUDIO_OUTPUT, 0, 0, "", "");
        }catch (Exception e)
        {

        }
    }

    protected void eventLog(String eventId, String eventName, String param1, String param2, String param3)
    {
        try {
            if(getIgtAidlInterface() != null)
            {
                if(eventId == null)
                {
                    eventId = "";
                }
                if(eventName == null)
                {
                    eventName = "";
                }
                if(param1 == null)
                {
                    param1 = "";
                }
                if(param2 == null)
                {
                    param2 = "";
                }
                if(param3 == null)
                {
                    param3 = "";
                }

                getIgtAidlInterface().eventLog(eventId, eventName, param1, param2, param3);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    protected void pLog(String pLog)
    {
        try {
            if(getIgtAidlInterface() != null)
            {
                if(pLog == null)
                {
                    return;
                }

                getIgtAidlInterface().pLog(pLog);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void logD(String tag, String msg) {
        try {
            getIgtAidlInterface().logD(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void logE(String tag, String msg) {
        try {
            getIgtAidlInterface().logE(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fileLog(String fileName, String tag, String msg) {
        try {
            getIgtAidlInterface().fileLog(fileName, tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void raw(String tag, String msg) {
        try {
            getIgtAidlInterface().raw(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void logic(String tag, String msg) {
        try {
            getIgtAidlInterface().logic(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void biz(String tag, String msg) {
        try {
            getIgtAidlInterface().biz(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void statistics(String tag, String msg) {
        try {
            getIgtAidlInterface().statistics(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Map queryCurrentCallStatus()
    {
        try {
            return getIgtAidlInterface().queryCurrentCallStatus();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    protected boolean setGreenLightBrightness(int brightness){

        int result = -1;
        try {
            result = getIgtAidlInterface().sendMessage(GT_MSG_SET_GREEN_LIGHT_BRIGHTNESS, brightness, 0, null , null);

        }catch (Throwable e){
            e.printStackTrace();
        }

        return  result == 0;
    }

    public void checkNumber(String number, ICommonCallback commonCallback) {
        try {
            getIgtAidlInterface().checkPhoneNumber(number, commonCallback);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
