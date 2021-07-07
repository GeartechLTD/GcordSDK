// IMdmEventCallback.aidl
package cn.com.geartech.gtmdm;

// Declare any non-default types here with import statements

interface IMdmEventCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onResult(boolean success, String errorMessage, int param1, int param2, String str1, String str2);
}
