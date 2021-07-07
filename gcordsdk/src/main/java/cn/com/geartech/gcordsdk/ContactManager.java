package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by pangyuning on 17/2/16.
 */

public class ContactManager {

    protected ContactManager()
    {

    }

    Context context;

    protected void init(Context context)
    {
        this.context = context;

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(EVENT_QUERY_3RD_CONTACT_RESULT))
                {
                    String name = intent.getStringExtra("name");

                    //Log.e("call", "query contact result:" +name);
                }
            }
        }, new IntentFilter(EVENT_QUERY_3RD_CONTACT_RESULT));
    }

    protected static final String EVENT_NOTIFY_QUERY_CONTACT_DATA_RESULT = "cn.com.geartech.event_notify_query_contact_data_result";


    public static String EVENT_QUERY_3RD_CONTACT_RESULT = "cn.com.geartech.event_query_3rd_contact_result";


    public interface Check3rdContactDataListener{
        public String onQueryContactData(String phoneNumber);
    }


    Check3rdContactDataListener check3rdContactDataListener;

    public void setCheck3rdContactDataListener(Check3rdContactDataListener check3rdContactDataListener) {
        this.check3rdContactDataListener = check3rdContactDataListener;
    }

    protected Check3rdContactDataListener getCheck3rdContactDataListener() {
        return check3rdContactDataListener;
    }

    public void notifyQueryContactDataResult(String name, String number)
    {
        if(name != null && number != null)
        {
            Intent intent = new Intent(EVENT_NOTIFY_QUERY_CONTACT_DATA_RESULT);
            intent.putExtra("name", name);
            intent.putExtra("number", number);
            context.sendBroadcast(intent);
        }
    }


}
