package cn.com.geartech.gcordsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DebugUtils;

import java.util.Deque;
import java.util.HashMap;

/**
 * Created by dinner on 15/5/26.
 */
public class ReportManager {

    static ReportManager instance;

    static String CTRL_REPORT_PROFILE = "cn.com.geartech.gtplatform.send_report_profile";
    static String PARAM_PACKAGE_NAME = "cn.com.geartech.gtplatform.package_name";
    static String PARAM_MESSAGE  = "cn.com.geartech.gtplatform.message";

    static String PROFILE_KEY_CITY = "city";
    static String PROFILE_KEY_NAME = "name";
    static String PROFILE_KEY_PROVINCE = "province";
    static String PROFILE_KEY_DISTRICT = "district";


    Context context;

    protected void setContext(Context c)
    {
        context = c;
    }

    private ReportManager()
    {}


    protected static ReportManager getInstance()
    {
        if(instance == null)
        {
            instance = new ReportManager();
        }

        return instance;
    }

    public void sendLocationInfo(String province, String city, String district)
    {
        HashMap<String, String> profile = new HashMap<String, String>();
        profile.put(PROFILE_KEY_CITY, city);
        profile.put(PROFILE_KEY_PROVINCE, province);
        profile.put(PROFILE_KEY_DISTRICT, district);

        sendProfile(profile);
    }


    protected void sendProfile(HashMap<String, String> profile)
    {
        if(profile == null)
        {
            return;
        }

        Intent intent = new Intent(CTRL_REPORT_PROFILE);
        String packageName = context.getPackageName();
        intent.putExtra(PARAM_PACKAGE_NAME, packageName);

        Bundle bundle = new Bundle();
        for(String key:profile.keySet())
        {
            bundle.putString(key, profile.get(key));
        }

        intent.putExtra(PARAM_MESSAGE, bundle);

        context.sendBroadcast(intent);


    }
}
