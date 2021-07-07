// IMdmCommandInterface.aidl
package cn.com.geartech.gtmdm;

// Declare any non-default types here with import statements

interface IMdmCommandInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onCommand(int cmdType, String command, int eventId);
}
