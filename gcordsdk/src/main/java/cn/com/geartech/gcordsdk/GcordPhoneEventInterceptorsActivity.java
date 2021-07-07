package cn.com.geartech.gcordsdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by pangyuning on 15/11/5.
 */
public class GcordPhoneEventInterceptorsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if(PhoneAPI.GCORD_INTENT_HANDLE_INCOMING_CALL.equals(getIntent().getAction()))
            {
                PSTNInternal.getInstance().notifyIncomingCallEvent("");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

        if(PhoneAPI.GCORD_INTENT_HANDLE_INCOMING_CALL.equals(getIntent().getAction()))
        {
            PSTNInternal.getInstance().notifyIncomingCallEvent("");
        }
    }
}
