package cn.com.geartech.gcordsdk;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by pangyuning on 17/10/26.
 */

public class SystemContactUtil {

    static Context context;

    public static void setContext(Context c)
    {
        context = c;
    }



    public static void insertContact(String number, String name, String company, String deparmentName,String position)
    {
        try {
            ContentValues values = new ContentValues();
            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = context.getContentResolver().insert(
                    ContactsContract.RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);

            // 向data表插入姓名数据
            if (name != null && name.length() > 0) {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, name);
                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }


            // 插入公司数据
            ContentValues companyContentValues = new ContentValues();

            if(company != null)
            {
                companyContentValues.put(ContactsContract.CommonDataKinds.Organization.COMPANY, company);
            }
            else
            {
                companyContentValues.put(ContactsContract.CommonDataKinds.Organization.COMPANY, "");
            }

            if(deparmentName != null)
            {
                companyContentValues.put(ContactsContract.CommonDataKinds.Organization.DEPARTMENT, deparmentName);
            }
            else
            {
                companyContentValues.put(ContactsContract.CommonDataKinds.Organization.DEPARTMENT, "");

            }

            if(position != null)
            {
                companyContentValues.put(ContactsContract.CommonDataKinds.Organization.TITLE, position);
            }
            else
            {
                companyContentValues.put(ContactsContract.CommonDataKinds.Organization.TITLE, "");
            }

            companyContentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            companyContentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, companyContentValues);



            // 向data表插入电话数据

            String a = number;
            if (a != null && a.length() > 0) {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, a);
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static boolean numberExists(String number)
    {
        try {
            String where = ContactsContract.RawContacts.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + " = ? ";
            String[] args = new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, number};
            Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, args , null);
            boolean exist = false;
            if(cursor.moveToFirst())
            {
                exist = true;
            }
            if(cursor != null)
            {
                cursor.close();
            }
            return exist;
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
