package cn.com.geartech.gcordsdk;

/**
 * Created by pangyuning on 16/8/1.
 */
public class GcordHelper {

    GcordSDK.InternalInitCallback internalInitCallback;

    protected void setInitCallback(GcordSDK.InternalInitCallback initCallback)
    {
        internalInitCallback = initCallback;
    }
}
