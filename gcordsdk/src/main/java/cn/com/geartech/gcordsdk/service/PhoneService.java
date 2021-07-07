package cn.com.geartech.gcordsdk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import java.util.List;

import cn.com.geartech.gcordsdk.CallLogManager;
import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.dataType.CallLogItem;

public class PhoneService extends IntentService {
    public static final String ACTION_GET_ALL_CALL_LOGS = "cn.com.geartech.action.GET_ALL_CALL_LOGS";
    private static CallLogManager.GetCallLogCallback callLogUpdateCallback;


    public PhoneService() {
        super("PhoneService");
    }

    public static void getAllCallLogs(CallLogManager.GetCallLogCallback callback) {
        callLogUpdateCallback = callback;
        Intent intent = new Intent(GcordSDK.getInstance().getContext(), PhoneService.class);
        intent.setAction(ACTION_GET_ALL_CALL_LOGS);
        GcordSDK.getInstance().getContext().startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        if (ACTION_GET_ALL_CALL_LOGS.equals(action)) {
            GcordSDK.getInstance().getCallLogManager().doGetCallLogs(new CallLogManager.GetCallLogCallback() {
                @Override
                public void onGetCallLogs(List<CallLogItem> callLogItems) {
                    if (callLogUpdateCallback != null) {
                        callLogUpdateCallback.onGetCallLogs(callLogItems);
                    }
                    callLogUpdateCallback = null;
                }
            });
        }
    }

}
