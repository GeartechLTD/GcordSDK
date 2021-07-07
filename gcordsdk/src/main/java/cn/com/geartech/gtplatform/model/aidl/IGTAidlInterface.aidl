// IGTAidlInterface.aidl
package cn.com.geartech.gtplatform.model.aidl;

import cn.com.geartech.gtplatform.model.aidl.IGTAidlCallback;
import cn.com.geartech.gtplatform.model.aidl.ICommonCallback;
// Declare any non-default types here with import statements

interface IGTAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    int getEthernetConnectionState();

    int getCurrentPowerLevel();

    int getCurrentPluggedType();

    boolean isPowerConnected();

    boolean isEthernetLineConnected();

    void registerAidlCallback(String packageName, int version, IGTAidlCallback callback);

    void checkUpgrade(String packageName);

    void downloadAndInstall(String packageName, String packageSHA1);

    void updateInstInterval(int interval);

    void fm();

    void startFMBroadcasting(int interval);

    void stopFMBroadcasting();

    void notifyAppForegroundState(String packageName, boolean isForeground, int pid);

    void setBootAnimation(String path);

    void installDownloadedPackage(String packageName);

    int setBootAnimationPath(String path);

    int setShutdownAnimationPath(String path);

    void walkDog(String packageName);

    void setLedOn(int ledNum, boolean on);

    void setLedFlashing(int ledNum, boolean flashing);

    void pLog(String pLog);

    void eventLog(String eventId, String eventName, String param1, String param2, String param3);

    Map getDeviceCapabilities();

    void reboot();

    float getFontScale();

    void setFontScale(float scale);

    void logD(String tag, String msg);

    void logE(String tag, String msg);

    void fileLog(String fileName, String tag, String msg);

    int getDeviceType();

    void raw(String tag, String msg);

    void logic(String tag, String msg);

    void biz(String tag, String msg);

    void statistics(String tag, String msg);

    String getUsbStoragePath();

    int queryScreenStrategy();

    int sendMessage(String msgName, int param1, int param2, String str1, String str2);

    void checkAllInOneUpgrade(String packageName, ICommonCallback callback);

    void downloadAllInOnePackages(String token, ICommonCallback callback);

    void enableAppWhiteList(String packageName, boolean enable);

    void addOrRemovePackageForWhiteList(String myPackageName, String packageName, boolean enable);

    String queryData(String key, String key2, int key3, int key4);

    void checkSystemPackageUpgrade(String packageName, ICommonCallback callback);

    void checkGTPUpgrade(String packageName, ICommonCallback callback);

    Map queryCurrentCallStatus();

    Map queryCellSignal();

    void checkPhoneNumber(String number, ICommonCallback callback);
}
