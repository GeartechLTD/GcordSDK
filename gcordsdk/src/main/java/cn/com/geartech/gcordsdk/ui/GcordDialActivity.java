package cn.com.geartech.gcordsdk.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import cn.com.geartech.gcordsdk.GTPrivate;
import cn.com.geartech.gcordsdk.GcordSDK;
import cn.com.geartech.gcordsdk.R;
import cn.com.geartech.gcordsdk.UnifiedPhoneController;
import cn.com.geartech.gcordsdk.dataType.CallStatusItem;
import cn.com.geartech.gcordsdk.ui.views.DigitsEditText;

public class GcordDialActivity extends Activity {
    private static final String TAG = "GcordDialActivity";
    private DigitsEditText digitsEditText = null;
    private View btnDel = null;
    private View dialPad = null;
    private View containerDialPadUp;
    private View containerPickedUp;

    private final Object mToneGeneratorLock = new Object();
    private ToneGenerator mToneGenerator = null;

    private static final String IS_PRE_DIAL = "is_pre_dial";
    private boolean isPreDial = true;

    /**
     * The length of DTMF tones in milliseconds
     */
    private static final int TONE_LENGTH_MS = 150;

    /**
     * The DTMF tone volume relative to other sounds in the stream
     */
    private static final int TONE_RELATIVE_VOLUME = 80;


