// IGcordPreferenceCallBack.aidl
package cn.com.geartech.app;

// Declare any non-default types here with import statements

interface IGcordPreferenceCallBack {
    String handlePhoneNumber(String phoneNumber);
    void onEthernetSettingResult(boolean success, String message);
}
