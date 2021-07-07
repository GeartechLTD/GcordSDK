/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Projects\\GTPlatform\\app\\src\\main\\aidl\\cn\\com\\geartech\\gtplatform\\model\\aidl\\IBTAidlCallback.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
// Declare any non-default types here with import statements

public interface IBTAidlCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IBTAidlCallback
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IBTAidlCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IBTAidlCallback interface,
 * generating a proxy if needed.
 */
public static IBTAidlCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IBTAidlCallback))) {
return ((IBTAidlCallback)iin);
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
case TRANSACTION_onGetBTVersion:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onGetBTVersion(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPairingStateChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onPairingStateChange(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onGetPairedDevice:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onGetPairedDevice(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onGetPairedDeviceCount:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onGetPairedDeviceCount(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onGetBTName:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onGetBTName(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onVolumeChange:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onVolumeChange(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPhoneNumReceive:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onPhoneNumReceive(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onHangup:
{
data.enforceInterface(DESCRIPTOR);
this.onHangup();
reply.writeNoException();
return true;
}
case TRANSACTION_onSoundChannelChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onSoundChannelChange(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onHFPConnectionStateChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onHFPConnectionStateChange(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onHFPIncomingCallEnd:
{
data.enforceInterface(DESCRIPTOR);
this.onHFPIncomingCallEnd();
reply.writeNoException();
return true;
}
case TRANSACTION_onHFPCallTerminated:
{
data.enforceInterface(DESCRIPTOR);
this.onHFPCallTerminated();
reply.writeNoException();
return true;
}
case TRANSACTION_onHFPInCall:
{
data.enforceInterface(DESCRIPTOR);
this.onHFPInCall();
reply.writeNoException();
return true;
}
case TRANSACTION_onBTIncomingCall:
{
data.enforceInterface(DESCRIPTOR);
this.onBTIncomingCall();
reply.writeNoException();
return true;
}
case TRANSACTION_onBTOutgoingCall:
{
data.enforceInterface(DESCRIPTOR);
this.onBTOutgoingCall();
reply.writeNoException();
return true;
}
case TRANSACTION_onGetPBAPStatus:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onGetPBAPStatus(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onGetPhonebook:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onGetPhonebook(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPhoneCallRejected:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onPhoneCallRejected(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPhoneMute:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onPhoneMute(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onDTMFPlayed:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onDTMFPlayed(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onCallingDeviceSwitched:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onCallingDeviceSwitched(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPhoneCallDial:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onPhoneCallDial(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPhoneCallAnswered:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onPhoneCallAnswered(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IBTAidlCallback
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
@Override public void onGetBTVersion(String version) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(version);
mRemote.transact(Stub.TRANSACTION_onGetBTVersion, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPairingStateChange(int status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(status);
mRemote.transact(Stub.TRANSACTION_onPairingStateChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onGetPairedDevice(String device) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(device);
mRemote.transact(Stub.TRANSACTION_onGetPairedDevice, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onGetPairedDeviceCount(int count) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(count);
mRemote.transact(Stub.TRANSACTION_onGetPairedDeviceCount, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onGetBTName(String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_onGetBTName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onVolumeChange(String volume) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(volume);
mRemote.transact(Stub.TRANSACTION_onVolumeChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPhoneNumReceive(String phoneNum) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(phoneNum);
mRemote.transact(Stub.TRANSACTION_onPhoneNumReceive, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onHangup() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onHangup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onSoundChannelChange(int status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(status);
mRemote.transact(Stub.TRANSACTION_onSoundChannelChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onHFPConnectionStateChange(int status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(status);
mRemote.transact(Stub.TRANSACTION_onHFPConnectionStateChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onHFPIncomingCallEnd() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onHFPIncomingCallEnd, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onHFPCallTerminated() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onHFPCallTerminated, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onHFPInCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onHFPInCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onBTIncomingCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onBTIncomingCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onBTOutgoingCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onBTOutgoingCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onGetPBAPStatus(int status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(status);
mRemote.transact(Stub.TRANSACTION_onGetPBAPStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onGetPhonebook(String phonebook) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(phonebook);
mRemote.transact(Stub.TRANSACTION_onGetPhonebook, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPhoneCallRejected(int result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(result);
mRemote.transact(Stub.TRANSACTION_onPhoneCallRejected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPhoneMute(int result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(result);
mRemote.transact(Stub.TRANSACTION_onPhoneMute, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onDTMFPlayed(int result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(result);
mRemote.transact(Stub.TRANSACTION_onDTMFPlayed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onCallingDeviceSwitched(int result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(result);
mRemote.transact(Stub.TRANSACTION_onCallingDeviceSwitched, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPhoneCallDial(int result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(result);
mRemote.transact(Stub.TRANSACTION_onPhoneCallDial, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPhoneCallAnswered(int result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(result);
mRemote.transact(Stub.TRANSACTION_onPhoneCallAnswered, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onGetBTVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onPairingStateChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onGetPairedDevice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onGetPairedDeviceCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onGetBTName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onVolumeChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_onPhoneNumReceive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_onHangup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_onSoundChannelChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_onHFPConnectionStateChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_onHFPIncomingCallEnd = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_onHFPCallTerminated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_onHFPInCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_onBTIncomingCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_onBTOutgoingCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_onGetPBAPStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_onGetPhonebook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_onPhoneCallRejected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_onPhoneMute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_onDTMFPlayed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_onCallingDeviceSwitched = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_onPhoneCallDial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_onPhoneCallAnswered = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
}
public void onGetBTVersion(String version) throws android.os.RemoteException;
public void onPairingStateChange(int status) throws android.os.RemoteException;
public void onGetPairedDevice(String device) throws android.os.RemoteException;
public void onGetPairedDeviceCount(int count) throws android.os.RemoteException;
public void onGetBTName(String name) throws android.os.RemoteException;
public void onVolumeChange(String volume) throws android.os.RemoteException;
public void onPhoneNumReceive(String phoneNum) throws android.os.RemoteException;
public void onHangup() throws android.os.RemoteException;
public void onSoundChannelChange(int status) throws android.os.RemoteException;
public void onHFPConnectionStateChange(int status) throws android.os.RemoteException;
public void onHFPIncomingCallEnd() throws android.os.RemoteException;
public void onHFPCallTerminated() throws android.os.RemoteException;
public void onHFPInCall() throws android.os.RemoteException;
public void onBTIncomingCall() throws android.os.RemoteException;
public void onBTOutgoingCall() throws android.os.RemoteException;
public void onGetPBAPStatus(int status) throws android.os.RemoteException;
public void onGetPhonebook(String phonebook) throws android.os.RemoteException;
public void onPhoneCallRejected(int result) throws android.os.RemoteException;
public void onPhoneMute(int result) throws android.os.RemoteException;
public void onDTMFPlayed(int result) throws android.os.RemoteException;
public void onCallingDeviceSwitched(int result) throws android.os.RemoteException;
public void onPhoneCallDial(int result) throws android.os.RemoteException;
public void onPhoneCallAnswered(int result) throws android.os.RemoteException;
}
