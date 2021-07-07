package cn.com.geartech.gcordsdk.dataType;

import android.util.Log;

import cn.com.geartech.gcordsdk.GcordSDK;

/**
 * Created by pangyuning on 17/2/24.
 */

public class UnifiedPhoneCall {

    public enum Phone_Type{
        PSTN,
        CELL,
        SIP
    }

    Phone_Type currentCallType;

    int tickCount = 0;

    int sessionId;


    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }

    @Deprecated
    protected void addTickCount()
    {
        tickCount++;
    }


    public int getTickCount() {
        if(callActiveTimeStamp != 0) {
            return (int) ((System.currentTimeMillis() - callActiveTimeStamp)/1000L);
        }else return 0;
    }

    public UnifiedPhoneCall(Phone_Type type)
    {
        Log.d("GcordSDK", "new Unify Phone Call !" + type);
        currentCallType = type;
        callBeginTimestamp = System.currentTimeMillis();
    }

    public Phone_Type getCurrentCallType()
    {
        return currentCallType;
    }

    public void setCurrentCallType(Phone_Type currentCallType) {
        this.currentCallType = currentCallType;
    }

    long callBeginTimestamp;

    public long getCallBeginTimestamp() {
        return callBeginTimestamp;
    }

    public void setCallBeginTimestamp(long callBeginTimestamp) {
        this.callBeginTimestamp = callBeginTimestamp;
    }

    long callActiveTimeStamp = 0L;

    public void setCallActiveTimeStamp(long callActiveTimeStamp) {
        this.callActiveTimeStamp = callActiveTimeStamp;
    }

    public long getCallActiveTimeStamp() {
        return callActiveTimeStamp;
    }

    long callDuration;

    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    public long getCallDuration() {
        return callDuration;
    }

    boolean isCallIn = false;

    public boolean isCallIn() {
        return isCallIn;
    }

    public void setCallIn(boolean callIn) {
        isCallIn = callIn;

        if (isCallIn && Phone_Type.PSTN.equals(currentCallType)){
            String phoneNumber = GcordSDK.getInstance().getPhoneAPI().getIncomingNumber();
            if (phoneNumber != null && phoneNumber.trim().length() > 0){
                setOpponentPhoneNumber(phoneNumber);
            }
        }
    }

    String opponentPhoneNumber;

    /**
     * 对方的号码, 如果是呼出的电话，以实际呼出的号码为准
     * @return
     */
    public String getOpponentPhoneNumber() {
        return opponentPhoneNumber;
    }

    String numberToDial;

    /**
     * 即将拨出的号码
     * @param
     */
    public String getNumberToDial()
    {
        return numberToDial;
    }

    public void setNumberToDial(String numberToDial)
    {
        this.numberToDial = numberToDial;
    }

    public void addNumberToDial(String numberToDial) {

        if(dialFinished)
        {
            return;
        }

        if(this.numberToDial == null)
        {
            this.numberToDial = numberToDial;
        }
        else
        {
            this.numberToDial += numberToDial;
        }
    }

    boolean dialFinished = false;

    public void setDialFinished()
    {
        dialFinished = true;
    }

    public void setOpponentPhoneNumber(String opponentPhoneNumber) {
        this.opponentPhoneNumber = opponentPhoneNumber;
    }

    boolean answered = false;

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isAnswered() {
        return answered;
    }

    @Deprecated
    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }
}
