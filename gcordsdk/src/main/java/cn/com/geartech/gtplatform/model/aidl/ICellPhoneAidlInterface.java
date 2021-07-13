/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package cn.com.geartech.gtplatform.model.aidl;
public interface ICellPhoneAidlInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements ICellPhoneAidlInterface
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.ICellPhoneAidlInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.ICellPhoneAidlInterface interface,
 * generating a proxy if needed.
 */
public static ICellPhoneAidlInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof ICellPhoneAidlInterface))) {
return ((ICellPhoneAidlInterface)iin);
}
return new Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
String descriptor = DESCRIPTOR;
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(descriptor);
return true;
}
case TRANSACTION_makeCall:
{
data.enforceInterface(descriptor);
String _arg0;
_arg0 = data.readString();
this.makeCall(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_dialDTMF:
{
data.enforceInterface(descriptor);
String _arg0;
_arg0 = data.readString();
this.dialDTMF(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_playDTMF:
{
data.enforceInterface(descriptor);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.playDTMF(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_stopPlayDTMF:
{
data.enforceInterface(descriptor);
String _arg0;
_arg0 = data.readString();
this.stopPlayDTMF(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getCurrentCallId:
{
data.enforceInterface(descriptor);
String _result = this.getCurrentCallId();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_isInCall:
{
data.enforceInterface(descriptor);
boolean _result = this.isInCall();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getCallStatus:
{
data.enforceInterface(descriptor);
int _result = this.getCallStatus();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCurrentCallNumber:
{
data.enforceInterface(descriptor);
String _result = this.getCurrentCallNumber();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_acceptCall:
{
data.enforceInterface(descriptor);
String _arg0;
_arg0 = data.readString();
this.acceptCall(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_endCall:
{
data.enforceInterface(descriptor);
String _arg0;
_arg0 = data.readString();
this.endCall(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_endCurrentCall:
{
data.enforceInterface(descriptor);
this.endCurrentCall();
reply.writeNoException();
return true;
}
case TRANSACTION_rejectCall:
{
data.enforceInterface(descriptor);
String _arg0;
_arg0 = data.readString();
this.rejectCall(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setMute:
{
data.enforceInterface(descriptor);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setMute(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isMute:
{
data.enforceInterface(descriptor);
boolean _result = this.isMute();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_startRecording:
{
data.enforceInterface(descriptor);
this.startRecording();
reply.writeNoException();
return true;
}
case TRANSACTION_stopRecording:
{
data.enforceInterface(descriptor);
this.stopRecording();
reply.writeNoException();
return true;
}
case TRANSACTION_isRecording:
{
data.enforceInterface(descriptor);
boolean _result = this.isRecording();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getCurrentInCallVolume:
{
data.enforceInterface(descriptor);
int _result = this.getCurrentInCallVolume();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getMaxInCallVolume:
{
data.enforceInterface(descriptor);
int _result = this.getMaxInCallVolume();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setInCallVolume:
{
data.enforceInterface(descriptor);
int _arg0;
_arg0 = data.readInt();
this.setInCallVolume(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isHandsFreeOn:
{
data.enforceInterface(descriptor);
boolean _result = this.isHandsFreeOn();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setHandsFreeOn:
{
data.enforceInterface(descriptor);
ICommonCallback _arg0;
_arg0 = ICommonCallback.Stub.asInterface(data.readStrongBinder());
this.setHandsFreeOn(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setHandleOn:
{
data.enforceInterface(descriptor);
ICommonCallback _arg0;
_arg0 = ICommonCallback.Stub.asInterface(data.readStrongBinder());
this.setHandleOn(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isSIMCardAvailable:
{
data.enforceInterface(descriptor);
boolean _result = this.isSIMCardAvailable();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isIncomingCall:
{
data.enforceInterface(descriptor);
boolean _result = this.isIncomingCall();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setBTOn:
{
data.enforceInterface(descriptor);
ICommonCallback _arg0;
_arg0 = ICommonCallback.Stub.asInterface(data.readStrongBinder());
this.setBTOn(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isBTOn:
{
data.enforceInterface(descriptor);
boolean _result = this.isBTOn();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isBTHeadsetConnected:
{
data.enforceInterface(descriptor);
boolean _result = this.isBTHeadsetConnected();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getCurrentCallBeginTS:
{
data.enforceInterface(descriptor);
long _result = this.getCurrentCallBeginTS();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_startRecordingWav:
{
data.enforceInterface(descriptor);
this.startRecordingWav();
reply.writeNoException();
return true;
}
case TRANSACTION_startRecordingMp3:
{
data.enforceInterface(descriptor);
this.startRecordingMp3();
reply.writeNoException();
return true;
}
case TRANSACTION_startRecordingMp3WithParams:
{
data.enforceInterface(descriptor);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.startRecordingMp3WithParams(_arg0, _arg1);
reply.writeNoException();
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements ICellPhoneAidlInterface
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
@Override public void makeCall(String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_makeCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void dialDTMF(String dtmf) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dtmf);
mRemote.transact(Stub.TRANSACTION_dialDTMF, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void playDTMF(String dtmf, String callId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dtmf);
_data.writeString(callId);
mRemote.transact(Stub.TRANSACTION_playDTMF, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopPlayDTMF(String callId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callId);
mRemote.transact(Stub.TRANSACTION_stopPlayDTMF, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public String getCurrentCallId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentCallId, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isInCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isInCall, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getCallStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCallStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public String getCurrentCallNumber() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentCallNumber, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void acceptCall(String callId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callId);
mRemote.transact(Stub.TRANSACTION_acceptCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void endCall(String callId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callId);
mRemote.transact(Stub.TRANSACTION_endCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void endCurrentCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_endCurrentCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rejectCall(String callId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(callId);
mRemote.transact(Stub.TRANSACTION_rejectCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setMute(boolean mute) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((mute)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setMute, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isMute() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isMute, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void startRecording() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startRecording, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopRecording() throws android.os.RemoteException
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
@Override public boolean isRecording() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isRecording, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getCurrentInCallVolume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentInCallVolume, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getMaxInCallVolume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMaxInCallVolume, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setInCallVolume(int volume) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(volume);
mRemote.transact(Stub.TRANSACTION_setInCallVolume, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isHandsFreeOn() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isHandsFreeOn, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setHandsFreeOn(ICommonCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setHandsFreeOn, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setHandleOn(ICommonCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setHandleOn, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isSIMCardAvailable() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isSIMCardAvailable, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isIncomingCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isIncomingCall, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setBTOn(ICommonCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setBTOn, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isBTOn() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isBTOn, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isBTHeadsetConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isBTHeadsetConnected, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public long getCurrentCallBeginTS() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentCallBeginTS, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void startRecordingWav() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startRecordingWav, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startRecordingMp3() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startRecordingMp3, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startRecordingMp3WithParams(int sampleRate, int bitRate) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sampleRate);
_data.writeInt(bitRate);
mRemote.transact(Stub.TRANSACTION_startRecordingMp3WithParams, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_makeCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_dialDTMF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_playDTMF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_stopPlayDTMF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getCurrentCallId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_isInCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getCallStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getCurrentCallNumber = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_acceptCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_endCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_endCurrentCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_rejectCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_setMute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_isMute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_startRecording = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_stopRecording = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_isRecording = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_getCurrentInCallVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_getMaxInCallVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_setInCallVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_isHandsFreeOn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_setHandsFreeOn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_setHandleOn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_isSIMCardAvailable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_isIncomingCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_setBTOn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_isBTOn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_isBTHeadsetConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_getCurrentCallBeginTS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_startRecordingWav = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_startRecordingMp3 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_startRecordingMp3WithParams = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public void makeCall(String number) throws android.os.RemoteException;
public void dialDTMF(String dtmf) throws android.os.RemoteException;
public void playDTMF(String dtmf, String callId) throws android.os.RemoteException;
public void stopPlayDTMF(String callId) throws android.os.RemoteException;
public String getCurrentCallId() throws android.os.RemoteException;
public boolean isInCall() throws android.os.RemoteException;
public int getCallStatus() throws android.os.RemoteException;
public String getCurrentCallNumber() throws android.os.RemoteException;
public void acceptCall(String callId) throws android.os.RemoteException;
public void endCall(String callId) throws android.os.RemoteException;
public void endCurrentCall() throws android.os.RemoteException;
public void rejectCall(String callId) throws android.os.RemoteException;
public void setMute(boolean mute) throws android.os.RemoteException;
public boolean isMute() throws android.os.RemoteException;
public void startRecording() throws android.os.RemoteException;
public void stopRecording() throws android.os.RemoteException;
public boolean isRecording() throws android.os.RemoteException;
public int getCurrentInCallVolume() throws android.os.RemoteException;
public int getMaxInCallVolume() throws android.os.RemoteException;
public void setInCallVolume(int volume) throws android.os.RemoteException;
public boolean isHandsFreeOn() throws android.os.RemoteException;
public void setHandsFreeOn(ICommonCallback callback) throws android.os.RemoteException;
public void setHandleOn(ICommonCallback callback) throws android.os.RemoteException;
public boolean isSIMCardAvailable() throws android.os.RemoteException;
public boolean isIncomingCall() throws android.os.RemoteException;
public void setBTOn(ICommonCallback callback) throws android.os.RemoteException;
public boolean isBTOn() throws android.os.RemoteException;
public boolean isBTHeadsetConnected() throws android.os.RemoteException;
public long getCurrentCallBeginTS() throws android.os.RemoteException;
public void startRecordingWav() throws android.os.RemoteException;
public void startRecordingMp3() throws android.os.RemoteException;
public void startRecordingMp3WithParams(int sampleRate, int bitRate) throws android.os.RemoteException;
}
