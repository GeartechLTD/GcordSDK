// ICommonCallback.aidl
package cn.com.geartech.gtplatform.model.aidl;

// Declare any non-default types here with import statements

interface ICommonCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onComplete(boolean ret, int num, int num2 ,String s, String s1);
}
