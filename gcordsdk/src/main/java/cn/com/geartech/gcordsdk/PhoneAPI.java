package cn.com.geartech.gcordsdk;

import android.content.Context;

/**
 * PSTN固话管理
 */
public class PhoneAPI {

    public static final String GCORD_INTENT_HANDLE_INCOMING_CALL = "gcord.intent_handle_incoming_call";

    public static final String GCORD_SCHEME_INCOMING_CALL = "gcord.scheme.incoming_call";

    public static int CODE_SUCCESS = 0;
    private static int ERROR_DIAL_WHILE_INCOMING_CALL = -1;

    public static int EXT_LINE_STATE_AVAILABLE = 0;
    public static int EXT_LINE_STATE_BUSY = 1;
    public static int EXT_LINE_STATE_NOT_CONNECTED = 2;

    public static int CALL_DIRECTION_NONE = -1;
    public static int CALL_DIRECTION_OUT = 1;
    public static int CALL_DIRECTION_IN = 0;

    /**
     * PSTN固话事件回调
     */
    public static abstract class PhoneEventListener {

        /**
         * PSTN的提机状态.当话机从待机状态变成提机状态时,会触发一次此事件
         *
         * @param state 免提或者手柄状态.
         */
        public abstract void onPickUp(PICKUP_STATE state);

        /**
         * PSTN来电事件.此事件必然会发生在onPhoneNumberReceived之前.使用自定义铃声的话,在此处开始播放铃声.
         */
        public abstract void onInComingCall();


        /**
         * 响铃结束事件.当拨号方挂断或者接听方接听之后,会触发此事件.使用自定义铃声的话,在此处结束播放铃声.
         * 由于PSTN协议的限制,此事件存在最多6s的延时.详情请查看PSTN事件解析.pdf
         */
        public abstract void onRingEnd();

        /**
         * 来电号码事件.如果用户没开通来显,则不会收到此事件.
         *
         * @param phoneNumber 来电的号码.如果是"P",则表示号码错误.如果是"O"(ou),则表示隐藏号码.
         */
        public abstract void onPhoneNumberReceived(String phoneNumber);

        /**
         * 通话中提机状态发生变化的回调.
         *
         * @param state 免提或者手柄状态
         */
        public abstract void onSwitchPhoneState(PICKUP_STATE state);

        /**
         * 通话挂断事件.当话机从提机状态变成挂断状态时,会触发此事件.
         * 对方挂断通话是不会触发此事件的.
         */
        public abstract void onHangOff();

        /**
         * 拨号的时候,每送出一个号码,就回调一次此事件.
         *
         * @param number 送出的号码,只会是0~9*#其中的一个.
         */
        public void onNumberSent(String number) {
        }

        /**
         * 电话线路从可用变成不可用时产生的回调.不可用包括外线没插好或者并线提机导致的繁忙.
         * 同一交换机下面的分机提机时,不会触发此事件.只有并线的机器提机才会触发此事件
         * 如果电话机跟pos机或者fax机做了并线,pos机或者fax机工作时,会产生干扰,也会导致线路不可用,触发此回调.
         */
        public void onLineBusy() {
        }

        /**
         * 电话线路从不可用变成可用时产生的回调.
         */
        public void onLineResumed() {
        }

        /**
         * 电话芯片工作异常时产生的回调.产生此事件时,请尝试重启话机.如果重启不好就需要返修.
         */
        public void onCommunicationError() {
        }

        /**
         * 忙音的回调,并非所有机型都支持此事件
         */
        public void onBusyTone() {
        }

        public void onDialFinish() {
        }

        public void onReversalOfPolarity() {

        }
    }

    /**
     * 调用闪断接口的回调
     */
    public interface FlashListener {
        /**
         * 闪断操作完成
         */
        void onFlashComplete();
    }


    public interface PickupActionListener {
        void onPickUp();
    }

