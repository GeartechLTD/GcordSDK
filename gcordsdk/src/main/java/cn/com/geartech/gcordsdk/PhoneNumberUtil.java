package cn.com.geartech.gcordsdk;

import android.os.RemoteException;
import android.text.TextUtils;

import org.json.JSONObject;
import org.json.JSONTokener;

import cn.com.geartech.gcordsdk.areacode.AreaCodeItem;
import cn.com.geartech.gtplatform.model.aidl.ICommonCallback;


/**
 * Created by pangyuning on 18/3/1.
 */

public class PhoneNumberUtil {

    public static boolean isCellPhoneNumber(String number)
    {
        if(TextUtils.isEmpty(number))
        {
            return false;
        }

        if(number.length() == 11 && number.startsWith("1"))
        {
            return true;
        }

        return false;
    }

    public interface addPrefixCallback{
        void onPrefixAdded(String result);
    }

    public static void addPrefixForPSTNNumberBySystemSetting(final String orgNumber, final addPrefixCallback callback)
    {
        if (callback == null)
        {
            return;
        }

        if(TextUtils.isEmpty(orgNumber))
        {
            callback.onPrefixAdded("");
        }

        final GcordPreference gcordPreference = GcordSDK.getInstance().getGcordPreference();
        if(gcordPreference.isSmartDialOn())
        {
            /**
             * 只有智能拨号打开的时候才需要加前缀
             */
            final String dialPrefix = gcordPreference.getDialPrefix();
            final String currentAreaCode = gcordPreference.getCurrentAreaCode();


            if(isCellPhoneNumber(orgNumber) && !TextUtils.isEmpty(currentAreaCode))
            {
                /**
                 * 本机设置的区号
                 */
                    GcordSDK.getInstance().getAreaCodeManager().getAreaCode(orgNumber, new AreaCodeManager.GetAreaCodeCallback() {
                        @Override
                        public void onGetAreaCode(AreaCodeItem areaCodeItem) {

                            String result = orgNumber;

                            if(areaCodeItem != null && !areaCodeItem.getAreaCode().equals(currentAreaCode))
                            {
                                //异地手机号加0
                                result = "0" + orgNumber;
                            }

                            if(!TextUtils.isEmpty(dialPrefix))
                            {
                                result = dialPrefix + result;
                            }

                            callback.onPrefixAdded(result);
                        }
                    });
            }
            else
            {
                String result = orgNumber;
                if(!TextUtils.isEmpty(gcordPreference.getDialPrefix()))
                {
                    if(orgNumber.length() > 5)
                    {
                        //5位数以下默认是内线号码
                        result = gcordPreference.getDialPrefix() + orgNumber;
                    }
                }

                callback.onPrefixAdded(result);
            }
        }
        else
        {
            callback.onPrefixAdded(orgNumber);
        }
    }

    /*
    check number info with online api...

     */
    public static void checkNumberInfo(String num, final AreaCodeManager.GetAreaCodeCallback callback){

        GTAidlHandler.getInstance().checkNumber(num, new ICommonCallback.Stub() {
            @Override
            public void onComplete(boolean ret, int num, int num2, String s, String s1){

                try {
                    if (!ret || TextUtils.isEmpty(s)) {
                        if (callback != null)
                            callback.onGetAreaCode(null);
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
                        }

                        item.setMobileArea(area);

                        if (jsonObject.has("provider")) {
                            item.setProvider(jsonObject.getString("provider"));
                        }

                        if (jsonObject.has("report")) {
                            item.setMark(jsonObject.getString("report"));
                        }

                        if (callback != null)
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onGetAreaCode(item);
                                }
                            }).start();
                    }


                } catch (Throwable e) {
                    e.printStackTrace();
                    if (callback != null)
                        callback.onGetAreaCode(null);
                }
            }
        });

    }
}
