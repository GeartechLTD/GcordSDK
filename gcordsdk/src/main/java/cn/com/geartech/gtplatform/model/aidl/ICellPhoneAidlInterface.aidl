// IPSTNAidlInterface.aidl
package cn.com.geartech.gtplatform.model.aidl;

// Declare any non-default types here with import statements
import cn.com.geartech.gtplatform.model.aidl.ICommonCallback;

interface ICellPhoneAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void makeCall(String number);

    void dialDTMF(String dtmf);

    void playDTMF(String dtmf, String callId);
    void stopPlayDTMF(String callId);

    String getCurrentCallId();

    boolean isInCall();

    int getCallStatus();

    String getCurrentCallNumber();

    void acceptCall(String callId);

    void endCall(String callId);

    void endCurrentCall();

    void rejectCall(String callId);

    void setMute(boolean mute);

    boolean isMute();

    void startRecording();
    void stopRecording();

    boolean isRecording();

    int getCurrentInCallVolume();

    int getMaxInCallVolume();

    void setInCallVolume(int volume);

    boolean isHandsFreeOn();

    void setHandsFreeOn(ICommonCallback callback);

    void setHandleOn(ICommonCallback callback);

    boolean isSIMCardAvailable();

    boolean isIncomingCall();

    void setBTOn(ICommonCallback callback);

    boolean isBTOn();

    boolean isBTHeadsetConnected();
}
