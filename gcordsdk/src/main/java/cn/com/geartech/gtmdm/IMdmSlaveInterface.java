/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/pangyuning/mdm/app/src/main/aidl/cn/com/geartech/gtmdm/IMdmSlaveInterface.aidl
 */
package cn.com.geartech.gtmdm;
public interface IMdmSlaveInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IMdmSlaveInterface
{
private static final String DESCRIPTOR = "cn.com.geartech.gtmdm.IMdmSlaveInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtmdm.IMdmSlaveInterface interface,
 * generating a proxy if needed.
 */
public static IMdmSlaveInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IMdmSlaveInterface))) {
return ((IMdmSlaveInterface)iin);
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
case TRANSACTION_hail:
{
data.enforceInterface(DESCRIPTOR);
IMdmCommandInterface _arg0;
_arg0 = IMdmCommandInterface.Stub.asInterface(data.readStrongBinder());
String _arg1;
_arg1 = data.readString();
this.hail(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_serve:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.serve(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_progress:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
String _arg2;
_arg2 = data.readString();
this.progress(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_failed:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
this.failed(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_serve2:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
String _arg1;
_arg1 = data.readString();
this.serve2(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_uploadFile:
{
data.enforceInterface(DESCRIPTOR);
String _arg0;
_arg0 = data.readString();
String _arg1;
_arg1 = data.readString();
IMdmEventCallback _arg2;
_arg2 = IMdmEventCallback.Stub.asInterface(data.readStrongBinder());
this.uploadFile(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IMdmSlaveInterface
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
@Override public void hail(IMdmCommandInterface commandCallback, String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((commandCallback!=null))?(commandCallback.asBinder()):(null)));
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_hail, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void serve(String eventId, String result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(eventId);
_data.writeString(result);
mRemote.transact(Stub.TRANSACTION_serve, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void progress(String eventId, int progress, String description) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(eventId);
_data.writeInt(progress);
_data.writeString(description);
mRemote.transact(Stub.TRANSACTION_progress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void failed(String eventId, String detail) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(eventId);
_data.writeString(detail);
mRemote.transact(Stub.TRANSACTION_failed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void serve2(int eventId, String result) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(eventId);
_data.writeString(result);
mRemote.transact(Stub.TRANSACTION_serve2, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void uploadFile(String filePath, String fileName, IMdmEventCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(filePath);
_data.writeString(fileName);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_uploadFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_hail = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_serve = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_progress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_failed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_serve2 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_uploadFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public void hail(IMdmCommandInterface commandCallback, String packageName) throws android.os.RemoteException;
public void serve(String eventId, String result) throws android.os.RemoteException;
public void progress(String eventId, int progress, String description) throws android.os.RemoteException;
public void failed(String eventId, String detail) throws android.os.RemoteException;
public void serve2(int eventId, String result) throws android.os.RemoteException;
public void uploadFile(String filePath, String fileName, IMdmEventCallback callback) throws android.os.RemoteException;
}
