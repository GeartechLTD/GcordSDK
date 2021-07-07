/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Projects\\GTPlatform\\app\\src\\main\\aidl\\cn\\com\\geartech\\gtplatform\\model\\aidl\\IBTAidlInterface.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
public interface IBTAidlInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IBTAidlInterface
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IBTAidlInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IBTAidlInterface interface,
 * generating a proxy if needed.
 */
public static IBTAidlInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IBTAidlInterface))) {
return ((IBTAidlInterface)iin);
}
return new Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
IBTAidlCallback _arg0;
_arg0 = IBTAidlCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getBTVersion:
{
data.enforceInterface(DESCRIPTOR);
this.getBTVersion();
reply.writeNoException();
return true;
}
case TRANSACTION_enterPairing:
{
data.enforceInterface(DESCRIPTOR);
this.enterPairing();
reply.writeNoException();
return true;
}
case TRANSACTION_deletePairedDevices:
{
data.enforceInterface(DESCRIPTOR);
this.deletePairedDevices();
reply.writeNoException();
return true;
}
    case TRANSACTION_disconnectLink: {
        data.enforceInterface(DESCRIPTOR);
        this.disconnectLink();
        reply.writeNoException();
        return true;
    }
    case TRANSACTION_enableBT: {
        data.enforceInterface(DESCRIPTOR);
        boolean _arg0;
        _arg0 = (0 != data.readInt());
        this.enableBT(_arg0);
        reply.writeNoException();
        return true;
    }
