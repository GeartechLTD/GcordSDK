package cn.com.geartech.gcordsdk;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.DebugUtils;
import android.util.Log;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dinner on 15/4/23.
 */
public class DialTransaction {
    boolean dialFinished = false;
    String dialNum = "";

    Timer timer;

    Context context;

    public DialTransaction(Context c)
    {
        context = c;
    }


    public void inputNumber(String number)
    {
        if(!dialFinished)
        {
            dialNum += number;
        }
        delayCheck();
    }

    public void checkDialFinish()
    {
        delayCheck();
    }

    public String getDialNum()
    {
        return dialNum;
    }


    public void start()
    {
        dialFinished  = false;
        //delayCheck();
    }

    boolean isDialing = false;

    void delayCheck()
    {
        if(timer != null)
        {
            timer.cancel();
        }

        isDialing = true;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialFinished = true;

                //Log.e("123", "dial finish!");
                if(isDialing)
                {
                    Intent intent = new Intent(PhoneAPI.EVENT_DIAL_FINISH);

                    intent.setPackage(context.getPackageName());
                    //Log.e("Gcord","Send Dial Finish");

                    context.sendBroadcast(intent);

                    if(PSTNInternal.getInstance().listeners != null)
                    {
                        for (PhoneAPI.PhoneEventListener phoneEventListener: PSTNInternal.getInstance().listeners)
                        {
                            if(phoneEventListener != null)
                            {
                                phoneEventListener.onDialFinish();
                            }
                        }
                    }
                }

                isDialing = false;
            }
        }, 4000);
    }


    public void end()
    {
        if(timer != null)
        {
            timer.cancel();
        }
        if(isDialing)
        {
            Intent intent = new Intent(PhoneAPI.EVENT_DIAL_FINISH);

            intent.setPackage(context.getPackageName());
            //Log.e("Gcord","Send Dial Finish");

            context.sendBroadcast(intent);

            if(PSTNInternal.getInstance().listeners != null)
            {
                for (PhoneAPI.PhoneEventListener phoneEventListener: PSTNInternal.getInstance().listeners)
                {
                    if(phoneEventListener != null)
                    {
                        phoneEventListener.onDialFinish();
                    }
                }
            }
        }

        isDialing = false;
        dialFinished = true;
    }
}
