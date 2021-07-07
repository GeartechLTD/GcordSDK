/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/kya/Documents/geartech/gtplatform/app/src/main/aidl/cn/com/geartech/gtplatform/model/aidl/IGTAidlInterface.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
// Declare any non-default types here with import statements

public interface IGTAidlInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IGTAidlInterface
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IGTAidlInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IGTAidlInterface interface,
 * generating a proxy if needed.
 */
public static IGTAidlInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IGTAidlInterface))) {
return ((IGTAidlInterface)iin);
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
case TRANSACTION_getEthernetConnectionState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getEthernetConnectionState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCurrentPowerLevel:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCurrentPowerLevel();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCurrentPluggedType:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCurrentPluggedType();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_isPowerConnected:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isPowerConnected();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isEthernetLineConnected:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isEthernetLineConnected();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_registerAidlCallback:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
IGTAidlCallback _arg2;
_arg2 = IGTAidlCallback.Stub.asInterface(data.readStrongBinder());
this.registerAidlCallback(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_checkUpgrade:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.checkUpgrade(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_downloadAndInstall:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.downloadAndInstall(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_updateInstInterval:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.updateInstInterval(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_fm:
{
data.enforceInterface(DESCRIPTOR);
this.fm();
reply.writeNoException();
return true;
}
case TRANSACTION_startFMBroadcasting:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.startFMBroadcasting(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_stopFMBroadcasting:
{
data.enforceInterface(DESCRIPTOR);
this.stopFMBroadcasting();
reply.writeNoException();
return true;
}
case TRANSACTION_notifyAppForegroundState:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
int _arg2;
_arg2 = data.readInt();
this.notifyAppForegroundState(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_setBootAnimation:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.setBootAnimation(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_installDownloadedPackage:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.installDownloadedPackage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setBootAnimationPath:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
int _result = this.setBootAnimationPath(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setShutdownAnimationPath:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
int _result = this.setShutdownAnimationPath(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_walkDog:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.walkDog(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setLedOn:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.setLedOn(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setLedFlashing:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.setLedFlashing(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_pLog:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.pLog(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_eventLog:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
String _arg2;
_arg2 = data.readString();
String _arg3;
_arg3 = data.readString();
String _arg4;
_arg4 = data.readString();
this.eventLog(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_getDeviceCapabilities:
{
data.enforceInterface(DESCRIPTOR);
java.util.Map _result = this.getDeviceCapabilities();
reply.writeNoException();
reply.writeMap(_result);
return true;
}
case TRANSACTION_reboot:
{
data.enforceInterface(DESCRIPTOR);
this.reboot();
reply.writeNoException();
return true;
}
case TRANSACTION_getFontScale:
{
data.enforceInterface(DESCRIPTOR);
float _result = this.getFontScale();
reply.writeNoException();
reply.writeFloat(_result);
return true;
}
case TRANSACTION_setFontScale:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
this.setFontScale(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_logD:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.logD(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_logE:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.logE(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_fileLog:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
String _arg2;
_arg2 = data.readString();
this.fileLog(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_getDeviceType:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getDeviceType();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_raw:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.raw(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_logic:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.logic(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_biz:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.biz(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_statistics:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.statistics(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getUsbStoragePath:
{
data.enforceInterface(DESCRIPTOR);
String _result = this.getUsbStoragePath();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_queryScreenStrategy:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.queryScreenStrategy();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sendMessage:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
String _arg3;
_arg3 = data.readString();
String _arg4;
_arg4 = data.readString();
int _result = this.sendMessage(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_checkAllInOneUpgrade:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
ICommonCallback _arg1;
_arg1 = ICommonCallback.Stub.asInterface(data.readStrongBinder());
this.checkAllInOneUpgrade(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_downloadAllInOnePackages:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
ICommonCallback _arg1;
_arg1 = ICommonCallback.Stub.asInterface(data.readStrongBinder());
this.downloadAllInOnePackages(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_enableAppWhiteList:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.enableAppWhiteList(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_addOrRemovePackageForWhiteList:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
boolean _arg2;
_arg2 = (0!=data.readInt());
this.addOrRemovePackageForWhiteList(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_queryData:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
String _result = this.queryData(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_checkSystemPackageUpgrade:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
ICommonCallback _arg1;
_arg1 = ICommonCallback.Stub.asInterface(data.readStrongBinder());
this.checkSystemPackageUpgrade(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_checkGTPUpgrade:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
ICommonCallback _arg1;
_arg1 = ICommonCallback.Stub.asInterface(data.readStrongBinder());
this.checkGTPUpgrade(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_queryCurrentCallStatus:
{
data.enforceInterface(DESCRIPTOR);
java.util.Map _result = this.queryCurrentCallStatus();
reply.writeNoException();
reply.writeMap(_result);
return true;
}
case TRANSACTION_queryCellSignal:
{
data.enforceInterface(DESCRIPTOR);
java.util.Map _result = this.queryCellSignal();
reply.writeNoException();
reply.writeMap(_result);
return true;
}
case TRANSACTION_checkPhoneNumber:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
ICommonCallback _arg1;
_arg1 = ICommonCallback.Stub.asInterface(data.readStrongBinder());
this.checkPhoneNumber(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IGTAidlInterface
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
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
@Override public int getEthernetConnectionState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEthernetConnectionState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getCurrentPowerLevel() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentPowerLevel, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getCurrentPluggedType() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentPluggedType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isPowerConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isPowerConnected, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isEthernetLineConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isEthernetLineConnected, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void registerAidlCallback(String packageName, int version, IGTAidlCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeInt(version);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerAidlCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void checkUpgrade(String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_checkUpgrade, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void downloadAndInstall(String packageName, String packageSHA1) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeString(packageSHA1);
mRemote.transact(Stub.TRANSACTION_downloadAndInstall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void updateInstInterval(int interval) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(interval);
mRemote.transact(Stub.TRANSACTION_updateInstInterval, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void fm() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_fm, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startFMBroadcasting(int interval) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(interval);
mRemote.transact(Stub.TRANSACTION_startFMBroadcasting, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopFMBroadcasting() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopFMBroadcasting, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void notifyAppForegroundState(String packageName, boolean isForeground, int pid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeInt(((isForeground)?(1):(0)));
_data.writeInt(pid);
mRemote.transact(Stub.TRANSACTION_notifyAppForegroundState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setBootAnimation(String path) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
mRemote.transact(Stub.TRANSACTION_setBootAnimation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void installDownloadedPackage(String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_installDownloadedPackage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int setBootAnimationPath(String path) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
mRemote.transact(Stub.TRANSACTION_setBootAnimationPath, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setShutdownAnimationPath(String path) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
mRemote.transact(Stub.TRANSACTION_setShutdownAnimationPath, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void walkDog(String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_walkDog, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setLedOn(int ledNum, boolean on) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(ledNum);
_data.writeInt(((on)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setLedOn, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setLedFlashing(int ledNum, boolean flashing) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(ledNum);
_data.writeInt(((flashing)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setLedFlashing, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void pLog(String pLog) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pLog);
mRemote.transact(Stub.TRANSACTION_pLog, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void eventLog(String eventId, String eventName, String param1, String param2, String param3) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(eventId);
_data.writeString(eventName);
_data.writeString(param1);
_data.writeString(param2);
_data.writeString(param3);
mRemote.transact(Stub.TRANSACTION_eventLog, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.Map getDeviceCapabilities() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.Map _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDeviceCapabilities, _data, _reply, 0);
_reply.readException();
ClassLoader cl = (ClassLoader)this.getClass().getClassLoader();
_result = _reply.readHashMap(cl);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void reboot() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_reboot, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public float getFontScale() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
float _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getFontScale, _data, _reply, 0);
_reply.readException();
_result = _reply.readFloat();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setFontScale(float scale) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(scale);
mRemote.transact(Stub.TRANSACTION_setFontScale, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void logD(String tag, String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(tag);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_logD, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void logE(String tag, String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(tag);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_logE, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void fileLog(String fileName, String tag, String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(fileName);
_data.writeString(tag);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_fileLog, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getDeviceType() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDeviceType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void raw(String tag, String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(tag);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_raw, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void logic(String tag, String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(tag);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_logic, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void biz(String tag, String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(tag);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_biz, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void statistics(String tag, String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(tag);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_statistics, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public String getUsbStoragePath() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUsbStoragePath, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int queryScreenStrategy() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_queryScreenStrategy, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int sendMessage(String msgName, int param1, int param2, String str1, String str2) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(msgName);
_data.writeInt(param1);
_data.writeInt(param2);
_data.writeString(str1);
_data.writeString(str2);
mRemote.transact(Stub.TRANSACTION_sendMessage, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void checkAllInOneUpgrade(String packageName, ICommonCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_checkAllInOneUpgrade, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void downloadAllInOnePackages(String token, ICommonCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(token);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_downloadAllInOnePackages, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void enableAppWhiteList(String packageName, boolean enable) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeInt(((enable)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_enableAppWhiteList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void addOrRemovePackageForWhiteList(String myPackageName, String packageName, boolean enable) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(myPackageName);
_data.writeString(packageName);
_data.writeInt(((enable)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_addOrRemovePackageForWhiteList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public String queryData(String key, String key2, int key3, int key4) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
_data.writeString(key2);
_data.writeInt(key3);
_data.writeInt(key4);
mRemote.transact(Stub.TRANSACTION_queryData, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void checkSystemPackageUpgrade(String packageName, ICommonCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_checkSystemPackageUpgrade, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void checkGTPUpgrade(String packageName, ICommonCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_checkGTPUpgrade, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.Map queryCurrentCallStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.Map _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_queryCurrentCallStatus, _data, _reply, 0);
_reply.readException();
ClassLoader cl = (ClassLoader)this.getClass().getClassLoader();
_result = _reply.readHashMap(cl);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.Map queryCellSignal() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.Map _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_queryCellSignal, _data, _reply, 0);
_reply.readException();
ClassLoader cl = (ClassLoader)this.getClass().getClassLoader();
_result = _reply.readHashMap(cl);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void checkPhoneNumber(String number, ICommonCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_checkPhoneNumber, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getEthernetConnectionState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getCurrentPowerLevel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getCurrentPluggedType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_isPowerConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_isEthernetLineConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_registerAidlCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_checkUpgrade = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_downloadAndInstall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_updateInstInterval = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_fm = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_startFMBroadcasting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_stopFMBroadcasting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_notifyAppForegroundState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_setBootAnimation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_installDownloadedPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_setBootAnimationPath = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_setShutdownAnimationPath = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_walkDog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_setLedOn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_setLedFlashing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_pLog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_eventLog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_getDeviceCapabilities = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_reboot = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_getFontScale = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_setFontScale = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_logD = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_logE = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_fileLog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_getDeviceType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_raw = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_logic = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_biz = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_statistics = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_getUsbStoragePath = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
static final int TRANSACTION_queryScreenStrategy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
static final int TRANSACTION_sendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
static final int TRANSACTION_checkAllInOneUpgrade = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
static final int TRANSACTION_downloadAllInOnePackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
static final int TRANSACTION_enableAppWhiteList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
static final int TRANSACTION_addOrRemovePackageForWhiteList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
static final int TRANSACTION_queryData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
static final int TRANSACTION_checkSystemPackageUpgrade = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
static final int TRANSACTION_checkGTPUpgrade = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
static final int TRANSACTION_queryCurrentCallStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
static final int TRANSACTION_queryCellSignal = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
static final int TRANSACTION_checkPhoneNumber = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public int getEthernetConnectionState() throws android.os.RemoteException;
public int getCurrentPowerLevel() throws android.os.RemoteException;
public int getCurrentPluggedType() throws android.os.RemoteException;
public boolean isPowerConnected() throws android.os.RemoteException;
public boolean isEthernetLineConnected() throws android.os.RemoteException;
public void registerAidlCallback(String packageName, int version, IGTAidlCallback callback) throws android.os.RemoteException;
public void checkUpgrade(String packageName) throws android.os.RemoteException;
public void downloadAndInstall(String packageName, String packageSHA1) throws android.os.RemoteException;
public void updateInstInterval(int interval) throws android.os.RemoteException;
public void fm() throws android.os.RemoteException;
public void startFMBroadcasting(int interval) throws android.os.RemoteException;
public void stopFMBroadcasting() throws android.os.RemoteException;
public void notifyAppForegroundState(String packageName, boolean isForeground, int pid) throws android.os.RemoteException;
public void setBootAnimation(String path) throws android.os.RemoteException;
public void installDownloadedPackage(String packageName) throws android.os.RemoteException;
public int setBootAnimationPath(String path) throws android.os.RemoteException;
public int setShutdownAnimationPath(String path) throws android.os.RemoteException;
public void walkDog(String packageName) throws android.os.RemoteException;
public void setLedOn(int ledNum, boolean on) throws android.os.RemoteException;
public void setLedFlashing(int ledNum, boolean flashing) throws android.os.RemoteException;
public void pLog(String pLog) throws android.os.RemoteException;
public void eventLog(String eventId, String eventName, String param1, String param2, String param3) throws android.os.RemoteException;
public java.util.Map getDeviceCapabilities() throws android.os.RemoteException;
public void reboot() throws android.os.RemoteException;
public float getFontScale() throws android.os.RemoteException;
public void setFontScale(float scale) throws android.os.RemoteException;
public void logD(String tag, String msg) throws android.os.RemoteException;
public void logE(String tag, String msg) throws android.os.RemoteException;
public void fileLog(String fileName, String tag, String msg) throws android.os.RemoteException;
public int getDeviceType() throws android.os.RemoteException;
public void raw(String tag, String msg) throws android.os.RemoteException;
public void logic(String tag, String msg) throws android.os.RemoteException;
public void biz(String tag, String msg) throws android.os.RemoteException;
public void statistics(String tag, String msg) throws android.os.RemoteException;
public String getUsbStoragePath() throws android.os.RemoteException;
public int queryScreenStrategy() throws android.os.RemoteException;
public int sendMessage(String msgName, int param1, int param2, String str1, String str2) throws android.os.RemoteException;
public void checkAllInOneUpgrade(String packageName, ICommonCallback callback) throws android.os.RemoteException;
public void downloadAllInOnePackages(String token, ICommonCallback callback) throws android.os.RemoteException;
public void enableAppWhiteList(String packageName, boolean enable) throws android.os.RemoteException;
public void addOrRemovePackageForWhiteList(String myPackageName, String packageName, boolean enable) throws android.os.RemoteException;
public String queryData(String key, String key2, int key3, int key4) throws android.os.RemoteException;
public void checkSystemPackageUpgrade(String packageName, ICommonCallback callback) throws android.os.RemoteException;
public void checkGTPUpgrade(String packageName, ICommonCallback callback) throws android.os.RemoteException;
public java.util.Map queryCurrentCallStatus() throws android.os.RemoteException;
public java.util.Map queryCellSignal() throws android.os.RemoteException;
public void checkPhoneNumber(String number, ICommonCallback callback) throws android.os.RemoteException;
}
