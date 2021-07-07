package cn.com.geartech.gtplatform.model.calllog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by pangyuning on 16/11/16.
 */

public class SimpleCallLogManager {

    public static final String EVENT_SIMPLE_CALLLOG_UPDATED = "cn.com.geartech.gcordSDK.simple_calllog_updated";

    static final String EVENT_CALL_LOG_ADDED = "cn.com.geartech.simple_call_log_added";
    static final String PARAM_CALL_LOG_DATA = "callLogData";

    static final String CALL_LOG_PATH = "/sdcard/CallLog/";
    static final String CALL_LOG_FILE = "calllog.dat";

    static final int MAX_CALLLOG_NUMBER = 300;

    ArrayList<SimpleCallLogItem> currentItems = new ArrayList<SimpleCallLogItem>();

    static SimpleCallLogManager instance = null;

    public static SimpleCallLogManager getInstance()
    {
        if(instance == null)
        {
            instance = new SimpleCallLogManager();
        }

        return instance;
    }


    SimpleCallLogManager()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SimpleCallLogItem> items = readAllCallLogItemsFromFile();

//        Log.d("CallLog","calllog size:" + items.size());
//
//        for (SimpleCallLogItem item:items)
//        {
//            Log.d("CallLog","calllog:" + item.callType + " " + item.callResult + " " + item.callBeginTimeStamp + " " +
//            item.callDuration + " " + item.opponentNumber);
//        }

                currentItems.clear();
                currentItems.addAll(items);
            }
        }).start();


    }

    public void init(Context context)
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EVENT_CALL_LOG_ADDED);
        context.registerReceiver(mReceiver, intentFilter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                if(intent.getAction().equals(EVENT_CALL_LOG_ADDED))
                {
                    String data = intent.getStringExtra(PARAM_CALL_LOG_DATA);
                    if(data != null)
                    {
                        Object item = SerializeObject.stringToObject(data);
                        if(item != null)
                        {
                            SimpleCallLogItem simpleCallLogItem = (SimpleCallLogItem)item;
                            currentItems.add(simpleCallLogItem);

                            //notify refresh
                        }
                    }

                    //Log.d("CallLog","current size:" + currentItems.size());

//                    for (SimpleCallLogItem item:currentItems)
//                    {
//                        Log.d("CallLog", "calllog :" + item.callType + " " + item.callResult + " " +
//                                item.opponentNumber + " " + item.callBeginTimeStamp);
//                    }

                    context.sendBroadcast(new Intent(EVENT_SIMPLE_CALLLOG_UPDATED));
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    };

    public List<SimpleCallLogItem> getCurrentCallLogs()
    {
        ArrayList<SimpleCallLogItem> logItems = new ArrayList<SimpleCallLogItem>();
        logItems.addAll(currentItems);

        return logItems;
    }
//    SimpleCallLogItem currentItem;
//
//    boolean isIncomingCall = false;
//    boolean answered = false;
//
//    String currentDialNumber = "";
//    long lastDialTimestamp = -1;
//
//    public void incomingCall()
//    {
//        currentItem = new SimpleCallLogItem();
//        currentItem.setCallType(SimpleCallLogItem.CALL_TYPE_IN);
//        isIncomingCall = true;
//        answered = false;
//        lastDialTimestamp = -1;
//    }
//
//    public void incomingNumber(String number)
//    {
//        if(currentItem != null && isIncomingCall)
//        {
//            currentItem.setOpponentNumber(number);
//            currentItem.setCallBeginTimeStamp(System.currentTimeMillis());
//        }
//    }
//
//    public void dial(String num)
//    {
//        long currentTimeStamp = System.currentTimeMillis();
//        if(!isIncomingCall &&  (lastDialTimestamp < 0 || currentTimeStamp - lastDialTimestamp <= 5000))
//        {
//            currentDialNumber += num;
//        }
//    }
//
//    public void pickUp()
//    {
//        long currentTimeStamp = System.currentTimeMillis();
//
//        if(isIncomingCall)
//        {
//            answered = true;
//            currentItem.setCallResult(SimpleCallLogItem.CALL_RESULT_ANSWERED);
//        }
//        else
//        {
//            currentItem = new SimpleCallLogItem();
//            currentItem.setCallType(SimpleCallLogItem.CALL_TYPE_OUT);
//            currentItem.setCallBeginTimeStamp(currentTimeStamp);
//        }
//
//
//        currentItem.setPickUpTimeStamp(currentTimeStamp);
//
//        currentDialNumber = "";
//        lastDialTimestamp = -1;
//    }
//
//    public void hangOff()
//    {
//        isIncomingCall = false;
//
//        lastDialTimestamp = -1;
//
//        currentItem.setCallDuration(System.currentTimeMillis() - currentItem.getPickUpTimeStamp());
//
//        if(!isIncomingCall)
//        {
//            currentItem.setOpponentNumber(currentDialNumber);
//            addCallLog(currentItem);
//        }
//        else
//        {
//            addCallLog(currentItem);
//        }
//
//        currentItem = null;
//    }
//
//    public void ringEnd()
//    {
//        if(!answered)
//        {
//            isIncomingCall = false;
//            currentItem.setCallResult(SimpleCallLogItem.CALL_RESULT_MISSED);
//
//            addCallLog(currentItem);
//            currentItem = null;
//        }
//    }
//
//
//
//    public void addCallLog(SimpleCallLogItem callLogItem)
//    {
//        currentItems.add(callLogItem);
//        if(currentItems.size() > MAX_CALLLOG_NUMBER)
//        {
//            currentItems.remove(0);
//        }
//
//        File callLogFile = openCallLogFile();
//        if(callLogFile != null)
//        {
//            try {
//                FileOutputStream fileOutputStream = new FileOutputStream(callLogFile, false);
//
//                String data = SerializeObject.objectToString(currentItems);
//                if(data != null)
//                {
//                    fileOutputStream.write(data.getBytes());
//                }
//
//                fileOutputStream.flush();
//                fileOutputStream.close();
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    File openCallLogFile()
//    {
//        try {
//            File callLogDir = new File(CALL_LOG_PATH);
//            if(callLogDir.exists() && !callLogDir.isDirectory())
//            {
//                callLogDir.delete();
//            }
//
//            if(!callLogDir.exists())
//            {
//                callLogDir.mkdirs();
//            }
//
//            String callLogFileName = CALL_LOG_PATH + CALL_LOG_FILE;
//
//            File callLogFile = new File(callLogFileName);
//            if(!callLogFile.exists())
//            {
//                callLogFile.createNewFile();
//            }
//
//            return callLogFile;
//
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//
//            return null;
//        }
//    }


    List<SimpleCallLogItem> readAllCallLogItemsFromFile()
    {
        ArrayList<SimpleCallLogItem> result = new ArrayList<SimpleCallLogItem>();

        String callLogFileName = CALL_LOG_PATH + CALL_LOG_FILE;

        File callLogFile = new File(callLogFileName);

        if(!callLogFile.exists())
        {
            return result;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(callLogFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String data = null;

            String tmp = "";

            while ((data = bufferedReader.readLine())!= null)
            {
                tmp += data;


            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            Object items =  SerializeObject.stringToObject(tmp);
            if(items != null)
            result = (ArrayList<SimpleCallLogItem>)items;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
}
