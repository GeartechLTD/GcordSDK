// SipPhoneApi.aidl
package cn.com.geartech.app;

// Declare any non-default types here with import statements

interface SipPhoneApi {
    int dial(String callUri);
    void sendDTMF(String dtmf);

    void hangoff(int sessionId, int reason);
    void answerPhoneCall(int sessionId);

    void login(String account, String password, String authName, int port, String registerAddress, String realm, String proxy, int transportMode);
    void logout();

    void startRecording(String path);
    void stopRecording();
    int getSpeakerVolume(int sessionId);
    void setSpeakerVolume(int sesssionId,int volume);

    void initSipPhone();
    void coerciveSipPhoneInitialization();

    String getUserName();
    String getAuthName();
    String getRealm();
    String getRegIp();
    String getPassword();
    int getPort();
    String getProxy();

    boolean isMicrophoneMute(int sessionId);
    void setMicrophoneMute(int sessionId, boolean isMute);
    void setMusicChannel(int musicChannel, boolean shouldStop);
    String getPhoneNumber(int sessionId);

    int getRegExpireTime();
    void setRegExpireTime(int time);
    boolean isSDKInitialized();
    int getSipRegTpt();
    void setLoginState(boolean isLogined);
    boolean isLogined();
    int dialByPhoneNumber(String phoneNumber);
    boolean shouldDialByLauncher();
    String getCurrentCallLogId();
}