    /**
     * 录音接口回调
     */
    public interface AudioRecordListener {
        /**
         * @param audioPath   录音文件的路径
         * @param customToken 调用录音接口时传进来的token
         */
        public abstract void onRecordSuccess(String audioPath, Object customToken);

        /**
         * @param errorCode    错误代码, 详见AUDIO_RECORD_ERROR_CODE_NOT_IN_CALL,AUDIO_RECORD_ERROR_CODE_DISK_ERROR,AUDIO_RECORD_ERROR_CODE_ALREADY_RECORDING,AUDIO_RECORD_ERROR_CODE_UNKNOWN
         * @param errorMessage 错误信息
         * @param customToken  调用录音接口时传进来的token
         */
        public abstract void onRecordFail(int errorCode, String errorMessage, Object customToken);
    }

    public enum Flash_Time {
        FLASH_100_MS,
        FLASH_300_MS,
        FLASH_600_MS,
        FLASH_1000_MS
    }

    public static final int VOLUME_CHANNEL_HANDLE = 0;
    public static final int VOLUME_CHANNEL_HANDSFREE = 1;

    public abstract interface VolumeCallback {
        /**
         * @param channel 1 for handsfree, 0 for handle
         * @param volume  volume of the channel
         */
        abstract void onVolume(int channel, int volume);
    }

    static PhoneAPI instance = null;


    public final static int AUDIO_RECORD_ERROR_CODE_NOT_IN_CALL = -1;
    public final static int AUDIO_RECORD_ERROR_CODE_DISK_ERROR = -2;
    public final static int AUDIO_RECORD_ERROR_CODE_ALREADY_RECORDING = -3;
    public final static int AUDIO_RECORD_ERROR_CODE_UNKNOWN = -99;

    public static final String EVENT_DIAL_FINISH = "cn.com.geartech.gtplatform.dial_finish_2";

    public static int MUSIC_CHANNEL_HANDS_FREE = 1;
    public static int MUSIC_CHANNEL_HANDLE = 0;


    public enum PICKUP_STATE {
        HANDLE,
        HANDS_FREE
    }


    protected PhoneAPI() {

    }

    protected static PhoneAPI getInstance() {
        if (instance == null) {
            instance = new PhoneAPI();
        }

        return instance;
    }

    protected void init(final Context c, String devId, String devToken) {
        PSTNInternal.getInstance().init(c, devId, devToken);
    }


    /**
     * 注册PSTN固话事件回调, 跟removePhoneEventListener相对应
     *
     * @param listener
     * @see PhoneEventListener
     */
    public void addPhoneEventListener(PhoneEventListener listener) {
        PSTNInternal.getInstance().addPhoneEventListener(listener);
    }

    /**
     * 反注册PSTN固话事件回调.
     *
     * @param listener
     */
    public void removePhoneEventListener(PhoneEventListener listener) {
        PSTNInternal.getInstance().removePhoneEventListener(listener);
    }

    /**
     * 注册PSTN固话事件回调, 多次调用的话只有最后一个会生效。
     *
     * @param listener
     */
    public void setPhoneEventListener(PhoneEventListener listener) {
        PSTNInternal.getInstance().setPhoneEventListener(listener);
    }

    /**
     * 拨号. 如果当前话机已经处于pstn提机状态,则直接送号码.如果未提机,则自动提机再送号码.
     * 在通话过程中调用此接口,则是送出DTMF号码.
     *
     * @param number 拨打的号码.
     * @return 0 为成功拨号. -1表示当前正在有通话进来,禁止拨号.
     */
    public int dial(final String number) {
        if (!PSTNInternal.getInstance().isInCall() && PSTNInternal.getInstance().firstRingComing) {
            return ERROR_DIAL_WHILE_INCOMING_CALL;
        }

        PSTNInternal.getInstance().dial(number);
        return CODE_SUCCESS;
    }

    public int safeDial(final String number) {
        if (!PSTNInternal.getInstance().isInCall() && PSTNInternal.getInstance().isRinging()) {
            return ERROR_DIAL_WHILE_INCOMING_CALL;
        }

        PSTNInternal.getInstance().dial(number);
        return CODE_SUCCESS;
    }

