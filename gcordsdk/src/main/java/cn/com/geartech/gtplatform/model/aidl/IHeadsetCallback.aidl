// IHeadsetCallback.aidl
package cn.com.geartech.gtplatform.model.aidl;

// Declare any non-default types here with import statements

interface IHeadsetCallback {

    void onConnectionStateChange(int status);

    void onPairingStateChange(int status);

    void onBatteryLevelChange(int percent);

    void onBtn1Pressed();
}