    /**
     * Stream type used to play the DTMF tones off call, and mapped to the volume control keys
     */
    private static final int DIAL_TONE_STREAM_TYPE = AudioManager.STREAM_MUSIC;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(GTPrivate.EVENT_DIAL_FINISH))
            {
            }
        }
    };


    UnifiedPhoneController.UnifiedPhoneEventListener unifiedPhoneEventListener = new UnifiedPhoneController.UnifiedPhoneEventListener() {
        @Override
        public void onIncomingCall() {

        }

        @Override
        public void onGetPhoneNumber(String number) {

        }

        @Override
        public void onTicking(int seconds) {
            if(seconds == 0)
            {
                if (!TextUtils.isEmpty(digitsEditText.getText())) {
                    GcordSDK.getInstance().getPhoneAPI().dial(digitsEditText.getText().toString());
                    toInCallActivity();
                } else {
                    containerDialPadUp.setVisibility(View.GONE);
                    containerPickedUp.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onCallTerminated() {
            finish();
        }

        @Override
        public void onDialFinish(String number) {
            toInCallActivity();
        }

        @Override
        public void onBusyTone() {

        }

        @Override
        public void onResumeInCallStatus(CallStatusItem item) {

        }
    };


//    PhoneAPI.PhoneEventListener mPhoneEventListener = new PhoneAPI.PhoneEventListener() {
//        @Override
//        public void onPickUp(PhoneAPI.PICKUP_STATE state) {
//
//        }
//
//        @Override
//        public void onInComingCall() {
//
//        }
//
//        @Override
//        public void onRingEnd() {
//
//        }
//
//        @Override
//        public void onPhoneNumberReceived(String phoneNumber) {
//
//        }
//
//        @Override
//        public void onSwitchPhoneState(PhoneAPI.PICKUP_STATE state) {
//
//        }
//
//        @Override
//        public void onHangOff() {
//            finish();
//        }
//    };

    public static void start(Context context, boolean isPreDial) {
        Intent i = new Intent(context, GcordDialActivity.class);
        i.putExtra(IS_PRE_DIAL, isPreDial);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //GcordSDK.getInstance().getPhoneAPI().addPhoneEventListener(mPhoneEventListener);
        UnifiedPhoneController.getInstance().addPhoneEventListener(unifiedPhoneEventListener);
        IntentFilter filter = new IntentFilter();
        filter.addAction(GTPrivate.EVENT_DIAL_FINISH);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //GcordSDK.getInstance().getPhoneAPI().removePhoneEventListener(mPhoneEventListener);
        UnifiedPhoneController.getInstance().removePhoneEventListener(unifiedPhoneEventListener);
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial2);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        isPreDial = getIntent().getBooleanExtra(IS_PRE_DIAL, true);
        initViews();
        initTone();

        if (!isPreDial) {
            GcordSDK.getInstance().getPhoneAPI().pickUpWithHandsFree();
        }
    }

    private void initViews() {
        digitsEditText = (DigitsEditText) findViewById(R.id.digits);
        btnDel = findViewById(R.id.btn_del);
        btnDel.setOnClickListener(onClickListener);
        btnDel.setOnLongClickListener(onLongClickListener);
        dialPad = findViewById(R.id.dialpad);

        View view = dialPad.findViewById(R.id.one);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.two);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.three);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.four);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.five);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.six);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.seven);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.eight);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.nine);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.zero);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.star);
        view.setOnClickListener(onDigitsClickListener);

        view = dialPad.findViewById(R.id.pound);
        view.setOnClickListener(onDigitsClickListener);

        containerDialPadUp = findViewById(R.id.container_dial_pad_up);
        containerPickedUp = findViewById(R.id.container_picked_up);

        containerDialPadUp.setVisibility(isPreDial ? View.VISIBLE : View.GONE);
        containerPickedUp.setVisibility(isPreDial ? View.GONE : View.VISIBLE);
    }

    private View.OnClickListener onDigitsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            int keyCode = -1;
            int tone = -1;
            if (id == R.id.one) {
                keyCode = KeyEvent.KEYCODE_1;
                tone = ToneGenerator.TONE_DTMF_1;
            } else if (id == R.id.two) {
                keyCode = KeyEvent.KEYCODE_2;
                tone = ToneGenerator.TONE_DTMF_2;
            } else if (id == R.id.three) {
                keyCode = KeyEvent.KEYCODE_3;
                tone = ToneGenerator.TONE_DTMF_3;
            } else if (id == R.id.four) {
                keyCode = KeyEvent.KEYCODE_4;
                tone = ToneGenerator.TONE_DTMF_4;
            } else if (id == R.id.five) {
                keyCode = KeyEvent.KEYCODE_5;
                tone = ToneGenerator.TONE_DTMF_5;
            } else if (id == R.id.six) {
                keyCode = KeyEvent.KEYCODE_6;
                tone = ToneGenerator.TONE_DTMF_6;
            } else if (id == R.id.seven) {
                keyCode = KeyEvent.KEYCODE_7;
                tone = ToneGenerator.TONE_DTMF_7;
            } else if (id == R.id.eight) {
                keyCode = KeyEvent.KEYCODE_8;
                tone = ToneGenerator.TONE_DTMF_8;
            } else if (id == R.id.nine) {
                keyCode = KeyEvent.KEYCODE_9;
                tone = ToneGenerator.TONE_DTMF_9;
            } else if (id == R.id.zero) {
                keyCode = KeyEvent.KEYCODE_0;
                tone = ToneGenerator.TONE_DTMF_0;
            } else if (id == R.id.pound) {
                keyCode = KeyEvent.KEYCODE_POUND;
                tone = ToneGenerator.TONE_DTMF_P;
            } else if (id == R.id.star) {
                keyCode = KeyEvent.KEYCODE_STAR;
                tone = ToneGenerator.TONE_DTMF_S;
            } else {

            }
            if (keyCode != -1) {
                keyPressed(keyCode);
            }

            if (tone != -1) {
                playTone(tone);
            }
        }
    };

    private void keyPressed(int keyCode) {
        btnDel.setVisibility(View.VISIBLE);
        String str = digitsEditText.getText().toString();
        String code = "";
        switch (keyCode) {
            case KeyEvent.KEYCODE_1:
                code = "1";
                break;
            case KeyEvent.KEYCODE_2:
                code = "2";
                break;
            case KeyEvent.KEYCODE_3:
                code = "3";
                break;
            case KeyEvent.KEYCODE_4:
                code = "4";
                break;
            case KeyEvent.KEYCODE_5:
                code = "5";
                break;
            case KeyEvent.KEYCODE_6:
                code = "6";
                break;
            case KeyEvent.KEYCODE_7:
                code = "7";
                break;
            case KeyEvent.KEYCODE_8:
                code = "8";
                break;
            case KeyEvent.KEYCODE_9:
                code = "9";
                break;
            case KeyEvent.KEYCODE_0:
                code = "0";
                break;
            case KeyEvent.KEYCODE_STAR:
                code = "*";
                break;
            case KeyEvent.KEYCODE_POUND:
                code = "#";
                break;
        }
        str += code;
        digitsEditText.setText(str);

        //先提机再拨号的情况，仅对PSTN通话生效
        if (GcordSDK.getInstance().getPhoneAPI().isInCall()) {
            if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
                UnifiedPhoneController.getInstance().dialDTMF("" + (keyCode - KeyEvent.KEYCODE_0));
                //GcordSDK.getInstance().getPhoneAPI().dial();
            } else if (keyCode == KeyEvent.KEYCODE_STAR) {
                UnifiedPhoneController.getInstance().dialDTMF("*");

                //GcordSDK.getInstance().getPhoneAPI().dial("*");
            } else if (keyCode == KeyEvent.KEYCODE_POUND) {
                //GcordSDK.getInstance().getPhoneAPI().dial("#");
                UnifiedPhoneController.getInstance().dialDTMF("#");
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = digitsEditText.getText().toString();
            if (TextUtils.isEmpty(str)) {
                return;
            }
            if (str.length() == 1) {
                str = "";
            } else {
                str = str.substring(0, str.length() - 1);
            }
            digitsEditText.setText(str);
            if (TextUtils.isEmpty(str)) {
                v.setVisibility(View.INVISIBLE);
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            digitsEditText.setText("");
            v.setVisibility(View.INVISIBLE);
            return true;
        }
    };

    private void initTone() {

        // if the mToneGenerator creation fails, just continue without it.  It is
        // a local audio signal, and is not as important as the dtmf tone itself.

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                try {
                    // we want the user to be able to control the volume of the toInCallActivity tones
                    // outside of a call, so we use the stream type that is also mapped to the
                    // volume control keys for this activity
                    mToneGenerator = new ToneGenerator(DIAL_TONE_STREAM_TYPE, TONE_RELATIVE_VOLUME);
                    setVolumeControlStream(DIAL_TONE_STREAM_TYPE);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    mToneGenerator = null;
                }
            }
        }
    }

    void playTone(int tone) {

        AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        if ((ringerMode == AudioManager.RINGER_MODE_SILENT)
                || (ringerMode == AudioManager.RINGER_MODE_VIBRATE)) {
            return;
        }

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                return;
            }
            mToneGenerator.startTone(tone, TONE_LENGTH_MS);
        }
    }


    public void onBtnDialClick(View view) {
        if (!TextUtils.isEmpty(digitsEditText.getText())) {


            String number = digitsEditText.getText().toString();
            Intent intent = new Intent(this, InCallActivity.class);
            intent.putExtra(InCallActivity.PARAM_CALL_NUMBER, number);
            intent.putExtra(InCallActivity.PARAM_ACTION, InCallActivity.ACTION_DIAL);
            startActivity(intent);

            //UnifiedPhoneController.getInstance().autoDial();
        }
    }

    private void toInCallActivity() {
//        String number = digitsEditText.getText().toString();
//        if (TextUtils.isEmpty(number)) {
//            return;
//        }
//
//        digitsEditText.setText("");
//
//        Intent intent = new Intent(this, InCallActivity.class);
//        intent.putExtra(InCallActivity.PARAM_CALL_NUMBER, number);
//        startActivity(intent);
    }

    @Override
    public void onDestroy() {

        try {

            if (mToneGenerator != null) {

                mToneGenerator.release();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
