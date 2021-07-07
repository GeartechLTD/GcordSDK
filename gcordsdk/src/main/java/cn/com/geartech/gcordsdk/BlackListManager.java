package cn.com.geartech.gcordsdk;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import cn.com.geartech.app.IBlackListAIDL;

/**
 * Created by cuizhy on 18/1/31.
 */

public class BlackListManager extends GcordHelper {
    public static final String ACTION_ADD_PHONE_NUMBER_TO_BLACKLIST = "cn.com.geartech.action.add_phone_number_to_blackList";
    public static final String ACTION_REMOVE_PHONE_NUMBER_FROM_BLACKLIST = "cn.com.geartech.action.remove_phone_number_from_blackList";

    public static final String PHONE_NUMBER_EXTRA_KEY = "phoneNumber";

    private static BlackListManager instance = null;
    private IBlackListAIDL iBlackListAIDL = null;

    private BlackListManager() {

    }

    protected void init(Application application) {
        boolean result = connectAidl();
        if (!result) {
            if (internalInitCallback != null) {
                internalInitCallback.onInitFailed();
            }
        }
    }

    public static final String BLACK_LIST_SERVICE = "cn.com.geartech.blacklist.service";

    private boolean connectAidl() {
        try {
            Context context = GcordSDK.getInstance().getContext();
            Intent intent = new Intent(BLACK_LIST_SERVICE);
            intent.setPackage(GcordSDK.LAUNCHER_PACKAGE_NAME);
            intent.putExtra("packageName", context.getPackageName());
            return context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                iBlackListAIDL = IBlackListAIDL.Stub.asInterface(service);
                service.linkToDeath(deathRecipient, 0);
                if (iBlackListAIDL != null) {
                    Log.w("black list manager", "black list service connected");
                }
                if (internalInitCallback != null) {
                    internalInitCallback.onInitFinished();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBlackListAIDL = null;
            Log.e("black list manager", "black list service disconnected");
            restartAidl(1000);
            if (internalInitCallback != null) {
                internalInitCallback.onInitFailed();
            }
        }
    };

    private void restartAidl(long milliseconds) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (iBlackListAIDL == null) {
                    connectAidl();
                }
            }
        }, milliseconds);
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.w("black list manager", "GcordUIDataManager aidl is dead");
            iBlackListAIDL = null;
            restartAidl(1000);
        }
    };

    protected static synchronized BlackListManager getInstance() {
        if (instance == null) {
            instance = new BlackListManager();
        }
        return instance;
    }

    /**
     * 把电话号码加入黑名单
     *
     * @param phoneNumber
     */
    public void addPhoneNumberToBlackList(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().length() <= 0) {
            return;
        }
        if (iBlackListAIDL != null) {
            try {
                iBlackListAIDL.addPhoneNumberToBlackList(phoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断电话号码是否黑名单
     *
     * @param phoneNumber
     * @return
     */
    public boolean isPhoneNumberInBlackList(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().length() <= 0) {
            return false;
        }
        if (iBlackListAIDL != null) {
            try {
                return iBlackListAIDL.isPhoneNumberInBlackList(phoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 把电话号码移出黑名单
     *
     * @param phoneNumber
     */
    public void removePhoneNumberFromBlackList(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().length() <= 0) {
            return;
        }
        if (iBlackListAIDL != null) {
            try {
                iBlackListAIDL.removePhoneNumberFromBlackList(phoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取黑名单上的所有电话号码
     * @return 黑名单上的所有电话号码
     */
    public String[] getBlackList() {
        if (iBlackListAIDL != null) {
            try {
                return iBlackListAIDL.getBlackList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
