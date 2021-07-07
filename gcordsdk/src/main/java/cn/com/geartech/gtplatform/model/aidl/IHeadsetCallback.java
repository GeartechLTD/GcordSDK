/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Projects\\GTPlatform\\app\\src\\main\\aidl\\cn\\com\\geartech\\gtplatform\\model\\aidl\\IHeadsetCallback.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
// Declare any non-default types here with import statements

public interface IHeadsetCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IHeadsetCallback
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IHeadsetCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IHeadsetCallback interface,
 * generating a proxy if needed.
 */
public static IHeadsetCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IHeadsetCallback))) {
return ((IHeadsetCallback)iin);
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
case TRANSACTION_onConnectionStateChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onConnectionStateChange(_arg0);
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
case TRANSACTION_onBatteryLevelChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onBatteryLevelChange(_arg0);
reply.writeNoException();
return true;
}
    case TRANSACTION_onBtn1Pressed: {
        data.enforceInterface(DESCRIPTOR);
        this.onBtn1Pressed();
        reply.writeNoException();
        return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IHeadsetCallback
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
@Override public void onConnectionStateChange(int status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(status);
mRemote.transact(Stub.TRANSACTION_onConnectionStateChange, _data, _reply, 0);
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
@Override public void onBatteryLevelChange(int percent) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(percent);
mRemote.transact(Stub.TRANSACTION_onBatteryLevelChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void onBtn1Pressed() throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(Stub.TRANSACTION_onBtn1Pressed, _data, _reply, 0);
            _reply.readException();
} finally {
            _reply.recycle();
            _data.recycle();
        }
    }
}
static final int TRANSACTION_onConnectionStateChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onPairingStateChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onBatteryLevelChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_onBtn1Pressed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void onConnectionStateChange(int status) throws android.os.RemoteException;
public void onPairingStateChange(int status) throws android.os.RemoteException;
public void onBatteryLevelChange(int percent) throws android.os.RemoteException;

    public void onBtn1Pressed() throws android.os.RemoteException;
}
