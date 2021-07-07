package cn.com.geartech.gcordsdk.dao;

/**
 * Created by pangyuning on 17/5/17.
 */

public class GlobalSettingsTable {


    public static final String GLOBAL_SETTINGS_NAME = "global_settings_table";
    public static final String GLOBAL_SETTING_ID = "global_setting_id";
    public static final String GLOBAL_SETTING_KEY = "global_setting_key";
    public static final String GLOBAL_SETTINGS_INT_VAL = "intval";
    public static final String GLOBAL_SETTINGS_STRING_VAL = "stringval";
    public static final String GLOBAL_SETTINGS_BOOLEAN_VAL = "boolval";

    public static final String[][] GLOBAL_SETTING_FIELDS = {
            {GLOBAL_SETTING_ID, "INTEGER", "primary key autoincrement"},
            {GLOBAL_SETTING_KEY,"varchar(255)","NOT NULL"},
            {GLOBAL_SETTINGS_INT_VAL,"INTEGER", null },
            {GLOBAL_SETTINGS_STRING_VAL,"varchar(255)", null},
            {GLOBAL_SETTINGS_BOOLEAN_VAL,"bit", null},

    };

    public static String getCreateTableString(String tableName, String[][] fields){

        String string = new String();

        string += "CREATE TABLE IF NOT EXISTS ";
        string += tableName;
        string += "(";

        boolean isFirst = true;

        for(String[] param:fields)
        {
            if(isFirst)
            {
                isFirst = false;
            }
            else
            {
                string += ",";
            }

            string += param[0];
            string += " ";
            string += param[1];
            if(param[2]!= null && param[2].length() > 0)
            {
                string += " ";
                string += param[2];
            }

        }


        string += ")";

        return string;
    }

}
