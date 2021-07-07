package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

import cn.com.geartech.gtplatform.model.aidl.IGTAidlInterface;


public class GcordUtil {

    public static String getUsbStoragePath() {
        String path = "";
        try {
            IGTAidlInterface i = GTAidlHandler.getIgtAidlInterface();
            if (i != null) {
                path = GTAidlHandler.getIgtAidlInterface().getUsbStoragePath();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(path)) {
            if (Build.VERSION.SDK_INT < 20) {
                return "/mnt/usb_storage/";
            } else {
                return "/storage/usbotg/";
            }
        }
        return path;
    }

    public static boolean doUsbFlashDiskExist(Context context) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devices = usbManager.getDeviceList();
        return (devices != null && devices.size() > 0);
    }


    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            return (info != null);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // wifi热点开关
    private static boolean openWifiAP(String apName, String password, Context context) {
        return setWifiAp(apName, password, context, true);
    }

    private static boolean closeWifiAP(String apName, String password, Context context) {
        return setWifiAp(apName, password, context, false);
    }

    private static boolean setWifiAp(String apName, String password, Context context, boolean shouldOpen) {
        if (context == null) {
            return false;
        }
        if (TextUtils.isEmpty(apName)) {
            return false;
        }

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (shouldOpen) {
            wifiManager.setWifiEnabled(false);
        }
        try {
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            try {

                Method isWifiApEnable = wifiManager.getClass().getMethod(
                        "isWifiApEnabled");
                boolean enable = (Boolean) isWifiApEnable.invoke(wifiManager);
                if (enable) {
                    Method getWifiApConfig = wifiManager.getClass().getMethod(
                            "getWifiApConfiguration");
                    WifiConfiguration apConfig = (WifiConfiguration) getWifiApConfig.invoke(wifiManager);
                    if (apConfig != null) {
                        if (shouldOpen) {
                            if (apConfig.SSID.equals(apName) && apConfig.preSharedKey.equals(password)) {
                                return true;
                            }
                        }
                        method.invoke(wifiManager, apConfig, false);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                //热点的配置类
                WifiConfiguration apConfig = new WifiConfiguration();
                //配置热点的名称(可以在名字后面加点随机数什么的)
                apConfig.SSID = apName;
                //配置热点的密码
                if (!TextUtils.isEmpty(password)) {
                    apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                }
                apConfig.preSharedKey = password;
                //通过反射调用设置热点

                //返回热点打开状态
                return (Boolean) method.invoke(wifiManager, apConfig, shouldOpen);
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String AP_NAME = "WIFI_AP_NAME";
    private static String AP_PWD = "WIFI_AP_PWD";
    private static String AP_ENABLE = "WIFI_AP_ENABLE";
    private static String EVENT_ETHERNET_CONNECTION_STATE_CHANGED = "cn.com.geartech.ethernet_state_changed";


    /**
     * 设置在插入网线时是否开启ap
     * @param context 上下文
     * @param enable 是否自动设置热点
     * @param apName 热点名称
     * @param password 热点密码, 空字符串表示不需要密码
     */
    public static void enableAutoAPForEthernet(Context context,
                                 boolean enable,
                                 String apName, String password) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(apName)) {
            return;
        }

        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = shp.edit();
        if (enable) {
            ed.putBoolean(AP_ENABLE, true)
                    .putString(AP_NAME, apName)
                    .putString(AP_PWD, password)
                    .apply();
        } else {
            ed.remove(AP_NAME)
                    .remove(AP_PWD)
                    .remove(AP_ENABLE)
                    .apply();
        }
    }

    protected static void registerEthernetStateChangeReceiver(Context context)
    {
        EthernetStateReceiver receiver = new EthernetStateReceiver();
        context.getApplicationContext().registerReceiver(receiver,new IntentFilter(EVENT_ETHERNET_CONNECTION_STATE_CHANGED));
    }

    protected static class EthernetStateReceiver extends BroadcastReceiver {
        private int lastEthernetState = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (EVENT_ETHERNET_CONNECTION_STATE_CHANGED.equals(action)) {
                int state = GcordSDK.getInstance().getSettingAPI().getEthernetConnectionState();
                SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
                if (SettingAPI.ETHER_STATE_CONNECTED == state) {
                    if (lastEthernetState == SettingAPI.ETHER_STATE_DISCONNECTED) {
                        boolean enable = shp.getBoolean(GcordUtil.AP_ENABLE, false);
                        if (enable) {
                            GcordUtil.openWifiAP(shp.getString(GcordUtil.AP_NAME, ""),
                                    shp.getString(GcordUtil.AP_PWD, ""), context);
                        }
                    }
                }else
                {
                    if(lastEthernetState == SettingAPI.ETHER_STATE_CONNECTED) {
                        boolean enable = shp.getBoolean(GcordUtil.AP_ENABLE, false);
                        if (enable) {
                            GcordUtil.closeWifiAP(shp.getString(GcordUtil.AP_NAME, ""),
                                    shp.getString(GcordUtil.AP_PWD, ""), context);
                        }
                    }
                }
                lastEthernetState = state;
            }
        }
    }
}
