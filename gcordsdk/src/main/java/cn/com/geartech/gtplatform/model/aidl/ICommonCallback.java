/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/pangyuning/gtplatform_push/app/src/main/aidl/cn/com/geartech/gtplatform/model/aidl/ICommonCallback.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
// Declare any non-default types here with import statements

public interface ICommonCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements ICommonCallback
{
private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.ICommonCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.ICommonCallback interface,
 * generating a proxy if needed.
 */
public static ICommonCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof ICommonCallback))) {
return ((ICommonCallback)iin);
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
case TRANSACTION_onComplete:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
String _arg3;
_arg3 = data.readString();
String _arg4;
_arg4 = data.readString();
this.onComplete(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements ICommonCallback
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
public void onComplete(boolean ret, int num, int num2, String s, String s1) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((ret)?(1):(0)));
_data.writeInt(num);
_data.writeInt(num2);
_data.writeString(s);
_data.writeString(s1);
mRemote.transact(Stub.TRANSACTION_onComplete, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onComplete = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public void onComplete(boolean ret, int num, int num2, String s, String s1) throws android.os.RemoteException;
}
