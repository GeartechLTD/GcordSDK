package cn.com.geartech.gcordsdk.dataType;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;

import cn.com.geartech.gcordsdk.CallLogManager;
import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gtplatform.model.calllog.SimpleCallLogItem;

/**
 * Created by pangyuning on 17/2/9.
 */

public class CallLogItem {


    //public static final String TABLE_NAME = "calllog";
    public static final Uri CALL_LOG_URI = Uri.parse("content://cn.com.geartech.calllogprovider/callHistory");
    public static final String CALL_CATEGORY_SIP = "sip";
    public static final String CALL_CATEGORY_CELL = "cell";
    public static final String CALL_CATEGORY_PSTN = "pstn";
    public static final String CALL_TYPE_IN = "call_in";
    public static final String CALL_TYPE_OUT = "call_out";
    @SuppressWarnings("unused")
    public static final String CALL_RESULT_ANSWERED = "answered";
    @SuppressWarnings("unused")
    public static final String CALL_RESULT_MISSED = "missed";
    public static final int FLAG_HAS_ANSWER_CALL = 1;
    public static final String COLUMN_CALLLOG_ID = "calllog_id"; // unique calllog id
    //backup fields
    public static final String COLUMN_CALL_LOG_PRIMARY_ID = "ext_string_1";
    public static final String COLUMN_CALL_TYPE = "call_type";
    protected static final String COLUMN_CALL_RESULT = "call_result";
    protected static final String COLUMN_CALL_DURATION = "call_duration"; // long
    public static final String COLUMN_CALL_BEGIN = "call_begin"; //long
    public static final String COLUMN_OPPONENT_NUMBER = "opponent_number";
    @SuppressWarnings("unused")
    protected static final String COLUMN_GCORD_TOKEN = "gcord_token"; // custom token, could be null
    protected static final String COLUMN_AUDIO_RECORD_ID = "audio_record_id";
    public static final String COLUMN_CALL_CATEGORY = "call_category";
    protected static final String COLUMN_DISPLAY_NAME = "display_name";
    @SuppressWarnings("unused")
    protected static final String EXT_STRING_2 = "ext_string_2";
    @SuppressWarnings("unused")
    protected static final String EXT_STRING_3 = "ext_string_3";
    @SuppressWarnings("unused")
    protected static final String EXT_STRING_4 = "ext_string_4";
    protected static final String COLUMN_CALL_LOG_FLAG = "ext_int_1";
    @SuppressWarnings("unused")
    protected static final String EXT_INT_2 = "ext_int_2";
    @SuppressWarnings("unused")
    protected static final String EXT_INT_3 = "ext_int_3";
    @SuppressWarnings("unused")
    protected static final String EXT_INT_4 = "ext_int_4";
    /*
    call in or call out
     */
    private String callType = "";

    private String callTime = "";
    private long callBeginTimeStamp;

    private long pickUpTimeStamp;

    private long callDuration;

    private String callLog_id;

    private String callRecord_id;


    /*
    answered or missed
     */
    private String callResult = "";


    private String opponentNumber = "";


    private String contactName = "";
    private String callCategory;
    private int flags = 0;
    private String primaryUUID = "";

    public static CallLogItem fromContentValues(ContentValues contentValues) {
        CallLogItem item = new CallLogItem();

        item.callLog_id = contentValues.getAsString(COLUMN_CALLLOG_ID);
        item.callRecord_id = contentValues.getAsString(COLUMN_AUDIO_RECORD_ID);
        item.opponentNumber = contentValues.getAsString(COLUMN_OPPONENT_NUMBER);
        item.callResult = contentValues.getAsString(COLUMN_CALL_RESULT);
        item.callType = contentValues.getAsString(COLUMN_CALL_TYPE);
        item.callCategory = contentValues.getAsString(COLUMN_CALL_CATEGORY);
        item.contactName = contentValues.getAsString(COLUMN_DISPLAY_NAME);

        try {
            if (contentValues.containsKey(COLUMN_CALL_LOG_PRIMARY_ID)) {
                item.primaryUUID = contentValues.getAsString(COLUMN_CALL_LOG_PRIMARY_ID);
            }

            if (contentValues.containsKey(COLUMN_CALL_LOG_FLAG)) {
                item.flags = contentValues.getAsInteger(COLUMN_CALL_LOG_FLAG);
            }
        } catch (Exception ignored) {

        }

        Long call_begin = contentValues.getAsLong(COLUMN_CALL_BEGIN);
        if (call_begin != null) {
            item.callBeginTimeStamp = call_begin;
        }

        Long callDuration = contentValues.getAsLong(COLUMN_CALL_DURATION);
        if (callDuration != null) {
            item.callDuration = callDuration;
        }


        return item;
    }

    @SuppressWarnings("unused")
    private static CallLogItem fromSimpleCallLogItem(SimpleCallLogItem simpleCallLogItem) {
        CallLogItem item = new CallLogItem();
        item.callBeginTimeStamp = simpleCallLogItem.getCallBeginTimeStamp();
        item.callDuration = simpleCallLogItem.getCallDuration();
        item.callType = simpleCallLogItem.getCallType();
        item.callResult = simpleCallLogItem.getCallResult();
        item.opponentNumber = simpleCallLogItem.getOpponentNumber();

        item.generateCallId();


        return item;
    }

