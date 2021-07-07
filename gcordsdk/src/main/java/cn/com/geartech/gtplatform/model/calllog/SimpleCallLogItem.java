package cn.com.geartech.gtplatform.model.calllog;

import java.io.Serializable;

/**
 * Created by pangyuning on 16/11/16.
 */

public class SimpleCallLogItem implements Serializable {

    static final String CALL_TYPE_IN = "call_in";
    static final String CALL_TYPE_OUT = "call_out";
    /*
    call in or call out
     */
    String callType = "";

    String callTime = "";
    long callBeginTimeStamp ;

    long pickUpTimeStamp;

    long callDuration;

    static final String CALL_RESULT_ANSWERED = "answered";
    static final String CALL_RESULT_MISSED = "missed";

    /*
    answered or missed
     */
    String callResult = "";


    String opponentNumber = "";


    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public long getCallBeginTimeStamp() {
        return callBeginTimeStamp;
    }

    public void setCallBeginTimeStamp(long callTimeStamp) {
        this.callBeginTimeStamp = callTimeStamp;
    }

    public long getPickUpTimeStamp() {
        return pickUpTimeStamp;
    }

    public void setPickUpTimeStamp(long pickUpTimeStamp) {
        this.pickUpTimeStamp = pickUpTimeStamp;
    }

    public long getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallResult() {
        return callResult;
    }

    public void setCallResult(String callResult) {
        this.callResult = callResult;
    }

    public String getOpponentNumber() {
        return opponentNumber;
    }

    public void setOpponentNumber(String opponentNumber) {
        this.opponentNumber = opponentNumber;
    }


    public String toString()
    {
        return SerializeObject.objectToString(this);
    }

    public static SimpleCallLogItem fromString(String stringData)
    {
        Object item = SerializeObject.stringToObject(stringData);
        if(item != null)
        {
            return (SimpleCallLogItem)item;
        }
        else
        {
            return null;
        }
    }



}
