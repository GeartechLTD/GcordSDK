package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.com.geartech.gcordsdk.DebugLog;
import cn.com.geartech.gcordsdk.PSTNInternal;
import cn.com.geartech.gcordsdk.PhoneAPI;

/**
 * Created by pangyuning on 15/12/7.
 */
public class IncomingCallReceiver extends BroadcastReceiver {

    static String INTENT_INTERCEPT_INCOMING_CALL = "cn.com.geartech.gcordsdk.intercept_incoming_call";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(INTENT_INTERCEPT_INCOMING_CALL))
        {
            String phoneNumber = intent.getStringExtra("phoneNumber");
            PSTNInternal.getInstance().notifyIncomingCallEvent(phoneNumber);
        }

    }
}
