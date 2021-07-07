package cn.com.geartech.gcordsdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.PhoneAPI;
import cn.com.geartech.gcordsdk.R;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;
import cn.com.geartech.gcordsdk.dataType.CallStatusItem;
import cn.com.geartech.gcordsdk.dataType.UnifiedPhoneCall;
import cn.com.geartech.gcordsdk.ui.views.InCallDialpad;
import cn.com.geartech.gcordsdk.ui.views.RecordButton;
import cn.com.geartech.gcordsdk.ui.views.WaitingTextView;

/**
 * Created by pangyuning on 16/8/30.
 */
public class InCallActivity extends Activity {

    public static final String PARAM_CALL_NUMBER = "phoneNumber";
    public static final String PARAM_ACTION = "action";
    public static final String ACTION_DIAL_PSTN = "dial_pstn";
    public static final String ACTION_INCOMING_CALL = "incoming_call";
    public static final String ACTION_POST_DIAL_PSTN = "post_dial_pstn";

    public static final String PARAM_HIDE_ANSWER_BUTTON = "hide_answer_btn";
    public static final String PARAM_SHOW_BACK_BUTTON = "show_back_button";


    public static final String ACTION_DIAL = "dial";

    public static final String ACTION_AUTO_DIAL = "auto_dial";

    private static boolean isActive = false;

    public static boolean isActive() {
        return isActive;
    }

    ImageView ivIncallBack;
    TextView text_contact_name;
    private View incall_dialpad_container = null;

    View inComingCall_Container;
    View inCall_Container;

    View btn_answer;

    TextView text_call_time;
    WaitingTextView text_tag;
    RecordButton btnRecord;

    Handler handler = new Handler();

    View btn_flash;

