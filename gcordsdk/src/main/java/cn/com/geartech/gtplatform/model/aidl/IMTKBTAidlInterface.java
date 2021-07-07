/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/bada/Documents/android projects/GTPlatform/app/src/main/aidl/cn/com/geartech/gtplatform/model/aidl/IMTKBTAidlInterface.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
public interface IMTKBTAidlInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IMTKBTAidlInterface
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IMTKBTAidlInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IMTKBTAidlInterface interface,
 * generating a proxy if needed.
 */
public static IMTKBTAidlInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IMTKBTAidlInterface))) {
return ((IMTKBTAidlInterface)iin);
}
return new Proxy(obj);
}
@Override
public android.os.IBinder asBinder()
{
return this;
}
@Override
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_addCallback:
{
data.enforceInterface(DESCRIPTOR);
IMTKBTCallback _arg0;
_arg0 = IMTKBTCallback.Stub.asInterface(data.readStrongBinder());
this.addCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeCallback:
{
data.enforceInterface(DESCRIPTOR);
IMTKBTCallback _arg0;
_arg0 = IMTKBTCallback.Stub.asInterface(data.readStrongBinder());
this.removeCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_restBluetooth:
{
data.enforceInterface(DESCRIPTOR);
this.restBluetooth();
reply.writeNoException();
return true;
}
case TRANSACTION_getLocalName:
{
data.enforceInterface(DESCRIPTOR);
this.getLocalName();
reply.writeNoException();
return true;
}
case TRANSACTION_setLocalName:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.setLocalName(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getLocalAddress:
{
data.enforceInterface(DESCRIPTOR);
this.getLocalAddress();
reply.writeNoException();
return true;
}
case TRANSACTION_setPairMode:
{
data.enforceInterface(DESCRIPTOR);
this.setPairMode();
reply.writeNoException();
return true;
}
case TRANSACTION_cancelPairMode:
{
data.enforceInterface(DESCRIPTOR);
this.cancelPairMode();
reply.writeNoException();
return true;
}
case TRANSACTION_connectLast:
{
data.enforceInterface(DESCRIPTOR);
this.connectLast();
reply.writeNoException();
return true;
}
case TRANSACTION_connectDevice:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.connectDevice(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_disconnect:
{
data.enforceInterface(DESCRIPTOR);
this.disconnect();
reply.writeNoException();
return true;
}
case TRANSACTION_deletePair:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.deletePair(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_startDiscovery:
{
data.enforceInterface(DESCRIPTOR);
this.startDiscovery();
reply.writeNoException();
return true;
}
case TRANSACTION_getPairList:
{
data.enforceInterface(DESCRIPTOR);
this.getPairList();
reply.writeNoException();
return true;
}
case TRANSACTION_stopDiscovery:
{
data.enforceInterface(DESCRIPTOR);
this.stopDiscovery();
reply.writeNoException();
return true;
}
case TRANSACTION_phoneAnswer:
{
data.enforceInterface(DESCRIPTOR);
this.phoneAnswer();
reply.writeNoException();
return true;
}
case TRANSACTION_phoneHangUp:
{
data.enforceInterface(DESCRIPTOR);
this.phoneHangUp();
reply.writeNoException();
return true;
}
case TRANSACTION_phoneDail:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.phoneDail(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_phoneTransmitDTMFCode:
{
data.enforceInterface(DESCRIPTOR);
char _arg0;
_arg0 = (char)data.readInt();
this.phoneTransmitDTMFCode(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_phoneTransfer:
{
data.enforceInterface(DESCRIPTOR);
this.phoneTransfer();
reply.writeNoException();
return true;
}
case TRANSACTION_phoneTransferBack:
{
data.enforceInterface(DESCRIPTOR);
this.phoneTransferBack();
reply.writeNoException();
return true;
}
case TRANSACTION_phoneBookStartUpdate:
{
data.enforceInterface(DESCRIPTOR);
this.phoneBookStartUpdate();
reply.writeNoException();
return true;
}
case TRANSACTION_callLogstartUpdate:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.callLogstartUpdate(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_musicPlayOrPause:
{
data.enforceInterface(DESCRIPTOR);
this.musicPlayOrPause();
reply.writeNoException();
return true;
}
case TRANSACTION_musicStop:
{
data.enforceInterface(DESCRIPTOR);
this.musicStop();
reply.writeNoException();
return true;
}
case TRANSACTION_musicPrevious:
{
data.enforceInterface(DESCRIPTOR);
this.musicPrevious();
reply.writeNoException();
return true;
}
case TRANSACTION_musicNext:
{
data.enforceInterface(DESCRIPTOR);
this.musicNext();
reply.writeNoException();
return true;
}
case TRANSACTION_musicMute:
{
data.enforceInterface(DESCRIPTOR);
this.musicMute();
reply.writeNoException();
return true;
}
case TRANSACTION_musicUnmute:
{
data.enforceInterface(DESCRIPTOR);
this.musicUnmute();
reply.writeNoException();
return true;
}
case TRANSACTION_musicBackground:
{
data.enforceInterface(DESCRIPTOR);
this.musicBackground();
reply.writeNoException();
return true;
}
case TRANSACTION_musicNormal:
{
data.enforceInterface(DESCRIPTOR);
this.musicNormal();
reply.writeNoException();
return true;
}
case TRANSACTION_getMusicInfo:
{
data.enforceInterface(DESCRIPTOR);
this.getMusicInfo();
reply.writeNoException();
return true;
}
case TRANSACTION_inquiryHfpStatus:
{
data.enforceInterface(DESCRIPTOR);
this.inquiryHfpStatus();
reply.writeNoException();
return true;
}
case TRANSACTION_getCurrentDeviceAddr:
{
data.enforceInterface(DESCRIPTOR);
this.getCurrentDeviceAddr();
reply.writeNoException();
return true;
}
case TRANSACTION_getCurrentDeviceName:
{
data.enforceInterface(DESCRIPTOR);
this.getCurrentDeviceName();
reply.writeNoException();
return true;
}
case TRANSACTION_pauseDownLoadContact:
{
data.enforceInterface(DESCRIPTOR);
this.pauseDownLoadContact();
reply.writeNoException();
return true;
}
case TRANSACTION_inquiryAuthorizationStatus:
{
data.enforceInterface(DESCRIPTOR);
this.inquiryAuthorizationStatus();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IMTKBTAidlInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override
public android.os.IBinder asBinder()
{
return mRemote;
}
public String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override
public void addCallback(IMTKBTCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void removeCallback(IMTKBTCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void restBluetooth() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_restBluetooth, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void getLocalName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getLocalName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void setLocalName(String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_setLocalName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void getLocalAddress() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getLocalAddress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void setPairMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_setPairMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void cancelPairMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cancelPairMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void connectLast() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_connectLast, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void connectDevice(String addr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(addr);
mRemote.transact(Stub.TRANSACTION_connectDevice, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void disconnect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disconnect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void deletePair(String addr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(addr);
mRemote.transact(Stub.TRANSACTION_deletePair, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void startDiscovery() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startDiscovery, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void getPairList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPairList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void stopDiscovery() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopDiscovery, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void phoneAnswer() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_phoneAnswer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void phoneHangUp() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_phoneHangUp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void phoneDail(String phonenum) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(phonenum);
mRemote.transact(Stub.TRANSACTION_phoneDail, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void phoneTransmitDTMFCode(char code) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((int)code));
mRemote.transact(Stub.TRANSACTION_phoneTransmitDTMFCode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void phoneTransfer() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_phoneTransfer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void phoneTransferBack() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_phoneTransferBack, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void phoneBookStartUpdate() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_phoneBookStartUpdate, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void callLogstartUpdate(int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_callLogstartUpdate, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void musicPlayOrPause() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicPlayOrPause, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void musicStop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicStop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void musicPrevious() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicPrevious, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void musicNext() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicNext, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void musicMute() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicMute, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void musicUnmute() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicUnmute, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void musicBackground() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicBackground, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void musicNormal() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_musicNormal, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void getMusicInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMusicInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void inquiryHfpStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_inquiryHfpStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void getCurrentDeviceAddr() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentDeviceAddr, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void getCurrentDeviceName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentDeviceName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void pauseDownLoadContact() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pauseDownLoadContact, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void inquiryAuthorizationStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_inquiryAuthorizationStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_addCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_removeCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_restBluetooth = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getLocalName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_setLocalName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getLocalAddress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_setPairMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_cancelPairMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_connectLast = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_connectDevice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_disconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_deletePair = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_startDiscovery = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getPairList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_stopDiscovery = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_phoneAnswer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_phoneHangUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_phoneDail = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_phoneTransmitDTMFCode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_phoneTransfer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_phoneTransferBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_phoneBookStartUpdate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_callLogstartUpdate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_musicPlayOrPause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_musicStop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_musicPrevious = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_musicNext = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_musicMute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_musicUnmute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_musicBackground = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_musicNormal = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_getMusicInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_inquiryHfpStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_getCurrentDeviceAddr = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_getCurrentDeviceName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
static final int TRANSACTION_pauseDownLoadContact = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
static final int TRANSACTION_inquiryAuthorizationStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
}
public void addCallback(IMTKBTCallback callback) throws android.os.RemoteException;
public void removeCallback(IMTKBTCallback callback) throws android.os.RemoteException;
public void restBluetooth() throws android.os.RemoteException;
public void getLocalName() throws android.os.RemoteException;
public void setLocalName(String name) throws android.os.RemoteException;
public void getLocalAddress() throws android.os.RemoteException;
public void setPairMode() throws android.os.RemoteException;
public void cancelPairMode() throws android.os.RemoteException;
public void connectLast() throws android.os.RemoteException;
public void connectDevice(String addr) throws android.os.RemoteException;
public void disconnect() throws android.os.RemoteException;
public void deletePair(String addr) throws android.os.RemoteException;
public void startDiscovery() throws android.os.RemoteException;
public void getPairList() throws android.os.RemoteException;
public void stopDiscovery() throws android.os.RemoteException;
public void phoneAnswer() throws android.os.RemoteException;
public void phoneHangUp() throws android.os.RemoteException;
public void phoneDail(String phonenum) throws android.os.RemoteException;
public void phoneTransmitDTMFCode(char code) throws android.os.RemoteException;
public void phoneTransfer() throws android.os.RemoteException;
public void phoneTransferBack() throws android.os.RemoteException;
public void phoneBookStartUpdate() throws android.os.RemoteException;
public void callLogstartUpdate(int type) throws android.os.RemoteException;
public void musicPlayOrPause() throws android.os.RemoteException;
public void musicStop() throws android.os.RemoteException;
public void musicPrevious() throws android.os.RemoteException;
public void musicNext() throws android.os.RemoteException;
public void musicMute() throws android.os.RemoteException;
public void musicUnmute() throws android.os.RemoteException;
public void musicBackground() throws android.os.RemoteException;
public void musicNormal() throws android.os.RemoteException;
public void getMusicInfo() throws android.os.RemoteException;
public void inquiryHfpStatus() throws android.os.RemoteException;
public void getCurrentDeviceAddr() throws android.os.RemoteException;
public void getCurrentDeviceName() throws android.os.RemoteException;
public void pauseDownLoadContact() throws android.os.RemoteException;
public void inquiryAuthorizationStatus() throws android.os.RemoteException;
}
