package cn.com.geartech.gcordsdk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;


import cn.com.geartech.gtplatform.model.calllog.SimpleCallLogManager;

/**
 * Created by kya on 15/4/22.
 */
public class GcordSDK {

    private GcordSDK() {

    }

    public static final String GCORD_META_DATA_KEY_NO_NAVIGATION_BAR_WHEN_LAUNCHED = "cn.com.geartech.gcordsdk.no_navigation_bar_when_launched";

    @Deprecated
    public enum Phone_Type {
        PSTN, // 0
        CELL_PHONE, // 1
        SIP, // 2
        ALL // 3
    }

    GcordSDKInitCallback initCallback;

    private Application mContext;

    Phone_Type currentPhoneType = Phone_Type.PSTN;

    private static GcordSDK _instance = null;

    /**
     * 获取GcordSDK的实例
     *
     * @return GcordSDK的实例
     */
    public static GcordSDK getInstance() {

        if (_instance == null) {
            _instance = new GcordSDK();
        }

        return _instance;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 设置初始化成功的回调
     *
     * @param initCallback 初始化成功的回调函数
     */
    public void setInitCallback(GcordSDKInitCallback initCallback) {
        this.initCallback = initCallback;
    }

    protected interface InternalInitCallback {
        void onInitFinished();

        void onInitFailed();
    }

    //    public void initSDK(Application context, String platformAssetPath)
//    {
//        initSDK(context);
//
//        UpgradeManager.installFromAssetIfNeeded(platformAssetPath, context);
//    }
    GTCapability capability = new GTCapability();

    /**
     * 获取Gcord电话机的功能
     *
     * @return 电话机功能列表
     * @see GTCapability
     */
    public GTCapability getCapability() {
        return capability;
    }

    /**
     * @param context  Application实例
     * @param devId    开发者ID,需要网上申请
     * @param appToken appToken,跟packageName绑定,需要网上申请
     */
    public void initSDK(Application context, String devId, String appToken) {
        DebugLog.logE("setup sdk!");

        mContext = context;

        String packageName = context.getPackageName();

        registerInitCallback(PSTNInternal.getInstance());
        registerInitCallback(GTAidlHandler.getInstance());
        registerInitCallback(HomeKeyManager.getInstance());
        //registerInitCallback(BTManager.getInstance());
        registerInitCallback(MdmManager.getInstance());
        registerInitCallback(GcordLoginHandler.getInstance());
        registerInitCallback(MTKBTManager.getInstance());



        phoneAPIInstance = PhoneAPI.getInstance();
        sipPhoneManager = SipPhoneManager.getInstance();


        registerInitCallback(sipPhoneManager);


        phoneAPIInstance.init(context, devId, appToken);
        sipPhoneManager.init(context);
        MTKBTManager.getInstance().init(context);


        GTAidlHandler.getInstance().setContext(context);

        SettingAPI.getInstance().init(context);
        UpgradeManager.getInstance().init(context);
        ReportManager.getInstance().setContext(context);
        PowerManager.getInstance().init(context);
        PrinterManager.getInstance().init(context);
        HomeKeyManager.getInstance().init(context);
        CellPhoneManager.getInstance().init(context);
        HandleManager.getInstance().init(context);
        //BTManager.getInstance().init(context);
        GcordSystemUIManager.getInstance().init(context);

        MdmManager.getInstance().init(context);

        doSetPhoneType(getPhoneType());

        SimpleCallLogManager.getInstance().init(context);

        CallLogManager.getInstance().init(context);
        AreaCodeManager.getInstance().init(context);

        UnifiedPhoneController.getInstance().init(context);
        GcordLoginHandler.getInstance().init(context);
        wallPaperManager = WallPaperManager.getInstance();
        wallPaperManager.init(context);

        blackListManager = BlackListManager.getInstance();
        registerInitCallback(blackListManager);
        blackListManager.init(context);

        contactManager.init(context);
        GcordUtil.registerEthernetStateChangeReceiver(context);

        EthernetManager.getInstance().init(context);


        SystemContactUtil.setContext(context);

        preference = GcordPreference.getInstance();
        registerInitCallback(preference);
        preference.init(context);


    }
    static final String LAUNCHER_PACKAGE_NAME = "cn.com.geartech.app";

    public void release() {
        PSTNInternal.getInstance().finish();
    }


    PhoneAPI phoneAPIInstance;
    SipPhoneManager sipPhoneManager = null;
    WallPaperManager wallPaperManager = null;
    BlackListManager blackListManager = null;

    /**
     * 获取PhoneAPI实例,用于处理固话业务
     *
     * @return PhoneAPI实例
     */
    public PhoneAPI getPhoneAPI() {
        return phoneAPIInstance;
    }

//    public void chenZongDianDo(){
//
//        //Log.e("chenZong", " chenZong will handle everything!");
//    }

    /**
     * 获取SettingAPI实例
     *
     * @return SettingAPI实例
     * @see PhoneAPI
     */
    public SettingAPI getSettingAPI() {
        return SettingAPI.getInstance();
    }

    /**
     * 获取UpgradeManager实例
     *
     * @return UpgradeManager实例
     * @see UpgradeManager
     */
    public UpgradeManager getUpgradeManager() {
        return UpgradeManager.getInstance();
    }


    public ReportManager getReportManager() {
        return ReportManager.getInstance();
    }

    public PowerManager getPowerManager() {
        return PowerManager.getInstance();
    }

    /**
     * 获取PrinterManager实例,用于处理打印机业务
     *
     * @return PrinterManager实例
     * @see PrinterManager
     */
    public PrinterManager getPrinterManager() {
        return PrinterManager.getInstance();
    }

    /**
     * 获取HomeKeyManager实例,用于处理Home键事件及设置Home键行为
     *
     * @return HomeKeyManager实例
     * @see HomeKeyManager
     */
    public HomeKeyManager getHomeKeyManager() {
        return HomeKeyManager.getInstance();
    }

    /**
     * 获取CellPhoneManager实例,用于处理CellPhone的电话事件
     *
     * @return CellPhoneManager实例
     * @see CellPhoneManager
     */
    public CellPhoneManager getCellPhoneManager() {
        return CellPhoneManager.getInstance();
    }

    /**
     * 获取HandleManager实例,用于处理手柄事件
     *
     * @return HandleManager实例
     * @see HandleManager
     */
    public HandleManager getHandleManager() {
        return HandleManager.getInstance();
    }

    //    /**
//     * 处理蓝牙相关事件
//     */
    public BTManager getBTManager() {
        return BTManager.getInstance();
    }

    public MdmManager getMdmManager() {
        return MdmManager.getInstance();
    }

    public GcordLoginHandler getGcordLoginHandler() {
        return GcordLoginHandler.getInstance();
    }

    ContactManager contactManager = new ContactManager();

    public ContactManager getContactManager() {
        return contactManager;
    }

    public WallPaperManager getWallPaperManager() {
        return wallPaperManager;
    }

    public BlackListManager getBlackListManager() {
        return blackListManager;
    }

    public GcordSystemUIManager getGcordSystemUIManager() {
        return GcordSystemUIManager.getInstance();
    }

    public UnifiedPhoneController getUnifiedPhoneController() {
        return UnifiedPhoneController.getInstance();
    }

    public MTKBTManager getMtkBtManager() {
        return MTKBTManager.getInstance();
    }

    @Deprecated
    public void setPhoneType(Phone_Type type) {
        currentPhoneType = type;

        try {
            Settings.System.putInt(mContext.getContentResolver(), SYS_PREF_PHONE_TYPE, currentPhoneType.ordinal());

        } catch (Exception e) {
            e.printStackTrace();
        }

        doSetPhoneType(type);
    }

    void doSetPhoneType(Phone_Type type) {
        switch (type) {
            case PSTN:
                //PSTNInternal.getInstance().setEnablePSTN(true);
                CellPhoneManager.getInstance().disableCellPhoneService();
                break;
            case CELL_PHONE:
                // PSTNInternal.getInstance().setEnablePSTN(false);
                CellPhoneManager.getInstance().enableCellPhoneService();
                break;
            case ALL:
                CellPhoneManager.getInstance().enableCellPhoneService();
                break;
        }
    }

    static final String SYS_PREF_PHONE_TYPE = "cn.com.geartech.sys_pref_phone_type";

    @Deprecated
    public Phone_Type getPhoneType() {
        try {
            int type = Settings.System.getInt(mContext.getContentResolver(), SYS_PREF_PHONE_TYPE, 0);
            if (type == 1) {
                currentPhoneType = Phone_Type.CELL_PHONE;
            } else if (type == 2) {
                currentPhoneType = Phone_Type.SIP;
            } else if (type == 3) {
                currentPhoneType = Phone_Type.ALL;
            } else {
                currentPhoneType = Phone_Type.PSTN;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  Log.e("eee","current status:" + currentPhoneType);

        return currentPhoneType;
    }

    public SipPhoneManager getSipPhoneManager() {
        return sipPhoneManager;
    }

    int aidlWaitingCount = 0;

    Handler handler = new Handler(Looper.getMainLooper());

    protected void registerInitCallback(final GcordHelper helper) {
        aidlWaitingCount++;

        //Log.e("GcordSDK","count:" +aidlWaitingCount);

        helper.setInitCallback(new InternalInitCallback() {
            @Override
            public void onInitFinished() {
                aidlWaitingCount--;

                //Log.e("GcordSDK","count:" +aidlWaitingCount);

                helper.setInitCallback(null);

                if (aidlWaitingCount == 0) {
                    notifyInitSuccess();
                }
            }

            @Override
            public void onInitFailed() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        aidlWaitingCount--;

                        //Log.e("GcordSDK","count:" +aidlWaitingCount);

                        helper.setInitCallback(null);

                        if (aidlWaitingCount == 0) {
                            notifyInitSuccess();
                        }
                    }
                });
            }
        });
    }

    void notifyInitSuccess() {
        if (initCallback != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initCallback.onInitFinished();
                }
            }, 500);

        }

        PowerManager.getInstance().checkScreenOnByPeriod();

        UnifiedPhoneController.getInstance().onSDKInited();
    }

    public CallLogManager getCallLogManager() {
        return CallLogManager.getInstance();
    }

    public AreaCodeManager getAreaCodeManager() {
        return AreaCodeManager.getInstance();
    }

    GcordPreference preference;

    public GcordPreference getGcordPreference() {
        return preference;
    }

    RingToneManager ringToneManager = new RingToneManager();

    public RingToneManager getRingToneManager() {
        return ringToneManager;
    }

    @SuppressWarnings("unused")
    public EthernetManager getEthernetManager() {
        return EthernetManager.getInstance();
    }

    public SimCardManager getSimCardManager() {
        return SimCardManager.getInstance();
    }

    public GcordWifiManager getWifiManager() {
        return GcordWifiManager.getInstance();
    }
}