    /**
     * 挂断当前通话并重新拨号
     *
     * @param number 拨打的号码
     */
    public void hangOffAndDial(final String number) {
        PSTNInternal.getInstance().hangOffAndDial(number);
    }

    /**
     * 使用手柄方式进行提机
     */
    public void pickUpWithHandle() {
        PSTNInternal.getInstance().pickUpWithHandle();
    }

    private volatile boolean recording = false;

    public boolean isRecording() {
        return recording;
    }

    /**
     * 使用免提方式进行提机
     */
    public void pickUpWithHandsFree() {
        //new Exception().printStackTrace();
        PSTNInternal.getInstance().pickUpWithHandsFree();
    }

    /**
     * 在PSTN通话中切换声道
     *
     * @param state 免提或者手柄状态
     */
    public void switchPhoneState(PICKUP_STATE state) {
        PSTNInternal.getInstance().SwitchPhoneState(state);
    }

    /**
     * 挂断当前PSTN通话
     */
    public void hangOff() {
        PSTNInternal.getInstance().hangOff();
    }

    /**
     * 拒绝当前pstn来电,其效果等同于先接听再挂断.
     */
    public void reject() {
        PSTNInternal.getInstance().reject();
    }


    /**
     * 开始录音, 格式为amr
     *
     * @param listener    录音回调
     * @param customToken 自定义的token.会在回调里面把这个token传回去.
     */
    public void startRecording(final AudioRecordListener listener, Object customToken) {
        synchronized (this) {
            PSTNInternal.getInstance().startRecording(listener, customToken);
            recording = true;
        }
    }

    /**
     * 开始录音, 格式为wav
     *
     * @param listener    录音回调
     * @param customToken 自定义的token.会在回调里面把这个token传回去.
     */
    public void startRecordingWav(final AudioRecordListener listener, Object customToken) {
        synchronized (this) {
            PSTNInternal.getInstance().startRecordingWav(listener, customToken);
            recording = true;
        }
    }

    /**
     * 开始录音, 格式为mp3
     *
     * @param listener    录音回调
     * @param customToken 自定义的token.会在回调里面把这个token传回去.
     */
    public void startRecordingMp3(final AudioRecordListener listener, Object customToken) {
        synchronized (this) {
            PSTNInternal.getInstance().startRecordingMp3(listener, customToken);
            recording = true;
        }
    }

    /**
     * 通话录音，录音格式是mp3
     * @param listener 录音回调
     * @param customToken 自定义的token.会在回调里面把这个token传回去.
     * @param sampleRate mp3文件的采样率，例如44100，8000等
     * @param bitRate mp3文件的比特率，例如32（表示32k的比特率）
     */
    public void startRecordingMp3(final AudioRecordListener listener, Object customToken, int sampleRate, int bitRate) {
        synchronized (this) {
            PSTNInternal.getInstance().startRecordingMp3(listener, customToken, sampleRate, bitRate);
            recording = true;
        }
    }

    /**
     * 停止当前录音
     */
    public void stopRecording() {
        synchronized (this) {
            PSTNInternal.getInstance().stopRecording();
            recording = false;
        }
    }


    public void setAudioRecordSortByDateDirectory(boolean use) {
        PSTNInternal.getInstance().setAudioRecordSortByDateDirectory(true);
    }

    public boolean isAudioRecordSortByDateDirectory() {
        return PSTNInternal.getInstance().isAudioRecordSortByDateDirectory();
    }

    /**
     * 设置打开/关闭和弦铃声. 打开后,pstn来电时系统会自动根据电信局的信号来响铃,体验跟传统的固话一样.
     *
     * @param turnOn
     */
    public void turnOnClassicRing(boolean turnOn) {
        PSTNInternal.getInstance().setUseClassicRing(turnOn);
        PSTNInternal.getInstance().setUseGlobalRingTone(!turnOn);
    }

