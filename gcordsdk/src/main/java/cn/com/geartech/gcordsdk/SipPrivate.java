package cn.com.geartech.gcordsdk;

import android.os.RemoteException;

/**
 * Created by cuizhy on 17/3/8.
 */

public class SipPrivate extends SipPhoneManager {

    //public static SipPrivate instance = null;

    protected static SipPhoneManager getInstance() {
        if(instance == null){
            instance = new SipPrivate();
        }
        return instance;
    }

    protected SipPrivate() {
        super();
    }

    public boolean isSipConnected()
    {
        if(sipPhoneApi != null)
        {
            try {
                sipPhoneApi.isSDKInitialized();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void connectToSip()
    {
        super.connectAIDL();
    }

    /**
     * force initialization
     * @throws RemoteException
     */
    public void coerciveSipPhoneInitialization() throws RemoteException {
        if (sipPhoneApi != null) {
            sipPhoneApi.coerciveSipPhoneInitialization();
        } else {
            connectAIDL();
            throw new RemoteException("aidl not connected");
        }
    }

    public boolean shouldDialByLauncher()
    {
        if (sipPhoneApi != null) {
            try {
                return sipPhoneApi.shouldDialByLauncher();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            connectAIDL();
        }
        return false;
    }

}
