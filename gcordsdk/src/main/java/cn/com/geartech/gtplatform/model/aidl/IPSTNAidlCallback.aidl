// IPSTNAidlCallback.aidl
package cn.com.geartech.gtplatform.model.aidl;

// Declare any non-default types here with import statements

interface IPSTNAidlCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onEvent(String eventType, String param1,String param2,String param3, String param4);

    void onMusicTunnelChange(int tunnel);
}
