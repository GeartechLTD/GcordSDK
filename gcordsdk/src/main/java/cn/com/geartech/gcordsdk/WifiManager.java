package cn.com.geartech.gcordsdk;

import android.net.wifi.ScanResult;

public class WifiManager {
    private static WifiManager instance;

    protected WifiManager() {

    }

    protected static WifiManager getInstance() {
        if (instance == null) {
            instance = new WifiManager();
        }
        return instance;
    }

    public void connectToWifi(ScanResult scanResult, String password) {
        connectToWifiImpl(scanResult.SSID, password);
    }

    public void connectToWifi(String ssid, String password) {
        connectToWifiImpl(ssid, password);
    }

    public void connectToWifiWithSetting(String ssid, String password, boolean useStaticIp, String staticIp, String gateway, int prefix, String firstDNS, String secondDNS, boolean useProxy, String proxyHost, int proxyPort, String proxyFilter){
        connectToWifiWithSettingImpl(ssid, password, useStaticIp, staticIp, gateway, prefix, firstDNS, secondDNS, useProxy, proxyHost, proxyPort, proxyFilter);
    }

    private void connectToWifiImpl(String ssid, String password){
        try {
            GcordPreference.getInstance().getAIDL().connectToWifi(ssid, password);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void connectToWifiWithSettingImpl(String ssid, String password, boolean useStaticIp, String staticIp, String gateway, int prefix, String firstDNS, String secondDNS, boolean useProxy, String proxyHost, int proxyPort, String proxyFilter) {

    }
}
