package cn.com.geartech.gcordsdk;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public final class EthernetManager {

    private static EthernetManager instance;

    static {
        instance = new EthernetManager();
    }

    private EthernetManager() {

    }

    protected static EthernetManager getInstance() {
        return instance;
    }

    /**
     * 获取以太网子网掩码
     *
     * @return 子网掩码
     */
    @SuppressWarnings("unused")
    public String getMask() {
        return getMaskImpl();
    }

    /**
     * 获取以太网ipv4地址
     *
     * @return ipv4 address
     */
    @SuppressWarnings("unused")
    public String getEthernetIPV4Address() {
        return getAddressImpl();
    }

    @SuppressWarnings("unused")
    protected void init(Application application) {

    }

    /**
     * 获取以太网网关
     *
     * @return gateway
     */
    @SuppressWarnings("unused")
    public String getGateway() {
        return getGateWayImpl();
    }


    /**
     * 获取以太网dns
     *
     * @return dns
     */
    public String[] getDns() {
        return getDnsImpl();
    }

    /**
     * 是否插入网线
     *
     * @return boolean
     */
    public boolean isEthernetCablePlugin() {
        return isEthernetCablePluginImpl();
    }

    /**
     * 获取以太网网卡MAC
     *
     * @return mac
     */
    public String getMac() {
        return getMacImpl();
    }

    /**
     * 当前正在使用的网络是否是以太网
     *
     * @return boolean
     */
    public boolean isEthernetActive() {
        return isEthernetActiveImpl();
    }

    /**
     * 设置以太网为DHCP模式，设置后{@link #isUseDHCP()} 会返回true
     *
     * @param useProxy  是否使用proxy
     * @param proxyHost 如果useProxy = true, 请传入有效的地址，如"192.168.0.1"。如果useProxy = false，传入的proxy会被忽略
     * @param port      如果useProxy = true, 请传入有效的端口。如果useProxy = false，传入的port会被忽略
     */
    public void setDHCPEthernet(boolean useProxy, String proxyHost, Integer port) {
        setDHCPEthernetImpl(useProxy, proxyHost, port);
    }

    /**
     * 设置以太网为静态地址模式，设置后{@link #isUseDHCP()} 会返回false
     *
     * @param ip        ip
     * @param gateway   网关
     * @param mask      掩码
     * @param autoDNS   是否自定义dns，如果true dns1，dns2至少有一个非空。如果false则不会修改dns
     * @param dns1      dns1
     * @param dns2      dns2
     * @param useProxy  是否使用proxy
     * @param proxyHost 如果useProxy = true, 请传入有效的地址，如"192.168.0.1"。如果useProxy = false，传入的proxy会被忽略
     * @param proxyPort 如果useProxy = true, 请传入有效的端口。如果useProxy = false，传入的port会被忽略
     */
    public void setStaticEthernet(String ip, String gateway, String mask,
                                  boolean autoDNS,
                                  String dns1, String dns2,
                                  boolean useProxy,
                                  String proxyHost, Integer proxyPort) {
        setStaticEthernetImpl(ip, gateway, mask, autoDNS, dns1, dns2, useProxy, proxyHost, proxyPort);
    }

    /**
     * 是否开启DHCP
     *
     * @return boolean
     */
    public boolean isUseDHCP() {
        return isDHCPImpl();
    }

    /**
     * 是否开启proxy
     *
     * @return boolean
     */
    public boolean isUseProxy() {
        return isUseProxyImpl();
    }

    private boolean isUseProxyImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isUseProxy();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取代理的port，未设置port会返回null
     *
     * @return port
     */
    public Integer getProxyPort() {
        return getProxyPortImpl();
    }

    private Integer getProxyPortImpl() {
        try {
            int port = GcordPreference.getInstance().getAIDL().getProxyPort();
            if (port == -1)
                return null;
            return port;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取代理的host
     *
     * @return host
     */
    public String getProxyHost() {
        return getProxyHostImpl();
    }

    private String getProxyHostImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getProxyHost();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isDHCPImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isUseDHCP();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean isEthernetCablePluginImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isEthernetCablePluginImpl();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getMacImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getMacImpl();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isEthernetActiveImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isEthernetActiveImpl();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getAddressImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getAddressImpl();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getMaskImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getMaskImpl();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getGateWayImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getGateWayImpl();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private String[] getDnsImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getDnsImpl();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    private void setStaticEthernetImpl(String ip, String gateway, String mask, boolean autoDNS, String dns1, String dns2, boolean useProxy, String proxy, Integer proxyPort) {
        try {
            GcordPreference.getInstance().getAIDL().setStaticEthernet(ip, gateway, mask, autoDNS, dns1, dns2, useProxy, proxy, proxyPort);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setDHCPEthernetImpl(boolean useProxy, String proxy, Integer port) {
        try {
            GcordPreference.getInstance().getAIDL().setDHCPEthernet(useProxy, proxy, port);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * possible message provided by android:
     * "setup static ethernet failed"
     * "ip is null"
     * "set ip address to StaticIpConfiguration failed"
     * "set gateway to StaticIpConfiguration failed"
     * "set DHCP failed"
     */
    public interface EthernetCallback {
        void onResult(boolean success, String message);
    }

    protected EthernetCallback ethernetCallback = null;

    /**
     * 以太网设置接口的回调，会返回 {@link #setDHCPEthernet(boolean, String, Integer) }
     * 和 {@link #setStaticEthernet(String, String, String, boolean, String, String, boolean, String, Integer)}
     * 的结果
     */
    public void setEthernetCallback(EthernetCallback callback) {
        this.ethernetCallback = callback;
    }
}
