package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Printer;

import cn.com.geartech.gtplatform.model.aidl.IPrinterAidlCallback;
import cn.com.geartech.gtplatform.model.aidl.IPrinterAidlInterface;

/**
 * Created by pangyuning on 15/12/3.
 */
public class PrinterManager {

    static PrinterManager instance = null;

    public static interface PrinterManagerCallback{
//        public abstract void onPrinterServiceConnected();
//        public abstract void onPrinterServiceDisconnected();
        public abstract void onPrinterConnected();
        public abstract void onPrinterDisconnected();

    }

    static final int MESSAGE_PRINTER_CONNECTED = 1001;
    static final int MESSAGE_PRINTER_DISCONNECTED = 1002;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case MESSAGE_PRINTER_CONNECTED:
                {
                    DebugLog.logE("printer connected");
                    if(printerManagerCallback != null)
                    {
                        printerManagerCallback.onPrinterConnected();
                    }
                }
                    break;
                case MESSAGE_PRINTER_DISCONNECTED:
                {
                    DebugLog.logE("printer disconnected");
                    if(printerManagerCallback != null)
                    {
                        printerManagerCallback.onPrinterDisconnected();
                    }
                }
                    break;
            }
        }
    };

    PrinterManagerCallback printerManagerCallback;

    public PrinterManagerCallback getPrinterManagerCallback() {
        return printerManagerCallback;
    }

    public void setPrinterManagerCallback(PrinterManagerCallback printerManagerCallback) {
        this.printerManagerCallback = printerManagerCallback;
    }

    private PrinterManager()
    {

    }

    static protected PrinterManager getInstance()
    {
        if(instance == null)
        {
            instance = new PrinterManager();
        }

        return instance;
    }

    Context context;
    boolean enablePrinterService;

    protected void init(Context c)
    {
        DebugLog.logE("printerManager init");
        context = c;
        context.registerReceiver(mReceiver, new IntentFilter(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED));
    }

    public void enablePrinterService()
    {
        DebugLog.logE("enable printer service");
        enablePrinterService = true;
        connectToPrinterService();
    }

    public void disablePrinterService()
    {
        DebugLog.logE("disable printer service");

        enablePrinterService = false;
    }

    void connectToPrinterService()
    {
        Intent intent = new Intent("cn.com.geartech.gtplatform.PRINTER_SERVICE");
        intent.setPackage("cn.com.geartech.gtplatform");
        intent.putExtra("packageName", context.getPackageName());

        boolean ret = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        DebugLog.logE("bind service result:" + ret);
    }

    IPrinterAidlInterface printerAidlInterface;

    IPrinterAidlCallback.Stub printerAidlCallback = new IPrinterAidlCallback.Stub() {
        @Override
        public void onPrinterConnected() throws RemoteException {
            try {

                Message message = new Message();
                message.what = MESSAGE_PRINTER_CONNECTED;
                handler.sendMessage(message);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        @Override
        public void onPrinterDisconnected() throws RemoteException {
            try {

                Message message = new Message();
                message.what = MESSAGE_PRINTER_DISCONNECTED;
                handler.sendMessage(message);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                DebugLog.logE("printer service connected");

                printerAidlInterface = IPrinterAidlInterface.Stub.asInterface(service);

                printerAidlInterface.registerCallBack(printerAidlCallback, context.getPackageName());

            }catch (Throwable e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            if(printerManagerCallback != null)
            {
                printerManagerCallback.onPrinterDisconnected();
            }

            printerAidlInterface = null;
        }
    };

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(PSTNInternal.EVENT_GT_PLATFORM_RESTARTED))
            {
                if(enablePrinterService)
                {
                    connectToPrinterService();
                }
            }
        }
    };

    public void printString(String content)
    {
        try {
            if(printerAidlInterface != null)
            {
                printerAidlInterface.printString(content);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置字体宽高，只能分别设置放大1,2,3,4倍
     */
    public void setPrintSize(int scaleWidth, int scaleHeight)
    {
        try {
            if(printerAidlInterface != null)
            {
                printerAidlInterface.setPrintSize(scaleWidth, scaleHeight);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setBold(boolean isBold)
    {
        try {
            if(printerAidlInterface != null)
            {
                printerAidlInterface.setBold(isBold);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setUnderline(boolean isUnderline)
    {
        try {
            if(printerAidlInterface != null)
            {
                printerAidlInterface.setUnderline(isUnderline);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有设置
     */
    public void clearSetting()
    {
        try {
            if(printerAidlInterface != null)
            {
                printerAidlInterface.clearSetting();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置行间距为 n*0.125 毫米,
     * 缺省值为 n=30
     *
     * @param n 0 <= n <= 255
     */
    public void setLineSpacing(int n) {
        try {
            if(printerAidlInterface != null)
            {
                printerAidlInterface.setLineSpacing(n);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置对齐方式
     *
     * @param n 0对应左对齐
     *          1对应居中
     *          2对应右对齐
     */

    public static final int ALIGNMENT_LEFT = 0;
    public static final int ALIGNMENT_CENTER = 1;
    public static final int ALIGNMENT_RIGHT = 2;

    public void setAlignment(int n) {
        try {
            if(printerAidlInterface != null)
            {
                printerAidlInterface.setAlignment(n);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 只能打印二位图，超过纸张大小时只会打印原图的一部分
     *
     * @param imagePath 本地图片的绝对路径
     */
    public void printImage(String imagePath)
    {
        try {
            if(printerAidlInterface != null)
            {
                printerAidlInterface.printImage(imagePath);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 将url转成二维码打印出来, 默认宽高为300*300
     *
     * @param url 必须以http开头
     */
    public void printQRCode(String url)
    {
        try {
            if(printerAidlInterface != null)
            {
                printerAidlInterface.printQRCode(url);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 将url转成二维码打印出来
     *
     * @param url    必须以http开头
     * @param width  (0, 300]
     * @param height
     */
    public void printQRCode(String url, int width, int height) {
        try {
            if (printerAidlInterface != null) {
                printerAidlInterface.printQRCodeFitSize(url, width, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置打印机
     */
    public void reset() {
        try {
            if (printerAidlInterface != null) {
                printerAidlInterface.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印自检页
     */
    public void printTestPage() {
        try {
            if (printerAidlInterface != null) {
                printerAidlInterface.printTestPage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印并进纸n毫米
     *
     * @param n [0, 255]
     */
    public void printBlank(int n) {
        try {
            if (printerAidlInterface != null) {
                printerAidlInterface.printBlank(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPrinterConnected()
    {
        try {
            if(printerAidlInterface != null)
            {
                return printerAidlInterface.getPrinterConnectionState() == 1;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public boolean hasPaper()
    {
        try {
            if(printerAidlInterface != null)
            {
                return printerAidlInterface.hasPaper();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

}
