// IBTAidlInterface.aidl
package cn.com.geartech.gtplatform.model.aidl;

// Declare any non-default types here with import statements
import cn.com.geartech.gtplatform.model.aidl.IBTAidlCallback;

interface IBTAidlInterface {

    void registerCallback(IBTAidlCallback callback);

    void getBTVersion();

    void enterPairing();

    void deletePairedDevices();

    void disconnectLink();

    void enableBT(boolean enable);

    void getPairedList();

    void volUp();

    void volDown();

    void callInCalling(String phoneNum);

    void getBTName();

    void getBTHFPStatus();

    /**
    * HFP指令处理
    * 来电,拒接等事件处理
    */

    void acceptPhoneCall();

    void rejectPhoneCall();

    void mute();

    void redial();

    void switchCallingDevice();

    void dial(String phoneNum);

    void hangup();

    void playDTMF(String dtmf);
    // PBAP
    void getPBAPStatus();

    void pbapConnect();

    void pbapDisconnect();

    void pbapDownloadPhonebook();

    void pbapDownloadAbort();

    void switchAudioChannel(int channel);
}
