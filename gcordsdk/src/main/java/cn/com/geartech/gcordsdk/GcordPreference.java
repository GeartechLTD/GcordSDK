package cn.com.geartech.gcordsdk;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.com.geartech.app.IGcordPreferenceAIDL;
import cn.com.geartech.app.IGcordPreferenceCallBack;
import cn.com.geartech.gcordsdk.dao.GlobalSettingConstants;

/**
 * Created by pangyuning on 17/5/19.
 */

public final class GcordPreference extends GcordHelper {

    public final static String GCORD_PREFERENCE_SERVICE = "cn.com.geartech.action.gcord_preference_service";
    private static final String HIDE_PHONE_NUMBER_KEY = "HIDE_PHONE_NUMBER_KEY";
    private IGcordPreferenceCallBack.Stub callBack;
    private ServiceConnection serviceConnection = null;
    private IGcordPreferenceAIDL iGcordPreferenceAIDL = null;
    private HashSet<String> installedPackages;
    private GcordPreferenceCallBack gcordPreferenceCallBack;

    private GcordPreference() {
        installedPackages = new HashSet<>();
    }

    static GcordPreference getInstance() {
        return SingletonHolder.instance;
    }

    void init(Context application) {
        if (callBack == null) {
            callBack = new IGcordPreferenceCallBack.Stub() {

                @Override
                public String handlePhoneNumber(String phoneNumber) throws RemoteException {
                    if (gcordPreferenceCallBack != null)
                        return gcordPreferenceCallBack.handlePhoneNumber(phoneNumber);
                    return phoneNumber;
                }

                @Override
                public void onEthernetSettingResult(boolean success, String message) throws RemoteException {
                    if(EthernetManager.getInstance().ethernetCallback != null){
                        EthernetManager.getInstance().ethernetCallback.onResult(success, message);
                    }
                }
            };
        }

        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    iGcordPreferenceAIDL = IGcordPreferenceAIDL.Stub.asInterface(service);
                    if (iGcordPreferenceAIDL != null) {
                        try {
                            iGcordPreferenceAIDL.registerPreferenceCallBack(callBack);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    if (internalInitCallback != null) {
                        internalInitCallback.onInitFinished();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    iGcordPreferenceAIDL = null;
                }
            };
        }
        connectAidl(application);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);
        intentFilter.addDataScheme("package");
        application.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String packageName = intent.getDataString();
                    String action = intent.getAction();
                    Log.d("GcordPreference", "action: " + action + ", packageName: " + packageName);
                    if (action == null) return;
                    if (packageName != null) {
                        packageName = packageName.trim();
                        if (packageName.length() > 0) {
                            switch (action) {
                                case Intent.ACTION_PACKAGE_ADDED:
                                    installedPackages.add(packageName);
                                    break;
                                case Intent.ACTION_PACKAGE_FULLY_REMOVED:
                                case Intent.ACTION_PACKAGE_REMOVED:
//                                    installedPackages.remove(packageName);
                            }
                        }
                    }
                }
            }
        }, intentFilter);
    }

    private void connectAidl(Context context) {
        try {
            Intent intent = new Intent(GCORD_PREFERENCE_SERVICE);
            intent.setPackage(GcordSDK.LAUNCHER_PACKAGE_NAME);
            intent.putExtra("packageName", context.getPackageName());
            boolean result = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            if (!result) {
                if (internalInitCallback != null) {
                    internalInitCallback.onInitFailed();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (internalInitCallback != null) {
                internalInitCallback.onInitFailed();
            }
        }
    }

    public int getSystemDialMode() {
        return SettingAPI.getInstance().getGlobalSettingIntValue(GlobalSettingConstants.PREF_SYSTEM_DIAL_MODE,
                GcordPreferenceConstants.SYSTEM_DIAL_MODE_PSTN);
    }

    /***
     * ????????????????????????
     * @param mode ??????????????????
     * @see GcordPreferenceConstants#SYSTEM_DIAL_MODE_CELL
     * @see GcordPreferenceConstants#SYSTEM_DIAL_MODE_PSTN
     * @see GcordPreferenceConstants#SYSTEM_DIAL_MODE_SIP
     */
    @SuppressWarnings("unused")
    public void setSystemDialMode(int mode) {
        if (mode != GcordPreferenceConstants.SYSTEM_DIAL_MODE_PSTN &&
                mode != GcordPreferenceConstants.SYSTEM_DIAL_MODE_CELL &&
                mode != GcordPreferenceConstants.SYSTEM_DIAL_MODE_SIP) {
            return;
        }

        SettingAPI.getInstance().setGlobalValue(GlobalSettingConstants.PREF_SYSTEM_DIAL_MODE, mode);
    }

    /**
     * ????????????????????????
     *
     * @return true ???????????????????????????false?????????????????????
     */
    public boolean isSmartDialOn() {
        return SettingAPI.getInstance().getGlobalSettingBooleanValue(GlobalSettingConstants.PREF_IS_SMART_DIAL_ON,
                false);
    }


    /***
     * ??????????????????
     * @param enable true-?????????????????????false-??????????????????
     * @return true ???????????????false????????????
     */
    @SuppressWarnings("unused")
    public boolean enableSmartDial(boolean enable) {
        return SettingAPI.getInstance().setGlobalSettingBooleanValue(GlobalSettingConstants.PREF_IS_SMART_DIAL_ON, enable);
    }

    /***
     * ?????????????????????
     * @return ???????????????
     */
    public String getCurrentAreaCode() {
        return SettingAPI.getInstance().getGlobalSettingStringValue(GlobalSettingConstants.PREF_LOCAL_AREA_CODE, "");
    }

    /**
     * ?????????????????????
     *
     * @param areaCode ??????????????????
     * @return true ???????????????false????????????
     */
    @SuppressWarnings("unused")
    public boolean setAreaCode(String areaCode) {
        return SettingAPI.getInstance().setGlobalSettingStringValue(GlobalSettingConstants.PREF_LOCAL_AREA_CODE, areaCode);
    }

    /**
     * ??????????????????
     *
     * @return ??????????????????????????????????????????????????????????????????2??????????????????????????????5??????????????????3??????????????????4??????????????????3???????????????5...4...
     */
    public String getDialPrefix() {
        return SettingAPI.getInstance().getGlobalSettingStringValue(GlobalSettingConstants.PREF_SYSTEM_DIAL_PREFIX, "");
    }

    /**
     * ????????????????????????Pair<Character, Integer>??????????????????????????????pair???first??????????????????second???????????????
     *
     * @param prefixPairs ???????????????????????????????????????????????????[1,4],????????????,???????????????null?????????????????????????????????????????????
     */
    @SuppressWarnings("unused")
    public void setDialPrefix(List<Pair<Character, Integer>> prefixPairs) throws IllegalArgumentException {
        StringBuilder prefix = new StringBuilder();
        if (prefixPairs != null && prefixPairs.size() > 0) {
            for (Pair<Character, Integer> pair : prefixPairs) {
                prefix.append(pair.first);
                String interval;
                switch (pair.second) {
                    case 1: {
                        interval = ".";
                        break;
                    }
                    case 2: {
                        interval = "..";
                        break;
                    }
                    case 3: {
                        interval = "...";
                        break;
                    }
                    case 4: {
                        interval = "....";
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("the dial interval of the prefix should be in [1,4]");

                }
                prefix.append(interval);
            }
        }
        SettingAPI.getInstance().setGlobalSettingStringValue(GlobalSettingConstants.PREF_SYSTEM_DIAL_PREFIX, prefix.toString());
    }

    /**
     * ??????????????????
     *
     * @return ??????????????????Pair<Character, Integer>??????????????????pair???first??????????????????second???????????????
     */
    @SuppressWarnings("unused")
    public List<Pair<Character, Integer>> getDialPrefixPairs() {
        String str = SettingAPI.getInstance().getGlobalSettingStringValue(GlobalSettingConstants.PREF_SYSTEM_DIAL_PREFIX, "");
        ArrayList<Pair<Character, Integer>> dialPrefixPairs = new ArrayList<>();
        if (str != null && str.length() > 0) {
            Character character = null;
            int interval = 0;
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if ('.' == ch) {
                    interval++;
                } else {
                    addDialPrefixPair(dialPrefixPairs, character, interval);
                    interval = 0;
                    character = ch;
                }
                if (i == str.length() - 1) {
                    addDialPrefixPair(dialPrefixPairs, character, interval);
                }
            }
        }
        return dialPrefixPairs;
    }

    private void addDialPrefixPair(ArrayList<Pair<Character, Integer>> dialPrefixPairs, Character character, int interval) {
        Pair<Character, Integer> pair;
        if (character != null && interval > 0) {
            pair = new Pair<>(character, interval);
            dialPrefixPairs.add(pair);
        }
    }

    /***
     * ????????????????????????????????????
     * @return ??????????????????????????????
     */
    public int getInnerNumberMaxLength() {
        return SettingAPI.getInstance().getGlobalSettingIntValue(GlobalSettingConstants.PREF_INNER_NUMBER_MAX_LENGTH, 5);
    }

    /**
     * ????????????????????????????????????
     *
     * @param maxLength ??????????????????????????????
     */
    @SuppressWarnings("unused")
    public void setInnerNumberMaxLength(int maxLength) {
        SettingAPI.getInstance().setGlobalValue(GlobalSettingConstants.PREF_INNER_NUMBER_MAX_LENGTH, maxLength);
    }

    /**
     * ??????????????????
     *
     * @return ??????????????????????????????0-??????1-??????2-???
     */
    @SuppressWarnings("unused")
    public @DialIntervalLevel
    int getDialIntervalLevel() {
        int millis = PSTNInternal.getInstance().getDialInterval();
        if (millis <= 200) return DialIntervalLevel.LEVEL_SHORT;
        else if (millis <= 250) return DialIntervalLevel.LEVEL_MEDIUM;
        else return DialIntervalLevel.LEVEL_LONG;
    }

    /***
     * ??????????????????
     * @param level ??????????????????????????????0-??????1-??????2-???
     * @see DialIntervalLevel
     */
    @SuppressWarnings("unused")
    public void setDialIntervalLevel(@DialIntervalLevel int level) throws IllegalArgumentException {
        int interval;
        switch (level) {
            case DialIntervalLevel.LEVEL_SHORT:
                interval = 200;
                break;
            case DialIntervalLevel.LEVEL_MEDIUM:
                interval = 250;
                break;
            case DialIntervalLevel.LEVEL_LONG:
                interval = 400;
                break;
            default:
                throw new IllegalArgumentException("dial interval level should be in [0,2]");
        }
        PSTNInternal.getInstance().setDialInterval(interval);
    }

    @SuppressWarnings("unused")
    public void setEnableSystemScreenSave(boolean enable) {
        SettingAPI.getInstance().setGlobalValue(GlobalSettingConstants.PREF_ENABLE_SYS_SCREEN_SAVE, enable);
    }

    public boolean isSystemScreenSaveEnabled() {
        return SettingAPI.getInstance().getGlobalSettingBooleanValue(GlobalSettingConstants.PREF_ENABLE_SYS_SCREEN_SAVE
                , true);
    }

    /**
     * ????????????????????????
     *
     * @param hidePhoneNumber true-?????????false-?????????
     */
    @SuppressWarnings("unused")
    public void hidePhoneNumber(boolean hidePhoneNumber) {
        Context context = GcordSDK.getInstance().getContext();
        String value = "";
        if (hidePhoneNumber)
            value = context.getPackageName() + "_" + 1;
        SettingAPI.getInstance().setGlobalSettingStringValue(HIDE_PHONE_NUMBER_KEY, value);
    }

    /**
     * ????????????????????????
     *
     * @return true-?????????false-?????????
     */
    public boolean shouldHidePhoneNumber() {
        Context context = GcordSDK.getInstance().getContext();
        String value = SettingAPI.getInstance().getGlobalSettingStringValue(HIDE_PHONE_NUMBER_KEY, null);
        if (value == null) return false;
        value = value.trim();
        if (value.length() == 0) return false;
        if (!value.contains("_") || value.endsWith("_") || value.length() < 3) return false;
        int lastIndex = value.lastIndexOf("_");
        String packageName = value.substring(0, lastIndex);
        if (packageName.length() <= 0) return false;
        value = value.substring(lastIndex + 1);
        if (installedPackages.contains(packageName)) return "1".equals(value);
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        if (info == null || info.isEmpty())
            return false;
        for (int i = 0; i < info.size(); i++) {
            PackageInfo packageInfo = info.get(i);
            installedPackages.add(packageInfo.packageName);
            if (packageName.equals(packageInfo.packageName)) {
                return "1".equals(value);
            }
        }
        SettingAPI.getInstance().setGlobalSettingStringValue(HIDE_PHONE_NUMBER_KEY, "");
        return false;
    }


    /**
     * ??????launcher????????????????????????
     * @return true-???????????????false-??????????????????
     */
    public boolean isLauncherAutoRecordingPhoneCalls() {
        try {
            if (iGcordPreferenceAIDL != null)
                return iGcordPreferenceAIDL.isLauncherAutoRecordingPhoneCalls();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ??????????????????????????????
     * @param enable true-???????????????false-????????????
     */
    public void enableLauncherAutoRecordingPhoneCalls(boolean enable) {
        try {
            if (iGcordPreferenceAIDL != null)
                iGcordPreferenceAIDL.enableLauncherAutoRecordingPhoneCalls(enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????????????????????
     *
     * @param gcordPreferenceCallBack ???????????????????????????
     */
    @SuppressWarnings("unused")
    public void setGcordPreferenceCallBack(GcordPreferenceCallBack gcordPreferenceCallBack) {
        this.gcordPreferenceCallBack = gcordPreferenceCallBack;
    }

    public @interface DialIntervalLevel {
        int LEVEL_SHORT = 0;
        int LEVEL_MEDIUM = 1;
        int LEVEL_LONG = 2;
    }

    private static class SingletonHolder {
        private final static GcordPreference instance;

        static {
            instance = new GcordPreference();
        }
    }

    public static abstract class GcordPreferenceCallBack {
        /**
         * ??????????????????
         *
         * @param phoneNumber ???????????????????????????
         * @return ???????????????????????????
         */
        public String handlePhoneNumber(String phoneNumber) {
            return phoneNumber;
        }
    }

    protected IGcordPreferenceAIDL getAIDL(){
        if(iGcordPreferenceAIDL == null) {
            this.init(GcordSDK.getInstance().getContext());
        }
        return iGcordPreferenceAIDL;
    }
}
