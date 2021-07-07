// IBlackListAIDL.aidl
package cn.com.geartech.app;

// Declare any non-default types here with import statements

interface IBlackListAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    boolean isPhoneNumberInBlackList(String phoneNumber);
    void addPhoneNumberToBlackList(String phoneNumber);
    void removePhoneNumberFromBlackList(String phoneNumber);
    String[] getBlackList();
}
