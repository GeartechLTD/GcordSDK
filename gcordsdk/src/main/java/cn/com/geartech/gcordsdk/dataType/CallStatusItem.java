package cn.com.geartech.gcordsdk.dataType;

import java.util.Map;

public class CallStatusItem {

    private static final String CALL_TYPE = "callType";
    private static final String CALL_ID = "callId";
    private static final String BEGIN_TIMESTAMP = "beginTS";
    private static final String IS_INCOMING_CALL = "isIncoming";
    private static final String OPPONENT_NUMBER = "opponentNumber";
    private static final String ACTIVE_TIMESTAMP = "activeTS";


    public String getCallType() {
        return callType;
    }

    public String getCallId() {
        return callId;
    }

    public long getCallBeginTimeStamp() {
        return callBeginTimeStamp;
    }

    public boolean isIncomingCall() {
        return isIncomingCall;
    }

    public String getOpponentNumber() {
        return opponentNumber;
    }

    public long getCallActiveTimeStamp() {
        return callActiveTimeStamp;
    }

    String callType;
    String callId;
    long callBeginTimeStamp;
    boolean isIncomingCall;
    String opponentNumber;
    long callActiveTimeStamp;



    public static CallStatusItem fromHashMap(Map<String,String> map)
    {
        if(map == null)
        {
            return null;
        }
        else
        {
            CallStatusItem item = new CallStatusItem();

            item.callType = (String) map.get(CALL_TYPE);
            item.callId = (String)map.get(CALL_ID);
            String beginTS = (String)map.get(BEGIN_TIMESTAMP);
            if(beginTS != null)
            {
                try {
                    item.callBeginTimeStamp = Long.parseLong(beginTS);
                }catch (Exception e)
                {

                }
            }

            String activeTS = (String)map.get(ACTIVE_TIMESTAMP);
            if(activeTS != null)
            {
                try {
                    item.callActiveTimeStamp = Long.parseLong(activeTS);
                }catch (Exception e)
                {

                }
            }

            item.opponentNumber = (String)map.get(OPPONENT_NUMBER);
            String incoming = (String)map.get(IS_INCOMING_CALL);
            if("1".equals(incoming))
            {
                item.isIncomingCall = true;
            }

            return item;
        }
    }

    @Override
    public String toString() {
        return "CallStatusItem{" +
                "callType='" + callType + '\'' +
                ", callId='" + callId + '\'' +
                ", callBeginTimeStamp=" + callBeginTimeStamp +
                ", isIncomingCall=" + isIncomingCall +
                ", opponentNumber='" + opponentNumber + '\'' +
                ", callActiveTimeStamp=" + callActiveTimeStamp +
                '}';
    }
}
