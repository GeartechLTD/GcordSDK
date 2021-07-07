// IGTAidlInterface.aidl
package cn.com.geartech.gtplatform.model.aidl;

//import cn.com.geartech.gtplatform.model.aidl.IAidlCallback;

// Declare any non-default types here with import statements

interface IHomeAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void setHomeActionType(String packageName, String actionType);

    void setNavigationBarEnable(boolean enable);

}
