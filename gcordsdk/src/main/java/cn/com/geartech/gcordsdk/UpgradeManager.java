package cn.com.geartech.gcordsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.geartech.gcordsdk.dataType.PackageUpgradeItem;
import cn.com.geartech.gtplatform.model.aidl.ICommonCallback;
import cn.com.geartech.gtplatform.model.aidl.IGTAidlInterface;

/**
 * Created by dinner on 15/5/11.
 */
public class UpgradeManager {

    public static final int UPGRADE_OPTION_MANUAL_DOWNLOAD = 1 << 2;
    public static final int UPGRADE_OPTION_INSTALL_CONFIRM = 1 << 3;
    final static String CTRL_REG_UPGRADE = "cn.com.geartech.gtplatform.reg_upgrade";
    final static String PARAM_DELAY_INSTALL = "cn.com.geartech.gtplatform.param_delay_install";
    final static String PARAM_SHOW_WAITING_PAGE = "cn.com.geartech.gtplatform.show_waiting_page";
    final static String PARAM_CHANNEL = "cn.com.geartech.gtplatform.channel";
    final static String PARAM_PACKAGE_NAME = "cn.com.geartech.gtplatform.packageName";
    final static String CTRL_CHECK_UPGRADE_NOW = "cn.com.geartech.gtplatform.check_upgrade_now";
    final static String CTRL_DOWNLOAD_PACKAGE = "cn.com.geartech.gtplatform.download_checked_package";
    final static String CTRL_INSTALL_DOWNLOADED_PACKAGE = "cn.com.geartech.gtplatform.install_downloaded_package";
    final static String EVENT_CHECK_VERSION_COMPLETE = "cn.com.geartech.gtplatform.check_version_complete";
    final static String PARAM_CHECK_UPGRADE_ERROR_MESSAGE = "cn.com.geartech.gtplatform.check_upgrade_error_message";
    final static String PARAM_HAS_NEW_VERSION = "cn.com.geartech.gtplatform.has_new_version";
    final static String PARAM_VERSION_NAME = "cn.com.geartech.gtplatform.versionName";
    final static String EVENT_CHECK_UPGRADE_DOWNLOAD_PROGRESS_CHANGE = "cn.com.geartech.gtplatform.event_check_upgrade_download_progress_change";
    final static String PARAM_PROGRESS = "cn.com.geartech.gtplatform.progress";
    final static String EVENT_DOWNLOAD_PACKAGE_COMPLETE = "cn.com.geartech.gtplatform.on_download_complete";
    final static String PARAM_DOWNLOAD_SUCCESS = "cn.com.geartech.gtplatform.download_success";
    final static String PARAM_ERROR_MESSAGE = "cn.com.geartech.gtplatform.error_message";
    final static String EVENT_MDM_RESTARTED = "cn.com.geartech.mdm_restarted";
    final static String CTRL_REG_MDM_UPGRADE = "cn.com.geartech.reg_mdm_upgrade";
    static final String SYS_PREF_GT_DOWNLOAD_OPTION = "cn.com.geartech.gtplatform.sts_setting_download_option";
    static UpgradeManager instance = null;
    Context context;
    UpgradeProgressCallback upgradeProgressCallback;
    boolean enableAutoUpgrade = false;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(EVENT_CHECK_VERSION_COMPLETE)) {
                String errorMessage = intent.getStringExtra(PARAM_CHECK_UPGRADE_ERROR_MESSAGE);
                String versionName = intent.getStringExtra(PARAM_VERSION_NAME);
                boolean hasNewVersion = intent.getBooleanExtra(PARAM_HAS_NEW_VERSION, false);

                if (errorMessage != null && errorMessage.length() > 0) {
                    if (upgradeProgressCallback != null) {
                        upgradeProgressCallback.onCheckUpgradeFailed(errorMessage);
                    }
                } else {
                    if (hasNewVersion) {
                        if (upgradeProgressCallback != null) {
                            upgradeProgressCallback.onCheckUpgradeResult(true, versionName);
                        }
                    } else {
                        if (upgradeProgressCallback != null) {
                            upgradeProgressCallback.onCheckUpgradeResult(false, null);
                        }
                    }
                }
            } else if (intent.getAction().equals(EVENT_CHECK_UPGRADE_DOWNLOAD_PROGRESS_CHANGE)) {
                int progress = intent.getIntExtra(PARAM_PROGRESS, 0);
                if (upgradeProgressCallback != null) {
                    upgradeProgressCallback.onDownloadProgressChange(progress);
                }
            } else if (intent.getAction().equals(EVENT_MDM_RESTARTED)) {
                if (enableAutoUpgrade) {
                    registerUpgrade();
                }
            }
