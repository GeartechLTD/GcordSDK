package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;

import cn.com.geartech.gtplatform.model.aidl.IBTAidlCallback;
import cn.com.geartech.gtplatform.model.aidl.IBTAidlInterface;

/**
 * @author xujiahui
 * @since 2016/7/22
 */
public class BTManager extends GcordHelper {
    private static final String TAG = "BTManager";

    private static BTManager sInstance = null;

    private ArrayList<OnBtOperationListener> onBtOperationListeners = null;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED)) {
                connectAIDL();
            }
        }
    };

    private BTManager()
    {

    }

    protected static BTManager getInstance() {
        if (sInstance == null) {
            sInstance = new BTManager();
        }

        return sInstance;
    }

    public void addOnBtOperationListener(OnBtOperationListener listener) {
        if (onBtOperationListeners == null) {
            onBtOperationListeners = new ArrayList<OnBtOperationListener>();
        }
        if (!onBtOperationListeners.contains(listener)) {
            onBtOperationListeners.add(listener);
        }
    }

    public void removeOnBtOperationListener(OnBtOperationListener listener) {
        if (onBtOperationListeners != null && onBtOperationListeners.contains(listener)) {
            onBtOperationListeners.remove(listener);
        }
    }

    public enum PairingState {
        IDLE,
        INVALID,
        PAIRING,
        SUCCESS,
        TIME_OUT,
        FAILED
    }

    public enum ConnectionState {
        NOT_CONNECTED,
        CONNECTING,
        CONNECTED
    }

    private Context context;

    void init(Context c) {
        context = c;
        IntentFilter intentFilter = new IntentFilter(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED);
        context.registerReceiver(mReceiver, intentFilter);
        connectAIDL();
    }

    private void connectAIDL() {
        Intent intent = new Intent("cn.com.geartech.gtplatform.model.aidl.IBTAidlInterface");
        intent.setPackage("cn.com.geartech.gtplatform");
        intent.putExtra("packageName", context.getPackageName());
        boolean ret = context.bindService(intent, btSC, Context.BIND_AUTO_CREATE);
        if (!ret) {
            if (internalInitCallback != null) {
                internalInitCallback.onInitFailed();
            }
        }
    }

    private IBTAidlInterface mBTAidlInterface;

    ServiceConnection btSC = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mBTAidlInterface = IBTAidlInterface.Stub.asInterface(service);
                if (mBTAidlInterface != null) {
                    mBTAidlInterface.registerCallback(mBTAidlCallback);
                }

                Log.e("GcordSDK", "on service connected");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBTAidlInterface = null;

            Log.e("GcordSDK", "on service disconnected");
        }
    };

    IBTAidlCallback.Stub mBTAidlCallback = new IBTAidlCallback.Stub() {
        @Override
        public void onGetBTVersion(String version) throws RemoteException {

        }

        @Override
        public void onPairingStateChange(int status) throws RemoteException {
            if (onBtOperationListeners != null) {
                for (OnBtOperationListener listener : onBtOperationListeners) {
                    listener.onPairingStateChange(PairingState.values()[status]);
                }
            }
        }

        @Override
        public void onGetPairedDevice(String device) throws RemoteException {
            String[] split = device.split(",");
            if ("0".equals(split[0])) {
                if (onBtOperationListeners != null) {
                    for (OnBtOperationListener listener : onBtOperationListeners) {
                        listener.onGetPairedDevice(split[2].replaceAll("\"", ""));
                    }
                }
            }
        }

        @Override
        public void onGetPairedDeviceCount(int count) throws RemoteException {
            if (onBtOperationListeners != null) {
                for (OnBtOperationListener listener :
                        onBtOperationListeners) {
                    listener.onGetPairedDeviceCount(count);
                }
            }
        }

        @Override
        public void onGetBTName(String name) throws RemoteException {
            if (onBtOperationListeners != null) {
                for (OnBtOperationListener listener :
                        onBtOperationListeners) {
                    listener.onGetBTName(name);
                }
            }
        }

        @Override
        public void onVolumeChange(String volume) throws RemoteException {
            if (onBtOperationListeners != null) {
                for (OnBtOperationListener listener :
                        onBtOperationListeners) {
                    listener.onVolumeChange(volume);
                }
            }
        }

        @Override
        public void onPhoneNumReceive(String phoneNum) throws RemoteException {
            if (onBtOperationListeners != null) {
                for (OnBtOperationListener listener :
                        onBtOperationListeners) {
                    listener.onPhoneNumberReceived(phoneNum);
                }
            }
        }

        @Override
        public void onHangup() throws RemoteException {

        }

        @Override
        public void onSoundChannelChange(int status) throws RemoteException {

        }

        @Override
        public void onHFPConnectionStateChange(int status) throws RemoteException {
            if (onBtOperationListeners != null) {
                for (OnBtOperationListener listener :
                        onBtOperationListeners) {
                    listener.onHFPConnectionStateChange(ConnectionState.values()[status]);
                }
            }
        }

        @Override
        public void onHFPIncomingCallEnd() throws RemoteException {

        }

        @Override
        public void onHFPCallTerminated() throws RemoteException {

        }

        @Override
        public void onHFPInCall() throws RemoteException {
            if (onBtOperationListeners != null) {
                for (OnBtOperationListener listener :
                        onBtOperationListeners) {
                    listener.onHFPInCall();
                }
            }
        }

        @Override
        public void onBTIncomingCall() throws RemoteException {
            if (onBtOperationListeners != null) {
                for (OnBtOperationListener listener :
                        onBtOperationListeners) {
                    listener.onBTIncomingCall();
                }
            }
        }

        @Override
        public void onBTOutgoingCall() throws RemoteException {

        }

        @Override
        public void onGetPBAPStatus(int status) throws RemoteException {
            if (status == 2) {
                BTManager.getInstance().pbapDownloadPhonebook();
            }
        }

        @Override
        public void onGetPhonebook(String phonebook) throws RemoteException {
            if (onBtOperationListeners != null) {
                for (OnBtOperationListener listener :
                        onBtOperationListeners) {
                    listener.onGetPhonebook(phonebook);
                }
            }
        }

        @Override
        public void onPhoneCallRejected(int result) throws RemoteException {
            try {
                if (onBtOperationListeners != null) {
                    for (OnBtOperationListener listener :
                            onBtOperationListeners) {
                        listener.onPhoneCallRejected(result);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onPhoneMute(int result) throws RemoteException {

        }

        @Override
        public void onDTMFPlayed(int result) throws RemoteException {

        }

        @Override
        public void onCallingDeviceSwitched(int result) throws RemoteException {

        }

        @Override
        public void onPhoneCallDial(int result) throws RemoteException {
            switchAudioChannel(6);
        }

        @Override
        public void onPhoneCallAnswered(int result) throws RemoteException {
            try {
                if (onBtOperationListeners != null) {
                    for (OnBtOperationListener listener :
                            onBtOperationListeners) {
                        listener.onPhoneCallAccepted(result);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };

    public void getBTVersion() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.getBTVersion();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void enterPairing() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.enterPairing();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void deletePairedDevices() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.deletePairedDevices();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void disconnectLink() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.disconnectLink();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void enableBT(boolean enable) {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.enableBT(enable);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void getPairedList() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.getPairedList();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void volUp() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.volUp();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void volDown() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.volDown();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void callInCalling(String phoneNum) {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.callInCalling(phoneNum);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void getBTName() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.getBTName();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void getBTHFPStatus() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.getBTHFPStatus();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void acceptPhoneCall() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.acceptPhoneCall();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void rejectPhoneCall() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.rejectPhoneCall();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void mute() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.mute();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void redial(String phoneNum) {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.redial();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void switchCallingDevice() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.switchCallingDevice();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void dial(String phoneNum) {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.dial(phoneNum);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void hangup() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.hangup();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * @param str 0-9,a-z,*#
     */
    public void playDTMF(String str) {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.playDTMF(str);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void getPBAPStatus() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.getPBAPStatus();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void pbapConnect() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.pbapConnect();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void pbapDisconnect() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.pbapDisconnect();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void pbapDownloadPhonebook() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.pbapDownloadPhonebook();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void pbapDownloadAbort() {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.pbapDownloadAbort();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void switchAudioChannel(int mode) {
        try {
            if (mBTAidlInterface != null) {
                mBTAidlInterface.switchAudioChannel(mode);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void startRecording(String path)
    {

    }

    public interface OnBtOperationListener {
        void onGetBTName(String name);

        void onVolumeChange(String volume);

        void onBTIncomingCall();

        void onPhoneNumberReceived(String phoneNumber);

        void onPairingStateChange(PairingState status);

        void onHFPConnectionStateChange(ConnectionState status);

        void onGetPairedDevice(String device);

        void onGetPairedDeviceCount(int count);

        void onGetPhonebook(String phonebook);

        void onPhoneCallAccepted(int result);

        void onPhoneCallRejected(int result);

        void onHFPInCall();
    }

    public static abstract class BtOperationListenerAdapter implements OnBtOperationListener {

        @Override
        public void onGetBTName(String name) {

        }

        @Override
        public void onVolumeChange(String volume) {

        }

        @Override
        public void onBTIncomingCall() {

        }

        @Override
        public void onPhoneNumberReceived(String phoneNumber) {

        }

        @Override
        public void onPairingStateChange(PairingState status) {

        }

        @Override
        public void onHFPConnectionStateChange(ConnectionState status) {

        }

        @Override
        public void onGetPairedDevice(String device) {

        }

        @Override
        public void onGetPairedDeviceCount(int count) {

        }

        @Override
        public void onGetPhonebook(String phonebook) {

        }

        @Override
        public void onPhoneCallAccepted(int result) {

        }

        @Override
        public void onPhoneCallRejected(int result) {

        }

        @Override
        public void onHFPInCall()
        {

        }
    }

}