    /**
     * 是否打开和弦铃声
     *
     * @return
     */
    public boolean isClassicRingOn() {
        return PSTNInternal.getInstance().shouldUseClassicRing();
    }

    public void setClassicRingPath(String path) {
        PSTNInternal.getInstance().setClassicRingPath(path);
    }

    public String getClassicRingPath() {
        return PSTNInternal.getInstance().getClassicRingPath();
    }

    public String getClassicalRingtoneFolderPath() {
        return PSTNInternal.getInstance().getClassicalRingtoneFolderPath();
    }

    public String getRingToneFolderPath() {

        return PSTNInternal.getInstance().getRingToneFolderPath();
    }

    /**
     * 设置闪断时长.闪断时长默认为300ms.闪断时长要根据交换机的需求来设置.
     *
     * @param flash_time 闪断时长
     */
    public void setFlashTime(Flash_Time flash_time) {
        switch (flash_time) {
            case FLASH_100_MS:
                PSTNInternal.getInstance().setFlashTime((byte) 0x31);
                break;
            case FLASH_300_MS:
                PSTNInternal.getInstance().setFlashTime((byte) 0x32);
                break;
            case FLASH_600_MS:
                PSTNInternal.getInstance().setFlashTime((byte) 0x33);
                break;
            case FLASH_1000_MS:
                PSTNInternal.getInstance().setFlashTime((byte) 0x34);
                break;
        }
    }

    /**
     * 获取闪断时长
     *
     * @return 当前系统设置的闪断时长
     */
    public Flash_Time getFlashTime() {
        switch (PSTNInternal.getInstance().getFlashTime()) {
            case 0x31:
                return Flash_Time.FLASH_100_MS;
            case 0x32:
                return Flash_Time.FLASH_300_MS;
            case 0x33:
                return Flash_Time.FLASH_600_MS;
            case 0x34:
                return Flash_Time.FLASH_1000_MS;
        }

        return Flash_Time.FLASH_300_MS;
    }

    /**
     * 闪断, 使用之前建议先设置好闪断时长,如果闪断时长设置不正确,交换机会把闪断认为是挂断+提机
     *
     * @param flashListener 闪断完成的回调
     */
    public void flash(final FlashListener flashListener) {
        PSTNInternal.getInstance().setFlashListener(flashListener);
        PSTNInternal.getInstance().setFlash(PSTNInternal.getInstance().getFlashTime());
    }


//    public void setHomeKeyCustomEventEnabled(boolean enable)
//    {
//        PSTNInternal.getInstance().setHomeKeyCustomEventEnabled(enable);
//    }

    /**
     * 判断当前是否处于PSTN提机状态
     * 并非是判断是否接通
     *
     * @return
     */
    public boolean isInCall() {
        return PSTNInternal.getInstance().isInCall();
    }

    @Deprecated
    public void performHomeEvent() {
        PSTNInternal.getInstance().performHomeEvent();
    }

    @Deprecated
    /**
     * 启用/禁用pstn功能
     * 不建议使用,除非完全不想使用pstn功能.
     */
    public void enablePSTN(boolean enable) {
        PSTNInternal.getInstance().setEnablePSTN(enable);
    }

//    public boolean isLineBusy()
//    {
//        return PSTNInternal.getInstance().isLineBusy;
//    }

    /**
     * 检查外线状态.检查完成后会触发PhoneEventListener的onLineBusy或者onLineResumed
     */
    public void checkExtLineState() {
        PSTNInternal.getInstance().checkExtLineState();
    }


    public void switchToHandsFree4VOIP() {
        PSTNInternal.getInstance().setVOIPTunnel(MUSIC_CHANNEL_HANDS_FREE);
    }

    public void switchToHandle4VOIP() {
        PSTNInternal.getInstance().setVOIPTunnel(MUSIC_CHANNEL_HANDLE);
    }

