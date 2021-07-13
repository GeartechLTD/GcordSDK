// IGcordPreferenceAIDL.aidl
package cn.com.geartech.app;
import cn.com.geartech.app.IGcordPreferenceCallBack;
// Declare any non-default types here with import statements

interface IGcordPreferenceAIDL {
    void registerPreferenceCallBack(IGcordPreferenceCallBack callBack);
    boolean isLauncherAutoRecordingPhoneCalls();
    void enableLauncherAutoRecordingPhoneCalls(boolean enable);
}
