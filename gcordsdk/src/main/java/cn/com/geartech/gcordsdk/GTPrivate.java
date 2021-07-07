package cn.com.geartech.gcordsdk;

import android.app.ActivityManager;
import android.content.Context;
import android.os.RemoteException;

import java.util.List;
import java.util.Objects;

import cn.com.geartech.gtplatform.model.aidl.IGTAidlInterface;

/**
 * Created by dinner on 15/4/27.
 */
public class GTPrivate extends PhoneAPI {

    static GTPrivate instance = null;



    protected GTPrivate()
    {
        super();
    }


    static protected GTPrivate getGTPrivateInstance()
    {
        if(instance == null)
        {
            instance = new GTPrivate();
        }

        return instance;
    }



    public void clickHome()
    {
        PSTNInternal.getInstance().clickHome();
    }








    public void autoPickUpForAction(PickupActionListener l)
    {
        PSTNInternal.getInstance().autoPickUpForAction(l);
    }


    public void setFlash(byte interval)
    {
        PSTNInternal.getInstance().setFlash(interval);
    }

    public void jumpToRegApp()
    {
        PSTNInternal.getInstance().jumpToRegApp();
    }

    public String getLastDialNumber()
    {
        return PSTNInternal.getInstance().getLastDialNumber();
    }

    public String getCurrentDialNumber()
    {
        return PSTNInternal.getInstance().getCurrentDialNumber();
    }



    public boolean isPickingUp(){
        return PSTNInternal.getInstance().isPickingUp();
    }

    public void registerPushListener(GTPushEvent callback)
    {
        GTAidlHandler.getInstance().registerPushListener(callback);
    }

    public void setCheckUpgradeListener(GTAidlHandler.CheckUpgradeCallback callback)
    {
        GTAidlHandler.getInstance().setCheckUpgradeCallback(callback);
    }

    public boolean hasCheckUpgradeListener()
    {
        return GTAidlHandler.getInstance().hasCheckUpgradeCallback();
    }

    public void checkUpgrade()
    {
        GTAidlHandler.getInstance().checkUpgrade();
    }

    public void downloadAndInstallPackage(String sha1)
    {
        GTAidlHandler.getInstance().installPackage(sha1);
    }

    public void setDownloadPackageCallback(GTAidlHandler.DownloadPackageCallback callback)
    {
        GTAidlHandler.getInstance().setDownloadPackageCallback(callback);
    }

    public void setDialInterval(int interval)
    {
        PSTNInternal.getInstance().setDialInterval(interval);
    }

    public int getDialInterval()
    {
        return PSTNInternal.getInstance().getDialInterval();
    }



    public void startFMBroadcasting(int interval)
    {
        GTAidlHandler.getInstance().startFMBroadcasting(interval);
    }

    public void stopFMBroadcasting()
    {
        GTAidlHandler.getInstance().stopFMBroadcasting();
    }

    public void fm()
    {
        GTAidlHandler.getInstance().fm();
    }

    public int getMusicChannel()
    {
        return PSTNInternal.getInstance().getVOIPTunnel();
    }

    public void setMusicChannel(int channel)
    {
        PSTNInternal.getInstance().setVOIPTunnel(channel);
    }

    public boolean isPhysicalHandleOn()
    {
        return PSTNInternal.getInstance().isHandleUp;
    }

    public void startInterceptIncomingCall()
    {
        PSTNInternal.getInstance().startInterceptIncomingCall();

    }

    public void endInterceptIncomingCall()
    {
        PSTNInternal.getInstance().endInterceptIncomingCall();
    }

    public void volUp()
    {
        PSTNInternal.getInstance().volUp();
    }

    public void volDown()
    {
        PSTNInternal.getInstance().volDown();
    }


    public boolean setMicrophoneMute(boolean mute)
    {
        return PSTNInternal.getInstance().setMicrophoneMute(mute);
    }

    public void setSystemDialMode(String mode)
    {
        SettingAPI.getInstance().writeLauncherDialMode(mode);
    }



    // olny launcher itself can call this method
    public void setUseGlobalRingTonePrivate(boolean b){
        PSTNInternal.getInstance().setUseGlobalRingTone(b);
        PSTNInternal.getInstance().setUseClassicRing(!b);
    }

    public boolean isUsingGlobalRingTonePrivate(){
        return PSTNInternal.getInstance().isUsingGlobalRingTone();
    }

    public void  sendMessage(String msgName){
        try {
            GTAidlHandler.getIgtAidlInterface().sendMessage(msgName,0,0,"","");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isClassicRingOn() {
        //return super.isClassicRingOn();
        return PSTNInternal.getInstance().shouldUseClassicRing();
    }
}
