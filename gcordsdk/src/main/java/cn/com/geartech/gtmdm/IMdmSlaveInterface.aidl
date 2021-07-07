// IMdmSlaveInterface.aidl
package cn.com.geartech.gtmdm;

// Declare any non-default types here with import statements
import cn.com.geartech.gtmdm.IMdmCommandInterface;
import cn.com.geartech.gtmdm.IMdmEventCallback;

interface IMdmSlaveInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void hail(IMdmCommandInterface commandCallback, String packageName);

    void serve(String eventId, String result);

    void progress(String eventId, int progress, String description);

    void failed(String eventId, String detail);

    void serve2(int eventId, String result);

    void uploadFile(String filePath, String fileName ,IMdmEventCallback callback);
}
