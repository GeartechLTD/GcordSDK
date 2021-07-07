// IPSTNAidlInterface.aidl
package cn.com.geartech.gtplatform.model.aidl;
import cn.com.geartech.gtplatform.model.aidl.IPSTNAidlCallback;
import cn.com.geartech.gtplatform.model.aidl.ICommonCallback;
// Declare any non-default types here with import statements

interface IPSTNAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void enablePSTN(boolean enable, String packageName);

    void setMusicTunnel(int tunnel);

    int getMusicTunnel();

    void registerAidlCallback(String packageName, int version, IPSTNAidlCallback callback);

    boolean isHandlePickedUp();

    int getPSTNStatus();

    void startInterceptIncomingCall(String packageName);

    void endInterceptIncomingCall(String packageName);

    int getExtLineStatus();

    void startRecording(ICommonCallback callback);

    void stopRecording();

    void setAudioState(int state); //internal usage

    int getAudioState();

    void volUp();

    void volDown();

    String getCurrentIncomingPhoneNumber();

    boolean setMicrophoneMute(boolean mute);

    boolean isMicrophoneMute();

    int getCurrentCallDirection();

    boolean isAutoAnswerOpen();

    void enableAutoAnswer(boolean enable);

    void cancelAutoAnswer();

    int getMaxVolume();

    void getVolume(int channel, IPSTNAidlCallback callback);

    void setVolume(int channel, int volume ,IPSTNAidlCallback callback);

    String getRegAppName();

    boolean isReversalOfPolarityEnabled();
}