    public int getCurrentMusicChannel4VOIP() {
        return PSTNInternal.getInstance().getVOIPTunnel();
    }

    /**
     * 设置是否等到来电号码再开始响铃.
     * 如果来电之后3秒内没有送号码,还是会响铃.
     *
     * @param shouldWait true-等待来电号码，false-收到来电直接响铃
     */
    public void setIncomingCallShouldWaitForPhoneNumber(boolean shouldWait) {
        PSTNInternal.getInstance().setIncomingCallShouldWaitForPhoneNumber(shouldWait);
    }

    /**
     * 设置拨号间隔, 建议取值范围为200~400ms.设置太短会拨不出号,设置太长会导致用户等待过久.
     *
     * @param interval 拨号间隔
     */
    public void setDialInterval(int interval) {
        PSTNInternal.getInstance().setDialInterval(interval);
    }

    /**
     * 获取当前拨号间隔
     *
     * @return
     */
    public int getDialInterval() {
        return PSTNInternal.getInstance().getDialInterval();
    }

    /**
     * 获取当前外线状态 EXT_LINE_STATE_AVAILABLE, EXT_LINE_STATE_BUSY, EXT_LINE_STATE_NOT_CONNECTED
     *
     * @return 当前外线状态
     */
    public int getExtLineState() {

        return PSTNInternal.getInstance().getExtLineState();
    }

    /**
     * 判断是否正在响铃
     *
     * @return
     */
    public boolean isRinging() {
        return PSTNInternal.getInstance().isRinging();
    }

    /**
     * 获取正在来电的号码
     *
     * @return
     */
    public String getIncomingNumber() {
        return PSTNInternal.getInstance().getIncomingNumber();
    }

//    /**
//     *
//     * @param mute true为静音,false为非静音。仅对当前通话生效,每一通电话的初始状态都是非静音
//     * @return true表示设置成功。false为设置失败或者当前设备不支持静音功能
//     */
//    public boolean setMicrophoneMute(boolean mute)
//    {
//        return PSTNInternal.getInstance().setMicrophoneMute(mute);
//    }

    /**
     * 静音麦克风，仅在通话中生效
     */

    public boolean setCurrentCallMute(boolean mute) {
        return PSTNInternal.getInstance().setMicrophoneMute(mute);
    }

    /**
     * 获取麦克风静音状态
     *
     * @return
     */
    public boolean isCurrentCallMute() {
        return PSTNInternal.getInstance().isCurrentCallMute();
    }

    /**
     * 获取当前通话是来电还是去电
     * CALL_DIRECTION_NONE CALL_DIRECTION_OUT CALL_DIRECTION_IN
     */
    public int getCurrentCallDirection() {
        return PSTNInternal.getInstance().getCallDirection();
    }

    /**
     * 判断是否处于免提提机状态
     *
     * @return
     */
    public boolean isHandsFreeOn() {
        return PSTNInternal.getInstance().isHandsFreeOn();
    }

    /**
     * 判断是否处于手柄提机状态
     *
     * @return
     */
    public boolean isHandleOn() {
        return PSTNInternal.getInstance().isHandleOn();
    }

    /**
     * 判断是否开启自动答录
     *
     * @return
     */
    public boolean isAutoAnswerOpen() {
        return PSTNInternal.getInstance().isAutoAnswerOpen();
    }

    /**
     * 开关自动答录
     *
     * @param enable
     */
    public void enableAutoAnswer(boolean enable) {
        PSTNInternal.getInstance().enableAutoAnswer(enable);
    }

    /**
     * 取消自动答录
     */
    public void cancelAutoAnswer() {
        PSTNInternal.getInstance().cancelAutoAnswer();
    }


    public void setUseGlobalRingTone(boolean shouldUse) {
        PSTNInternal.getInstance().setUseGlobalRingTone(shouldUse);
        PSTNInternal.getInstance().setUseClassicRing(!shouldUse);
    }

