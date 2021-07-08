package cn.com.geartech.gcordsdk;

import android.net.wifi.ScanResult;

public class GcordWifiManager {
    private static GcordWifiManager instance;

    protected GcordWifiManager() {

    }

    protected static GcordWifiManager getInstance() {
        if (instance == null) {
            instance = new GcordWifiManager();
        }
        return instance;
    }

    /**
     * 使用密码连接到一个wifi
     *
     * @param scanResult android的WifiManager调用getScanResults()之后获得List<ScanResult>，传入需要的ScanResult
     * @param password   密码
     */
    public void connectToWifi(ScanResult scanResult, String password) {
        connectToWifiImpl(scanResult.SSID, password);
    }

    /**
     * 使用密码连接到一个wifi
     *
     * @param ssid     android的WifiManager调用getScanResults()之后获得List<ScanResult>，传入需要的ScanResult的ssid
     * @param password 密码
     */
    public void connectToWifi(String ssid, String password) {
        connectToWifiImpl(ssid, password);
    }

    /**
     * 使用静态ip，wifi proxy等连接，或者修改一个wifi
     * 如果useStaticIp为true，则String staticIp, String gateway, int prefix,
     * String firstDNS, String secondDNS需要填写相应的值，如果为false，这些可以为空
     * <p>
     * 如果useProxy为true，则String proxyHost, int proxyPort,
     * String proxyFilter需填写相应的数值，如果为false，这些可以为空
     *
     * @param ssid        ssid
     * @param password    密码
     * @param useStaticIp 是否静态地址
     * @param staticIp    静态地址的ip
     * @param gateway     网关
     * @param prefix      前缀
     * @param firstDNS    dns1
     * @param secondDNS   dns2
     * @param useProxy    是否使用proxy
     * @param proxyHost   proxy的host
     * @param proxyPort   proxy的port
     * @param proxyFilter 不使用proxy的地址，比如192.168.0.*
     */
    public void connectToWifiWithSetting(String ssid, String password, boolean useStaticIp, String staticIp, String gateway, int prefix, String firstDNS, String secondDNS, boolean useProxy, String proxyHost, int proxyPort, String proxyFilter) {
        connectToWifiWithSettingImpl(ssid, password, useStaticIp, staticIp, gateway, prefix, firstDNS, secondDNS, useProxy, proxyHost, proxyPort, proxyFilter);
    }

    private void connectToWifiImpl(String ssid, String password) {
        try {
            GcordPreference.getInstance().getAIDL().connectToWifi(ssid, password);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void connectToWifiWithSettingImpl(String ssid, String password, boolean useStaticIp, String staticIp, String gateway, int prefix, String firstDNS, String secondDNS, boolean useProxy, String proxyHost, int proxyPort, String proxyFilter) {
        try {
            GcordPreference.getInstance().getAIDL().connectToWifiWithSetting(ssid, password, useStaticIp, staticIp, gateway, prefix, firstDNS, secondDNS, useProxy, proxyHost, proxyPort, proxyFilter);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
