package cn.com.geartech.gcordsdk;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import cn.com.geartech.app.GcordAccountAIDL;
import cn.com.geartech.app.GcordAccountCallBack;
import cn.com.geartech.gtplatform.model.aidl.IGTAidlInterface;

/**
 * Created by cuizhy on 17/9/5.
 */

public class GcordLoginHandler extends GcordHelper {
    public static final String ACTION_GCORD_ACCOUNT_SERVICE = "cn.com.geartech.gcordsdk.gcordaccount.service";
    protected static final String GCORD_KEY_INT_GCORD_LOGIN_STATUS = "gcord_login_status";
    protected static final String GCORD_KEY_STR_GCORD_LOGIN_MASTER_APP = "gcord_login_master_app";
    protected static final String GT_MSG_GCORD_LOGIN_STATUS_CHANGE = "gocrd_login_status_change";

    private int gcordLoginStatus = GCORD_LOGIN_STATUS_OFFLINE;

    public static final int GCORD_LOGIN_STATUS_ON_LINE = 1;
    public static final int GCORD_LOGIN_STATUS_EXPIRED = 2;
    public static final int GCORD_LOGIN_STATUS_OFFLINE = 3;
    public static final String CN_COM_GEARTECH_APP_RECONNECT_CENTRAL_SERVICE_AIDL = "cn.com.geartech.app.reconnect_central_service_aidl";

    private String packageName = null;


    private IGTAidlInterface igtAidlInterface = null;
    private static GcordLoginHandler instance = null;
    private Application application = null;
    private GcordAccountAIDL gcordAccountAIDL = null;
    private Handler handler = null;

    private GcordAccountCallBack.Stub callBack = new GcordAccountCallBack.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onGcordLoginStatusChanged(int status) throws RemoteException {
            Log.w("GcordLoginHandler", "gcord login status change,status=" + status);
            setGcordLoginStatus(status);
        }
    };


    private GcordLoginHandler() {

    }

    protected boolean connectAIDL() {
        Intent intent = new Intent(ACTION_GCORD_ACCOUNT_SERVICE);
        intent.setPackage("cn.com.geartech.app");
        intent.putExtra("packageName", application.getPackageName());
        return application.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                gcordAccountAIDL = GcordAccountAIDL.Stub.asInterface(service);
                service.linkToDeath(deathRecipient, 0);
                if (gcordAccountAIDL != null) {
                    gcordAccountAIDL.registerCallBack(packageName, callBack);
                    gcordLoginStatus = gcordAccountAIDL.getGcordLoginStatus(packageName);
                    Log.e("GcordLoginHandler", "gcordui service connected");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            gcordAccountAIDL = null;
            Log.e("GcordLoginHandler", "gcordui service disconnected");
            restartAidl(1000);
        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.w("GcordLoginHandler", "GcordUIDataManager aidl is dead");
            gcordAccountAIDL = null;
            restartAidl(1000);
        }
    };

    protected void restartAidl(long milliseconds) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gcordAccountAIDL == null) {
                    connectAIDL();
                }
            }
        }, milliseconds);
    }

    protected void init(Application application) {
        this.application = application;
        packageName = application.getPackageName();
        handler = new Handler();
        if (!"cn.com.geartech.app".equals(packageName)) {
            boolean result = connectAIDL();
            IntentFilter intentFilter = new IntentFilter(CN_COM_GEARTECH_APP_RECONNECT_CENTRAL_SERVICE_AIDL);
            application.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (CN_COM_GEARTECH_APP_RECONNECT_CENTRAL_SERVICE_AIDL.equals(action)) {
                        restartAidl(1000L);
                    }
                }
            }, intentFilter);
            if (result) {
                if (internalInitCallback != null) {
                    internalInitCallback.onInitFinished();
                }
            } else {
                if (internalInitCallback != null) {
                    internalInitCallback.onInitFailed();
                }
            }
        }else
        {
            if(internalInitCallback != null) {
                internalInitCallback.onInitFinished();
            }
        }
    }

    protected static synchronized GcordLoginHandler getInstance() {
        if (null == instance) {
            instance = new GcordLoginHandler();
        }
        return instance;
    }

    protected void setIgtAidlInterface(IGTAidlInterface igtAidlInterface) {
        this.igtAidlInterface = igtAidlInterface;
    }

    public int queryGcordLoginStatus() {
        return gcordLoginStatus;
    }

    public String queryMasterApp() {
        if (packageName == null) {
            packageName = application.getPackageName();
        }
        return packageName;
    }

    public void pushGcordLoginStatus(int status, String thirdPartPacakgeName) {
        if (TextUtils.isEmpty(thirdPartPacakgeName) || thirdPartPacakgeName.trim().length() <= 0) {
            thirdPartPacakgeName = packageName;
        }
        this.gcordLoginStatus = status;
        if (this.igtAidlInterface != null) {
            try {
                igtAidlInterface.sendMessage(GT_MSG_GCORD_LOGIN_STATUS_CHANGE, gcordLoginStatus, 0, packageName, thirdPartPacakgeName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getGcordToken() {
        if (gcordAccountAIDL != null) {
            try {
                return gcordAccountAIDL.getGcordAccountToken(packageName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }

    public void setGcordLoginStatus(int status) {
        this.gcordLoginStatus = status;
    }


}
