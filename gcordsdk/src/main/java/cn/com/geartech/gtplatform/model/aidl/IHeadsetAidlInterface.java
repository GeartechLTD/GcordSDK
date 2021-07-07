/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Projects\\GTPlatform\\app\\src\\main\\aidl\\cn\\com\\geartech\\gtplatform\\model\\aidl\\IHeadsetAidlInterface.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
public interface IHeadsetAidlInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IHeadsetAidlInterface
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IHeadsetAidlInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IHeadsetAidlInterface interface,
 * generating a proxy if needed.
 */
public static IHeadsetAidlInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IHeadsetAidlInterface))) {
return ((IHeadsetAidlInterface)iin);
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
IHeadsetCallback _arg0;
_arg0 = IHeadsetCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_startPairing:
{
data.enforceInterface(DESCRIPTOR);
this.startPairing();
reply.writeNoException();
return true;
}
case TRANSACTION_stopPairing:
{
data.enforceInterface(DESCRIPTOR);
this.stopPairing();
reply.writeNoException();
return true;
}
case TRANSACTION_isConnected:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isConnected();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isPaired:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isPaired();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_unPair:
{
data.enforceInterface(DESCRIPTOR);
this.unPair();
reply.writeNoException();
return true;
}
case TRANSACTION_isCharging:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isCharging();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isAllowCharging:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isAllowCharging();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setAllowCharging:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setAllowCharging(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_is3Point5MMHeadsetConnected:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.is3Point5MMHeadsetConnected();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isWirelessHeadsetConnected:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isWirelessHeadsetConnected();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_checkPairingState:
{
data.enforceInterface(DESCRIPTOR);
this.checkPairingState();
reply.writeNoException();
return true;
}
case TRANSACTION_checkConnectionState:
{
data.enforceInterface(DESCRIPTOR);
this.checkConnectionState();
reply.writeNoException();
return true;
}
case TRANSACTION_checkBatteryLevel:
{
data.enforceInterface(DESCRIPTOR);
this.checkBatteryLevel();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IHeadsetAidlInterface
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
@Override public void registerCallback(IHeadsetCallback callback) throws android.os.RemoteException
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
@Override public void startPairing() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startPairing, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopPairing() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopPairing, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isConnected, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isPaired() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isPaired, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void unPair() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_unPair, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isCharging() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isCharging, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isAllowCharging() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isAllowCharging, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setAllowCharging(boolean allowCharging) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((allowCharging)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setAllowCharging, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean is3Point5MMHeadsetConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_is3Point5MMHeadsetConnected, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isWirelessHeadsetConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isWirelessHeadsetConnected, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void checkPairingState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_checkPairingState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void checkConnectionState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_checkConnectionState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void checkBatteryLevel() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_checkBatteryLevel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_startPairing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_stopPairing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_isConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_isPaired = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_unPair = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_isCharging = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_isAllowCharging = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_setAllowCharging = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_is3Point5MMHeadsetConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_isWirelessHeadsetConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_checkPairingState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_checkConnectionState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_checkBatteryLevel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
}
public void registerCallback(IHeadsetCallback callback) throws android.os.RemoteException;
public void startPairing() throws android.os.RemoteException;
public void stopPairing() throws android.os.RemoteException;
public boolean isConnected() throws android.os.RemoteException;
public boolean isPaired() throws android.os.RemoteException;
public void unPair() throws android.os.RemoteException;
public boolean isCharging() throws android.os.RemoteException;
public boolean isAllowCharging() throws android.os.RemoteException;
public void setAllowCharging(boolean allowCharging) throws android.os.RemoteException;
public boolean is3Point5MMHeadsetConnected() throws android.os.RemoteException;
public boolean isWirelessHeadsetConnected() throws android.os.RemoteException;
public void checkPairingState() throws android.os.RemoteException;
public void checkConnectionState() throws android.os.RemoteException;
public void checkBatteryLevel() throws android.os.RemoteException;
}
