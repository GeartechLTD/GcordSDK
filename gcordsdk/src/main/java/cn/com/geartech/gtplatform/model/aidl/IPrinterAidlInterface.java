/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/xu/projects/GTPlatform/app/src/main/aidl/cn/com/geartech/gtplatform/model/aidl/IPrinterAidlInterface.aidl
 */
package cn.com.geartech.gtplatform.model.aidl;
// Declare any non-default types here with import statements

public interface IPrinterAidlInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IPrinterAidlInterface
{
    private static final String DESCRIPTOR = "cn.com.geartech.gtplatform.model.aidl.IPrinterAidlInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.com.geartech.gtplatform.model.aidl.IPrinterAidlInterface interface,
 * generating a proxy if needed.
 */
public static IPrinterAidlInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
    if (((iin != null) && (iin instanceof IPrinterAidlInterface))) {
        return ((IPrinterAidlInterface) iin);
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
case TRANSACTION_printString:
{
data.enforceInterface(DESCRIPTOR);
    String _arg0;
_arg0 = data.readString();
this.printString(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setPrintSize:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.setPrintSize(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setBold:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setBold(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setUnderline:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setUnderline(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_clearSetting:
{
data.enforceInterface(DESCRIPTOR);
this.clearSetting();
reply.writeNoException();
return true;
}
case TRANSACTION_setLineSpacing:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setLineSpacing(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setAlignment:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setAlignment(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_printImage:
{
data.enforceInterface(DESCRIPTOR);
    String _arg0;
_arg0 = data.readString();
this.printImage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_printQRCode:
{
data.enforceInterface(DESCRIPTOR);
    String _arg0;
_arg0 = data.readString();
this.printQRCode(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_printQRCodeFitSize:
{
data.enforceInterface(DESCRIPTOR);
    String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
this.printQRCodeFitSize(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
    case TRANSACTION_reset: {
        data.enforceInterface(DESCRIPTOR);
        this.reset();
        reply.writeNoException();
        return true;
    }
    case TRANSACTION_printTestPage: {
        data.enforceInterface(DESCRIPTOR);
        this.printTestPage();
        reply.writeNoException();
        return true;
    }
    case TRANSACTION_printBlank: {
        data.enforceInterface(DESCRIPTOR);
        int _arg0;
        _arg0 = data.readInt();
        this.printBlank(_arg0);
        reply.writeNoException();
        return true;
    }
case TRANSACTION_getPrinterConnectionState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPrinterConnectionState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_registerCallBack:
{
data.enforceInterface(DESCRIPTOR);
    IPrinterAidlCallback _arg0;
    _arg0 = IPrinterAidlCallback.Stub.asInterface(data.readStrongBinder());
    String _arg1;
_arg1 = data.readString();
this.registerCallBack(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_hasPaper:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.hasPaper();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}

    private static class Proxy implements IPrinterAidlInterface
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
public void printString(String content) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(content);
mRemote.transact(Stub.TRANSACTION_printString, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void setPrintSize(int scaleWidth, int scaleHeight) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(scaleWidth);
_data.writeInt(scaleHeight);
mRemote.transact(Stub.TRANSACTION_setPrintSize, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void setBold(boolean isBold) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((isBold)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setBold, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void setUnderline(boolean isUnderline) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((isUnderline)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setUnderline, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void clearSetting() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_clearSetting, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void setLineSpacing(int n) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(n);
mRemote.transact(Stub.TRANSACTION_setLineSpacing, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void setAlignment(int n) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(n);
mRemote.transact(Stub.TRANSACTION_setAlignment, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void printImage(String imagePath) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(imagePath);
mRemote.transact(Stub.TRANSACTION_printImage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void printQRCode(String url) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
mRemote.transact(Stub.TRANSACTION_printQRCode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void printQRCodeFitSize(String url, int width, int height) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(url);
_data.writeInt(width);
_data.writeInt(height);
mRemote.transact(Stub.TRANSACTION_printQRCodeFitSize, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public void reset() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
    try {
        _data.writeInterfaceToken(DESCRIPTOR);
        mRemote.transact(Stub.TRANSACTION_reset, _data, _reply, 0);
        _reply.readException();
    } finally {
        _reply.recycle();
        _data.recycle();
    }
}

    @Override
    public void printTestPage() throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(Stub.TRANSACTION_printTestPage, _data, _reply, 0);
            _reply.readException();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    @Override
    public void printBlank(int n) throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeInt(n);
            mRemote.transact(Stub.TRANSACTION_printBlank, _data, _reply, 0);
            _reply.readException();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    @Override
    public int getPrinterConnectionState() throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPrinterConnectionState, _data, _reply, 0);
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
    public void registerCallBack(IPrinterAidlCallback callback, String packageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
_data.writeString(packageName);
mRemote.transact(Stub.TRANSACTION_registerCallBack, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}

    @Override
    public boolean hasPaper() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_hasPaper, _data, _reply, 0);
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
static final int TRANSACTION_printString = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setPrintSize = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_setBold = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_setUnderline = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_clearSetting = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_setLineSpacing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_setAlignment = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_printImage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_printQRCode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_printQRCodeFitSize = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_reset = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_printTestPage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_printBlank = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_getPrinterConnectionState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_registerCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_hasPaper = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
}
/**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
public void printString(String content) throws android.os.RemoteException;
public void setPrintSize(int scaleWidth, int scaleHeight) throws android.os.RemoteException;
public void setBold(boolean isBold) throws android.os.RemoteException;
public void setUnderline(boolean isUnderline) throws android.os.RemoteException;
public void clearSetting() throws android.os.RemoteException;
public void setLineSpacing(int n) throws android.os.RemoteException;
public void setAlignment(int n) throws android.os.RemoteException;

    public void printImage(String imagePath) throws android.os.RemoteException;

    public void printQRCode(String url) throws android.os.RemoteException;

    public void printQRCodeFitSize(String url, int width, int height) throws android.os.RemoteException;

    public void reset() throws android.os.RemoteException;

    public void printTestPage() throws android.os.RemoteException;

    public void printBlank(int n) throws android.os.RemoteException;
public int getPrinterConnectionState() throws android.os.RemoteException;

    public void registerCallBack(IPrinterAidlCallback callback, String packageName) throws android.os.RemoteException;
public boolean hasPaper() throws android.os.RemoteException;
}