case TRANSACTION_getPairedList:
{
data.enforceInterface(DESCRIPTOR);
this.getPairedList();
reply.writeNoException();
return true;
}
case TRANSACTION_volUp:
{
data.enforceInterface(DESCRIPTOR);
this.volUp();
reply.writeNoException();
return true;
}
case TRANSACTION_volDown:
{
data.enforceInterface(DESCRIPTOR);
this.volDown();
reply.writeNoException();
return true;
}
case TRANSACTION_callInCalling:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.callInCalling(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getBTName:
{
data.enforceInterface(DESCRIPTOR);
this.getBTName();
reply.writeNoException();
return true;
}
case TRANSACTION_getBTHFPStatus:
{
data.enforceInterface(DESCRIPTOR);
this.getBTHFPStatus();
reply.writeNoException();
return true;
}
case TRANSACTION_acceptPhoneCall:
{
data.enforceInterface(DESCRIPTOR);
this.acceptPhoneCall();
reply.writeNoException();
return true;
}
case TRANSACTION_rejectPhoneCall:
{
data.enforceInterface(DESCRIPTOR);
this.rejectPhoneCall();
reply.writeNoException();
return true;
}
case TRANSACTION_mute:
{
data.enforceInterface(DESCRIPTOR);
this.mute();
reply.writeNoException();
return true;
}
case TRANSACTION_redial:
{
data.enforceInterface(DESCRIPTOR);
this.redial();
reply.writeNoException();
return true;
}
case TRANSACTION_switchCallingDevice:
{
data.enforceInterface(DESCRIPTOR);
this.switchCallingDevice();
reply.writeNoException();
return true;
}
case TRANSACTION_dial:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.dial(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_hangup:
{
data.enforceInterface(DESCRIPTOR);
this.hangup();
reply.writeNoException();
return true;
}
case TRANSACTION_playDTMF:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.playDTMF(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getPBAPStatus:
{
data.enforceInterface(DESCRIPTOR);
this.getPBAPStatus();
reply.writeNoException();
return true;
}
case TRANSACTION_pbapConnect:
{
data.enforceInterface(DESCRIPTOR);
this.pbapConnect();
reply.writeNoException();
return true;
}
case TRANSACTION_pbapDisconnect:
{
data.enforceInterface(DESCRIPTOR);
this.pbapDisconnect();
reply.writeNoException();
return true;
}
case TRANSACTION_pbapDownloadPhonebook:
{
data.enforceInterface(DESCRIPTOR);
this.pbapDownloadPhonebook();
reply.writeNoException();
return true;
}
case TRANSACTION_pbapDownloadAbort:
{
data.enforceInterface(DESCRIPTOR);
this.pbapDownloadAbort();
reply.writeNoException();
return true;
}
case TRANSACTION_switchAudioChannel:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.switchAudioChannel(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IBTAidlInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void registerCallback(IBTAidlCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getBTVersion() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBTVersion, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void enterPairing() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_enterPairing, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void deletePairedDevices() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_deletePairedDevices, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void disconnectLink() throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(Stub.TRANSACTION_disconnectLink, _data, _reply, 0);
            _reply.readException();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    @Override
    public void enableBT(boolean enable) throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeInt(((enable) ? (1) : (0)));
            mRemote.transact(Stub.TRANSACTION_enableBT, _data, _reply, 0);
            _reply.readException();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }
@Override public void getPairedList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPairedList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void volUp() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_volUp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void volDown() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_volDown, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callInCalling(String phoneNum) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(phoneNum);
mRemote.transact(Stub.TRANSACTION_callInCalling, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getBTName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBTName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getBTHFPStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBTHFPStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
    * HFP指令处理
    * 来电,拒接等事件处理
    */
@Override public void acceptPhoneCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_acceptPhoneCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rejectPhoneCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_rejectPhoneCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void mute() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_mute, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void redial() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_redial, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void switchCallingDevice() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_switchCallingDevice, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void dial(String phoneNum) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(phoneNum);
mRemote.transact(Stub.TRANSACTION_dial, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void hangup() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_hangup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void playDTMF(String dtmf) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dtmf);
mRemote.transact(Stub.TRANSACTION_playDTMF, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// PBAP

@Override public void getPBAPStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPBAPStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void pbapConnect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pbapConnect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void pbapDisconnect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pbapDisconnect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void pbapDownloadPhonebook() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pbapDownloadPhonebook, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void pbapDownloadAbort() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pbapDownloadAbort, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void switchAudioChannel(int channel) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(channel);
mRemote.transact(Stub.TRANSACTION_switchAudioChannel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getBTVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_enterPairing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_deletePairedDevices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_disconnectLink = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_enableBT = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_getPairedList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_volUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_volDown = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_callInCalling = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getBTName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getBTHFPStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_acceptPhoneCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_rejectPhoneCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_mute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_redial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_switchCallingDevice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_dial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_hangup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_playDTMF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_getPBAPStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_pbapConnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_pbapDisconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_pbapDownloadPhonebook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_pbapDownloadAbort = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_switchAudioChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
}
public void registerCallback(IBTAidlCallback callback) throws android.os.RemoteException;
public void getBTVersion() throws android.os.RemoteException;
public void enterPairing() throws android.os.RemoteException;
public void deletePairedDevices() throws android.os.RemoteException;

    public void disconnectLink() throws android.os.RemoteException;

    public void enableBT(boolean enable) throws android.os.RemoteException;
public void getPairedList() throws android.os.RemoteException;
public void volUp() throws android.os.RemoteException;
public void volDown() throws android.os.RemoteException;
public void callInCalling(String phoneNum) throws android.os.RemoteException;
public void getBTName() throws android.os.RemoteException;
public void getBTHFPStatus() throws android.os.RemoteException;
/**
    * HFP指令处理
    * 来电,拒接等事件处理
    */
public void acceptPhoneCall() throws android.os.RemoteException;
public void rejectPhoneCall() throws android.os.RemoteException;
public void mute() throws android.os.RemoteException;
public void redial() throws android.os.RemoteException;
public void switchCallingDevice() throws android.os.RemoteException;
public void dial(String phoneNum) throws android.os.RemoteException;
public void hangup() throws android.os.RemoteException;
public void playDTMF(String dtmf) throws android.os.RemoteException;
// PBAP

public void getPBAPStatus() throws android.os.RemoteException;
public void pbapConnect() throws android.os.RemoteException;
public void pbapDisconnect() throws android.os.RemoteException;
public void pbapDownloadPhonebook() throws android.os.RemoteException;
public void pbapDownloadAbort() throws android.os.RemoteException;
public void switchAudioChannel(int channel) throws android.os.RemoteException;
}
