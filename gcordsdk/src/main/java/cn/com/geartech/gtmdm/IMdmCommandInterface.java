/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/pangyuning/mdm/app/src/main/aidl/cn/com/geartech/gtmdm/IMdmCommandInterface.aidl
 */
package cn.com.geartech.gtmdm;
// Declare any non-default types here with import statements

public interface IMdmCommandInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IMdmCommandInterface
{
private static final String DESCRIPTOR = "cn.com.geartech.gtmdm.IMdmCommandInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtmdm.IMdmCommandInterface interface,
 * generating a proxy if needed.
 */
public static IMdmCommandInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IMdmCommandInterface))) {
return ((IMdmCommandInterface)iin);
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
case TRANSACTION_onCommand:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
this.onCommand(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IMdmCommandInterface
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
@Override public void onCommand(int cmdType, String command, int eventId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(cmdType);
_data.writeString(command);
_data.writeInt(eventId);
mRemote.transact(Stub.TRANSACTION_onCommand, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public void onCommand(int cmdType, String command, int eventId) throws android.os.RemoteException;
}
