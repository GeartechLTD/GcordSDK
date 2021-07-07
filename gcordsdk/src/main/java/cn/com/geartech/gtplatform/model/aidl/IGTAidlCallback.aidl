// IGTAidlCallback.aidl
package cn.com.geartech.gtplatform.model.aidl;

// Declare any non-default types here with import statements

interface IGTAidlCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onPushMessage(String message);


    void onEvent(String eventType,inout Bundle bundle);

    void onCheckUpgradeResult(boolean success, boolean needUpdate, String newVersionName,
                              String upgradeInfo, String packageSHA1, String errorMessage,
                              int flag,inout List<String> fieldKeys,inout List<String> fieldValues);

    void onDownloadProgressChange(int progress);

    void onDowndloadComplete(boolean success, String errorMessage);


    void onInstallComplete(boolean success, String errorMessage);

    int fetchDogBell(String packageName);

    int getHandlePickUpType();

    String queryContactData(String number);

    void onScreenStrategyChange(int strategy, int param1, int param2);

    int queryInt(String key);

    String queryString(String key);

    int quertInt1(String key, String param);
}
