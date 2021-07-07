package cn.com.geartech.gcordsdk;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.com.geartech.gcordsdk.dataType.CallLogItem;
import cn.com.geartech.gcordsdk.service.PhoneService;

/**
 * Created by pangyuning on 17/2/9.
 */

public class CallLogManager {

    public static final String ACTION_DELETE_ALL_CALL_LOG = "cn.com.geartech.ACTION.delete_all_call_log";
    public static final String EXTRA_DELETED_CALL_LOG_ID = "DELETED_CALL_LOG_ID";
    private static final String EVENT_CALL_LOG_ADDED = "cn.com.geartech.simple_call_log_added";
    private static final String EVENT_CALL_LOG_UPDATED = "cn.com.geartech.gt_call_log_updated";
    private static final String GT_DATA_KEY_CALL_LOG_ID = "gt_data_key_calllog_id";
    static CallLogManager _instance = null;
    Context context;
    CallLogUpdateCallback callLogUpdateCallback;
    Handler handler = new Handler();
    List<CallLogItem> callLogItems = new ArrayList<>();
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(EVENT_CALL_LOG_UPDATED)) {
                if (callLogUpdateCallback != null) {
                    Log.d("calllog", "updated");
                    Parcelable parcelable = intent.getParcelableExtra("content");
                    ContentValues lastData = (ContentValues) parcelable;
                    final CallLogItem data = CallLogItem.fromContentValues(lastData);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callLogUpdateCallback != null) {
                                callLogUpdateCallback.onNewCallLogAdded(data);
                            }
                        }
                    });
                    // get calllog again
                    PhoneService.getAllCallLogs(callback);
                }
            }
        }
    };
    private GetCallLogCallback callback = new GetCallLogCallback() {
        @Override
        public void onGetCallLogs(final List<CallLogItem> callLogItems) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    callLogUpdateCallback.onCallLogUpdated(callLogItems);
                }
            });
        }
    };
    protected CallLogManager() {
    }

    protected static CallLogManager getInstance() {
        if (_instance == null) {
            _instance = new CallLogManager();
        }

        return _instance;
    }

    protected void init(Context c) {
        context = c;

        IntentFilter intentFilter = new IntentFilter(EVENT_CALL_LOG_UPDATED);
        context.registerReceiver(mReceiver, intentFilter);
    }

    /**
     * 设置CallLog更新时候的回调
     *
     * @param callback CallLog更新时候的回调
     */
    public void setCallLogUpdateCallback(CallLogUpdateCallback callback) {
        callLogUpdateCallback = callback;
    }

    /**
     * 获取CallLog
     *
     * @param callback 成功获取CallLog后回调
     */
    @SuppressWarnings({"unused"})
    public void getCallLogs(final GetCallLogCallback callback) {
        if (this.callLogItems == null || this.callLogItems.size() == 0) {
            doGetCallLogs(callback);
        } else {
            if (callback != null) {
                callback.onGetCallLogs(callLogItems);
            }
        }
    }

    public void getCallLogsWithoutCache(final GetCallLogCallback callback) {
//        if (this.callLogItems == null || this.callLogItems.size() == 0) {
//            doGetCallLogs(callback);
//        } else {
//            if (callback != null) {
//                callback.onGetCallLogs(callLogItems);
//            }
//        }
        doGetCallLogs(callback);
    }

    public void deleteCallLog(final String callLogId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse("content://cn.com.geartech.calllogprovider/callHistory");
                ContentResolver contentResolver = context.getContentResolver();
                try {

                    //String[] projection = null;
                    if (callLogId != null && callLogId.trim().length() != 0) {
                        String selection = CallLogItem.COLUMN_CALLLOG_ID + " = ? ";
                        String[] selectionArgs = new String[]{callLogId};
                        int delCount = contentResolver.delete(uri, selection, selectionArgs);
                        // Log.d("delCount", String.valueOf(delCount));
                        if (!TextUtils.isEmpty(callLogId)) {
                            Intent intent = new Intent(ACTION_DELETE_ALL_CALL_LOG);
                            intent.putExtra(EXTRA_DELETED_CALL_LOG_ID, callLogId);
                            context.sendBroadcast(intent);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressWarnings("unused")
    public void deleteCallLog(final CallLogItem callLogItem) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String callLogId = callLogItem.getCallLog_id();
                Uri uri = Uri.parse("content://cn.com.geartech.calllogprovider/callHistory");
                ContentResolver contentResolver = context.getContentResolver();
                try {

                    //String[] projection = null;
                    if (callLogId != null && callLogId.trim().length() != 0) {
                        deleteCallLog(callLogId);
                    } else {
                        String selection = CallLogItem.COLUMN_CALLLOG_ID + " IS NULL AND call_type=? " +
                                "AND call_begin=? ";
                        String[] selectionArgs = new String[]{callLogItem.getCallType(), String.valueOf(callLogItem.getCallBeginTimeStamp())};
                        String phoneNumber = callLogItem.getOpponentNumber();
                        if (phoneNumber != null) {
                            selection += (" AND opponent_number='" + phoneNumber + "'");
                        }
                        String callResult = callLogItem.getCallResult();
                        if (callResult != null) {
                            selection += (" AND call_result='" + callResult + "'");
                        }
                        contentResolver.delete(uri, selection, selectionArgs);
                        // Log.d("delCount", String.valueOf(delCount));
                        if (!TextUtils.isEmpty(callLogId)) {
                            Intent intent = new Intent(ACTION_DELETE_ALL_CALL_LOG);
                            intent.putExtra(EXTRA_DELETED_CALL_LOG_ID, callLogId);
                            context.sendBroadcast(intent);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressWarnings("unused")
    public void deleteAllCallLogs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse("content://cn.com.geartech.calllogprovider/callHistory");
                ContentResolver contentResolver = context.getContentResolver();
                try {

                    //String[] projection = null;
                    //String selection = CallLogItem.COLUMN_CALLLOG_ID + " = ? ";
                    //String[] selectionArgs = new String[]{callLogId};
                    int delCount = contentResolver.delete(uri, null, null);
                    // Log.d("delCount", String.valueOf(delCount));
                    Intent intent = new Intent(ACTION_DELETE_ALL_CALL_LOG);
                    context.sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String getCurrentCallLogId() {
        try {
            return GTAidlHandler.getIgtAidlInterface().queryData(GT_DATA_KEY_CALL_LOG_ID, "", 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param fileName 录音文件名
     * @return 录音文件路径
     */
    @SuppressWarnings("unused")
    public String getRecordFilePath(String fileName) {
        if (fileName == null || fileName.trim().length() == 0) return "";
        String filePath = "";
        try {
            String folder = fileName.length() > 6 ? fileName.substring(0, 6) : "";
            filePath = Environment.getExternalStorageDirectory() + "/GcordAudioRecord/"
                    + context.getPackageName() + "/" + folder + "/" + fileName;
            File file = new File(filePath);
            if (file.exists()) return filePath;

            folder = fileName.length() > 8 ? fileName.substring(0, 8) : "";
            filePath = Environment.getExternalStorageDirectory() + "/GTSysData/CallRecord/" + folder + "/" + fileName;
            file = new File(filePath);
            if (file.exists()) return filePath;

            filePath = Environment.getExternalStorageDirectory().getPath();
            if (filePath != null) {
                if (!filePath.endsWith("/") && !(filePath.endsWith("\\"))) {
                    filePath += "/";
                }

                filePath = filePath + "GTPhoneCallRecord/"+fileName;
                file = new File(filePath);
                if (file.exists()) return filePath;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void doGetCallLogs(final GetCallLogCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                ArrayList<CallLogItem> callLogItems = new ArrayList<>();

                ContentResolver contentResolver = context.getContentResolver();


                Uri uri = Uri.parse("content://cn.com.geartech.calllogprovider/callHistory");

                try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {

                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            //Log.e("calllog" , "parse calllog db");
                            try {
                                ContentValues contentValues = new ContentValues();

                                DatabaseUtils.cursorRowToContentValues(cursor, contentValues);

                                CallLogItem item = CallLogItem.fromContentValues(contentValues);

                                callLogItems.add(item);


                                // Log.e("GcordSDKCallLog", "num:" + item.getOpponentNumber());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    if (callback != null) {
                        callback.onGetCallLogs(callLogItems);
                    }
                    CallLogManager.this.callLogItems.clear();
                    CallLogManager.this.callLogItems.addAll(callLogItems);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        if (Looper.getMainLooper() == Looper.myLooper()) {
            new Thread(runnable).start();
        } else {
            runnable.run();
        }

    }

    /**
     * 获取CallLog的回调
     */
    public interface GetCallLogCallback {

        /**
         * @param callLogItems calllog列表
         */
        void onGetCallLogs(List<CallLogItem> callLogItems);
    }

    /**
     * CallLog更新的回调
     */
    public interface CallLogUpdateCallback {
        /**
         * 返回整个更新后的calllog列表
         *
         * @param callLogItems
         */
        abstract void onCallLogUpdated(List<CallLogItem> callLogItems);

        /**
         * 新生成了一条calllog
         *
         * @param callLogItem
         */
        void onNewCallLogAdded(CallLogItem callLogItem);
    }
}
