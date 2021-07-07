// GcordAccountAIDL.aidl
package cn.com.geartech.app;

import cn.com.geartech.app.GcordAccountCallBack;

// Declare any non-default types here with import statements

interface GcordAccountAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    int getGcordLoginStatus(String packageName);
    String getGcordAccountToken(String packageName);
    void registerCallBack(String packageName,GcordAccountCallBack callBack);
    void unregisterCallBack(String packageName);

    void requestCode(String packageName, String phone);
    void performLogin(String packageName, String phone, String code);
}