    public static String callLogIdFromCallBeginTimeStamp(long callBeginTimeStamp) {
        String id = "" + android.text.format.DateFormat.format("yyyyMMddkkmmss", callBeginTimeStamp);
        id += (callBeginTimeStamp % 100);
        return id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 判断是呼入还是呼出类型。取值为
     *
     * @return 通话类型
     * @see #CALL_TYPE_IN
     * @see #CALL_TYPE_OUT
     */
    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    @SuppressWarnings("unused")
    public String getCallTime() {
        return callTime;
    }

    @SuppressWarnings("unused")
    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    /**
     * 通话开始的时间戳，单位为毫秒
     *
     * @return 通话开始的时间戳
     */
    public long getCallBeginTimeStamp() {
        return callBeginTimeStamp;
    }

    @SuppressWarnings("unused")
    public void setCallBeginTimeStamp(long callTimeStamp) {
        this.callBeginTimeStamp = callTimeStamp;
    }

    @SuppressWarnings("unused")
    public long getPickUpTimeStamp() {
        return pickUpTimeStamp;
    }

    @SuppressWarnings("unused")
    public void setPickUpTimeStamp(long pickUpTimeStamp) {
        this.pickUpTimeStamp = pickUpTimeStamp;
    }

    /**
     * 通话时长，单位毫秒
     *
     * @return 通话时长
     */
    public long getCallDuration() {
        return callDuration;
    }

    @SuppressWarnings("unused")
    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    /**
     * 来电通话结果，用于判断是否未接电话。此值仅对呼入电话生效。取值为
     *
     * @return 来电通话结果
     * @see #CALL_RESULT_MISSED
     * @see #CALL_RESULT_ANSWERED
     */
    public String getCallResult() {
        return callResult;
    }

    @SuppressWarnings("unused")
    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }

    /**
     * 对方的通话号码
     *
     * @return 通话号码
     */
    public String getOpponentNumber() {
        return opponentNumber;
    }

    @SuppressWarnings("unused")
    public void setOpponentNumber(String opponentNumber) {
        this.opponentNumber = opponentNumber;
    }

    public String getCallLog_id() {
        return callLog_id;
    }

    @SuppressWarnings("unused")
    public void setCallLog_id(String callLog_id) {
        this.callLog_id = callLog_id;
    }

    @SuppressWarnings("unused")
    public String getCallRecord_id() {
        return callRecord_id;
    }

    @SuppressWarnings("unused")
    public void setCallRecord_id(String callRecord_id) {
        this.callRecord_id = callRecord_id;
    }

    /**
     * 获取通话的唯一id
     *
     * @return 通话id
     */
    @SuppressWarnings("unused")
    public String getPrimaryUUID() {
        return primaryUUID;
    }

    @SuppressWarnings("unused")
    public void setPrimaryUUID(String primaryUUID) {
        this.primaryUUID = primaryUUID;
    }

    /**
     * 判断是固话，sim卡通话还是sip通话
     *
     * @return 通话方式
     * @see #CALL_CATEGORY_PSTN
     * @see #CALL_CATEGORY_CELL
     * @see #CALL_CATEGORY_SIP
     */
    public String getCallCategory() {
        return callCategory;
    }

    @SuppressWarnings("unused")
    public void setCallCategory(String callCategory) {
        this.callCategory = callCategory;
    }

    @SuppressWarnings("unused")
    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();


        contentValues.put(COLUMN_CALL_BEGIN, callBeginTimeStamp);
        contentValues.put(COLUMN_CALL_DURATION, callDuration);

        if (callType != null) {
            contentValues.put(COLUMN_CALL_TYPE, callType);
        }

        if (callResult != null) {
            contentValues.put(COLUMN_CALL_RESULT, callResult);
        }

        if (opponentNumber != null) {
            contentValues.put(COLUMN_OPPONENT_NUMBER, getOpponentNumber());
        }

        if (callLog_id != null) {
            contentValues.put(COLUMN_CALLLOG_ID, callLog_id);
        }

        if (callRecord_id != null) {
            contentValues.put(COLUMN_AUDIO_RECORD_ID, callRecord_id);
        }

        if (callCategory != null) {
            contentValues.put(COLUMN_CALL_CATEGORY, callCategory);
        }

        if (contactName != null) {
            contentValues.put(COLUMN_DISPLAY_NAME, contactName);
        }
        if (primaryUUID != null) {
            contentValues.put(COLUMN_CALL_LOG_PRIMARY_ID, primaryUUID);
        }
        return contentValues;
    }

    private void generateCallId() {
        if (callLog_id != null && callLog_id.trim().length() > 0) return;
        String id = "" + android.text.format.DateFormat.format("yyyyMMddkkmmss", callBeginTimeStamp);
        id += (callBeginTimeStamp % 100);
        callLog_id = id;
    }

    /**
     * 录音文件路径
     *
     * @return 录音文件路径, 为null 即没有录音。
     */
    public String getAudioRecordPath() {
        String fileName = getCallRecord_id();
        return GcordSDK.getInstance().getCallLogManager().getRecordFilePath(fileName);
    }

    public boolean wasAutoAnswered() {
        return (flags & FLAG_HAS_ANSWER_CALL) == FLAG_HAS_ANSWER_CALL;
    }
}
