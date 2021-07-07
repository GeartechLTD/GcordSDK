// GcordAccountCallBack.aidl
package cn.com.geartech.app;

// Declare any non-default types here with import statements

interface GcordAccountCallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    void onGcordLoginStatusChanged(int status);
}
