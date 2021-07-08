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

    interface EthernetCallback{
        void onResult(boolean success, String message);
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
   public String getMask(){
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

    public boolean isEthernetCablePlugin(){
        return isEthernetCablePluginImpl();
    }

    public String getMac() {
        return getMacImpl();
    }

    public boolean isEthernetActive() {
        return isEthernetActiveImpl();
    }

    public void setDHCPEthernet(boolean useProxy, String proxy, Integer port) {
        setDHCPEthernetImpl(useProxy, proxy, port);
    }

    public void setStaticEthernet(String ip, String gateway, String mask,
                               boolean autoDNS,
                               String dns1, String dns2,
                               boolean useProxy,
                               String proxy, Integer proxyPort){
        setStaticEthernetImpl(ip, gateway, mask, autoDNS, dns1, dns2, useProxy, proxy, proxyPort);
    }

    public boolean isUseDHCP() {
        return isDHCPImpl();
    }

    public boolean isUseProxy() {
        return isUseProxyImpl();
    }

    private boolean isUseProxyImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isUseProxy();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return false;
    }

    public Integer getProxyPort() {
        return getProxyPortImpl();
    }

    private Integer getProxyPortImpl() {
        try {
            int port = GcordPreference.getInstance().getAIDL().getProxyPort();
            if(port == -1)
                return null;
            return port;
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    public String getProxyHost() {
        return getProxyHostImpl();
    }

    private String getProxyHostImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getProxyHost();
        }catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isDHCPImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isUseDHCP();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return true;
    }

    private boolean isEthernetCablePluginImpl(){
        try {
            return GcordPreference.getInstance().getAIDL().isEthernetCablePluginImpl();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return false;
    }
    private String getMacImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getMacImpl();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }
    private boolean isEthernetActiveImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isEthernetActiveImpl();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return false;
    }
    private String getAddressImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getAddressImpl();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }
    private String getMaskImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getMaskImpl();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }
    private String getGateWayImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getGateWayImpl();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    private String[] getDnsImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getDnsImpl();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return new String[0];
    }

    private void setStaticEthernetImpl(String ip, String gateway, String mask, boolean autoDNS, String dns1, String dns2, boolean useProxy, String proxy, Integer proxyPort) {
        try {
            GcordPreference.getInstance().getAIDL().setStaticEthernet(ip, gateway, mask, autoDNS, dns1, dns2, useProxy, proxy, proxyPort);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    private void setDHCPEthernetImpl(boolean useProxy, String proxy, Integer port) {
        try {
            GcordPreference.getInstance().getAIDL().setDHCPEthernet(useProxy, proxy, port);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    protected EthernetCallback ethernetCallback = null;
    public void setEthernetCallback(EthernetCallback callback){
        this.ethernetCallback = callback;
    }
}
