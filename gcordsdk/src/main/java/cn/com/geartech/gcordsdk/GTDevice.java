package cn.com.geartech.gcordsdk;

import android.content.Intent;
import android.provider.Settings;

/**
 * @author xujiahui
 * @since 26/05/2017
 */
public class GTDevice {

    public enum DEVICE_TYPE {
        GCORD_PRO_1,
        GCORD_PRO_2,
        GCORD_PRO3,
        GCORD_NEW_PRO1T
    }

    public static DEVICE_TYPE getDeviceType() {
        try {
            return DEVICE_TYPE.values()[GTAidlHandler.getIgtAidlInterface().getDeviceType()];
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return DEVICE_TYPE.GCORD_PRO_1;
    }

    @SuppressWarnings("unused")
    public static void hideNavigationBar() {
        GcordSDK.getInstance().getContext().sendBroadcast(new Intent("cn.com.geartech.remove_navigation_bar"));
    }

    public static void showNavigationBar() {
        GcordSDK.getInstance().getContext().sendBroadcast(new Intent("cn.com.geartech.add_navigation_bar"));
    }

    /**
     * 获取话机的序列号
     * @return serial number of the device
     */
    private  static String deviceSn = "";
    @SuppressWarnings("unused")
    public static String getDeviceSn()
    {

        if (deviceSn == null || deviceSn.length() == 0)
        {
            try {
                deviceSn = Settings.System.getString(GcordSDK.getInstance().getContext().getContentResolver(),
                        SettingAPI.SYS_SETTING_GT_MACHINE_ID);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        return deviceSn;
    }
}
