package cn.com.geartech.gcordsdk;

import java.util.Map;

/**
 * Created by pangyuning on 16/7/29.
 */
public class GTCapability {

    protected static int PSTN = 10000;
    private static int CELL = 10001;
    private static int PRINTER = 10002;
    private static int CAMERA = 10003;
    private static int WIRELESS_HEADSET = 10004;
    private static int _3POINT5MM_HEADSET = 10005;
    private static int BT_CELLPHONE_MANAGER = 10006;
    private static int PSTN_VOL_ADJUSTMENT = 10007;
    private static int PSTN_MUTE = 10008;
    private static int PSTN_AUTO_ANSWER = 10009;
    private static int PSTN_SUPPORT_REVERSAL_OF_POLARITY = 10010;

    Map capabilityMap;

    GTCapability()
    {

    }

    void setCapabilityMap(Map map)
    {
        capabilityMap = map;
    }

    private boolean hasCapability(int capability)
    {
        try {
            if(capabilityMap != null)
            {
                Integer c = capability;
                Boolean supported = (Boolean) capabilityMap.get(c);
                if(supported != null)
                {
                    return supported;
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return false;
    }

    /**
     * 话机是否支持pstn通话
     * @return
     */
    public boolean isPSTNSupported()
    {
        return hasCapability(PSTN);
    }

    /**
     * 话机是否支持蜂窝移动数据通话
     * @return
     */
    public boolean isCellSupported()
    {
        return hasCapability(CELL);
    }

    public boolean isReversalOfPolaritySupported(){
        return hasCapability(PSTN_SUPPORT_REVERSAL_OF_POLARITY);
    }

    /**
     * 话机是否支持外接打印机
     * @return
     */
    public boolean isPrinterSupported()
    {
        return hasCapability(PRINTER);
    }

    public boolean isCameraSupported()
    {
        return hasCapability(CAMERA);
    }

    /**
     * 话机是否支持无线手柄
     * @return
     */
    public boolean isWirelessHeadsetSupported()
    {
        return hasCapability(WIRELESS_HEADSET);
    }

    /**
     * 话机是否支持3.5mm耳机插孔
     * @return
     */
    public boolean is3Point5MMHeadsetSupported()
    {
        return hasCapability(_3POINT5MM_HEADSET);
    }

    /**
     * 话机是否支持蓝牙接管手机来电
     */
    public boolean isBTCellPhoneManagerSupported()
    {
        return hasCapability(BT_CELLPHONE_MANAGER);
    }

    /**
     * 话机是否支持调整PSTN通话音量
     * @return
     */
    public boolean isPSTNVolumeAdjustmentSupported(){
        return hasCapability(PSTN_VOL_ADJUSTMENT);
    }

    public boolean isPSTNMuteSupported()
    {
        return hasCapability(PSTN_MUTE);
    }

    public boolean isPSTNAutoAnswerSupported()
    {
        return hasCapability(PSTN_AUTO_ANSWER);
    }

}
