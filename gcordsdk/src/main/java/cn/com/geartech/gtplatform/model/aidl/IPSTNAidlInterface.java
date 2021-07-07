/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/bada/Documents/project_from_github/extra/gtplatform/app/src/main/aidl/cn/com/geartech/gtplatform/model/aidl/IPSTNAidlInterface.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
// Declare any non-default types here with import statements

public interface IPSTNAidlInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IPSTNAidlInterface
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IPSTNAidlInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IPSTNAidlInterface interface,
 * generating a proxy if needed.
 */
public static IPSTNAidlInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IPSTNAidlInterface))) {
return ((IPSTNAidlInterface)iin);
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
case TRANSACTION_enablePSTN:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
String _arg1;
_arg1 = data.readString();
this.enablePSTN(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setMusicTunnel:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setMusicTunnel(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getMusicTunnel:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getMusicTunnel();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_registerAidlCallback:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
IPSTNAidlCallback _arg2;
_arg2 = IPSTNAidlCallback.Stub.asInterface(data.readStrongBinder());
this.registerAidlCallback(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_isHandlePickedUp:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isHandlePickedUp();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getPSTNStatus:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPSTNStatus();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_startInterceptIncomingCall:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.startInterceptIncomingCall(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_endInterceptIncomingCall:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.endInterceptIncomingCall(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getExtLineStatus:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getExtLineStatus();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_startRecording:
{
data.enforceInterface(DESCRIPTOR);
ICommonCallback _arg0;
_arg0 = ICommonCallback.Stub.asInterface(data.readStrongBinder());
this.startRecording(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_stopRecording:
{
data.enforceInterface(DESCRIPTOR);
this.stopRecording();
reply.writeNoException();
return true;
}
case TRANSACTION_setAudioState:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setAudioState(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getAudioState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getAudioState();
reply.writeNoException();
reply.writeInt(_result);
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
case TRANSACTION_getCurrentIncomingPhoneNumber:
{
data.enforceInterface(DESCRIPTOR);
String _result = this.getCurrentIncomingPhoneNumber();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_setMicrophoneMute:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _result = this.setMicrophoneMute(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isMicrophoneMute:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isMicrophoneMute();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getCurrentCallDirection:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCurrentCallDirection();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_isAutoAnswerOpen:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isAutoAnswerOpen();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_enableAutoAnswer:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.enableAutoAnswer(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_cancelAutoAnswer:
{
data.enforceInterface(DESCRIPTOR);
this.cancelAutoAnswer();
reply.writeNoException();
return true;
}
case TRANSACTION_getMaxVolume:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getMaxVolume();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getVolume:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
IPSTNAidlCallback _arg1;
_arg1 = IPSTNAidlCallback.Stub.asInterface(data.readStrongBinder());
this.getVolume(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setVolume:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
IPSTNAidlCallback _arg2;
_arg2 = IPSTNAidlCallback.Stub.asInterface(data.readStrongBinder());
this.setVolume(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_getRegAppName:
{
data.enforceInterface(DESCRIPTOR);
String _result = this.getRegAppName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_isReversalOfPolarityEnabled:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isReversalOfPolarityEnabled();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IPSTNAidlInterface
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
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
@Override
public void enablePSTN(boolean enable, String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((enable)?(1):(0)));
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_enablePSTN, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void setMusicTunnel(int tunnel) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(tunnel);
mRemote.transact(Stub.TRANSACTION_setMusicTunnel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public int getMusicTunnel() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMusicTunnel, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public void registerAidlCallback(String packageName, int version, IPSTNAidlCallback callback) throws android.os.RemoteException
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
@Override
public boolean isHandlePickedUp() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isHandlePickedUp, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public int getPSTNStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPSTNStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public void startInterceptIncomingCall(String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_startInterceptIncomingCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void endInterceptIncomingCall(String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_endInterceptIncomingCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public int getExtLineStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getExtLineStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public void startRecording(ICommonCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_startRecording, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void stopRecording() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopRecording, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void setAudioState(int state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(state);
mRemote.transact(Stub.TRANSACTION_setAudioState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//internal usage

@Override
public int getAudioState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAudioState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public void volUp() throws android.os.RemoteException
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
@Override
public void volDown() throws android.os.RemoteException
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
@Override
public String getCurrentIncomingPhoneNumber() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentIncomingPhoneNumber, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public boolean setMicrophoneMute(boolean mute) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((mute)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setMicrophoneMute, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public boolean isMicrophoneMute() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isMicrophoneMute, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public int getCurrentCallDirection() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentCallDirection, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public boolean isAutoAnswerOpen() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isAutoAnswerOpen, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public void enableAutoAnswer(boolean enable) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((enable)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_enableAutoAnswer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void cancelAutoAnswer() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cancelAutoAnswer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public int getMaxVolume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMaxVolume, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public void getVolume(int channel, IPSTNAidlCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(channel);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_getVolume, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public void setVolume(int channel, int volume, IPSTNAidlCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(channel);
_data.writeInt(volume);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setVolume, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override
public String getRegAppName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRegAppName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override
public boolean isReversalOfPolarityEnabled() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isReversalOfPolarityEnabled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_enablePSTN = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setMusicTunnel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getMusicTunnel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_registerAidlCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_isHandlePickedUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getPSTNStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_startInterceptIncomingCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_endInterceptIncomingCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getExtLineStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_startRecording = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_stopRecording = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_setAudioState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getAudioState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_volUp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_volDown = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_getCurrentIncomingPhoneNumber = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_setMicrophoneMute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_isMicrophoneMute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_getCurrentCallDirection = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_isAutoAnswerOpen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_enableAutoAnswer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_cancelAutoAnswer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_getMaxVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_getVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_setVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_getRegAppName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_isReversalOfPolarityEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public void enablePSTN(boolean enable, String packageName) throws android.os.RemoteException;
public void setMusicTunnel(int tunnel) throws android.os.RemoteException;
public int getMusicTunnel() throws android.os.RemoteException;
public void registerAidlCallback(String packageName, int version, IPSTNAidlCallback callback) throws android.os.RemoteException;
public boolean isHandlePickedUp() throws android.os.RemoteException;
public int getPSTNStatus() throws android.os.RemoteException;
public void startInterceptIncomingCall(String packageName) throws android.os.RemoteException;
public void endInterceptIncomingCall(String packageName) throws android.os.RemoteException;
public int getExtLineStatus() throws android.os.RemoteException;
public void startRecording(ICommonCallback callback) throws android.os.RemoteException;
public void stopRecording() throws android.os.RemoteException;
public void setAudioState(int state) throws android.os.RemoteException;
//internal usage

public int getAudioState() throws android.os.RemoteException;
public void volUp() throws android.os.RemoteException;
public void volDown() throws android.os.RemoteException;
public String getCurrentIncomingPhoneNumber() throws android.os.RemoteException;
public boolean setMicrophoneMute(boolean mute) throws android.os.RemoteException;
public boolean isMicrophoneMute() throws android.os.RemoteException;
public int getCurrentCallDirection() throws android.os.RemoteException;
public boolean isAutoAnswerOpen() throws android.os.RemoteException;
public void enableAutoAnswer(boolean enable) throws android.os.RemoteException;
public void cancelAutoAnswer() throws android.os.RemoteException;
public int getMaxVolume() throws android.os.RemoteException;
public void getVolume(int channel, IPSTNAidlCallback callback) throws android.os.RemoteException;
public void setVolume(int channel, int volume, IPSTNAidlCallback callback) throws android.os.RemoteException;
public String getRegAppName() throws android.os.RemoteException;
public boolean isReversalOfPolarityEnabled() throws android.os.RemoteException;
}