//            else if(intent.getAction().equals(EVENT_DOWNLOAD_PACKAGE_COMPLETE))
//            {
//                boolean downloadSuccess = intent.getBooleanExtra(PARAM_DOWNLOAD_SUCCESS, false);
//                if(downloadSuccess)
//                {
//                    if(upgradeProgressCallback!=null)
//                    {
//                        upgradeProgressCallback.onDownloadComplete();
//                    }
//                }
//                else
//                {
//                    String errorMessage = intent.getStringExtra(PARAM_ERROR_MESSAGE);
//                    if(upgradeProgressCallback != null)
//                    {
//                        upgradeProgressCallback.onDownloadFailed(errorMessage);
//                    }
//                }
//            }
        }
    };

    private UpgradeManager() {

    }

    protected static UpgradeManager getInstance() {
        if (instance == null) {
            instance = new UpgradeManager();
        }
        return instance;
    }

    static protected int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.parseInt(oldNumbers[i]);
            int newVersionPart = Integer.parseInt(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = 1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = -1;
                break;
            }
        }

        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? -1 : 1;
        }

        return res;
    }

    protected static String calBigFileSHA1(String filePath) {

        File file = new File(filePath);
        if (!file.exists())
            return null;

        MessageDigest messagedigest;

        FileInputStream in = null;

        byte[] hash = null;

        try {

            in = new FileInputStream(file);
            messagedigest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024 * 1024];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, len);
            }

            in.close();

            hash = messagedigest.digest();
        } catch (Exception ignored) {

        }

        if (hash != null) {
            StringBuilder buf = new StringBuilder();
            for (byte b : hash) {
                int halfbyte = (b >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                    halfbyte = b & 0x0F;
                } while (two_halfs++ < 1);
            }
            return buf.toString();
        } else {
            return null;
        }
    }


    protected static String calBigFileSHA1(InputStream inputStream) {
        MessageDigest messagedigest;
        byte[] hash = null;

        try {
            messagedigest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024 * 1024 * 1];
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, len);
            }

            hash = messagedigest.digest();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (hash != null) {
            StringBuilder buf = new StringBuilder();
            for (byte b : hash) {
                int halfbyte = (b >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                    halfbyte = b & 0x0F;
                } while (two_halfs++ < 1);
            }
            return buf.toString();
        } else {
            return null;
        }
    }

    public void setUpgradeProgressCallback(UpgradeProgressCallback upgradeProgressCallback) {
        this.upgradeProgressCallback = upgradeProgressCallback;
    }

    protected void init(Context c) {
        context = c;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EVENT_CHECK_VERSION_COMPLETE);
        intentFilter.addAction(EVENT_CHECK_UPGRADE_DOWNLOAD_PROGRESS_CHANGE);
        intentFilter.addAction(EVENT_MDM_RESTARTED);
        context.registerReceiver(mReceiver, intentFilter);

        registerUpgrade();
    }

    public void checkUpgradeNow() {
        Intent intent = new Intent(CTRL_CHECK_UPGRADE_NOW);
        intent.putExtra(PARAM_PACKAGE_NAME, context.getPackageName());
        //intent.putExtra(PARAM_PACKAGE_NAME,"com.smartque");
        context.sendBroadcast(intent);
    }

    public void installPackage() {
        Intent intent = new Intent(CTRL_INSTALL_DOWNLOADED_PACKAGE);
        intent.putExtra(PARAM_PACKAGE_NAME, context.getPackageName());
        context.sendBroadcast(intent);
    }

    public void setUpgradeOption(int options) {
        String prefName = SYS_PREF_GT_DOWNLOAD_OPTION + context.getPackageName();
        Settings.System.putInt(context.getContentResolver(), prefName, options);
    }

    public void installDownloadedPackage(InstallCallback callback) {
        try {
            IGTAidlInterface igtAidlInterface = GTAidlHandler.getIgtAidlInterface();
            if (igtAidlInterface != null) {
                GTAidlHandler.getInstance().setInstallCallback(callback);
                igtAidlInterface.installDownloadedPackage(context.getPackageName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void registerUpgrade() {
        Intent intent = new Intent(CTRL_REG_MDM_UPGRADE);
        intent.putExtra(PARAM_PACKAGE_NAME, context.getPackageName());
        intent.putExtra(PSTNInternal.PARAM_DEV_ID, PSTNInternal.getInstance().getDevId());
        intent.putExtra(PSTNInternal.PARAM_DEV_TOKEN, PSTNInternal.getInstance().getDevToken());

        context.sendBroadcast(intent);

        enableAutoUpgrade = true;
    }

    public long getLastUpdateDate(String packageName) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/GTUART/";

        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // loop for 10 days
        for (int i = 0; i < 10; i++) {
            String s = sdf.format(date);
            String fileName = path + s + "e.log";
            long lastUpdateDate = readFromFile(fileName, packageName);
            if (lastUpdateDate > 0) {
                return lastUpdateDate;
            }

            date.setTime(date.getTime() - 24L * 3600 * 1000);
        }

        return -1;
    }

    private long readFromFile(String filePath, String packageName) {
        try {

            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            String ts = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splited = line.split(",");
                if (splited.length >= 3) {
                    if (splited[1].equals("700005") && splited[2].equals(packageName)) {
                        ts = splited[0];
                    }
                }
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            if (ts != null) {
                try {
                    return Long.parseLong(ts);
                } catch (Exception ignored) {

                }
            }

        } catch (Exception ignored) {

        }

        return -1;
    }

    public UpDateErrorItem readLastUpdateError(String packageName) {
        //UpDateErrorItem item = new UpDateErrorItem();

        String path = Environment.getExternalStorageDirectory().getPath() + "/GTUART/";

        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // loop for 10 days
        for (int i = 0; i < 10; i++) {
            String s = sdf.format(date);
            String fileName = path + s + "e.log";

            UpDateErrorItem errorItem = readUpdateError(path, packageName);
            if (errorItem != null) {
                return errorItem;
            }

            date.setTime(date.getTime() - 24L * 3600 * 1000);
        }

        return null;
    }

    UpDateErrorItem readUpdateError(String filePath, String packageName) {
        UpDateErrorItem item = new UpDateErrorItem();

        try {

            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = null;

            String ts = null;
            String errorMsg = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splited = line.split(",");
                if (splited != null && splited.length >= 5) {
                    if (splited[1].equals("700006") && splited[2].equals(packageName)) {
                        ts = splited[0];
                        errorMsg = splited[3] + ":" + splited[4];
                    }
                }
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            if (ts != null) {
                try {
                    UpDateErrorItem errorItem = new UpDateErrorItem();
                    errorItem.ts = Long.parseLong(ts);
                    errorItem.errorMessage = errorMsg;

                    return errorItem;
                } catch (Exception e) {

                }
            }

        } catch (Exception e) {

        }


        return null;
    }

    /**
     * 检查软件更新
     * @param callback 检查结果回调
     */
    public void checkSystemAllInOneUpdate(final CheckAllInOneUpdateCallback callback) {
        String packageName = context.getPackageName();

        try {
            GTAidlHandler.getIgtAidlInterface().checkAllInOneUpgrade(packageName, new ICommonCallback.Stub() {
                @Override
                public void onComplete(boolean ret, int hasResult, int num2, String s, String s1) throws RemoteException {
                    if (!ret) {
                        // error
                        if (callback != null) {
                            callback.onCheckFailed(s);
                        }
                    } else if (hasResult == 0) {
                        // up to date
                        if (callback != null) {
                            callback.onUpToDate();
                        }
                    } else if (hasResult == 1) {
                        // has result
                        if (callback != null) {
                            ArrayList<PackageUpgradeItem> items = new ArrayList<>();
                            try {
                                JSONTokener jsonTokener = new JSONTokener(s);
                                JSONObject dataObject = (JSONObject) jsonTokener.nextValue();
                                JSONArray packages = dataObject.getJSONArray("packages");
                                for (int i = 0; i < packages.length(); i++) {
                                    JSONObject packageItem = packages.getJSONObject(i);
                                    PackageUpgradeItem packageUpgradeItem = new PackageUpgradeItem();
                                    packageUpgradeItem.setPackageName(packageItem.getString("packageName"));
                                    packageUpgradeItem.setVersionName(packageItem.getString("version"));

                                    if (packageItem.has("info")) {
                                        String info = packageItem.getString("info");
                                        if (info != null) {
                                            packageUpgradeItem.setInfo(info);
                                        }

                                    }
                                    if (packageItem.has("changes")) {
                                        String changes = packageItem.getString("changes");
                                        if (changes != null) {
                                            packageUpgradeItem.setChanges(changes);
                                        }
                                    }

                                    items.add(packageUpgradeItem);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            callback.onCheckResult(items, s1);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkSystemAppUpdate(final CheckAllInOneUpdateCallback callback) {
        String packageName = context.getPackageName();

        try {
            GTAidlHandler.getIgtAidlInterface().checkSystemPackageUpgrade(packageName, new ICommonCallback.Stub() {
                @Override
                public void onComplete(boolean ret, int hasResult, int num2, String s, String s1) throws RemoteException {
                    if (!ret) {
                        // error
                        if (callback != null) {
                            callback.onCheckFailed(s);
                        }
                    } else if (hasResult == 0) {
                        // up to date
                        if (callback != null) {
                            callback.onUpToDate();
                        }
                    } else if (hasResult == 1) {
                        // has result
                        if (callback != null) {
                            ArrayList<PackageUpgradeItem> items = new ArrayList<PackageUpgradeItem>();
                            try {
                                Log.d("UpgradeTest", "str:" + s);
                                JSONTokener jsonTokener = new JSONTokener(s);
                                JSONObject dataObject = (JSONObject) jsonTokener.nextValue();
                                JSONArray packages = dataObject.getJSONArray("packages");
                                for (int i = 0; i < packages.length(); i++) {
                                    JSONObject packageItem = packages.getJSONObject(i);
                                    PackageUpgradeItem packageUpgradeItem = new PackageUpgradeItem();
                                    packageUpgradeItem.setPackageName(packageItem.getString("packageName"));
                                    packageUpgradeItem.setVersionName(packageItem.getString("version"));

                                    if (packageItem.has("info")) {
                                        String info = packageItem.getString("info");
                                        if (info != null) {
                                            packageUpgradeItem.setInfo(info);
                                        }
                                    }

                                    items.add(packageUpgradeItem);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            callback.onCheckResult(items, s1);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载并安装更新
     * @param token 更新token，检查更新的结果中返回
     * @param callback 安装回调
     */
    public void downloadAndInstall(String token, final DownloadAndInstallCallback callback) {
        try {
            GTAidlHandler.getIgtAidlInterface().downloadAllInOnePackages(token, new ICommonCallback.Stub() {
                @Override
                public void onComplete(boolean success, int num, int num2, String s, String s1) throws RemoteException {
                    if (num == 1) {
                        // download result
                        if (success) {
                            // notify success
                            if (callback != null) {
                                callback.onResult(true, "");
                            }
                        } else {
                            String errorMessage = s;
                            if (callback != null) {
                                callback.onResult(false, errorMessage);
                            }
                        }
                    } else if (num == 2) {
                        // progress change
                        int index = 0;
                        int maxIndex = 0;
                        try {
                            index = Integer.parseInt(s);
                            maxIndex = Integer.parseInt(s1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (callback != null) {
                            callback.onProgressChange(num2, index, maxIndex);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface UpgradeProgressCallback {

        void onCheckUpgradeResult(boolean hasNewVersion, String newVersionName);

        void onCheckUpgradeFailed(String errorMessage);

        void onDownloadProgressChange(int progress);

    }

    public interface InstallCallback {

        void onInstallComplete();// should never be called since the app is killed during installation

        void onInstallFailed(String errorMessage);

    }

    /**
     * 检查更新回调
     */
    public interface CheckAllInOneUpdateCallback {
        /**
         * 软件更新信息
         * @param apps 有更新的软件列表
         * @param token 当前更新的token，用于后续下载安装软件更新
         */

        void onCheckResult(List<PackageUpgradeItem> apps, String token);

        /**
         * 检查更新发生异常
         * @param errorMessage 错误信息
         */
        void onCheckFailed(String errorMessage);

        /**
         * 所有软件已经更新到最新版本
         */
        void onUpToDate();
    }

    /**
     * 下载安装回调
     */
    public interface DownloadAndInstallCallback {
        /**
         * 安装更新结果回调
         * 1、如果更新列表中不包括开发者的软件或者cn.com.geartech.gtplatform，安装成功会调用该回调
         * 2、如果更新列表中包括开发者的软件或者cn.com.geartech.gtplatform，则改回调不会调用
         * @param success 是否安装完成
         * @param errorMessage 如果安装失败，返回的错误信息
         */
        void onResult(boolean success, String errorMessage);

        /**
         * 下载更新的回调
         * @param percent 下载进度
         * @param index 当前下载第几项
         * @param maxIndex 总的更新数目
         */
        void onProgressChange(int percent, int index, int maxIndex);
    }

    public static class UpDateErrorItem {
        long ts = -1;
        String errorMessage;

        public long getTimeStamp() {
            return ts;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

}
