package cn.com.geartech.gcordsdk;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;

import org.json.JSONObject;
import org.json.JSONTokener;

import cn.com.geartech.gcordsdk.areacode.AreaCodeIndex;
import cn.com.geartech.gcordsdk.areacode.AreaCodeItem;
import cn.com.geartech.gtplatform.model.aidl.ICommonCallback;

/**
 * @author xujiahui
 * @since 18/04/2017
 */
public class AreaCodeManager {
    public static String COLUMN_MOBILE_NUMBER = "mobilenumber";
    public static String COLUMN_MOBILE_AREA = "mobilearea";
    public static String COLUMN_MOBILE_TYPE = "mobiletype";
    public static String COLUMN_AREA_CODE = "areacode";
    private static AreaCodeManager instance;
    Handler handler = new Handler(Looper.getMainLooper());
    private Context mContext;

    protected static AreaCodeManager getInstance() {
        if (instance == null) {
            instance = new AreaCodeManager();
        }
        return instance;
    }

    public void init(Context context) {
        AreaCodeIndex.setUp();
        mContext = context;
    }

    private boolean checkGTPVersion() {
        PackageManager packageManager = GcordSDK.getInstance().getContext().getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo("cn.com.geartech.gtplatform", 0);

                if (packageInfo != null) {
                    return getInstance().compareVersionNames("2.1.82", packageInfo.versionName) >= 0;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

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

    public void getAreaCode(String phoneNum, final boolean shouldChcekOnline, final GetAreaCodeCallback callback) {
        //Log.d("GcordSDK", "enter getAreaCode");
        if (phoneNum == null || phoneNum.trim().length() <= 0) {
            if (callback != null) callback.onGetAreaCode(null);
            return;
        }
        phoneNum = phoneNum.trim();
        if (phoneNum.startsWith("086") || phoneNum.startsWith("+86")) {
            if (phoneNum.length() > 3) {
                phoneNum = phoneNum.substring(3);
            } else {
                if (callback != null) callback.onGetAreaCode(null);
                return;
            }
        }

        boolean forceCellNumber = false;

        if (phoneNum.length() == 12 && phoneNum.startsWith("0")) {
            forceCellNumber = true;
        }

        String areaCode = AreaCodeIndex.getAreaCodeFromPhoneNumber(phoneNum);

        // Log.d("GcordSDK", "Area Code:" + areaCode);


        if (areaCode != null && areaCode.length() > 0) {

            final AreaCodeItem item = new AreaCodeItem();
            item.setAreaCode(areaCode);
            item.setMobileType("");
            String province = AreaCodeIndex.getProvince(areaCode);
            if (!TextUtils.isEmpty(province)) {
                item.setMobileArea(province + " " + AreaCodeIndex.getCity(areaCode));
            } else {
                item.setMobileArea(AreaCodeIndex.getCity(areaCode));
            }

            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onGetAreaCode(item);
                    }
                });
            }
        } else if (phoneNum.length() <= 7) {

            //Log.d("GcordSDK", "number too short");

            if (callback != null) {
                //callback.onGetAreaCode(null);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onGetAreaCode(null);
                    }
                });
            }
        } else {

            //  Log.d("GcordSDK", "phone number");
            String n = phoneNum;
            if (forceCellNumber) {
                n = phoneNum.substring(1);
            }

            final String num = n;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        try {
                            ContentResolver contentResolver = mContext.getContentResolver();
                            Uri uri = Uri.parse("content://cn.com.geartech.areacodeprovider/");
                            String prefix = num.substring(0, 7);
                            String[] columns = {COLUMN_AREA_CODE, COLUMN_MOBILE_AREA, COLUMN_MOBILE_TYPE};
                            String where = COLUMN_MOBILE_NUMBER + " = '" + prefix + "'";
                            Cursor cursor = contentResolver.query(uri, columns, where, null, null);
                            if (cursor != null) {
                                if (cursor.moveToNext()) {
                                    try {
                                        ContentValues contentValues = new ContentValues();
                                        DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
                                        final AreaCodeItem item = new AreaCodeItem();

                                        item.setAreaCode(contentValues.getAsString(COLUMN_AREA_CODE));
                                        item.setMobileArea(contentValues.getAsString(COLUMN_MOBILE_AREA));
                                        item.setMobileType(contentValues.getAsString(COLUMN_MOBILE_TYPE));

                                        if (callback != null) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    callback.onGetAreaCode(item);
                                                }
                                            });
                                        }
                                        try {
                                            cursor.close();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                        return;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    cursor.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            if (shouldChcekOnline && checkGTPVersion()) {
                                GTAidlHandler.getInstance().checkNumber(num, new ICommonCallback.Stub() {
                                    @Override
                                    public void onComplete(boolean ret, int num, int num2, String s, String s1) throws RemoteException {

                                        try {
                                            if (!ret || TextUtils.isEmpty(s)) {
                                                if (callback != null) {
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onGetAreaCode(null);
                                                        }
                                                    });
                                                }
                                            } else {
                                                final AreaCodeItem item = new AreaCodeItem();
                                                JSONTokener jsonTokener = new JSONTokener(s);
                                                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                                                String area = "";
                                                if (jsonObject.has("province")) {
                                                    area += jsonObject.getString("province");
                                                    area += " ";
                                                }

                                                if (jsonObject.has("city")) {
                                                    area += jsonObject.getString("city");
                                                    item.setAreaCode(AreaCodeIndex.getAreaCode(jsonObject.getString("city")));
                                                }

                                                item.setMobileArea(area);

                                                if (jsonObject.has("provider")) {
                                                    item.setProvider(jsonObject.getString("provider"));
                                                }

                                                if (jsonObject.has("report")) {
                                                    item.setMark(jsonObject.getString("report"));
                                                }

                                                if (callback != null) {
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onGetAreaCode(item);
                                                        }
                                                    });
                                                }
                                            }


                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                            if (callback != null) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        callback.onGetAreaCode(null);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });

                            } else {
                                if (callback != null) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onGetAreaCode(null);
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (callback != null) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onGetAreaCode(null);
                                    }
                                });
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                        if (callback != null) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onGetAreaCode(null);
                                }
                            });
                        }
                    }

                }
            }).start();
        }
    }

    public void getAreaCode(final String phoneNum, final GetAreaCodeCallback callback) {
        getAreaCode(phoneNum, true, callback);
    }

    public interface GetAreaCodeCallback {
        void onGetAreaCode(AreaCodeItem areaCodeItem);
    }
}
