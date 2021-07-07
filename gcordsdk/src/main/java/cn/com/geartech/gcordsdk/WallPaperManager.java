package cn.com.geartech.gcordsdk;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import cn.com.geartech.gcordsdk.ui.WallPaperActivity;

/**
 * Created by cuizhy on 18/1/10.
 */

public class WallPaperManager {
    private static final String IS_WALL_PAPER_ENABLED = "IS_WALL_PAPER_ENABLED";
    private static WallPaperManager instance = null;
    private Handler handler;
    private SharedPreferences pref;
    private boolean isWallPaperEnabled = false;
    private String sysPhotosFolder = null;
    private boolean isDebug = false;

    public static final String ACTION_LOCK_SCREEN = "cn.com.geartech.ACTION_LOCK_SCREEN";
    public static final String ACTION_UNLOCK_SCREEN = "cn.com.geartech.ACTION_UNLOCK_SCREEN";

    private WallPaperManager() {
        handler = new Handler(Looper.getMainLooper());
        pref = GcordSDK.getInstance().getContext().getSharedPreferences(PREF_WALL_PAPER_TIME_IN_GCORD_SDK, Context.MODE_PRIVATE);
        sysPhotosFolder = Environment.getExternalStorageDirectory().getPath() + File.separator + "GTDefaultPhotos2";
        this.showWallPaperDelay = pref.getLong(SHOW_WALL_PAPER_DELAY_IN_GCORD_SDK, 30 * 1000);
        this.switchInterval = pref.getLong(SWITCH_WALL_PAPER_INTERVAL_IN_GCORD_SDK, 30 * 1000);

        this.wallPaperImageFolder = pref.getString(WALL_PAPER_IMAGE_FOLDER, sysPhotosFolder);
        this.isWallPaperEnabled = pref.getBoolean(IS_WALL_PAPER_ENABLED, false);
    }

    protected static synchronized WallPaperManager getInstance() {
        if (instance == null) {
            instance = new WallPaperManager();
        }
        return instance;
    }

    private Context mContext;

    protected void init(Application application) {
        if (this.isWallPaperEnabled) {
            scheduleWallPaperDelay();
        }
        mContext = application;
        loadPhotosItems(getWallPaperImageFolder(), photosItems);
        loadPhotosItems(sysPhotosFolder, defaultPhotoItems);
    }

    private ArrayList<String> photosItems = new ArrayList<>();
    private ArrayList<String> defaultPhotoItems = new ArrayList<>();

    public ArrayList<String> getPhotosItems() {

        if(customPhotosItem != null && customPhotosItem.size() > 0){
            return customPhotosItem;
        }

        if (!photosItems.isEmpty()) {
            return photosItems;
        }
        return defaultPhotoItems;
    }
    private ArrayList<String> customPhotosItem = new ArrayList<>();

    public void setCustomPhotosItem(ArrayList<String> customPhotosItem) {
        this.customPhotosItem.clear();
        this.customPhotosItem.addAll(customPhotosItem);
    }

