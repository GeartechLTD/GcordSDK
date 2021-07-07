/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/pangyuning/gtplatform_release/app/src/main/aidl/cn/com/geartech/gtplatform/model/aidl/IGTAidlCallback.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
// Declare any non-default types here with import statements

public interface IGTAidlCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IGTAidlCallback
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IGTAidlCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IGTAidlCallback interface,
 * generating a proxy if needed.
 */
public static IGTAidlCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IGTAidlCallback))) {
return ((IGTAidlCallback)iin);
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
case TRANSACTION_onPushMessage:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
this.onPushMessage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onEvent:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
android.os.Bundle _arg1;
if ((0!=data.readInt())) {
_arg1 = android.os.Bundle.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.onEvent(_arg0, _arg1);
reply.writeNoException();
if ((_arg1!=null)) {
reply.writeInt(1);
_arg1.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_onCheckUpgradeResult:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _arg1;
_arg1 = (0!=data.readInt());
String _arg2;
_arg2 = data.readString();
String _arg3;
_arg3 = data.readString();
String _arg4;
_arg4 = data.readString();
String _arg5;
_arg5 = data.readString();
int _arg6;
_arg6 = data.readInt();
java.util.List<String> _arg7;
_arg7 = data.createStringArrayList();
java.util.List<String> _arg8;
_arg8 = data.createStringArrayList();
this.onCheckUpgradeResult(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
reply.writeNoException();
reply.writeStringList(_arg7);
reply.writeStringList(_arg8);
return true;
}
case TRANSACTION_onDownloadProgressChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onDownloadProgressChange(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onDowndloadComplete:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
String _arg1;
_arg1 = data.readString();
this.onDowndloadComplete(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onInstallComplete:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
String _arg1;
_arg1 = data.readString();
this.onInstallComplete(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_fetchDogBell:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
int _result = this.fetchDogBell(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getHandlePickUpType:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getHandlePickUpType();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_queryContactData:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _result = this.queryContactData(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_onScreenStrategyChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
this.onScreenStrategyChange(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_queryInt:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
int _result = this.queryInt(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_queryString:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _result = this.queryString(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_quertInt1:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
int _result = this.quertInt1(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IGTAidlCallback
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
@Override public void onPushMessage(String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_onPushMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onEvent(String eventType, android.os.Bundle bundle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(eventType);
if ((bundle!=null)) {
_data.writeInt(1);
bundle.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onEvent, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
bundle.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onCheckUpgradeResult(boolean success, boolean needUpdate, String newVersionName, String upgradeInfo, String packageSHA1, String errorMessage, int flag, java.util.List<String> fieldKeys, java.util.List<String> fieldValues) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((success)?(1):(0)));
_data.writeInt(((needUpdate)?(1):(0)));
_data.writeString(newVersionName);
_data.writeString(upgradeInfo);
_data.writeString(packageSHA1);
_data.writeString(errorMessage);
_data.writeInt(flag);
_data.writeStringList(fieldKeys);
_data.writeStringList(fieldValues);
mRemote.transact(Stub.TRANSACTION_onCheckUpgradeResult, _data, _reply, 0);
_reply.readException();
_reply.readStringList(fieldKeys);
_reply.readStringList(fieldValues);
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onDownloadProgressChange(int progress) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(progress);
mRemote.transact(Stub.TRANSACTION_onDownloadProgressChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onDowndloadComplete(boolean success, String errorMessage) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((success)?(1):(0)));
_data.writeString(errorMessage);
mRemote.transact(Stub.TRANSACTION_onDowndloadComplete, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onInstallComplete(boolean success, String errorMessage) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((success)?(1):(0)));
_data.writeString(errorMessage);
mRemote.transact(Stub.TRANSACTION_onInstallComplete, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int fetchDogBell(String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_fetchDogBell, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getHandlePickUpType() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getHandlePickUpType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public String queryContactData(String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_queryContactData, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void onScreenStrategyChange(int strategy, int param1, int param2) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(strategy);
_data.writeInt(param1);
_data.writeInt(param2);
mRemote.transact(Stub.TRANSACTION_onScreenStrategyChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int queryInt(String key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
mRemote.transact(Stub.TRANSACTION_queryInt, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public String queryString(String key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
mRemote.transact(Stub.TRANSACTION_queryString, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int quertInt1(String key, String param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(key);
_data.writeString(param);
mRemote.transact(Stub.TRANSACTION_quertInt1, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_onPushMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onCheckUpgradeResult = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onDownloadProgressChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onDowndloadComplete = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onInstallComplete = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_fetchDogBell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getHandlePickUpType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_queryContactData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_onScreenStrategyChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_queryInt = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_queryString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_quertInt1 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public void onPushMessage(String message) throws android.os.RemoteException;
public void onEvent(String eventType, android.os.Bundle bundle) throws android.os.RemoteException;
public void onCheckUpgradeResult(boolean success, boolean needUpdate, String newVersionName, String upgradeInfo, String packageSHA1, String errorMessage, int flag, java.util.List<String> fieldKeys, java.util.List<String> fieldValues) throws android.os.RemoteException;
public void onDownloadProgressChange(int progress) throws android.os.RemoteException;
public void onDowndloadComplete(boolean success, String errorMessage) throws android.os.RemoteException;
public void onInstallComplete(boolean success, String errorMessage) throws android.os.RemoteException;
public int fetchDogBell(String packageName) throws android.os.RemoteException;
public int getHandlePickUpType() throws android.os.RemoteException;
public String queryContactData(String number) throws android.os.RemoteException;
public void onScreenStrategyChange(int strategy, int param1, int param2) throws android.os.RemoteException;
public int queryInt(String key) throws android.os.RemoteException;
public String queryString(String key) throws android.os.RemoteException;
public int quertInt1(String key, String param) throws android.os.RemoteException;
}
