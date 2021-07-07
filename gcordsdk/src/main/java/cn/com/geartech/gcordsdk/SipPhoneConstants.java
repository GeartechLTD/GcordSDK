package cn.com.geartech.gcordsdk;

/**
 * Created by cuizhy on 16/8/16.
 */
public class SipPhoneConstants {

    public final static int LOGIN_STATE_IDLE = 0; /**< @brief Register idle state */
    public final static int LOGIN_STATE_REGING = 1; /**< @brief Register registering state */
    public final static int LOGIN_STATE_REGED = 2; /**< @brief Register registered state */
    public final static int LOGIN_STATE_UNREGING = 3; /**< @brief Register unregistering state */

    public final static int LOGIN_ERR_AUTH_FAILED = (0xE100+3); /**< @brief Register authentication failed, invalid user or password. */
    public final static int LOGIN_ERR_INVALID_USER = (0xE100+4); /**< @brief Register using invalid user. */
    public final static int LOGIN_ERR_TIMEOUT = (0xE100+5); /**< @brief Register timeout. */
    public final static int LOGIN_ERR_SERV_BUSY = (0xE100+6); /**< @brief Register server busy. */
    public final static int LOGIN_ERR_SERV_NOT_REACH = (0xE100+7); /**< @brief Register server not reached. */
    public final static int LOGIN_ERR_SRV_FORBIDDEN = (0xE100+8); /**< @brief Register forbidden. */
    public final static int LOGIN_ERR_SRV_UNAVAIL = (0xE100+9); /**< @brief Register unavailable. */
    public final static int LOGIN_ERR_DNS_QRY = (0xE100+10); /**< @brief Register dns query error. */
    public final static int LOGIN_ERR_NETWORK = (0xE100+11); /**< @brief Register network error. */
    public final static int LOGIN_ERR_DEACTED = (0xE100+12); /**< @brief Register deactived. */
    public final static int LOGIN_ERR_PROBATION = (0xE100+13); /**< @brief Register probation */
    public final static int LOGIN_ERR_INTERNAL = (0xE100+14); /**< @brief Register internal error. */
    public final static int LOGIN_ERR_NO_RESOURCE = (0xE100+15); /**< @brief Register no resource. */
    public final static int LOGIN_ERR_REJECTED = (0xE100+16); /**< @brief Register be rejected. */
    public final static int LOGIN_ERR_OTHER = (0xE100+200);
    public final static int LOGIN_ERR_SEND_MSG = (0xE100+2); /**< @brief Send message error. */

    public final static int EN_MTC_TPT_UDP = 0; /**< @brief UDP transport */
    public final static int EN_MTC_TPT_TCP = EN_MTC_TPT_UDP + 1; /**< @brief TCP transport */
    public final static int EN_MTC_TPT_TLS = EN_MTC_TPT_TCP + 1; /**< @brief TCP transport */


    public final static int INVALIDID = -1;

    // HANG_OFF_REASON_TYPE
    public final static int HANG_OFF_REASON_NORMAL = 0; /**< @brief normal session term, bye or cancel */
    public final static int HANG_OFF_REASON_BUSY = HANG_OFF_REASON_NORMAL + 1; /**< @brief the session is rejected */
    public final static int HANG_OFF_REASON_DECLINE = HANG_OFF_REASON_BUSY + 1; /**< @brief decline the session */
    public final static int HANG_OFF_REASON_NOT_AVAILABLE = HANG_OFF_REASON_DECLINE + 1; /**< @brief the user is not available */
    public final static int HANG_OFF_REASON_INTERRUPT = HANG_OFF_REASON_NOT_AVAILABLE + 1; /**< @brief active interrupt because of unavailable network */

    public final static int SDK_INIT_RESULT_GET_DEVICEID_ERROR = -907;/**< @brief get device id failed */
    public final static int SDK_INIT_RESULT_ERR_NEED_ACT_LICSEN = (0xEB00+1); /**< @brief not activate licsence, need activate it */
    public final static int SDK_INIT_RESULT_OTEHR_ERROR = -100; /**< @brief license download error */


}
