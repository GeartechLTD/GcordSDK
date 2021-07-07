package cn.com.geartech.gcordsdk;

/**
 * Created by pangyuning on 17/6/22.
 */

public class RingToneManager {

    protected RingToneManager()
    {

    }

    /*
    设置底层自动播放来电铃声，对sim卡通话以及pstn通话均生效，对于pstn通话，优先级低于classicRing
     */
    public void setUseGlobalRingTone(boolean shouldUse)
    {
        PSTNInternal.getInstance().setUseGlobalRingTone(shouldUse);
    }

    /*
    是否使用底层自动播放来电铃声
     */
    public boolean isUseGlobalRingTone()
    {
        return PSTNInternal.getInstance().isUsingGlobalRingTone();
    }

    /*
    设置铃声文件路径
     */
    public boolean setGlobalRingTonePath(String path){

        return PSTNInternal.getInstance().setCurrentRingTone(path);
    }

    /*
    获取当前铃声路径
     */
    public String getGlobalRingTonePath(){

        return PSTNInternal.getInstance().getCurrentRingTone();
    }

    /**
     * 设置打开/关闭和弦铃声. 打开后,pstn来电时系统会自动根据电信局的信号来响铃,体验跟传统的固话一样.
     * @param turnOn
     */
    public void turnOnClassicRing(boolean turnOn)
    {
        PSTNInternal.getInstance().setUseClassicRing(turnOn);
    }

    /**
     * 是否打开和弦铃声
     * @return
     */
    public boolean isClassicRingOn()
    {
        return PSTNInternal.getInstance().shouldUseClassicRing();
    }

    /*
    自定义和弦铃声
     */
    public void setClassicRingPath(String path)
    {
        PSTNInternal.getInstance().setClassicRingPath(path);


    }

    public String getClassicRingPath()
    {
        return PSTNInternal.getInstance().getClassicRingPath();
    }

    public String getClassicalRingtoneFolderPath() {
        return PSTNInternal.getInstance().getClassicalRingtoneFolderPath();
    }

    public String getRingToneFolderPath(){

        return PSTNInternal.getInstance().getRingToneFolderPath();
    }

    public int getMaxVolume() {
        return getMaxVolumeImpl();
    }

    private int getMaxVolumeImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getMaxVolume();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return 0;
    }

    public int getCurrentVolume() {
        return getCurrentVolumeImpl();
    }

    private int getCurrentVolumeImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().getCurrentVolume();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setVolume(int volume){
        setVolumeImpl(volume);
    }

    private void setVolumeImpl(int volume) {
        try {
            GcordPreference.getInstance().getAIDL().setVolume(volume);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    boolean isTouchEffectEnabled(){
        return isTouchEffectEnabledImpl();
    }

    private boolean isTouchEffectEnabledImpl() {
        try {
            return GcordPreference.getInstance().getAIDL().isTouchEffectEnabled();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return false;
    }

    void setTouchEffectEnabled(boolean enabled){
        setTouchEffectEnabledImpl(enabled);
    }

    private void setTouchEffectEnabledImpl(boolean enabled) {
        try {
            GcordPreference.getInstance().getAIDL().setTouchEffectEnabled(enabled);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


}
