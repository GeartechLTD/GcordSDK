// IGcordPreferenceAIDL.aidl
package cn.com.geartech.app;
import cn.com.geartech.app.IGcordPreferenceCallBack;
// Declare any non-default types here with import statements

interface IGcordPreferenceAIDL {
    void registerPreferenceCallBack(IGcordPreferenceCallBack callBack);
    boolean isLauncherAutoRecordingPhoneCalls();
    void enableLauncherAutoRecordingPhoneCalls(boolean enable);

    //wifi
    void connectToWifi(String ssid, String password);
    void connectToWifiWithSetting(String ssid, String password, boolean useStaticIp, String staticIp, String gateway, int prefix, String firstDNS, String secondDNS, boolean useProxy, String proxyHost, int proxyPort, String proxyFilter);
    //sim
    boolean isSimReady(int index);
    String getSimOperator(int index);
    String getPhoneNumber(int index);
    int getDefaultPhoneIndex();
    int getDefaultDataIndex();
    boolean setDefaultPhone(int index);
    boolean setDefaultDataSim(int index);
    boolean setDataEnabled(boolean enabled);
    boolean isDataEnabled();
    int getIncallSimIndex();

    //eth
    boolean isEthernetCablePluginImpl();
    String getMacImpl();
    boolean isEthernetActiveImpl();
    String getAddressImpl();
    String getMaskImpl();
    String getGateWayImpl();
    String[] getDnsImpl();
    void setStaticEthernet(String ip, String gateway, String mask, boolean autoDNS, String dns1, String dns2, boolean useProxy, String proxy, int proxyPort);
    void setDHCPEthernet(boolean useProxy, String proxy, int proxyPort);
    boolean isUseDHCP();
    boolean isUseProxy();
    String getProxyHost();
    int getProxyPort();

    //sound
    int getMaxVolume();
    int getCurrentVolume();
    void setVolume(int volume);
    boolean isTouchEffectEnabled();
    void setTouchEffectEnabled(boolean enabled);

    //screen
    int getScreenBrightness();
    void setScreenBrightness(int brightness);

    int getScreenSaverDelay();
    void setScreenSaverDelay(int delay);

    int getScreenSaverMode();
    void setScreenSaverMode(int mode);

    //time
    boolean isAutoSetTimeOn();
    void setAutoSetTimeOn(boolean on);
    String getTimeZone();
    void setTimeZone(String timeZone);

    boolean isRadioOn();
    void setRadioOn(boolean on);
}
