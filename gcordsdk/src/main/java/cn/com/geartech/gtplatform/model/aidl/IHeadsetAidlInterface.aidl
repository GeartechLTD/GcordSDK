// IHeadsetAidInterface.aidl
package cn.com.geartech.gtplatform.model.aidl;

// Declare any non-default types here with import statements
import cn.com.geartech.gtplatform.model.aidl.IHeadsetCallback;

interface IHeadsetAidlInterface {

    void registerCallback(IHeadsetCallback callback);

    void startPairing();

    void stopPairing();

    boolean isConnected();

    boolean isPaired();

    void unPair();

    boolean isCharging();

    boolean isAllowCharging();

    void setAllowCharging(boolean allowCharging);

    boolean is3Point5MMHeadsetConnected();

    boolean isWirelessHeadsetConnected();

    void checkPairingState();

    void checkConnectionState();

    void checkBatteryLevel();
}
