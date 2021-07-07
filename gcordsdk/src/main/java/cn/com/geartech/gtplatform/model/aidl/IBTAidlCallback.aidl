// IBTAidlCallback.aidl
package cn.com.geartech.gtplatform.model.aidl;

// Declare any non-default types here with import statements

interface IBTAidlCallback {

    void onGetBTVersion(String version);

    void onPairingStateChange(int status);

    void onGetPairedDevice(String device);

    void onGetPairedDeviceCount(int count);

    void onGetBTName(String name);

    void onVolumeChange(String volume);

    void onPhoneNumReceive(String phoneNum);

    void onHangup();

    void onSoundChannelChange(int status);

    void onHFPConnectionStateChange(int status);

    void onHFPIncomingCallEnd();

    void onHFPCallTerminated();

    void onHFPInCall();

    void onBTIncomingCall();

    void onBTOutgoingCall();

    void onGetPBAPStatus(int status);

    void onGetPhonebook(String phonebook);

    void onPhoneCallRejected(int result);

    void onPhoneMute(int result);

    void onDTMFPlayed(int result);

    void onCallingDeviceSwitched(int result);

    void onPhoneCallDial(int result);

    void onPhoneCallAnswered(int result);
}
