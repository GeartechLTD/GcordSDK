package cn.com.geartech.gcordsdk;

import android.content.Context;
import android.content.Intent;

import cn.com.geartech.gcordsdk.ui.GcordDialActivity;

/**
 * Created by cuizhy on 16/8/29.
 */
public class GcordSystemUIManager {

    public static final String IS_OPEN_BY_OTHER_APP = "IS_OPEN_BY_OTHER_APP";
    private static GcordSystemUIManager instance = null;
    private Context context = null;

    private GcordSystemUIManager()
    {

    }

    protected static GcordSystemUIManager getInstance() {
        if(instance == null)
        {
            instance = new GcordSystemUIManager();
        }
        return instance;
    }

    public void init(Context context)
    {
        this.context = context;
    }

    public void startSystemDialActivity(Context context)
    {
        Intent intent = new Intent(context, GcordDialActivity.class);
        context.startActivity(intent);
    }
}