    @SuppressWarnings("unused")
    public boolean isUseGlobalRingTone() {
        return PSTNInternal.getInstance().isUsingGlobalRingTone();
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean setGlobalRingTonePath(String path) {

        return PSTNInternal.getInstance().setCurrentRingTone(path);
    }

    public String getGlobalRingTonePath() {

        return PSTNInternal.getInstance().getCurrentRingTone();
    }


    public String calculateBareNumberWithDialPreference(String orgNumber) {
        if (orgNumber == null) {
            return "";
        }

        orgNumber = orgNumber.replace(".", "");
        orgNumber = orgNumber.replace("-", "");
        orgNumber = orgNumber.replace("+", "");
        String prefix = GcordSDK.getInstance().getGcordPreference().getDialPrefix();
        prefix = prefix.replace(".", "");

        boolean isSmareDialOn = GcordSDK.getInstance().getGcordPreference().isSmartDialOn();
        String areaCode = GcordSDK.getInstance().getGcordPreference().getCurrentAreaCode();

        String result = orgNumber;

        if (isSmareDialOn && orgNumber.startsWith(prefix)) {
            result = result.substring(prefix.length());
        }

        if (result.startsWith("0") && result.length() > 1) {
            if (isMobileNumber(result.substring(1))) {
                // mobile number
                return result.substring(1);
            }
        }

        return result;
    }

    static boolean isMobileNumber(String number) {
        if (number == null) {
            return false;
        }
        return number.length() == 11 && number.startsWith("1");
    }

    /**
     * @param channel        0 for 手柄 1 for 免提
     * @param volumeCallback
     */
    public void getVolume(int channel, VolumeCallback volumeCallback) {
        PSTNInternal.getInstance().getVolume(channel, volumeCallback);
    }

    /**
     * @param volume         音量, 取值为0 ~ MaxVolume。
     * @param channel
     * @param volumeCallback
     * @see #getMaxVolume()
     */
    public void setVolume(int volume, int channel, VolumeCallback volumeCallback) {
        PSTNInternal.getInstance().setVolume(volume, channel, volumeCallback);
    }

    /**
     * 获取设备的最大音量。
     * 如果不支持调节音量，则返回-1
     *
     * @return
     */
    public int getMaxVolume() {
        return PSTNInternal.getInstance().getMaxVolume();
    }

    /**
     * 开始往pstn模块播放安卓声音
     */
    public void benginOutputAudioToPSTN() {
        GTAidlHandler.getInstance().beginPSTNAudioOutput();
    }

    /**
     * 停止往pstn模块播放安卓声音
     */

    public void stopOutputAudioToPSTN() {
        GTAidlHandler.getInstance().stopPSTNAudioOutput();
    }

    /**
     * 如果设为true，则不会在call log文件夹里面额外拷贝一份录音
     *
     * @param noCopy
     */

    public void setShouldNotCopyExtraAudioRecordToSysFolder(boolean noCopy) {
        PSTNInternal.getInstance().setShouldNotCopyExtraAudioRecordToSysFolder(noCopy);
    }


    public boolean isReversalOfPolarityEnabled() {
        return PSTNInternal.getInstance().isReversalOfPolarityEnabled();
    }

    /**
     * 读取收到忙音事件是否自动挂机
     * 注意：关闭自动挂机后，通话中对端挂机后话机将不会自动挂机；之后如果开发者不手动hangOff()，后续将无法打进和拨出电话。
     */
    public boolean isAutoHangOffWhenBusyTone() {
        return PSTNInternal.getInstance().isAutoHangOffWhenBusyTone();
    }

    /**
     * 设置收到忙音事件是否自动挂机
     * 注意：关闭自动挂机后，通话中对端挂机后话机将不会自动挂机；之后如果开发者不手动hangOff()，后续将无法打进和拨出电话。
     */
    public void setAutoHangOffWhenBusyTone(boolean should) {
        PSTNInternal.getInstance().setAutoHangOffWhenBusyTone(should);
    }
}