    private void loadPhotosItems(final String path, final ArrayList<String> result) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    result.clear();
                    String folder = path;
                    if (folder == null || folder.trim().length() <= 0) {
                        folder = getWallPaperImageFolder();
                    }
                    File file = new File(folder);
                    if (file.exists() && file.isDirectory()) {
                        File[] files = file.listFiles();
                        if (files != null) {
                            for (File item :
                                    files) {
                                try {
                                    if (item.isFile()) {
                                        String path = item.getPath();
                                        if ((path.trim().toLowerCase().endsWith("jpg") || path.trim().toLowerCase().endsWith("png"))) {
                                            result.add(path);
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 屏保功能是否开启
     *
     * @return true-已开启，false-未开启
     */
    public boolean isWallPaperEnabled() {
        return isWallPaperEnabled;
    }

    /**
     * 设置是否启用屏保功能
     *
     * @param wallPaperEnabled true-启用，false-禁用
     */
    public synchronized void setWallPaperEnabled(boolean wallPaperEnabled) {
        try {
            if (wallPaperEnabled) {
                if (!isWallPaperEnabled) {
                    GcordSDK.getInstance().getHandleManager().addHandleEventListener(handleEventListener);
                    mContext.registerReceiver(broadcastReceiver, new IntentFilter(ACTION_TOUCH_MONITOR));
                }
            } else {
                if (isWallPaperEnabled) {
                    GcordSDK.getInstance().getHandleManager().removeHandleEventListener(handleEventListener);
                    mContext.unregisterReceiver(broadcastReceiver);
                }
            }
        }catch (Throwable e){

        }

        isWallPaperEnabled = wallPaperEnabled;
        pref.edit().putBoolean(IS_WALL_PAPER_ENABLED, isWallPaperEnabled).apply();
        scheduleWallPaperDelay();
    }

    public final static String ACTION_TOUCH_MONITOR = "cn.com.geartech.touch.monitor";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_TOUCH_MONITOR.equals(intent.getAction())) {
                int hasTouch = intent.getIntExtra("hasTouch", 0);
                if (1 == hasTouch) {
                    onUserInteraction();
                }
            }
        }
    };

    private long showWallPaperDelay;
    private final int MAX_DELAY = 60 * 60 * 1000;

    private long switchInterval;
    private final int MAX_INTERVAL = 10 * 60 * 1000;

    /**
     * set wall paper showing interval
     *
     * @param delay 出现墙纸的间隔，以毫秒为单位
     */
    public synchronized void setShowWallPaperDelay(long delay) {
        this.showWallPaperDelay = delay;
        if (this.showWallPaperDelay > MAX_DELAY) {
            this.showWallPaperDelay = MAX_DELAY;
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(SHOW_WALL_PAPER_DELAY_IN_GCORD_SDK, showWallPaperDelay);
        editor.apply();
    }

    /**
     * 设置切换间隔 毫秒
     */
    public synchronized void setWallpaperSwitchInterval(long interval) {
        try {
            this.switchInterval = interval;
            if (this.switchInterval > MAX_INTERVAL) {
                this.switchInterval = MAX_INTERVAL;
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong(SWITCH_WALL_PAPER_INTERVAL_IN_GCORD_SDK, switchInterval);
            editor.apply();

        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public long getWallpaperSwitchInterval() {
        return switchInterval;
    }

    public static final String PREF_WALL_PAPER_TIME_IN_GCORD_SDK = "PREF_WALL_PAPER_TIME_IN_GCORD_SDK";
    private static final String SHOW_WALL_PAPER_DELAY_IN_GCORD_SDK = "SHOW_WALL_PAPER_DELAY_IN_GCORD_SDK";
    private static final String SWITCH_WALL_PAPER_INTERVAL_IN_GCORD_SDK = "SWITCH_WALL_PAPER_INTERVAL_IN_GCORD_SDK";

    private static final String WALL_PAPER_IMAGE_FOLDER = "WALL_PAPER_IMAGE_FOLDER";

    /**
     * 获取墙纸出现的间隔
     *
     * @return 以毫秒为单位
     */
    public long getShowWallPaperDelay() {
        return showWallPaperDelay;
    }

    private Class<? extends WallPaperActivity> customWallPaperActivity;
    public void setCustomWallPaperActivity(final Class<? extends WallPaperActivity> customWallPaperActivity) {
        this.customWallPaperActivity = customWallPaperActivity;
    }

    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            if (!GcordSDK.getInstance().getSettingAPI().isAppInForeground() || GcordSDK.getInstance().getPhoneAPI().isInCall()) {
                return;
            }
            if (WallPaperActivity.wallPaperActivity == null) {
                try {
                    long millis = System.currentTimeMillis();
                    if (currentMillis != 0) {
                        long delta = millis - currentMillis;
                        if (isDebug) {
                            Log.e("show wall paper", "last click on " + String.valueOf(delta) + " milliseconds before, and scheduled time: " + showWallPaperDelay);
                        }
                    }

                    Intent intent ;
                    if(customWallPaperActivity == null) {
                        intent = new Intent(GcordSDK.getInstance().getContext(), WallPaperActivity.class);
                    }else{
                        intent = new Intent(GcordSDK.getInstance().getContext(), customWallPaperActivity);
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    GcordSDK.getInstance().getContext().startActivity(intent);
                    handler.removeCallbacks(delayRunnable);

                    intent = new Intent(ACTION_LOCK_SCREEN);
                    intent.setPackage(mContext.getPackageName());
                    mContext.sendBroadcast(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    public synchronized void startWallPaper() {
        if (isWallPaperEnabled) {
            handler.removeCallbacks(delayRunnable);
            handler.post(delayRunnable);
        }
    }

    public synchronized void hideWallpaper() {
        if (mContext != null) {
            Intent intent = new Intent(WallPaperActivity.HIDE_WALL_PAPER_ACTION);
            intent.setPackage(mContext.getPackageName());
            mContext.sendBroadcast(intent);
        }
    }

    private void scheduleWallPaperDelay() {
        synchronized (delayRunnable) {
            try {
                handler.removeCallbacks(delayRunnable);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (isWallPaperEnabled) {
                handler.postDelayed(delayRunnable, this.showWallPaperDelay);
            }
        }
    }

    private long currentMillis = 0;

    /**
     * 用户与app有交互时调用，延迟墙纸出现的时间
     */
    public void onUserInteraction() {
        long millis = System.currentTimeMillis();
        if (currentMillis != 0) {
            long delta = millis - currentMillis;
            if (isDebug) {
                Log.e("onUserInteraction", "last click：" + String.valueOf(delta) + " milliseconds before, and scheduled time: " + showWallPaperDelay);
            }
        }
        currentMillis = millis;
        try {
            if (WallPaperActivity.wallPaperActivity != null) {
                WallPaperActivity.wallPaperActivity.hideWallPaper();
            }
            scheduleWallPaperDelay();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    HomeKeyManager.HomeKeyListener homeKeyListener = new HomeKeyManager.HomeKeyListener() {
        @Override
        public void onHomeClicked() {
            onUserInteraction();
        }

        @Override
        public void onResumedByHomeKey() {
            onUserInteraction();
        }
    };

    private HandleManager.HandleEventListener handleEventListener = new HandleManager.HandleEventListener() {
        @Override
        public void onHandlePickedUp() {
            onUserInteraction();
        }

        @Override
        public void onHandlePutDown() {
            onUserInteraction();
        }
    };

    private String wallPaperImageFolder = null;

    /**
     * 设置屏保图片存储的位置
     *
     * @param folder 屏保图片存储的位置
     */
    public synchronized void setWallPaperImageFolder(String folder) {
        File file = new File(folder);
        if (file.exists() && file.isDirectory()) {
            wallPaperImageFolder = folder;
            pref.edit().putString(WALL_PAPER_IMAGE_FOLDER, folder).apply();
            loadPhotosItems(getWallPaperImageFolder(), photosItems);
        }
    }

    /**
     * 获取屏保图片存储的位置
     *
     * @return 屏保图片存储的位置
     */
    public String getWallPaperImageFolder() {
        if (wallPaperImageFolder == null) {
            wallPaperImageFolder = pref.getString(WALL_PAPER_IMAGE_FOLDER, sysPhotosFolder);
        }
        return wallPaperImageFolder;
    }


    public void setSystemWallpaperFolder(String folder, int version)
    {

        Intent intent = new Intent("cn.com.geartech.update_oem_folder");
        intent.putExtra("path", folder);
        intent.putExtra("version", version);
        intent.setPackage("cn.com.geartech.app");
        mContext.sendBroadcast(intent);
    }

}
