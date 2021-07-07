package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import cn.com.geartech.gtmdm.IMdmCommandInterface;
import cn.com.geartech.gtmdm.IMdmEventCallback;
import cn.com.geartech.gtmdm.IMdmSlaveInterface;

/**
 * Created by pangyuning on 16/3/7.
 */
public class MdmManager  extends  GcordHelper{

    static MdmManager instance = null;

    static final String EVENT_ALL_HAIL_MADAM = "cn.com.geartech.all_hail_mdm";

    public static final String EVENT_GT_DEVICE_CFG_UPDATED = "cn.com.geartech.gtmdm.event_gt_device_cfg_updated";
    public static final String PARAM_CFG_NAME = "name";

    private MdmManager()
    {

    }

    protected static MdmManager getInstance() {

        if(instance == null)
        {
            instance = new MdmManager();
        }

        return instance;
    }

    Context context;

    protected void init(Context context)
    {
        if(this.context == null)
        {
            this.context = context;
            IntentFilter intentFilter = new IntentFilter(EVENT_ALL_HAIL_MADAM);
            intentFilter.addAction(EVENT_GT_DEVICE_CFG_UPDATED);
            context.registerReceiver(mReceiver, intentFilter);
            connectToMdm();
            handler = new Handler();
        }
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(EVENT_ALL_HAIL_MADAM))
            {
                connectToMdm();
            }
            else if(intent.getAction().equals(EVENT_GT_DEVICE_CFG_UPDATED))
            {
                String cfgName = intent.getStringExtra(PARAM_CFG_NAME);
                updateConfig(cfgName);
            }
        }
    };


    void updateConfig(final String cfgName)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String value = readConfig(cfgName);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mdmConfigUpdateCallback != null)
                        {
                            mdmConfigUpdateCallback.onCfgUpdated(cfgName, value);
                        }
                    }
                });
            }
        }).start();
    }

    final static String CFG_DIR = "GTConfig/";


    String readContentByFilePath(String path)
    {
        File cfgFile = new File(path);
        if(cfgFile.exists())
        {
            try {
                FileInputStream fileInputStream = new FileInputStream(cfgFile);
                InputStreamReader reader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                }

                bufferedReader.close();
                reader.close();
                fileInputStream.close();

                String result = sb.toString();
                return result;

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return "";
    }

    /**
     * 堵塞式IO, 建议在线程上运行
     * @param key
     * @return
     */
    public String readConfig(String key)
    {
        String dir = Environment.getExternalStorageDirectory().getPath();
        if (!dir.endsWith("/") && !(dir.endsWith("\\")))
        {
            dir += "/";
        }
        dir += CFG_DIR;

        String cfgPath = dir + key + ".cfg";
        return readContentByFilePath(cfgPath);
    }

    public String readBackupConfig(String key)
    {
        String dir = Environment.getExternalStorageDirectory().getPath();
        if (!dir.endsWith("/") && !(dir.endsWith("\\")))
        {
            dir += "/";
        }
        dir += CFG_DIR;

        String cfgPath = dir + key + ".oldcfg";
        return readContentByFilePath(cfgPath);
    }

    void  connectToMdm()
    {
        //Log.e("mdmpush", "connect to mdm");

        Intent intent = new Intent("cn.com.geartech.mdmService");
        intent.setPackage("cn.com.geartech.gtmdm");

        boolean ret = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if(!ret)
        {
            if(internalInitCallback != null)
            {
                internalInitCallback.onInitFailed();
            }
        }
    }

    IMdmSlaveInterface slaveInterface = null;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {


            //Log.e("mdmPush","mdm connected");

            try {
                slaveInterface = IMdmSlaveInterface.Stub.asInterface(service);
                if(slaveInterface != null)
                {
                    slaveInterface.hail(commandInterface, context.getPackageName());
                }

                if(internalInitCallback != null)
                {
                    internalInitCallback.onInitFinished();
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            slaveInterface = null;
        }
    };

    Handler handler = null;

    IMdmCommandInterface.Stub commandInterface = new IMdmCommandInterface.Stub() {
        @Override
        public void onCommand(final int cmdType,final String command,final int eventId) throws RemoteException {


            //Log.e("Mdmpush", command);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(mdmMessageHandler != null)
                    {
                        mdmMessageHandler.onMessage(cmdType, eventId, command);
                    }
                }
            });
        }
    };


    public static interface MdmMessageHandler{
        public abstract void onMessage(int cmdType, int eventId, String command);
    }

    public static interface MdmConfigUpdateCallback{
        public abstract void onCfgUpdated(String key, String contents);
    }

    MdmMessageHandler mdmMessageHandler;

    MdmConfigUpdateCallback mdmConfigUpdateCallback;

    public void setMdmConfigUpdateCallback(MdmConfigUpdateCallback mdmConfigUpdateCallback) {
        this.mdmConfigUpdateCallback = mdmConfigUpdateCallback;
    }

    public void setMdmMessageHandler(MdmMessageHandler mdmMessageHandler) {
        this.mdmMessageHandler = mdmMessageHandler;
    }

    public void sendMessage(int type, String jsonStringData)
    {
        try {
            if(slaveInterface != null)
            {
                slaveInterface.serve2(type, jsonStringData);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void eventLog(String eventId, String eventName, String param1, String param2, String param3)
    {
        GTAidlHandler.getInstance().eventLog(eventId, eventName, param1, param2, param3);
    }

    public void pLog(String pLog)
    {
        GTAidlHandler.getInstance().pLog(pLog);
    }

    public void uploadFile(String filePath, String fileName, IMdmEventCallback callback)
    {
        try {
            if(slaveInterface != null)
            {
                slaveInterface.uploadFile(filePath, fileName, callback);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