    boolean isIncomingCall = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incall2);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initViews();
        checkCommands();

        UnifiedPhoneController.getInstance().addPhoneEventListener(unifiedPhoneEventListener);

        try {
            if (text_contact_name.getText().toString().length() == 0) {
                text_contact_name.setText(UnifiedPhoneController.getInstance().getCurrentPhoneCall().getOpponentPhoneNumber());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        InCallDialpad inCallDialpad = (InCallDialpad) findViewById(R.id.incall_dial_pad);
        inCallDialpad.setInCallDialPadListener(new InCallDialpad.InCallDialPadListener() {
            @Override
            public void onNumberDialed(String number) {
                text_contact_name.append(number);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initViews();
    }

    void checkCommands() {
        String action = getIntent().getStringExtra(PARAM_ACTION);
        String number = getIntent().getStringExtra(PARAM_CALL_NUMBER);
        callTimeCount = getIntent().getLongExtra("callTime", 0);
        if (callTimeCount != 0) {
            startTimeTick();
        }
        boolean recordSelected = getIntent().getBooleanExtra("recordSelected", false);

        int hide_answer_btn = getIntent().getIntExtra(PARAM_HIDE_ANSWER_BUTTON, 0);

        if (hide_answer_btn == 1) {
            //Log.e("GcordSDK", "hide_answer_btn");
            btn_answer.setVisibility(View.GONE);

        }

        ivIncallBack.setVisibility(getIntent().getBooleanExtra(PARAM_SHOW_BACK_BUTTON, false) ? View.VISIBLE : View.INVISIBLE);

        if (action != null) {

            Log.d("GcordSDK", "InCall Action:" + action);

            if (action.equals(ACTION_DIAL)) {
                UnifiedPhoneController.getInstance().autoDial(number);

                isIncomingCall = false;
                switchToInCallMode();

//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startTimeTick();
//                        text_tag.clear();
//                    }
//                }, 8000);

                text_tag.setText("呼叫中");
            } else if (action.equals(ACTION_DIAL_PSTN) || action.equals(ACTION_POST_DIAL_PSTN)) {
                if (number != null && number.length() > 0 && action.equals(ACTION_DIAL_PSTN)) {
                    GcordSDK.getInstance().getPhoneAPI().dial(number);
                }


                isIncomingCall = false;
                switchToInCallMode();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startTimeTick();
                        text_tag.clear();
                    }
                }, 4000);

                text_tag.setText("呼叫中");
            } else if (action.equals(ACTION_AUTO_DIAL)) {
                UnifiedPhoneController.getInstance().autoDial(number);
            } else if (action.equals(ACTION_INCOMING_CALL)) {
                isIncomingCall = true;
                switchToInComingCallMode();

                if(UnifiedPhoneController.getInstance().getCurrentPhoneCall() != null &&
                        UnifiedPhoneController.getInstance().getCurrentPhoneCall().getCurrentCallType() == UnifiedPhoneCall.Phone_Type.SIP)
                {
                    //play ring tone
                    playRingTone();
                }

                text_tag.setText("等待接听");
            }
        }

        Log.e("incallui", "dial number:" + number);

        text_contact_name.setText(number);
        btnRecord.setSelected(recordSelected);
    }

    void initViews() {
        ivIncallBack = (ImageView) findViewById(R.id.iv_incall_back);
        ivIncallBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InCallFloatingWindow.show(getApplicationContext(), callTimeCount, text_contact_name.getText().toString(), btnRecord.isSelected());
                finish();
            }
        });

        text_contact_name = (TextView) findViewById(R.id.text_contact_name);

        incall_dialpad_container = findViewById(R.id.incall_dialpad_container);

        View view = findViewById(R.id.btn_keyboard);
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyBoardClick(v);
            }
        });

        inComingCall_Container = findViewById(R.id.call_in_container);
        inCall_Container = findViewById(R.id.incall_btn_container);

        text_call_time = (TextView) findViewById(R.id.text_call_timer);
        btnRecord = (RecordButton) findViewById(R.id.btn_record);

        text_tag = (WaitingTextView) findViewById(R.id.text_tag);

        btn_flash = findViewById(R.id.btn_flash);


        btn_answer = findViewById(R.id.call_in_answer);
    }

    public void onKeyBoardClick(View view) {
        int visibility = incall_dialpad_container.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }
        incall_dialpad_container.setVisibility(visibility);

        try {
            if (UnifiedPhoneController.getInstance().getCurrentPhoneCall().getCurrentCallType() != UnifiedPhoneCall.Phone_Type.PSTN) {
                btn_flash.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        GcordSDK.getInstance().getPhoneAPI().addPhoneEventListener(phoneEventListener);

        if (GcordSDK.getInstance().getPhoneAPI().getIncomingNumber() != null &&
                GcordSDK.getInstance().getPhoneAPI().getIncomingNumber().length() > 0) {
            text_contact_name.setText(GcordSDK.getInstance().getPhoneAPI().getIncomingNumber());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        GcordSDK.getInstance().getPhoneAPI().removePhoneEventListener(phoneEventListener);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        cancelTimeTick();

        UnifiedPhoneController.getInstance().removePhoneEventListener(unifiedPhoneEventListener);

        stopPlayRingTone();
    }

    PhoneAPI.PhoneEventListener phoneEventListener = new PhoneAPI.PhoneEventListener() {
        @Override
        public void onPickUp(PhoneAPI.PICKUP_STATE state) {


        }

        @Override
        public void onInComingCall() {

        }

        @Override
        public void onRingEnd() {
            if (isIncomingCall && !GcordSDK.getInstance().getPhoneAPI().isInCall()) {
                finish();
            }
        }

        @Override
        public void onPhoneNumberReceived(String phoneNumber) {
        }

        @Override
        public void onSwitchPhoneState(PhoneAPI.PICKUP_STATE state) {

        }

        @Override
        public void onHangOff() {
            finish();
        }

        @Override
        public void onNumberSent(String number) {
            super.onNumberSent(number);

        }
    };

    UnifiedPhoneController.UnifiedPhoneEventListener unifiedPhoneEventListener = new UnifiedPhoneController.UnifiedPhoneEventListener() {
        @Override
        public void onIncomingCall() {

        }

        @Override
        public void onGetPhoneNumber(String number) {
            text_contact_name.setText(number);
        }

        @Override
        public void onTicking(int seconds) {


            if (seconds == 0) {
                if (isIncomingCall) {
                    switchToInCallMode();
                }

                startTimeTick();

                stopPlayRingTone();
            }

            text_tag.clear();

        }

        @Override
        public void onCallTerminated() {
            stopPlayRingTone();
            finish();
        }

        @Override
        public void onDialFinish(String number) {

        }

        @Override
        public void onBusyTone() {

        }

        @Override
        public void onResumeInCallStatus(CallStatusItem item) {

        }
    };

    void switchToInComingCallMode() {
        inComingCall_Container.setVisibility(View.VISIBLE);
        inCall_Container.setVisibility(View.GONE);
    }

    void switchToInCallMode() {
        inComingCall_Container.setVisibility(View.GONE);
        inCall_Container.setVisibility(View.VISIBLE);
    }

    Timer timer;

    long callTimeCount = 0; // s

    void startTimeTick() {
        cancelTimeTick();
        timer = new Timer();
        timerTask = new TickCountTimerTask();
        timer.schedule(timerTask, 0, 1000);

        text_call_time.setVisibility(View.VISIBLE);
    }

    String timeStr = "";

    class TickCountTimerTask extends TimerTask {
        @Override
        public void run() {
            timeStr = "";
            long count = callTimeCount;
            callTimeCount++;
            long hour = count / 3600;
            if (hour > 0) {
                timeStr += hour;
                timeStr += ":";
                count = count % 3600;
            }

            long minute = count / 60;
            if (minute < 10) {
                timeStr += "0" + minute;
            } else {
                timeStr += minute;
            }

            timeStr += ":";

            long second = count % 60;
            if (second < 10) {
                timeStr += "0" + second;
            } else {
                timeStr += second;
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    text_call_time.setText(timeStr);

                }
            });
        }
    }

    TimerTask timerTask = new TickCountTimerTask();


    void cancelTimeTick() {
        if (timer != null) {

            timer.cancel();
            timer = null;
        }

        if (timerTask != null)
            timerTask.cancel();
    }


    MediaPlayer mediaPlayer;

    void playRingTone()
    {
        stopPlayRingTone();
        mediaPlayer = MediaPlayer.create(this, R.raw.gt_defaultring);
        mediaPlayer.setLooping(true);
//        mediaPlayer.prepareAsync();
        mediaPlayer.setVolume(1.0f, 1.0f);

        mediaPlayer.start();

//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mediaPlayer.start();
//            }
//        });
    }

    void stopPlayRingTone()
    {
        if(mediaPlayer != null)
        {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
