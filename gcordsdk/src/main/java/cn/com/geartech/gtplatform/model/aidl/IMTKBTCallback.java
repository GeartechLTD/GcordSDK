/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/bada/Documents/android projects/GTPlatform/app/src/main/aidl/cn/com/geartech/gtplatform/model/aidl/IMTKBTCallback.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
// Declare any non-default types here with import statements

public interface IMTKBTCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IMTKBTCallback
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IMTKBTCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IMTKBTCallback interface,
 * generating a proxy if needed.
 */
public static IMTKBTCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IMTKBTCallback))) {
return ((IMTKBTCallback)iin);
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
case TRANSACTION_onInitSucceed:
{
data.enforceInterface(DESCRIPTOR);
this.onInitSucceed();
reply.writeNoException();
return true;
}
case TRANSACTION_onCurrentDeviceName:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onCurrentDeviceName(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onLocalAddress:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onLocalAddress(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onInPairMode:
{
data.enforceInterface(DESCRIPTOR);
this.onInPairMode();
reply.writeNoException();
return true;
}
case TRANSACTION_onExitPairMode:
{
data.enforceInterface(DESCRIPTOR);
this.onExitPairMode();
reply.writeNoException();
return true;
}
case TRANSACTION_onHfpConnected:
{
data.enforceInterface(DESCRIPTOR);
this.onHfpConnected();
reply.writeNoException();
return true;
}
case TRANSACTION_onA2dpConnected:
{
data.enforceInterface(DESCRIPTOR);
this.onA2dpConnected();
reply.writeNoException();
return true;
}
case TRANSACTION_onCurrentAndPairList:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
String _arg1;
_arg1 = data.readString();
String _arg2;
_arg2 = data.readString();
this.onCurrentAndPairList(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onHfpDisconnected:
{
data.enforceInterface(DESCRIPTOR);
this.onHfpDisconnected();
reply.writeNoException();
return true;
}
case TRANSACTION_onA2dpDisconnected:
{
data.enforceInterface(DESCRIPTOR);
this.onA2dpDisconnected();
reply.writeNoException();
return true;
}
case TRANSACTION_onSppDisconnect:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onSppDisconnect(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onDiscovery:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
this.onDiscovery(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onDiscoveryDone:
{
data.enforceInterface(DESCRIPTOR);
this.onDiscoveryDone();
reply.writeNoException();
return true;
}
case TRANSACTION_onTalking:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onTalking(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onHangUp:
{
data.enforceInterface(DESCRIPTOR);
this.onHangUp();
reply.writeNoException();
return true;
}
case TRANSACTION_onCallSucceed:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onCallSucceed(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onIncoming:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onIncoming(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onHfpRemote:
{
data.enforceInterface(DESCRIPTOR);
this.onHfpRemote();
reply.writeNoException();
return true;
}
case TRANSACTION_onVoiceDisconnected:
{
data.enforceInterface(DESCRIPTOR);
this.onVoiceDisconnected();
reply.writeNoException();
return true;
}
case TRANSACTION_onHfpLocal:
{
data.enforceInterface(DESCRIPTOR);
this.onHfpLocal();
reply.writeNoException();
return true;
}
case TRANSACTION_onVoiceConnected:
{
data.enforceInterface(DESCRIPTOR);
this.onVoiceConnected();
reply.writeNoException();
return true;
}
case TRANSACTION_onPhoneBook:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.onPhoneBook(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onPhoneBookDone:
{
data.enforceInterface(DESCRIPTOR);
this.onPhoneBookDone();
reply.writeNoException();
return true;
}
case TRANSACTION_onCalllogDone:
{
data.enforceInterface(DESCRIPTOR);
this.onCalllogDone();
reply.writeNoException();
return true;
}
case TRANSACTION_onCalllog:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
String _arg1;
_arg1 = data.readString();
String _arg2;
_arg2 = data.readString();
this.onCalllog(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onHfpStatus:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onHfpStatus(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onMusicPlaying:
{
data.enforceInterface(DESCRIPTOR);
this.onMusicPlaying();
reply.writeNoException();
return true;
}
case TRANSACTION_onMusicStopped:
{
data.enforceInterface(DESCRIPTOR);
this.onMusicStopped();
reply.writeNoException();
return true;
}
case TRANSACTION_onMusicInfo:
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
int _arg4;
_arg4 = data.readInt();
String _arg5;
_arg5 = data.readString();
this.onMusicInfo(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
return true;
}
case TRANSACTION_onCurrentAddr:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onCurrentAddr(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onCurrentName:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onCurrentName(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onGetPairedListEnd:
{
data.enforceInterface(DESCRIPTOR);
this.onGetPairedListEnd();
reply.writeNoException();
return true;
}
case TRANSACTION_onConnecting:
{
data.enforceInterface(DESCRIPTOR);
this.onConnecting();
reply.writeNoException();
return true;
}
case TRANSACTION_onOutGoingOrTalkingNumber:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onOutGoingOrTalkingNumber(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onAuthorizationResult:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onAuthorizationResult(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IMTKBTCallback
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
public void onInitSucceed() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onInitSucceed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onCurrentDeviceName(String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_onCurrentDeviceName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onLocalAddress(String addr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(addr);
mRemote.transact(Stub.TRANSACTION_onLocalAddress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onInPairMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onInPairMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onExitPairMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onExitPairMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onHfpConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onHfpConnected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onA2dpConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onA2dpConnected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onCurrentAndPairList(int index, String name, String addr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(index);
_data.writeString(name);
_data.writeString(addr);
mRemote.transact(Stub.TRANSACTION_onCurrentAndPairList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onHfpDisconnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onHfpDisconnected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onA2dpDisconnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onA2dpDisconnected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onSppDisconnect(int index) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(index);
mRemote.transact(Stub.TRANSACTION_onSppDisconnect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onDiscovery(String name, String addr, int rssi) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
_data.writeString(addr);
_data.writeInt(rssi);
mRemote.transact(Stub.TRANSACTION_onDiscovery, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onDiscoveryDone() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onDiscoveryDone, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onTalking(String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_onTalking, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onHangUp() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onHangUp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onCallSucceed(String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_onCallSucceed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onIncoming(String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_onIncoming, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onHfpRemote() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onHfpRemote, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onVoiceDisconnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onVoiceDisconnected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onHfpLocal() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onHfpLocal, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onVoiceConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onVoiceConnected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onPhoneBook(String name, String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_onPhoneBook, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onPhoneBookDone() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onPhoneBookDone, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onCalllogDone() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onCalllogDone, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onCalllog(int type, String name, String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
_data.writeString(name);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_onCalllog, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onHfpStatus(int status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(status);
mRemote.transact(Stub.TRANSACTION_onHfpStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onMusicPlaying() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onMusicPlaying, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onMusicStopped() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onMusicStopped, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onMusicInfo(String music_name, String artist_nameString, int duration, int pos, int total, String album) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(music_name);
_data.writeString(artist_nameString);
_data.writeInt(duration);
_data.writeInt(pos);
_data.writeInt(total);
_data.writeString(album);
mRemote.transact(Stub.TRANSACTION_onMusicInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onCurrentAddr(String addr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(addr);
mRemote.transact(Stub.TRANSACTION_onCurrentAddr, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onCurrentName(String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_onCurrentName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onGetPairedListEnd() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onGetPairedListEnd, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onConnecting() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onConnecting, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onOutGoingOrTalkingNumber(String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_onOutGoingOrTalkingNumber, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void onAuthorizationResult(int result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(result);
mRemote.transact(Stub.TRANSACTION_onAuthorizationResult, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onInitSucceed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onCurrentDeviceName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onLocalAddress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onInPairMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onExitPairMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onHfpConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_onA2dpConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_onCurrentAndPairList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_onHfpDisconnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_onA2dpDisconnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_onSppDisconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_onDiscovery = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_onDiscoveryDone = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_onTalking = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_onHangUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_onCallSucceed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_onIncoming = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_onHfpRemote = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_onVoiceDisconnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_onHfpLocal = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_onVoiceConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_onPhoneBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_onPhoneBookDone = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_onCalllogDone = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_onCalllog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_onHfpStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_onMusicPlaying = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_onMusicStopped = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_onMusicInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_onCurrentAddr = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_onCurrentName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_onGetPairedListEnd = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_onConnecting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_onOutGoingOrTalkingNumber = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_onAuthorizationResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
}
public void onInitSucceed() throws android.os.RemoteException;
public void onCurrentDeviceName(String name) throws android.os.RemoteException;
public void onLocalAddress(String addr) throws android.os.RemoteException;
public void onInPairMode() throws android.os.RemoteException;
public void onExitPairMode() throws android.os.RemoteException;
public void onHfpConnected() throws android.os.RemoteException;
public void onA2dpConnected() throws android.os.RemoteException;
public void onCurrentAndPairList(int index, String name, String addr) throws android.os.RemoteException;
public void onHfpDisconnected() throws android.os.RemoteException;
public void onA2dpDisconnected() throws android.os.RemoteException;
public void onSppDisconnect(int index) throws android.os.RemoteException;
public void onDiscovery(String name, String addr, int rssi) throws android.os.RemoteException;
public void onDiscoveryDone() throws android.os.RemoteException;
public void onTalking(String number) throws android.os.RemoteException;
public void onHangUp() throws android.os.RemoteException;
public void onCallSucceed(String number) throws android.os.RemoteException;
public void onIncoming(String number) throws android.os.RemoteException;
public void onHfpRemote() throws android.os.RemoteException;
public void onVoiceDisconnected() throws android.os.RemoteException;
public void onHfpLocal() throws android.os.RemoteException;
public void onVoiceConnected() throws android.os.RemoteException;
public void onPhoneBook(String name, String number) throws android.os.RemoteException;
public void onPhoneBookDone() throws android.os.RemoteException;
public void onCalllogDone() throws android.os.RemoteException;
public void onCalllog(int type, String name, String number) throws android.os.RemoteException;
public void onHfpStatus(int status) throws android.os.RemoteException;
public void onMusicPlaying() throws android.os.RemoteException;
public void onMusicStopped() throws android.os.RemoteException;
public void onMusicInfo(String music_name, String artist_nameString, int duration, int pos, int total, String album) throws android.os.RemoteException;
public void onCurrentAddr(String addr) throws android.os.RemoteException;
public void onCurrentName(String name) throws android.os.RemoteException;
public void onGetPairedListEnd() throws android.os.RemoteException;
public void onConnecting() throws android.os.RemoteException;
public void onOutGoingOrTalkingNumber(String number) throws android.os.RemoteException;
public void onAuthorizationResult(int result) throws android.os.RemoteException;
}
